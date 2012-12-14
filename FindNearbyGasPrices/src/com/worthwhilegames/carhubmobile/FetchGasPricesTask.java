package com.worthwhilegames.carhubmobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.worthwhilegames.carhubmobile.models.GasPriceRecord;

public class FetchGasPricesTask extends AsyncTask<String, Integer, String> {

	interface FetchGasPricesTaskCallback {
		void gasPricesDidUpdate();
	}

	private Context mContext;
	private FetchGasPricesTaskCallback mDelegate;

	public FetchGasPricesTask(Context ctx) {
		this.mContext = ctx;
	}

	public FetchGasPricesTask(Context ctx, FetchGasPricesTaskCallback delegate) {
		this.mContext = ctx;
		this.mDelegate = delegate;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
	 */
	@Override
	protected void onProgressUpdate(Integer... progress) {
		// not used
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
		// not used
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(String r) {
		try {
			JSONObject result = new JSONObject(r);
			JSONArray stations = result.getJSONArray("stations");
			Log.e("Number of Stations:", "" + stations.length());

			if (stations.length() > 0) {
				// Delete all previous records
				GasPriceRecord.deleteAll(GasPriceRecord.class);

				// Add all records from the current request
				for (int i = 0; i < stations.length(); i++) {
					JSONObject row = stations.getJSONObject(i);

					GasPriceRecord newRecord = new GasPriceRecord(mContext);
					newRecord.setAddress(row.getString("address"));
					newRecord.setStationId(row.getString("id"));
					newRecord.setStation(row.getString("station"));
					newRecord.setPrice(row.getString("price"));
					newRecord.setDistance(row.getString("distance"));
					newRecord.setLastUpdated(row.getString("date"));
					newRecord.setRegion(row.getString("region"));
					newRecord.setCity(row.getString("city"));
					newRecord.save();

					if (Util.isDebugBuild) {
						Log.e("Station Name:", newRecord.getStation());
						Log.e("Address:", newRecord.getAddress());
						Log.e("Price:", newRecord.getPrice());
						Log.e("Distance:", newRecord.getDistance());
					}
				}
			}
		} catch (JSONException ex) {
			Log.e("Exception:", "Request not completed");
		}

		if (mDelegate != null) {
			mDelegate.gasPricesDidUpdate();
		}
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected String doInBackground(String... params) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(params[0]);
		try {
			Log.e("", params[0]);
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();

			// Good code
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;

				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}

			} else {
				Log.e(FetchGasPricesTask.class.toString(), "Failed to get request");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return builder.toString();
	}
}