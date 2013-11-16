package com.worthwhilegames.carhubmobile;

import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.appspot.car_hub.carhub.Carhub;
import com.worthwhilegames.carhubmobile.adapters.MenuImageAdapter;
import com.worthwhilegames.carhubmobile.adapters.MenuImageAdapter.ImageTextWrapper;
import com.worthwhilegames.carhubmobile.carhubkeys.CarHubKeys;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;
import com.worthwhilegames.carhubmobile.sync.OldSyncAdapter;
import com.worthwhilegames.carhubmobile.util.AuthenticatedHttpRequest;

/**
 * @author breber
 */
public class UserVehicleActivity extends AdActivity implements AuthenticatedHttpRequest.AuthenticatedHttpRequestCallback {

    private static final int REQUEST_ACCOUNT_PICKER = 2;

    /**
     * Current credentials
     */
    protected GoogleAccountCredential mCreds;

    /**
     * The Carhub service for interacting with AppEngine
     */
    protected Carhub mService;


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
        SharedPreferences settings = Util.getSharedPrefs(this);
        mCreds = GoogleAccountCredential.usingAudience(this, CarHubKeys.CARHUB_KEY);
        setAccountName(settings.getString(Util.PREF_ACCOUNT_NAME, null));

        Carhub.Builder bl = new Carhub.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), mCreds);
        mService = bl.build();

        if (mCreds.getSelectedAccountName() == null) {
            // Not signed in, show login window or request an account.
            chooseAccount();
        }

        updateUi();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ACCOUNT_PICKER:
                if (data != null && data.getExtras() != null) {
                    String accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        setAccountName(accountName);

                        performUpdate();
                    }
                }
                break;
        }
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mCreds.getSelectedAccountName() != null) {
            // Already signed in, begin app!
            performUpdate();
        }

        updateUi();
    }

    /**
     * Perform all necessary UI updates, then call execute request
     */
    protected void performUpdate() {
        setProgressBarIndeterminateVisibility(true);

        OldSyncAdapter.performSync(this, mService, this);
    }

    private void chooseAccount() {
        startActivityForResult(mCreds.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    // setAccountName definition
    private void setAccountName(String accountName) {
        SharedPreferences settings = Util.getSharedPrefs(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Util.PREF_ACCOUNT_NAME, accountName);
        editor.commit();
        mCreds.setSelectedAccountName(accountName);
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

    public void taskDidFinish(Class<? extends AuthenticatedHttpRequest> cls) {
        updateUi();

        setProgressBarIndeterminateVisibility(false);
    }
}
