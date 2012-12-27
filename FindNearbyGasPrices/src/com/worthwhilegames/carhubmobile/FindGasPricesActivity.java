package com.worthwhilegames.carhubmobile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.worthwhilegames.carhubmobile.FetchGasPricesTask.FetchGasPricesTaskCallback;
import com.worthwhilegames.carhubmobile.models.GasPriceRecord;

/**
 * @author jamiekujawa
 */
@SuppressLint("DefaultLocale")
public class FindGasPricesActivity extends ListActivity implements FetchGasPricesTaskCallback {

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
	 * A listener to update the current location of the Android device
	 */
	private LocationListener locationListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.find_gas_prices);

		// initialize
		currentLatitude = 0.0;
		currentLongitude = 0.0;

		// create a location manager
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// create a new progress bar to be displayed when the app is searching
		// for gas prices
		setProgressBarIndeterminateVisibility(false);

		// create a new location listener. This will update the location
		// everytime the device has a location change
		locationListener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				updateLocation(location);
			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// not used
			}

			@Override
			public void onProviderEnabled(String provider) {
				// not used
			}

			@Override
			public void onProviderDisabled(String provider) {
				// not used
			}
		};

		// Update with the last known good location so the user will get search results
		String locationProvider = LocationManager.NETWORK_PROVIDER;
		if (locationManager != null) {
			updateLocation(locationManager.getLastKnownLocation(locationProvider));

			// request updates from both the network provider as well as the GPS signal
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		}

		// search button listener
		Button search = (Button) findViewById(R.id.goButton);
		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				performUpdate();
			}
		});

		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				GasPriceRecord model = (GasPriceRecord) a.getItemAtPosition(position);

				Intent rowClick = new Intent(FindGasPricesActivity.this, UpdatePriceActivity.class);
				rowClick.putExtra(UpdatePriceActivity.EXTRA_STATION_NAME, model.getStation());
				rowClick.putExtra(UpdatePriceActivity.EXTRA_STATION_ADDRESS, model.getAddress());
				rowClick.putExtra(UpdatePriceActivity.EXTRA_STATION_ID, model.getStationId());

				startActivity(rowClick);
			}
		});
	}

	/**
	 * Function to update the latitude and longitude variables
	 * @param location a location object with the new location
	 */
	private void updateLocation(Location location) {
		if (location != null) {
			currentLocation = location;

			double latitude = currentLocation.getLatitude();
			double longitude = currentLocation.getLongitude();

			if (latitude != 0.0) {
				currentLatitude = currentLocation.getLatitude();
			}

			if (longitude != 0.0) {
				currentLongitude = currentLocation.getLongitude();
			}
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// This will cover the Android menu button press
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent settingsButtonClick = new Intent(this, SettingsActivity.class);
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
		performUpdate();
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
	 * Perform all necessary UI updates, then call getGasPrices
	 */
	private void performUpdate() {
		setProgressBarIndeterminateVisibility(true);

		getGasPrices();
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
		Boolean useCurrentLocation = sharedPref.getBoolean("useCurrentLocation", true);

		if (matcher.matches() || useCurrentLocation) {
			List<Address> addresses = null;

			// Convert zip code to address
			try {
				Log.e("Longitude: ", "" + currentLongitude);
				Log.e("Latitude: ", "" + currentLatitude);

				int distance = sharedPref.getInt("Distance", 5);
				String fuelType = sharedPref.getString("Fuel Type", "Mid");
				String sortBy = sharedPref.getString("Sort By", "Price");

				FetchGasPricesTask request = new FetchGasPricesTask(this, this);

				double lat = 0;
				double lon = 0;
				boolean executeSearch = true;
				Geocoder geo = new Geocoder(getApplicationContext(), Locale.getDefault());

				if (useCurrentLocation) {
					lat = currentLatitude;
					lon = currentLongitude;
				} else {
					addresses = geo.getFromLocationName(zipCode, 1);

					if (!addresses.isEmpty() && addresses != null) {
						lat = addresses.get(0).getLatitude();
						lon = addresses.get(0).getLongitude();
					} else {
						executeSearch = false;
					}
				}

				if (executeSearch) {
					Log.e("Searching: ", "Searching for information.");
					// execute the get request
					request.execute("http://api.mygasfeed.com/stations/radius/" + lat
							+ "/" + lon + "/" + distance + "/"
							+ fuelType.toLowerCase().trim() + "/"
							+ sortBy.toLowerCase() + "/zax22arsix.json".trim());
				} else {
					setProgressBarIndeterminateVisibility(false);
					Toast.makeText(this,  "Please try search again. Unable to find current location.", Toast.LENGTH_SHORT).show();
				}
			} catch (IOException e) {
				setProgressBarIndeterminateVisibility(false);
				Toast.makeText(this, "Please try search again. Unable to find current location.", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}

		} else {
			Toast.makeText(this, "Please enter a valid zip code", Toast.LENGTH_LONG).show();
			setProgressBarIndeterminateVisibility(false);
		}
	}

	private void updateUi() {
		setProgressBarIndeterminateVisibility(false);

		// Get all GasPriceRecords from the database
		List<GasPriceRecord> gasRecords = GasPriceRecord.listAll(GasPriceRecord.class);

		List<GasPriceRecord> data = new ArrayList<GasPriceRecord>();
		List<GasPriceRecord> naData = new ArrayList<GasPriceRecord>();

		for (GasPriceRecord r : gasRecords) {
			if (r.getPrice().equalsIgnoreCase("n/a")) {
				naData.add(r);
			} else {
				data.add(r);
			}
		}

		data.addAll(naData);
		setListAdapter(new GasPriceAdapter(FindGasPricesActivity.this, R.layout.gaspricerowlayout, data));
	}

	@Override
	public void gasPricesDidUpdate() {
		updateUi();
	}
}
