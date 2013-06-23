package com.worthwhilegames.carhubmobile;

import android.accounts.AccountManager;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.carhub.*;
import com.worthwhilegames.carhubmobile.util.AuthenticatedHttpRequest;

/**
 * @author breber
 */
public abstract class AppEngineListActivity extends AdListActivity implements AuthenticatedHttpRequest.AuthenticatedHttpRequestCallback {

    private static final String PREF_ACCOUNT_NAME = "accountName";

    private static final int REQUEST_ACCOUNT_PICKER = 2;

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
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.user_vehicle_list);

		setProgressBarIndeterminateVisibility(false);

		TextView tv = (TextView) findViewById(android.R.id.empty);
		tv.setText(emptyResource);

        // Inside your Activity class onCreate method
        SharedPreferences settings = getSharedPreferences("CarHubMobile", 0);
        mCreds = GoogleAccountCredential.usingAudience(this,
                "server:client_id:280486107933-fkp13pk6dv84vdkumqu1vj5hh0o74he3.apps.googleusercontent.com");
        setAccountName(settings.getString(PREF_ACCOUNT_NAME, null));

        Carhub.Builder bl = new Carhub.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), mCreds);
        mService = bl.build();

        if (mCreds.getSelectedAccountName() == null) {
            // Not signed in, show login window or request an account.
            chooseAccount();
        }
	}

	@Override
	protected void onStart() {
		super.onStart();
        taskDidFinish();
	}

	@Override
	protected void onStop() {
		super.onStop();
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
	}

    private void chooseAccount() {
        startActivityForResult(mCreds.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    // setAccountName definition
    private void setAccountName(String accountName) {
        SharedPreferences settings = getSharedPreferences("CarHubMobile", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_ACCOUNT_NAME, accountName);
        editor.commit();
        mCreds.setSelectedAccountName(accountName);
    }

	/**
	 * Perform all necessary UI updates, then call execute request
	 */
	protected abstract void performUpdate();

	@Override
	public abstract void taskDidFinish();
}
