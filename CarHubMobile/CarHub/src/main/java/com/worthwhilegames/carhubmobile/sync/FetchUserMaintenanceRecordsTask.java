package com.worthwhilegames.carhubmobile.sync;

import android.content.Context;
import com.google.api.services.carhub.Carhub;
import com.google.api.services.carhub.model.MaintenanceRecord;
import com.google.api.services.carhub.model.MaintenanceRecordCollection;
import com.worthwhilegames.carhubmobile.Util;
import com.worthwhilegames.carhubmobile.models.UserMaintenanceRecord;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;
import com.worthwhilegames.carhubmobile.util.AuthenticatedHttpRequest;

import java.io.IOException;
import java.util.List;

public class FetchUserMaintenanceRecordsTask extends AuthenticatedHttpRequest {

    private UserVehicleRecord mVehicle;

	public FetchUserMaintenanceRecordsTask(Context ctx, Carhub service, AuthenticatedHttpRequestCallback delegate, UserVehicleRecord vehicle) {
		super(ctx, service, delegate);

        mVehicle = vehicle;
	}

    @Override
    public String doInBackground(Void ... unused) {
        MaintenanceRecordCollection maintenanceRecords;
        long prevLastModified = Util.getSharedPrefs(mContext).getLong(FetchUserMaintenanceRecordsTask.class.getSimpleName() + "_lastUpdate", 0);
        long currentTime = System.currentTimeMillis();

        try {
            // Send all records that are dirty
            List<UserMaintenanceRecord> dirtyRecords = UserMaintenanceRecord.findAllDirty(UserMaintenanceRecord.class);
            for (UserMaintenanceRecord rec : dirtyRecords) {
                // Convert to UserVehicle
                MaintenanceRecord toSend = rec.toAPI();

                // Send to AppEngine
                MaintenanceRecord sent = mService.maintenance().store(toSend).execute();

                // Update our local copy (last updated, remote id, etc)
                rec.fromAPI(sent);

                // Save the record
                rec.setDirty(false);
                rec.setLastUpdated(currentTime);
                rec.save();
            }

            // Get a list of all records currently on the server
            maintenanceRecords = mService.maintenance().list(Integer.parseInt(mVehicle.getRemoteId())).setModifiedSince(prevLastModified + "").execute();
            if (maintenanceRecords != null) {
                for (MaintenanceRecord r : maintenanceRecords.getItems()) {
                    // Try and find a record locally to update
                    UserMaintenanceRecord toUpdate = UserMaintenanceRecord.findByRemoteId(UserMaintenanceRecord.class, r.getServerId());

                    // If one can't be found, create a new one
                    if (toUpdate == null) {
                        toUpdate = new UserMaintenanceRecord(mContext);
                    }

                    // Update the local copy with the server information
                    toUpdate.fromAPI(r);
                    toUpdate.setLastUpdated(currentTime);
                    toUpdate.save();
                }

                // TODO: go through all records that we have locally, but not remotely and delete them

            }

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
