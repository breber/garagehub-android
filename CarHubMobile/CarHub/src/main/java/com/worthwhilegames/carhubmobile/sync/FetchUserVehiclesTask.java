package com.worthwhilegames.carhubmobile.sync;

import android.content.Context;
import com.google.api.services.carhub.model.*;
import com.google.api.services.carhub.*;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;
import com.worthwhilegames.carhubmobile.util.AuthenticatedHttpRequest;

import java.io.IOException;
import java.util.List;

public class FetchUserVehiclesTask extends AuthenticatedHttpRequest {

	public FetchUserVehiclesTask(Context ctx, Carhub service) {
		this(ctx, service, null);
	}

	public FetchUserVehiclesTask(Context ctx, Carhub service, AuthenticatedHttpRequestCallback delegate) {
		super(ctx, service, delegate);
	}

    @Override
    public String doInBackground(Void ... unused) {
        UserVehicleCollection vehicles;
        long currentTime = System.currentTimeMillis();

        try {
            // Send all records that are dirty
            List<UserVehicleRecord> dirtyRecords = UserVehicleRecord.findAllDirty(UserVehicleRecord.class);
            for (UserVehicleRecord rec : dirtyRecords) {
                // Convert to UserVehicle
                UserVehicle toSend = rec.toUserVehicle();

                // Send to AppEngine
                UserVehicle sent = mService.vehicles().store(toSend).execute();

                // Update our local copy (last updated, remote id, etc)
                rec.fromAPI(sent);

                // Save the record
                rec.setDirty(false);
                rec.setLastUpdated(currentTime);
                rec.save();
            }

            // Get a list of all records currently on the server
            vehicles = mService.vehicles().list().execute();
            if (vehicles != null) {
                for (UserVehicle v : vehicles.getItems()) {
                    // Try and find a record locally to update
                    UserVehicleRecord toUpdate = UserVehicleRecord.findByRemoteId(UserVehicleRecord.class, v.getAppengineId());

                    // If one can't be found, create a new one
                    if (toUpdate == null) {
                        toUpdate = new UserVehicleRecord(mContext);
                    }

                    // Update the local copy with the server information
                    toUpdate.fromAPI(v);
                    toUpdate.setLastUpdated(currentTime);
                    toUpdate.save();
                }

                // TODO: go through all records that we have locally, but not remotely and delete them

            }
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
