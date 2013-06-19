package com.worthwhilegames.carhubmobile.sync;

import android.content.Context;
import android.util.Log;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.carhub.model.*;
import com.google.api.services.carhub.*;
import com.orm.StringUtil;
import com.worthwhilegames.carhubmobile.models.SyncableRecord;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;
import com.worthwhilegames.carhubmobile.util.AuthenticatedHttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        try {
            vehicles = mService.vehicles().list().execute();
//            if (vehicles != null) {
//                for (UserVehicle v : vehicles.getItems()) {
//                    Log.d("USERVEHICLE", v.toString());
//                }
//            }
        } catch (IOException e) {
            Log.d("VehicleList", e.getMessage(), e);
        }

        return "";
    }

	@Override
	protected void processData(String r) {
		Log.d(FetchUserVehiclesTask.class.getName(), "Result: " + r);
		if (r != null) {
			// TODO: actually perform syncing of data (not just fetch)
			try {
				JSONObject result = new JSONObject(r);
				JSONArray activeIds = result.getJSONArray("activeIds");
				Set<String> activeIdSet = new HashSet<String>();
				List<UserVehicleRecord> allRecords = UserVehicleRecord.listAll(UserVehicleRecord.class);

				// Build a Set of exiting IDs for easy lookup
				for (int i = 0; i < activeIds.length(); i++) {
					activeIdSet.add(activeIds.getInt(i) + "");
				}

				// Go through and delete records that aren't active anymore
				for (SyncableRecord rec : allRecords) {
					if (!activeIdSet.contains(rec.getRemoteId())) {
						Log.d(FetchUserVehiclesTask.class.getName(), "Deleting: " + rec.getRemoteId());
						rec.delete();
					}
				}

				// Add all records from the current request
				JSONArray records = result.getJSONArray("vehicles");
				for (int i = 0; i < records.length(); i++) {
					JSONObject row = records.getJSONObject(i);
					String remoteId = row.getString("id");
					List<UserVehicleRecord> existingRecords = null;
					UserVehicleRecord newRecord = null;

					if (remoteId == null) {
						continue;
					}

					existingRecords = UserVehicleRecord.find(UserVehicleRecord.class, StringUtil.toSQLName("mRemoteId") + " = ?", remoteId);

					if (existingRecords == null || existingRecords.isEmpty()) {
						newRecord = new UserVehicleRecord(super.mContext);
					} else {
						newRecord = existingRecords.get(0);
					}

					if (newRecord != null) {
						newRecord.setRemoteId(remoteId);
						newRecord.setMake(row.getString("make"));
						newRecord.setModel(row.getString("model"));
						newRecord.setYear(row.getString("year"));
						newRecord.setColor(row.getString("color"));
						newRecord.setPlates(row.getString("plates"));
						newRecord.setLastUpdated((long) row.getDouble("lastmodified"));
						newRecord.save();
					}
				}
			} catch (JSONException ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	protected void onPostExecute(Object r) {
		super.onPostExecute(r);

		if (mDelegate != null) {
			mDelegate.taskDidFinish();
		}
	}
}
