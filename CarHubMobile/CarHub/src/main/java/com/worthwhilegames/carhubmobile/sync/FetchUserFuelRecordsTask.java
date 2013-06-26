package com.worthwhilegames.carhubmobile.sync;

import android.content.Context;
import com.google.api.services.carhub.Carhub;
import com.google.api.services.carhub.model.FuelRecord;
import com.google.api.services.carhub.model.FuelRecordCollection;
import com.worthwhilegames.carhubmobile.Util;
import com.worthwhilegames.carhubmobile.models.UserFuelRecord;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;
import com.worthwhilegames.carhubmobile.util.AuthenticatedHttpRequest;

import java.io.IOException;

public class FetchUserFuelRecordsTask extends AuthenticatedHttpRequest {

    private UserVehicleRecord mVehicle;

    public FetchUserFuelRecordsTask(Context ctx, Carhub service, AuthenticatedHttpRequestCallback delegate, UserVehicleRecord vehicle) {
        super(ctx, service, delegate);

        mVehicle = vehicle;
    }

    @Override
    public String doInBackground(Void ... unused) {
        FuelRecordCollection records;
        long prevLastModified = Util.getSharedPrefs(mContext).getLong(FetchUserFuelRecordsTask.class.getSimpleName() + "_lastUpdate", 0);
        long currentTime = System.currentTimeMillis();

        try {
            // Send all records that are dirty
            for (UserFuelRecord rec : UserFuelRecord.findAllDirty(UserFuelRecord.class)) {
                // Convert to UserVehicle
                FuelRecord toSend = rec.toAPI();

                // Send to AppEngine
                FuelRecord sent = mService.fuel().store(toSend).execute();

                // Update our local copy (last updated, remote id, etc)
                rec.fromAPI(sent);

                // Save the record
                rec.setDirty(false);
                rec.setLastUpdated(currentTime);
                rec.save();
            }

            String pageToken = null;

            do {
                Carhub.Fuel.List query = mService.fuel().list(Integer.parseInt(mVehicle.getRemoteId()));
                if (prevLastModified != 0) {
                    query = query.setModifiedSince(prevLastModified + "");
                }
                query = query.setOrder("-odometerEnd");
                query = query.setPageToken(pageToken);

                // Get a list of all records currently on the server
                records = query.execute();
                if (records != null) {
                    for (FuelRecord r : records.getItems()) {
                        // Try and find a record locally to update
                        UserFuelRecord toUpdate = UserFuelRecord.findByRemoteId(UserFuelRecord.class, r.getServerId());

                        // If one can't be found, create a new one
                        if (toUpdate == null) {
                            toUpdate = new UserFuelRecord(mContext);
                        }

                        // Update the local copy with the server information
                        toUpdate.fromAPI(r);
                        toUpdate.setLastUpdated(currentTime);
                        toUpdate.save();
                    }

                    pageToken = records.getNextPageToken();
                }
            } while (pageToken != null);

            // TODO: go through all records that we have locally, but not remotely and delete them

            Util.getSharedPrefs(mContext).edit().putLong(FetchUserFuelRecordsTask.class.getSimpleName() + "_lastUpdate", currentTime).commit();
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
