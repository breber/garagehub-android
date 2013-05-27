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
import com.worthwhilegames.carhubmobile.models.UserBaseExpenseRecord;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;

public class FetchUserBaseExpenseRecordsTask extends AuthenticatedHttpRequest {

	private UserVehicleRecord mVehicle;

	public FetchUserBaseExpenseRecordsTask(Context ctx, UserVehicleRecord aVehicle) {
		this(ctx, aVehicle, null);
	}

	public FetchUserBaseExpenseRecordsTask(Context ctx, UserVehicleRecord aVehicle, AuthenticatedHttpRequestCallback delegate) {
		super(ctx, delegate);
		this.mVehicle = aVehicle;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		this.isPost = false;
		// TODO: update to only get changes since last sync
		this.url = Constants.WEBSITE_URL + "api/expense/base/" + mVehicle.getRemoteId();
	}

	@Override
	protected void processData(String r) {
		Log.d(FetchUserBaseExpenseRecordsTask.class.getName(), "Result: " + r);

		if (r != null) {
			// TODO: actually perform syncing of data (not just fetch)
			try {
				JSONObject result = new JSONObject(r);
				JSONArray activeIds = result.getJSONArray("activeIds");
				Set<String> activeIdSet = new HashSet<String>();
				List<UserBaseExpenseRecord> allRecords = UserBaseExpenseRecord.listAll(UserBaseExpenseRecord.class);

				// Build a Set of exiting IDs for easy lookup
				for (int i = 0; i < activeIds.length(); i++) {
					activeIdSet.add(activeIds.getInt(i) + "");
				}

				// Go through and delete records that aren't active anymore
				for (SyncableRecord rec : allRecords) {
					if (!activeIdSet.contains(rec.getRemoteId())) {
						Log.d(FetchUserBaseExpenseRecordsTask.class.getName(), "Deleting: " + rec.getRemoteId());
						rec.delete();
					}
				}

				// Add all records from the current request
				JSONArray records = result.getJSONArray("records");
				for (int i = 0; i < records.length(); i++) {
					JSONObject row = records.getJSONObject(i);
					String remoteId = row.getString("id");
					List<UserBaseExpenseRecord> existingRecords = null;
					UserBaseExpenseRecord newRecord = null;

					if (remoteId == null) {
						continue;
					}

					existingRecords = UserBaseExpenseRecord.find(UserBaseExpenseRecord.class, StringUtil.toSQLName("mRemoteId") + " = ?", remoteId);

					if (existingRecords == null || existingRecords.isEmpty()) {
						newRecord = new UserBaseExpenseRecord(super.mContext);
					} else {
						newRecord = existingRecords.get(0);
					}

					if (newRecord != null) {
						newRecord.setRemoteId(remoteId);
						newRecord.setLastUpdated((long) row.getDouble("lastmodified"));
						newRecord.setVehicle(mVehicle);
						JSONObject date = row.getJSONObject("date");
						newRecord.setDate(date.getLong("timestamp"));
						newRecord.setCategoryId("" + row.getInt("categoryid")); // TODO
						newRecord.setLocation(row.getString("location"));
						newRecord.setDescription(row.getString("description"));
						newRecord.setAmount((float) row.getDouble("amount"));
						newRecord.setPictureUrl(row.getString("pictureurl"));

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
