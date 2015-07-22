package com.worthwhilegames.carhubmobile.sync;

import android.content.Context;
import com.appspot.car_hub.carhub.Carhub;
import com.appspot.car_hub.carhub.model.FuelRecord;
import com.appspot.car_hub.carhub.model.FuelRecordCollection;
import com.appspot.car_hub.carhub.model.ModelsActiveRecords;
import com.worthwhilegames.carhubmobile.Util;
import com.worthwhilegames.carhubmobile.models.UserFuelRecord;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FetchUserFuelRecordsTask implements ISyncTask {
    /**
     * The Vehicle to get fuel records for
     */
    private UserVehicleRecord mVehicle;

    /**
     * The context
     */
    private Context mContext;

    /**
     * The GarageHub service for interacting with AppEngine
     */
    protected Carhub mService;

    public FetchUserFuelRecordsTask(Context ctx, Carhub service, UserVehicleRecord vehicle) {
        mContext = ctx;
        mService = service;
        mVehicle = vehicle;
    }

    @Override
    public boolean performTask() {
        FuelRecordCollection records;
        long prevLastModified = Util.getSharedPrefs(mContext).getLong(FetchUserFuelRecordsTask.class.getSimpleName() + "_" + mVehicle.getRemoteId() + "_lastUpdate", 0);

        try {
            // Handle all records deleted on the server
            List<UserFuelRecord> allLocal = UserFuelRecord.getRecordsForVehicle(UserFuelRecord.class, mVehicle);
            List<UserFuelRecord> toDelete = new ArrayList<UserFuelRecord>();
            ModelsActiveRecords active = mService.fuel().active(Long.parseLong(mVehicle.getRemoteId())).execute();
            List<String> activeList = active.getActive();
            if (activeList != null) {
                for (UserFuelRecord rec : allLocal) {
                    String remoteId = rec.getRemoteId();
                    if (remoteId != null && !"".equals(remoteId) && !activeList.contains(remoteId)) {
                        toDelete.add(rec);
                    }
                }

                UserFuelRecord.deleteAllInList(UserFuelRecord.class, toDelete);
            }

            // Get a list of all records currently on the server
            String pageToken = null;
            do {
                Carhub.Fuel.List query = mService.fuel().list(Long.parseLong(mVehicle.getRemoteId()));
                if (prevLastModified != 0) {
                    query = query.setModifiedSince(prevLastModified + "");
                }
                query = query.setPageToken(pageToken);

                // Get a list of all records currently on the server
                records = query.execute();
                if (records != null && records.getItems() != null) {
                    for (FuelRecord r : records.getItems()) {
                        // Try and find a record locally to update
                        UserFuelRecord toUpdate = UserFuelRecord.findByRemoteId(UserFuelRecord.class, r.getServerId());

                        // If one can't be found, create a new one
                        if (toUpdate == null) {
                            toUpdate = new UserFuelRecord();
                        }

                        // Update the local copy with the server information
                        toUpdate.fromAPI(r);
                        toUpdate.setDirty(false);
                        toUpdate.save();
                    }

                    pageToken = records.getNextPageToken();
                }
            } while (pageToken != null);


            // Send all records that are dirty
            for (UserFuelRecord rec : UserFuelRecord.findAllDirty(UserFuelRecord.class)) {
                // Convert to UserVehicle
                FuelRecord toSend = rec.toAPI();

                // Send to AppEngine
                FuelRecord sent;

                if (toSend.getServerId() != null &&
                        !"".equals(toSend.getServerId())) {
                    sent = mService.fuel().update(toSend).execute();
                } else {
                    sent = mService.fuel().add(toSend).execute();
                }

                // Update our local copy (last updated, remote id, etc)
                rec.fromAPI(sent);

                // Save the record
                rec.setDirty(false);
                rec.save();
            }

            long currentTime = System.currentTimeMillis();
            Util.getSharedPrefs(mContext).edit().putLong(FetchUserFuelRecordsTask.class.getSimpleName() + "_" + mVehicle.getRemoteId() + "_lastUpdate", currentTime).commit();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
