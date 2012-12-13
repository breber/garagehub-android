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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableRow;
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
		if(locationManager != null){
			updateLocation(locationManager.getLastKnownLocation(locationProvider));
	
			//request updates from both the network provider as well as the GPS signal
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		}

		//search button listener
		Button search = (Button) findViewById(R.id.goButton);
		search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
				pb.setVisibility(View.VISIBLE);
				getGasPrices();
			}
			
		});
		
		//settings button listener
		ImageButton settings = (ImageButton)findViewById(R.id.settingsButton);
		settings.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				Intent settingsButtonClick = new Intent(FindGasPricesActivity.this,
				SettingsActivity.class);
				startActivity(settingsButtonClick);
			}
			
		});
		
		ListView myList = (ListView)findViewById(R.id.scrollView1);
		myList.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
	        public void onItemClick(AdapterView<?> a, View v, int position, long id) {
	         	GasPriceRowModel model = (GasPriceRowModel)a.getItemAtPosition(position);
	         	if(Util.isDebugBuild){
	         		Toast.makeText(FindGasPricesActivity.this, model.getSationID(), Toast.LENGTH_LONG).show();
	         	}
	         	
	    		Intent rowClick = new Intent(
				FindGasPricesActivity.this,
				UpdatePriceActivity.class);
	
				rowClick.putExtra("StationName", model.getStationName());
				rowClick.putExtra("StationAddress",model.getStationAddress());
				rowClick.putExtra("StationID", model.getSationID());
				
				startActivity(rowClick);
	        }
			
        });

	}

	/**
	 * Function to update the latitude and longitude variables
	 * @param location a location object with the new location
	 */
	void updateLocation(Location location) {
		if(location != null){
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
			
			List<GasPriceRowModel> data = new ArrayList<GasPriceRowModel>();
			List<GasPriceRowModel> naData = new ArrayList<GasPriceRowModel>();
			ListView myList = (ListView)FindGasPricesActivity.this.findViewById(R.id.scrollView1);

			try {

				JSONArray stations = result.getJSONArray("stations");
				Log.e("Number of Stations:", "" + stations.length());

				if (stations.length() == 0) {

					GasPriceRowModel model = new GasPriceRowModel("No stations found. Please try searching again.", "", "", "", "", "");
					
				} else {
					Display display = getWindowManager().getDefaultDisplay(); 
					int width = display.getWidth();
					
					List<TableRow> rows = new ArrayList<TableRow>();

					for (int i = 0; i < stations.length(); i++) {
						JSONObject row = stations.getJSONObject(i);

						GasPriceRowModel model = new GasPriceRowModel(row.getString("station"), row.getString("id"), row.getString("address"), row.getString("price"), row.getString("distance"), "");

						if(row.getString("price").equalsIgnoreCase("n/a")){
							naData.add(model);
						}else{							
							data.add(model);
						}
						
					}
										
					data.addAll(naData);
					GasPriceAdapter adapter = new GasPriceAdapter(FindGasPricesActivity.this, R.layout.gaspricerowlayout, data);
					
					View header = (View)getLayoutInflater().inflate(R.layout.gaspricerowheader, null);
					myList.addHeaderView(header);
					
					myList.setAdapter(adapter);
				}

			} catch (JSONException ex) {
				Log.e("Exception:", "Request not completed");
			}

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
