package com.worthwhilegames.carhubmobile;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.brianreber.library.AuthUtil;
import com.brianreber.library.AuthenticatedHttpRequest.AuthenticatedHttpRequestCallback;

/**
 * @author breber
 */
public abstract class AppEngineListActivity extends ListActivity implements AuthenticatedHttpRequestCallback {

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			performUpdate();
		}
	};

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

		if (!AuthUtil.isLoggedIn(this)) {
			AuthUtil.startLogin(this);
		} else if (AuthUtil.hasInvalidAuthToken(this)) {
			AuthUtil.performLoginFromPrefs(this, Constants.WEBSITE_URL);
		} else {
			taskDidFinish();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		registerReceiver(receiver, new IntentFilter(com.brianreber.library.Constants.TOKEN_INVALIDATED));
	}

	@Override
	protected void onStop() {
		super.onStop();

		try {
			unregisterReceiver(receiver);
		} catch (IllegalArgumentException e) {
			// We didn't get far enough to register the receiver
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == com.brianreber.library.Constants.ACCOUNT_CHOOSER_REQUEST && resultCode == RESULT_OK) {
			AuthUtil.performLoginFromResult(this, data, Constants.WEBSITE_URL);
		} else if (requestCode == com.brianreber.library.Constants.USER_RECOVERY_INTENT) {
			AuthUtil.performLoginFromPrefs(this, Constants.WEBSITE_URL);
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		performUpdate();
	}

	/**
	 * Perform all necessary UI updates, then call execute request
	 */
	protected abstract void performUpdate();

	@Override
	public abstract void taskDidFinish();
}
