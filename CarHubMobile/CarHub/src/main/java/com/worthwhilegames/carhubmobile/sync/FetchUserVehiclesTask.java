package com.worthwhilegames.carhubmobile.sync;

import android.content.Context;
import com.google.api.services.carhub.Carhub;
import com.google.api.services.carhub.model.ModelsActiveRecords;
import com.google.api.services.carhub.model.UserVehicle;
import com.google.api.services.carhub.model.UserVehicleCollection;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;
import com.worthwhilegames.carhubmobile.util.AuthenticatedHttpRequest;

import java.io.IOException;
import java.util.ArrayList;
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
            // Handle all records deleted on the server
            List<UserVehicleRecord> allLocalVehicles = UserVehicleRecord.listAll(UserVehicleRecord.class);
            List<UserVehicleRecord> toDeleteVehicles = new ArrayList<UserVehicleRecord>();
            ModelsActiveRecords activeVehicles = mService.vehicle().active().execute();
            List<String> activeVehiclesList = activeVehicles.getActive();
            for (UserVehicleRecord rec : allLocalVehicles) {
                if (!activeVehiclesList.contains(rec.getRemoteId())) {
                    toDeleteVehicles.add(rec);
                }
            }

            UserVehicleRecord.deleteAllInList(UserVehicleRecord.class, toDeleteVehicles);


            // Get a list of all records currently on the server
            vehicles = mService.vehicle().list().execute();
            if (vehicles != null && vehicles.getItems() != null) {
                for (UserVehicle v : vehicles.getItems()) {
                    // Try and find a record locally to update
                    UserVehicleRecord toUpdate = UserVehicleRecord.findByRemoteId(UserVehicleRecord.class, v.getServerId());

                    // If one can't be found, create a new one
                    if (toUpdate == null) {
                        toUpdate = new UserVehicleRecord(mContext);
                    }

                    // Update the local copy with the server information
                    toUpdate.fromAPI(v);
                    toUpdate.setDirty(false);
                    toUpdate.save();
                }
            }


            // Send all records that are dirty
            List<UserVehicleRecord> dirtyRecords = UserVehicleRecord.findAllDirty(UserVehicleRecord.class);
            for (UserVehicleRecord rec : dirtyRecords) {
                // Convert to UserVehicle
                UserVehicle toSend = rec.toAPI();

                // Send to AppEngine
                UserVehicle sent = mService.vehicle().store(toSend).execute();

                // Update our local copy (last updated, remote id, etc)
                rec.fromAPI(sent);

                // Save the record
                rec.setDirty(false);
                rec.save();
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
