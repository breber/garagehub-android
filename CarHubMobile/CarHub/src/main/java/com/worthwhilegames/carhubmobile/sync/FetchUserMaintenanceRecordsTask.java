package com.worthwhilegames.carhubmobile.sync;

import android.content.Context;
import com.appspot.car_hub.carhub.Carhub;
import com.appspot.car_hub.carhub.model.MaintenanceRecord;
import com.appspot.car_hub.carhub.model.MaintenanceRecordCollection;
import com.appspot.car_hub.carhub.model.ModelsActiveRecords;
import com.worthwhilegames.carhubmobile.Util;
import com.worthwhilegames.carhubmobile.models.UserMaintenanceRecord;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FetchUserMaintenanceRecordsTask implements ISyncTask {
    /**
     * The Vehicle to get maintenance records for
     */
    private UserVehicleRecord mVehicle;

    /**
     * The context
     */
    private Context mContext;

    /**
     * The Carhub service for interacting with AppEngine
     */
    protected Carhub mService;

    public FetchUserMaintenanceRecordsTask(Context ctx, Carhub service, UserVehicleRecord vehicle) {
        mContext = ctx;
        mService = service;
        mVehicle = vehicle;
    }

    @Override
    public boolean performTask() {
        MaintenanceRecordCollection records;
        long prevLastModified = Util.getSharedPrefs(mContext).getLong(FetchUserMaintenanceRecordsTask.class.getSimpleName() + "_" + mVehicle.getRemoteId() + "_lastUpdate", 0);

        try {
            // Handle all records deleted on the server
            List<UserMaintenanceRecord> allLocal = UserMaintenanceRecord.listAll(UserMaintenanceRecord.class);
            List<UserMaintenanceRecord> toDelete = new ArrayList<UserMaintenanceRecord>();
            ModelsActiveRecords active = mService.maintenance().active(Long.parseLong(mVehicle.getRemoteId())).execute();
            List<String> activeList = active.getActive();
            if (activeList != null) {
                for (UserMaintenanceRecord rec : allLocal) {
                    String remoteId = rec.getRemoteId();
                    if (remoteId != null && !"".equals(remoteId) && !activeList.contains(remoteId)) {
                        toDelete.add(rec);
                    }
                }

                UserMaintenanceRecord.deleteAllInList(UserMaintenanceRecord.class, toDelete);
            }

            // Get a list of all records currently on the server
            String pageToken = null;
            do {
                Carhub.Maintenance.List query = mService.maintenance().list(Long.parseLong(mVehicle.getRemoteId()));
                if (prevLastModified != 0) {
                    query = query.setModifiedSince(prevLastModified + "");
                }
                query = query.setPageToken(pageToken);

                // Get a list of all records currently on the server
                records = query.execute();
                if (records != null && records.getItems() != null) {
                    for (MaintenanceRecord r : records.getItems()) {
                        // Try and find a record locally to update
                        UserMaintenanceRecord toUpdate = UserMaintenanceRecord.findByRemoteId(UserMaintenanceRecord.class, r.getServerId());

                        // If one can't be found, create a new one
                        if (toUpdate == null) {
                            toUpdate = new UserMaintenanceRecord(mContext);
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
            for (UserMaintenanceRecord rec : UserMaintenanceRecord.findAllDirty(UserMaintenanceRecord.class)) {
                // Convert to UserVehicle
                MaintenanceRecord toSend = rec.toAPI();

                // Send to AppEngine
                MaintenanceRecord sent;

                if (toSend.getServerId() != null &&
                        !"".equals(toSend.getServerId())) {
                    sent = mService.maintenance().update(toSend).execute();
                } else {
                    sent = mService.maintenance().add(toSend).execute();
                }

                // Update our local copy (last updated, remote id, etc)
                rec.fromAPI(sent);

                // Save the record
                rec.setDirty(false);
                rec.save();
            }

            long currentTime = System.currentTimeMillis();
            Util.getSharedPrefs(mContext).edit().putLong(FetchUserMaintenanceRecordsTask.class.getSimpleName() + "_" + mVehicle.getRemoteId() + "_lastUpdate", currentTime).commit();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
