package com.worthwhilegames.carhubmobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author jamiekujawa
 */
public class UpdatePriceActivity extends AdActivity {

    public static final String EXTRA_STATION_NAME = "StationName";
    public static final String EXTRA_STATION_ADDRESS = "StationAddress";
    public static final String EXTRA_STATION_ID = "StationID";
    public static final String EXTRA_STATION_LAT = "StationLat";
    public static final String EXTRA_STATION_LNG = "StationLng";

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        // get the current intent
        Intent i = this.getIntent();

        // get the string extras passed in
        final String stationName = i.getStringExtra(EXTRA_STATION_NAME);
        String stationAddress = i.getStringExtra(EXTRA_STATION_ADDRESS);
        final String stationLat = i.getStringExtra(EXTRA_STATION_LAT);
        final String stationLng = i.getStringExtra(EXTRA_STATION_LNG);
        i.getStringExtra(EXTRA_STATION_ID);


        // set layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_price);

        setProgressBarIndeterminateVisibility(false);

        SharedPreferences sharedPref = Util.getSharedPrefs(this);
        Spinner fuelTypeSpinner = (Spinner) findViewById(R.id.spinnerfueltypeUpdate);

        // get the current selected option from the shared preferences
        if (fuelTypeSpinner != null) {
            // get the value from shared preferences
            String fuelType = sharedPref.getString("Fuel Type", "Mid");

            // make an array adapter of all options specified in the xml
            @SuppressWarnings("unchecked")
            ArrayAdapter<String> fuelTypeAdapter = (ArrayAdapter<String>) fuelTypeSpinner.getAdapter();

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

        Button gps = (Button) findViewById(R.id.gps);
        gps.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + stationLat + "," + stationLng));
                startActivity(myIntent);
            }
        });

        Button viewMap = (Button) findViewById(R.id.viewMap);
        viewMap.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "geo:0,0?q="+ stationLat + "," + stationLng;
                startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
            }
        });
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_update_price, menu);
        return true;
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // This will cover the Android menu button press
        switch (item.getItemId()) {
            case R.id.menu_save:
                Intent i = UpdatePriceActivity.this.getIntent();

                // String stationName = i.getStringExtra("StationName");
                // String stationAddress = i.getStringExtra("StationAddress");

                String stationID = i.getStringExtra("StationID");
                EditText newPrice = (EditText) findViewById(R.id.newPrice);

                if ("".equals(newPrice.getText().toString().trim())) {
                    Toast.makeText(UpdatePriceActivity.this, "Please Enter a new price.", Toast.LENGTH_SHORT).show();
                } else {
                    Spinner fuelTypeSpinner = (Spinner) findViewById(R.id.spinnerfueltypeUpdate);

                    UpdatePriceRequest update = new UpdatePriceRequest();
                    update.execute(
                            "http://api.mygasfeed.com/locations/price/zax22arsix.json",
                            newPrice.getText().toString(),
                            (String) fuelTypeSpinner.getSelectedItem(),
                            stationID);

                    //ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar2);
                    //pb.setVisibility(View.VISIBLE);
                    Toast.makeText(UpdatePriceActivity.this, "Sending Request.", Toast.LENGTH_SHORT).show();
                    setProgressBarIndeterminateVisibility(true);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            // Convert string to object
            try {
                JSONObject result = new JSONObject(r);

                if (result != null) {
                    JSONObject status = result.getJSONObject("status");
                    String message = status.getString("message");
                    setProgressBarIndeterminateVisibility(false);
                    if (status.get("error").equals("NO")) {
                        Toast.makeText(UpdatePriceActivity.this, message + ". Successfully updated price.", Toast.LENGTH_SHORT).show();
                        UpdatePriceActivity.this.finish();
                    } else {
                        Toast.makeText(UpdatePriceActivity.this, "Could not update price.", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @SuppressLint("DefaultLocale")
        @Override
        protected String doInBackground(String... params) {
            String responseString = null;

            try {
                if (Util.isDebugBuild) {
                    Log.e("URL", params[0]);
                    Log.e("Station Price", params[1]);
                    Log.e("Station Fuel Type", params[2]);
                    Log.e("Station ID", params[3]);
                }

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(params[0]);
                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("price", params[1].trim()));
                postParameters.add(new BasicNameValuePair("fueltype", params[2].toLowerCase().trim()));
                postParameters.add(new BasicNameValuePair("stationid", params[3].trim()));
                httppost.setEntity(new UrlEncodedFormEntity(postParameters));

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                responseString = Util.readStreamAsString(entity.getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (Util.isDebugBuild) {
                Log.e("---Result---:", responseString);
            }

            return responseString;
        }
    }

}
