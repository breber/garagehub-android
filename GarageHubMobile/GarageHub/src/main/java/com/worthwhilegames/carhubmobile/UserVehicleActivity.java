package com.worthwhilegames.carhubmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.appspot.car_hub.garagehub.Garagehub;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.worthwhilegames.carhubmobile.adapters.MenuImageAdapter;
import com.worthwhilegames.carhubmobile.adapters.MenuImageAdapter.ImageTextWrapper;
import com.worthwhilegames.carhubmobile.garagehubkeys.GarageHubKeys;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;

/**
 * @author breber
 */
public class UserVehicleActivity extends AdActivity {

    /**
     * Current credentials
     */
    protected GoogleAccountCredential mCreds;

    /**
     * The GarageHub service for interacting with AppEngine
     */
    protected Garagehub mService;


    private UserVehicleRecord mVehicle;

    private ImageTextWrapper[] mImageTextWrappers = {
            new MenuImageAdapter.ImageTextWrapper(R.raw.expense, R.string.expenseManager, UserExpenseManagerActivity.class),
            new MenuImageAdapter.ImageTextWrapper(R.raw.maintenance, R.string.maintenanceRecords, UserMaintenanceListActivity.class),
            new MenuImageAdapter.ImageTextWrapper(R.raw.fuel, R.string.fuelRecords, UserFuelListActivity.class),
    };

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_overview);

        Long vehicleId = getIntent().getLongExtra(Constants.INTENT_DATA_VEHICLE, 0);
        mVehicle = UserVehicleRecord.findById(UserVehicleRecord.class, vehicleId);

        if (mVehicle == null) {
            Toast.makeText(this, "Vehicle doesn't exist", Toast.LENGTH_LONG).show();
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new MenuImageAdapter(this, mImageTextWrappers));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageTextWrapper item = (ImageTextWrapper) parent.getItemAtPosition(position);
                Intent i = new Intent(UserVehicleActivity.this, item.mIntent);
                i.putExtra(Constants.INTENT_DATA_VEHICLE, mVehicle.getId());
                startActivity(i);
            }
        });

        // Inside your Activity class onCreate method
        mCreds = GoogleAccountCredential.usingAudience(this, GarageHubKeys.GARAGEHUB_KEY);
        mCreds.setSelectedAccountName(Util.getAccountName(this));

        // If we don't have an account, we shouldn't be in the UserVehicleActivity anyways
        if (mCreds.getSelectedAccountName() == null) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        Garagehub.Builder bl = new Garagehub.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), mCreds);
        mService = bl.build();

        updateUi();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();

        updateUi();
    }

    private void updateUi() {
        setTitle(mVehicle.getYear() + " " + mVehicle.getMake() + " " + mVehicle.getModel());

        TextView currentField = (TextView) findViewById(R.id.vehicleColor);
        String color = mVehicle.getColor();
        if (color != null && !"".equals(color)) {
            currentField.setText(color);
        } else {
            currentField.setText("N/A");
        }

        currentField = (TextView) findViewById(R.id.vehiclePlates);
        String plates = mVehicle.getPlates();
        if (plates != null && !"".equals(plates)) {
            currentField.setText(plates);
        } else {
            currentField.setText("N/A");
        }

        currentField = (TextView) findViewById(R.id.currentOdometer);
        int odometer = mVehicle.getLatestOdometer();
        if (odometer >= 0) {
            currentField.setText(odometer + "");
        } else {
            currentField.setText("Unknown");
        }

        currentField = (TextView) findViewById(R.id.totalCost);
        float totalCost = mVehicle.getTotalCost();
        if (totalCost >= 0) {
            currentField.setText(String.format("$%.02f", totalCost));
        } else {
            currentField.setText("Unknown");
        }
    }
}
