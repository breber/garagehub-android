package com.example.findnearbygasprices;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

public class SettingsActivity extends Activity {

	/**
	 * A SharedPreferences object to get the preferences set by the user
	 */
	private SharedPreferences sharedPref;

	/**
	 * A SharedPreferences editor for changing the game preferences based on
	 * user input
	 */
	private SharedPreferences.Editor prefsEditor;

	private Spinner distanceSpinner;
	private Spinner fuelTypeSpinner;
	private Spinner sortBySpinner;
	private CheckBox useCurrentLocation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		// create the shared preferences object
		sharedPref = this.getSharedPreferences("Preferences", 0);

		// create the preferences editor for editing the preferences
		prefsEditor = sharedPref.edit();

		distanceSpinner = (Spinner) findViewById(R.id.spinnerradius);
		fuelTypeSpinner = (Spinner) findViewById(R.id.spinnerfueltype);
		sortBySpinner = (Spinner) findViewById(R.id.spinnersortby);
		useCurrentLocation = (CheckBox)findViewById(R.id.useCurrentLocation);

		if (distanceSpinner != null) {
			// get the value from shared preferences
			Integer distanceToSearch = sharedPref.getInt("Distance", 5);

			// make an array adapter of all options specified in the xml
			@SuppressWarnings("unchecked")
			ArrayAdapter<String> distanceAdapter = (ArrayAdapter<String>) distanceSpinner
					.getAdapter();

			// find the current position
			int spinnerPosition = distanceAdapter.getPosition(distanceToSearch
					.toString());

			// set the correct position to true
			distanceSpinner.setSelection(spinnerPosition);
		}

		if (fuelTypeSpinner != null) {
			// get the value from shared preferences
			String fuelType = sharedPref.getString("Fuel Type", "Mid");

			// make an array adapter of all options specified in the xml
			@SuppressWarnings("unchecked")
			ArrayAdapter<String> fuelTypeAdapter = (ArrayAdapter<String>) fuelTypeSpinner
					.getAdapter();

			// find the current position
			int spinnerPosition = fuelTypeAdapter.getPosition(fuelType
					.toString());

			// set the correct position to true
			fuelTypeSpinner.setSelection(spinnerPosition);
		}

		if (sortBySpinner != null) {
			// get the value from shared preferences
			String sortBy = sharedPref.getString("Sort By", "Price");

			// make an array adapter of all options specified in the xml
			@SuppressWarnings("unchecked")
			ArrayAdapter<String> sortByAdapter = (ArrayAdapter<String>) sortBySpinner
					.getAdapter();

			// find the current position
			int spinnerPosition = sortByAdapter.getPosition(sortBy.toString());

			// set the correct position to true
			sortBySpinner.setSelection(spinnerPosition);
		}
		
		if(useCurrentLocation != null){
			Boolean useLocation = sharedPref.getBoolean("useCurrentLocation", false);
			
			useCurrentLocation.setChecked(useLocation);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {

		// set the result of the activity
		setResult(RESULT_OK);

		// set number of computers
		if (distanceSpinner != null) {
			prefsEditor.putInt("Distance", Integer
					.parseInt((String) distanceSpinner.getSelectedItem()));
		}

		// set difficulty of computers to preferences
		if (fuelTypeSpinner != null) {
			prefsEditor.putString("Fuel Type",
					(String) fuelTypeSpinner.getSelectedItem());
		}

		// set language to preferences
		if (sortBySpinner != null) {
			prefsEditor.putString("Sort By", sortBySpinner.getSelectedItem()
					.toString());
		}
		
		if(useCurrentLocation != null){
			prefsEditor.putBoolean("useCurrentLocation", useCurrentLocation.isChecked());
		}

		// commit the changes to the shared preferences
		prefsEditor.commit();

		// finish the activity
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
