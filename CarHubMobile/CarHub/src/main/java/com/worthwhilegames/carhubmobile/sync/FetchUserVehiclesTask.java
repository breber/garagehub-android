package com.worthwhilegames.carhubmobile.sync;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.brianreber.library.AuthenticatedHttpRequest;
import com.orm.StringUtil;
import com.worthwhilegames.carhubmobile.Constants;
import com.worthwhilegames.carhubmobile.models.SyncableRecord;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;

public class FetchUserVehiclesTask extends AuthenticatedHttpRequest {

	public FetchUserVehiclesTask(Context ctx) {
		this(ctx, null);
	}

	public FetchUserVehiclesTask(Context ctx, AuthenticatedHttpRequestCallback delegate) {
		super(ctx, delegate);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		this.isPost = false;
		// TODO: update to only get changes since last sync
		this.url = Constants.WEBSITE_URL + "api/vehicles/list";
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
	protected void onPostExecute(String r) {
		super.onPostExecute(r);

		if (mDelegate != null) {
			mDelegate.taskDidFinish();
		}
	}
}
