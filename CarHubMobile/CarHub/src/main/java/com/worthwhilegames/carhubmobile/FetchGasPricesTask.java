package com.worthwhilegames.carhubmobile;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.worthwhilegames.carhubmobile.models.GasPriceRecord;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

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
		if (mDelegate != null) {
			mDelegate.gasPricesDidUpdate();
		}
	}

	/**
	 * Process data on background thread
	 * 
	 * @param r
	 */
	private void processData(String r) {
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
					newRecord.setLat(row.getString("lat"));
					newRecord.setLng(row.getString("lng"));
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
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected String doInBackground(String... params) {
		String result = null;
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(params[0]);
		try {
			HttpResponse response = client.execute(httpGet);

			if (HttpURLConnection.HTTP_OK == response.getStatusLine().getStatusCode()) {
				result = HttpUtils.readStreamAsString(response.getEntity().getContent());
				processData(result);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}
}
