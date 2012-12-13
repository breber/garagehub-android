package com.worthwhilegames.carhubmobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author jamiekujawa
 *
 */
@SuppressLint("DefaultLocale")
public class FindGasPricesActivity extends Activity {

	/**
	 * A SharedPreferences object to get the preferences set by the user
	 */
	private SharedPreferences sharedPref;

	/**
	 * A Location object to keep track of the current location
	 */
	private Location currentLocation;
	
	/**
	 * A double representing the current latitude
	 */
	private double currentLatitude;
	
	/**
	 * A double representing the current longitude
	 */
	private double currentLongitude;
	
	/**
	 * A Geocoder object for address translation
	 */
	private Geocoder geo;
	
	/**
	 * A listener to update the current location of the Android device
	 */
	private LocationListener locationListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		//initialize
		currentLatitude = 0.0;
		currentLongitude = 0.0;
		
		//create
		super.onCreate(savedInstanceState);
		
		//set view
		setContentView(R.layout.find_gas_prices);

		//create a new geocoder
		geo = new Geocoder(FindGasPricesActivity.this);
		
		//create a location manager
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		//create a new progress bar to be displayed when the app is searching for
		//gas prices
		ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
		pb.setVisibility(View.INVISIBLE);

		//create a new location listener. This will update the location everytime
		//the device has a location change
		locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				updateLocation(location);
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// not used
			}

			public void onProviderEnabled(String provider) {
				// not used
			}

			public void onProviderDisabled(String provider) {
				// not used
			}
		};
		
		//Update with the last known good location so the user will get search results
		String locationProvider = LocationManager.NETWORK_PROVIDER;
		updateLocation(locationManager.getLastKnownLocation(locationProvider));

		//request updates from both the network provider as well as the GPS signal
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 0, 0, locationListener);

		//search button listener
		Button search = (Button) findViewById(R.id.goButton);
		search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TableLayout myTable = (TableLayout) FindGasPricesActivity.this
						.findViewById(R.id.main_table);
				myTable.removeAllViews();
				ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
				pb.setVisibility(View.VISIBLE);
				getGasPrices();
			}
			
		});
	}

	/**
	 * Function to update the latitude and longitude variables
	 * @param location a location object with the new location
	 */
	void updateLocation(Location location) {
		currentLocation = location;
		
		double latitude = currentLocation.getLatitude();
		double longitude = currentLocation.getLongitude();
		
		if(latitude != 0.0){
			currentLatitude = currentLocation.getLatitude();
		}
		
		if(longitude != 0.0){
			currentLongitude = currentLocation.getLongitude();
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		//This will cover the Android menu button press
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent settingsButtonClick = new Intent(FindGasPricesActivity.this,
					SettingsActivity.class);
			startActivity(settingsButtonClick);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume(){
		
		super.onResume();
		TableLayout myTable = (TableLayout) FindGasPricesActivity.this
				.findViewById(R.id.main_table);
		myTable.removeAllViews();
		ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
		pb.setVisibility(View.VISIBLE);
		getGasPrices();
		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
		
	}

	/**
	 * This method will send a request to myGasFeed to get the gas prices
	 */
	@SuppressLint("DefaultLocale")
	public void getGasPrices() {

		EditText zip = (EditText) findViewById(R.id.editText1);
		String zipCode = zip.getText().toString();

		// pattern to match for the zip code
		Pattern pattern = Pattern.compile("[0-9][0-9][0-9][0-9][0-9]");
		Matcher matcher = pattern.matcher(zipCode);

		// create the shared preferences object
		sharedPref = this.getSharedPreferences("Preferences", 0);
		Boolean useCurrentLocation = sharedPref.getBoolean(
				"useCurrentLocation", false);

		if (matcher.matches() || useCurrentLocation) {

			List<Address> addresses = null;

			// Convert zip code to address
			try {
				
				Log.e("Longitude: ", "" + currentLongitude);
				Log.e("Latitude: ", "" + currentLatitude);

				int distance = sharedPref.getInt("Distance", 5);
				String fuelType = sharedPref.getString("Fuel Type", "Mid");
				String sortBy = sharedPref.getString("Sort By", "Price");

				JSONRequest request = new JSONRequest();

				double lat;
				double lon;

				if (useCurrentLocation) {
					lat = currentLatitude;
					lon = currentLongitude;
				} else {
					addresses = geo.getFromLocationName(zipCode, 1);
					lat = addresses.get(0).getLatitude();
					lon = addresses.get(0).getLongitude();
				}

				// execute the get request
				request.execute("http://api.mygasfeed.com/stations/radius/" + lat
						+ "/" + lon + "/" + distance + "/"
						+ fuelType.toLowerCase().trim() + "/"
						+ sortBy.toLowerCase() + "/zax22arsix.json".trim());
				
			} catch (IOException e) {
				Toast.makeText(this, "Please try search again. Unable to find current location.",
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}


		} else {
			Toast.makeText(this, "Please enter a valid zip code",
					Toast.LENGTH_LONG).show();
			ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
			pb.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * @author jamiekujawa
	 *
	 */
	class JSONRequest extends AsyncTask<String, Integer, String> {

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		 */
		protected void onProgressUpdate(Integer... progress) {
			// not used
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		protected void onPreExecute() {
			// not used
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@SuppressWarnings("deprecation")
		protected void onPostExecute(String r) {
			Log.e("result:", r.toString());

			JSONObject result = null;

			// Convert string to object
			try {
				result = new JSONObject(r);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
			pb.setVisibility(View.INVISIBLE);
			TableLayout myTable = (TableLayout) FindGasPricesActivity.this
					.findViewById(R.id.main_table);
			myTable.removeAllViews();
			int idCounter = 1000;

			TableRow rowHeading = new TableRow(FindGasPricesActivity.this);
			rowHeading.setId(idCounter++);
			rowHeading.setBackgroundColor(Color.GRAY);
			rowHeading.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

			// Station name
			TextView labelName = new TextView(FindGasPricesActivity.this);
			labelName.setId(idCounter++);
			labelName.setText("Station");
			labelName.setTextColor(Color.WHITE);
			labelName.setPadding(5, 5, 5, 5);
			rowHeading.addView(labelName);

			// Station address
			TextView labelAddress = new TextView(FindGasPricesActivity.this);
			labelAddress.setId(idCounter++);
			labelAddress.setText("Address");
			labelAddress.setTextColor(Color.WHITE);
			labelAddress.setPadding(5, 5, 5, 5);
			rowHeading.addView(labelAddress);

			// Station price
			TextView labelPrice = new TextView(FindGasPricesActivity.this);
			labelPrice.setId(idCounter++);
			labelPrice.setText("Price");
			labelPrice.setTextColor(Color.WHITE);
			labelPrice.setPadding(5, 5, 5, 5);
			rowHeading.addView(labelPrice);

			// Station distance
			TextView labelDistance = new TextView(FindGasPricesActivity.this);
			labelDistance.setId(idCounter++);
			labelDistance.setText("Distance");
			labelDistance.setTextColor(Color.WHITE);
			labelDistance.setPadding(5, 5, 5, 5);
			rowHeading.addView(labelDistance);

			// Add row
			myTable.addView(rowHeading, new TableLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

			try {

				JSONArray stations = result.getJSONArray("stations");
				Log.e("Number of Stations:", "" + stations.length());

				if (stations.length() == 0) {
					myTable.removeAllViews();
					TableRow rowToAdd = new TableRow(
							FindGasPricesActivity.this);
					rowToAdd.setId(idCounter++);
					rowToAdd.setBackgroundColor(Color.WHITE);
					rowToAdd.setLayoutParams(new LayoutParams(
							LayoutParams.FILL_PARENT,
							LayoutParams.FILL_PARENT));

					TextView stationName = new TextView(
							FindGasPricesActivity.this);

					stationName.setId(idCounter++);
					stationName.setText("No stations found. Please try searching again.");
					stationName.setTextColor(Color.BLACK);
					stationName.setPadding(5, 5, 5, 5);
					rowToAdd.addView(stationName);
					myTable.addView(rowToAdd, new TableLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
					
				} else {
					Display display = getWindowManager().getDefaultDisplay(); 
					int width = display.getWidth();
					
					List<TableRow> rows = new ArrayList<TableRow>();

					for (int i = 0; i < stations.length(); i++) {
						JSONObject row = stations.getJSONObject(i);

						TableRow rowToAdd = new TableRow(
								FindGasPricesActivity.this);
						rowToAdd.setId(idCounter++);
						rowToAdd.setBackgroundColor(Color.WHITE);
						rowToAdd.setLayoutParams(new LayoutParams(
								LayoutParams.FILL_PARENT,
								LayoutParams.FILL_PARENT));

						TextView stationName = new TextView(
								FindGasPricesActivity.this);
						stationName.setWidth((int)(width*.25));

						stationName.setId(idCounter++);
						stationName.setText(row.getString("station"));
						Log.e("Station Name:", row.getString("station"));
						stationName.setTextColor(Color.BLACK);
						stationName.setPadding(5, 5, 5, 5);
						rowToAdd.addView(stationName);

						TextView stationAddress = new TextView(
								FindGasPricesActivity.this);
						stationAddress.setWidth((int)(width*.35));
						stationAddress.setId(idCounter++);
						stationAddress.setText(row.getString("address"));
						stationAddress.setTextColor(Color.BLACK);
						stationAddress.setPadding(5, 5, 5, 5);
						rowToAdd.addView(stationAddress);

						Log.e("Address:", row.getString("address"));
						TextView stationPrice = new TextView(
								FindGasPricesActivity.this);
						stationPrice.setId(idCounter++);
						stationPrice.setText(row.getString("price"));
						Log.e("Price:", row.getString("price"));
						stationPrice.setTextColor(Color.BLACK);
						stationPrice.setPadding(5, 5, 5, 5);
						stationPrice.setWidth((int)(width*.15));
						rowToAdd.addView(stationPrice);

						TextView stationDistance = new TextView(
								FindGasPricesActivity.this);
						stationDistance.setId(idCounter++);
						stationDistance.setText(row.getString("distance"));
						Log.e("Distance:", row.getString("distance"));
						stationDistance.setTextColor(Color.BLACK);
						stationDistance.setPadding(5, 5, 5, 5);
						stationDistance.setWidth((int)(width*.25));
						rowToAdd.addView(stationDistance);

						TextView stationID = new TextView(
								FindGasPricesActivity.this);
						stationID.setId(idCounter++);
						stationID.setText(row.getString("id"));
						Log.e("ID:", row.getString("id"));
						stationID.setTextColor(Color.BLACK);
						stationID.setPadding(5, 5, 5, 5);
						rowToAdd.addView(stationID);

						rowToAdd.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View view) {
								Intent rowClick = new Intent(
										FindGasPricesActivity.this,
										UpdatePriceActivity.class);
								TableRow t = (TableRow) view;
								TextView firstTextView = (TextView) t
										.getChildAt(0);
								TextView secondTextView = (TextView) t
										.getChildAt(1);
								TextView fifthTextView = (TextView) t
										.getChildAt(4);
								String stationName = firstTextView.getText()
										.toString();
								String stationAddress = secondTextView
										.getText().toString();
								String stationID = fifthTextView.getText()
										.toString();

								rowClick.putExtra("StationName", stationName);
								rowClick.putExtra("StationAddress",
										stationAddress);
								rowClick.putExtra("StationID", stationID);

								startActivity(rowClick);
							}
						});

						if(row.getString("price").equalsIgnoreCase("n/a")){
							rows.add(rowToAdd);
						}else{							
							myTable.addView(rowToAdd, new TableLayout.LayoutParams(
									LayoutParams.FILL_PARENT,
									LayoutParams.WRAP_CONTENT));
						}
					}
					
					for(int j = 0; j < rows.size(); j++){
						myTable.addView(rows.get(j), new TableLayout.LayoutParams(
								LayoutParams.FILL_PARENT,
								LayoutParams.WRAP_CONTENT));
					}
				}

			} catch (JSONException ex) {
				Log.e("Exception:", "Request not completed");
			}

			myTable.setColumnCollapsed(4, true);

		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(String... params) {
			Log.e("", "---doInBackground---");
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
					Log.e("", "---Status Code 200---");
					HttpEntity entity = response.getEntity();
					InputStream content = entity.getContent();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(content));
					String line;

					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}

				} else {
					Log.e(JSONRequest.class.toString(), "Failed to get request");
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
