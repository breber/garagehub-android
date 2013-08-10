package com.worthwhilegames.carhubmobile.sync;

import android.content.Context;
import com.google.api.services.carhub.Carhub;
import com.google.api.services.carhub.model.FuelRecord;
import com.google.api.services.carhub.model.FuelRecordCollection;
import com.google.api.services.carhub.model.ModelsActiveRecords;
import com.worthwhilegames.carhubmobile.Util;
import com.worthwhilegames.carhubmobile.models.UserFuelRecord;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;
import com.worthwhilegames.carhubmobile.util.AuthenticatedHttpRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FetchUserFuelRecordsTask extends AuthenticatedHttpRequest {

    private UserVehicleRecord mVehicle;

    public FetchUserFuelRecordsTask(Context ctx, Carhub service, AuthenticatedHttpRequestCallback delegate, UserVehicleRecord vehicle) {
        super(ctx, service, delegate);

        mVehicle = vehicle;
    }

    @Override
    public String doInBackground(Void ... unused) {
        FuelRecordCollection records;
        long prevLastModified = Util.getSharedPrefs(mContext).getLong(FetchUserFuelRecordsTask.class.getSimpleName() + "_" + mVehicle.getRemoteId() + "_lastUpdate", 0);

        try {
            // Handle all records deleted on the server
            List<UserFuelRecord> allLocal = UserFuelRecord.listAll(UserFuelRecord.class);
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
                            toUpdate = new UserFuelRecord(mContext);
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
                    sent = mService.fuel().update(toSend.getServerId(), toSend).execute();
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
        }

        return "";
    }

    @Override
    protected void onPostExecute(Object r) {
        super.onPostExecute(r);

        if (mDelegate != null) {
            mDelegate.taskDidFinish(FetchUserFuelRecordsTask.class);
        }
    }
}
