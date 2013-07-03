package com.worthwhilegames.carhubmobile.sync;

import android.content.Context;
import com.google.api.services.carhub.Carhub;
import com.google.api.services.carhub.model.MaintenanceRecord;
import com.google.api.services.carhub.model.MaintenanceRecordCollection;
import com.google.api.services.carhub.model.ModelsActiveRecords;
import com.worthwhilegames.carhubmobile.Util;
import com.worthwhilegames.carhubmobile.models.UserMaintenanceRecord;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;
import com.worthwhilegames.carhubmobile.util.AuthenticatedHttpRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FetchUserMaintenanceRecordsTask extends AuthenticatedHttpRequest {

    private UserVehicleRecord mVehicle;

    public FetchUserMaintenanceRecordsTask(Context ctx, Carhub service, AuthenticatedHttpRequestCallback delegate, UserVehicleRecord vehicle) {
        super(ctx, service, delegate);

        mVehicle = vehicle;
    }

    @Override
    public String doInBackground(Void ... unused) {
        MaintenanceRecordCollection records;
        long prevLastModified = Util.getSharedPrefs(mContext).getLong(FetchUserMaintenanceRecordsTask.class.getSimpleName() + "_lastUpdate", 0);

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
                Carhub.Maintenance.List query = mService.maintenance().list(Integer.parseInt(mVehicle.getRemoteId()));
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
                    sent = mService.maintenance().update(toSend.getServerId(), toSend).execute();
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
            Util.getSharedPrefs(mContext).edit().putLong(FetchUserMaintenanceRecordsTask.class.getSimpleName() + "_lastUpdate", currentTime).commit();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    protected void onPostExecute(Object r) {
        super.onPostExecute(r);

        if (mDelegate != null) {
            mDelegate.taskDidFinish();
        }
    }
}
