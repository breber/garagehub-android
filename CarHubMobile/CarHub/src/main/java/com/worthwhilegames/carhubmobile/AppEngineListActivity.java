package com.worthwhilegames.carhubmobile;

import android.accounts.AccountManager;
import android.content.*;
import android.os.Bundle;
import android.widget.TextView;
import com.appspot.car_hub.carhub.Carhub;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.worthwhilegames.carhubmobile.carhubkeys.CarHubKeys;
import com.worthwhilegames.carhubmobile.sync.SyncAdapter;

/**
 * @author breber
 */
public abstract class AppEngineListActivity extends AdListActivity {

    private static final int REQUEST_ACCOUNT_PICKER = 2;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUi();
        }
    };

    /**
     * Current credentials
     */
    protected GoogleAccountCredential mCreds;

    /**
     * The Carhub service for interacting with AppEngine
     */
    protected Carhub mService;

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
        mCreds = GoogleAccountCredential.usingAudience(this, CarHubKeys.CARHUB_KEY);
        setAccountName(Util.getAccountName(this));

        Carhub.Builder bl = new Carhub.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), mCreds);
        mService = bl.build();

        if (mCreds.getSelectedAccountName() == null) {
            // Not signed in, show login window or request an account.
            chooseAccount();
        } else {
            // Signed in, so start a sync
            Util.startSync(this, false);
        }
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

                        Util.startSync(this, true);
                        updateUi();
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

    protected abstract void updateUi();
}
