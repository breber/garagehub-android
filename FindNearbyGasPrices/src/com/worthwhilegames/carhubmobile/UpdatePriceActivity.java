package com.worthwhilegames.carhubmobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author jamiekujawa
 */
public class UpdatePriceActivity extends Activity {

	public static final String EXTRA_STATION_NAME = "StationName";
	public static final String EXTRA_STATION_ADDRESS = "StationAddress";
	public static final String EXTRA_STATION_ID = "StationID";

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// get the current intent
		Intent i = this.getIntent();

		// get the string extras passed in
		String stationName = i.getStringExtra(EXTRA_STATION_NAME);
		String stationAddress = i.getStringExtra(EXTRA_STATION_ADDRESS);
		i.getStringExtra(EXTRA_STATION_ID);

		// set layout
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_price);

		// get the progress bar item
		ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar2);
		pb.setVisibility(View.INVISIBLE);

		SharedPreferences sharedPref = Util.getSharedPrefs(this);
		Spinner fuelTypeSpinner = (Spinner) findViewById(R.id.spinnerfueltypeUpdate);

		// get the current selected option from the shared preferences
		if (fuelTypeSpinner != null) {
			// get the value from shared preferences
			String fuelType = sharedPref.getString("Fuel Type", "Mid");

			// make an array adapter of all options specified in the xml
			@SuppressWarnings("unchecked")
			ArrayAdapter<String> fuelTypeAdapter = (ArrayAdapter<String>) fuelTypeSpinner
			.getAdapter();

			// find the current position
			int spinnerPosition = fuelTypeAdapter.getPosition(fuelType.toString());

			// set the correct position to true
			fuelTypeSpinner.setSelection(spinnerPosition);
		}

		// set the current view fields
		TextView stationNameTextView = (TextView) findViewById(R.id.stationName);
		stationNameTextView.setText(stationName);

		TextView stationAddressTextView = (TextView) findViewById(R.id.stationAddress);
		stationAddressTextView.setText(stationAddress);

		Button updatePrice = (Button) findViewById(R.id.updatePrice);
		updatePrice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = UpdatePriceActivity.this.getIntent();

				// String stationName = i.getStringExtra("StationName");
				// String stationAddress = i.getStringExtra("StationAddress");

				String stationID = i.getStringExtra("StationID");
				EditText newPrice = (EditText) findViewById(R.id.newPrice);

				if ("".equals(newPrice.getText().toString().trim())) {
					Toast.makeText(UpdatePriceActivity.this,
							"Please Enter a new price.", Toast.LENGTH_SHORT)
							.show();
				} else {
					Spinner fuelTypeSpinner = (Spinner) findViewById(R.id.spinnerfueltypeUpdate);

					UpdatePriceRequest update = new UpdatePriceRequest();
					update.execute(
							"http://api.mygasfeed.com/locations/price/zax22arsix.json",
							newPrice.getText().toString(),
							(String) fuelTypeSpinner.getSelectedItem(),
							stationID);

					ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar2);
					pb.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	/**
	 * @author jamiekujawa
	 */
	class UpdatePriceRequest extends AsyncTask<String, Integer, String> {

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
			ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar2);
			pb.setVisibility(View.INVISIBLE);

			JSONObject result = null;

			// Convert string to object
			try {
				result = new JSONObject(r);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (result != null) {
				try {
					JSONObject status = result.getJSONObject("status");
					String message = status.getString("message");

					if (status.get("error").equals("NO")) {
						Toast.makeText(UpdatePriceActivity.this,
								message + ". Successfully updated price.",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(UpdatePriceActivity.this,
								"Could not update price.", Toast.LENGTH_SHORT)
								.show();
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@SuppressLint("DefaultLocale")
		@Override
		protected String doInBackground(String... params) {
			Log.e("", "---UpdatePriceInBackground---");
			StringBuilder builder = new StringBuilder();
			HttpClient httpclient;
			HttpPost httppost;
			ArrayList<NameValuePair> postParameters;
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost(params[0]);

			try {
				Log.e("URL", params[0]);
				Log.e("Station Price", params[1]);
				Log.e("Station Fuel Type", params[2]);
				Log.e("Station ID", params[3]);
				postParameters = new ArrayList<NameValuePair>();
				postParameters.add(new BasicNameValuePair("price", params[1].trim()));
				postParameters.add(new BasicNameValuePair("fueltype", params[2].toLowerCase().trim()));
				postParameters.add(new BasicNameValuePair("stationid", params[3].trim()));
				httppost.setEntity(new UrlEncodedFormEntity(postParameters));
				HttpResponse response = httpclient.execute(httppost);

				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;

				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Log.e("---Result---:", builder.toString());
			return builder.toString();
		}
	}

}
