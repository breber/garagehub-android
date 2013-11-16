package com.worthwhilegames.carhubmobile.sync;

import android.content.Context;
import com.appspot.car_hub.carhub.Carhub;
import com.appspot.car_hub.carhub.model.ModelsActiveRecords;
import com.appspot.car_hub.carhub.model.UserVehicle;
import com.appspot.car_hub.carhub.model.UserVehicleCollection;
import com.worthwhilegames.carhubmobile.Util;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FetchUserVehiclesTask implements ISyncTask {
    /**
     * The context
     */
    private Context mContext;

    /**
     * The Carhub service for interacting with AppEngine
     */
    protected Carhub mService;

    public FetchUserVehiclesTask(Context ctx, Carhub service) {
        mContext = ctx;
        mService = service;
    }

    @Override
    public boolean performTask() {
        UserVehicleCollection records;
        long prevLastModified = Util.getSharedPrefs(mContext).getLong(FetchUserVehiclesTask.class.getSimpleName() + "_lastUpdate", 0);

        try {
            // Handle all records deleted on the server
            List<UserVehicleRecord> allLocalVehicles = UserVehicleRecord.listAll(UserVehicleRecord.class);
            List<UserVehicleRecord> toDelete = new ArrayList<UserVehicleRecord>();
            ModelsActiveRecords activeVehicles = mService.vehicle().active().execute();
            List<String> activeList = activeVehicles.getActive();
            if (activeList != null) {
                for (UserVehicleRecord rec : allLocalVehicles) {
                    String remoteId = rec.getRemoteId();
                    if (remoteId != null && !"".equals(remoteId) && !activeList.contains(remoteId)) {
                        toDelete.add(rec);
                    }
                }

                UserVehicleRecord.deleteAllInList(UserVehicleRecord.class, toDelete);
            }

            // Get a list of all records currently on the server
            String pageToken = null;
            do {
                Carhub.Vehicle.List query = mService.vehicle().list();
                if (prevLastModified != 0) {
                    query = query.setModifiedSince(prevLastModified + "");
                }
                query = query.setPageToken(pageToken);

                // Get a list of all records currently on the server
                records = query.execute();
                if (records != null && records.getItems() != null) {
                    for (UserVehicle r : records.getItems()) {
                        // Try and find a record locally to update
                        UserVehicleRecord toUpdate = UserVehicleRecord.findByRemoteId(UserVehicleRecord.class, r.getServerId());

                        // If one can't be found, create a new one
                        if (toUpdate == null) {
                            toUpdate = new UserVehicleRecord(mContext);
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
            List<UserVehicleRecord> dirtyRecords = UserVehicleRecord.findAllDirty(UserVehicleRecord.class);
            for (UserVehicleRecord rec : dirtyRecords) {
                // Convert to UserVehicle
                UserVehicle toSend = rec.toAPI();

                // Send to AppEngine
                UserVehicle sent;

                if (toSend.getServerId() != null &&
                        !"".equals(toSend.getServerId())) {
                    sent = mService.vehicle().update(toSend.getServerId(), toSend).execute();
                } else {
                    sent = mService.vehicle().add(toSend).execute();
                }

                // Update our local copy (last updated, remote id, etc)
                rec.fromAPI(sent);

                // Save the record
                rec.setDirty(false);
                rec.save();
            }

            long currentTime = System.currentTimeMillis();
            Util.getSharedPrefs(mContext).edit().putLong(FetchUserVehiclesTask.class.getSimpleName() + "_lastUpdate", currentTime).commit();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
