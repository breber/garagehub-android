package com.worthwhilegames.carhubmobile;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.*;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import com.appspot.car_hub.garagehub.Garagehub;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.worthwhilegames.carhubmobile.garagehubkeys.GarageHubKeys;
import com.worthwhilegames.carhubmobile.sync.SyncAdapter;

/**
 * @author breber
 */
public abstract class AppEngineListActivity extends AdListActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int REQUEST_ACCOUNT_PICKER = 2;
    private static final int REQUEST_ACCOUNT_PERM = 3;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUi();

            if (mDialog != null) {
                mDialog.dismiss();
            }
        }
    };

    /**
     * Current credentials
     */
    protected GoogleAccountCredential mCreds;

    /**
     * The GarageHub service for interacting with AppEngine
     */
    protected Garagehub mService;

    /**
     * The progress dialog for initial loading
     */
    private ProgressDialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        onCreate(savedInstanceState, R.string.noVehiclesRegistered);
    }

    public void onCreate(Bundle savedInstanceState, int emptyResource) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_vehicle_list);

        TextView tv = (TextView) findViewById(android.R.id.empty);
        tv.setText(emptyResource);

        // Inside your Activity class onCreate method
        mCreds = GoogleAccountCredential.usingAudience(this, GarageHubKeys.GARAGEHUB_KEY);
        mCreds.setSelectedAccountName(Util.getAccountName(this));

        Garagehub.Builder bl = new Garagehub.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), mCreds);
        bl.setApplicationName("GarageHub");
        mService = bl.build();

        updateUi();

        if (mCreds.getSelectedAccountName() == null) {
            // Not signed in, show login window or request an account.
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS}, REQUEST_ACCOUNT_PERM);
            } else {
                chooseAccount();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK) {
                    if (data != null && data.getExtras() != null) {
                        String accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
                        if (accountName != null) {
                            Util.setAccountName(this, accountName);
                            mCreds.setSelectedAccountName(accountName);

                            // Show a loading dialog for the first sync
                            mDialog = ProgressDialog.show(AppEngineListActivity.this, "",
                                    "Loading. Please wait...", true);

                            // Start the sync
                            Util.startSync(this, true);
                        }
                    }
                } else {
                    // If we don't get a successful account, finish the activity
                    setResult(RESULT_CANCELED);
                    finish();
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
            updateUi();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Listen for broadcast message
        registerReceiver(broadcastReceiver, new IntentFilter(SyncAdapter.SYNC_FINISHED_BROADCAST));
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Unregister
        unregisterReceiver(broadcastReceiver);
    }

    /* (non-Javadoc)
     * @see ActivityCompat.OnRequestPermissionsResultCallback#onRequestPermissionsResult()
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        chooseAccount();
    }

    private void chooseAccount() {
        startActivityForResult(mCreds.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    protected abstract void updateUi();
}
