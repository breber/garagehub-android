package com.worthwhilegames.carhubmobile.sync;

import android.accounts.Account;
import android.content.*;
import android.os.Bundle;
import android.util.Log;
import com.appspot.car_hub.carhub.Carhub;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.worthwhilegames.carhubmobile.Util;
import com.worthwhilegames.carhubmobile.carhubkeys.CarHubKeys;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;

import java.util.List;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public static String SYNC_STARTED_BROADCAST = "com.worthwhilegames.carhubmobile.sync.SyncAdapter.SYNC_STARTED";
    public static String SYNC_FINISHED_BROADCAST = "com.worthwhilegames.carhubmobile.sync.SyncAdapter.SYNC_FINISHED";

    private Context mContext;

    /**
     * Current credentials
     */
    protected GoogleAccountCredential mCreds;

    /**
     * The Carhub service for interacting with AppEngine
     */
    protected Carhub mService;

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContext = context;
    }

    @Override
    public void onPerformSync(Account account,
                              Bundle bundle,
                              String s,
                              ContentProviderClient contentProviderClient,
                              SyncResult syncResult) {
        if (Util.isDebugBuild) {
            Log.d("SyncAdapter", "onPerformSync");
        }

        // Send a message saying we finished syncing
        getContext().sendBroadcast(new Intent(SYNC_STARTED_BROADCAST));

        // Inside your Activity class onCreate method
        SharedPreferences settings = Util.getSharedPrefs(mContext);
        mCreds = GoogleAccountCredential.usingAudience(mContext, CarHubKeys.CARHUB_KEY);
        setAccountName(settings.getString(Util.PREF_ACCOUNT_NAME, null));

        Carhub.Builder bl = new Carhub.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), mCreds);
        mService = bl.build();

        if (mCreds.getSelectedAccountName() != null) {
            FetchCategoryRecordsTask request = new FetchCategoryRecordsTask(mContext, mService);
            request.performTask();

            if (Util.isDebugBuild) {
                Log.d("SyncAdapter", "fetched categories");
            }

            // Fetch Vehicles
            FetchUserVehiclesTask vehiclesTask = new FetchUserVehiclesTask(mContext, mService);
            vehiclesTask.performTask();

            if (Util.isDebugBuild) {
                Log.d("SyncAdapter", "fetched vehicles");
            }

            List<UserVehicleRecord> vehicles = UserVehicleRecord.listAll(UserVehicleRecord.class);

            for (UserVehicleRecord rec : vehicles) {
                // Once we have vehicles, sync Fuel, Maintenance and Expenses
                FetchUserMaintenanceRecordsTask maintTask = new FetchUserMaintenanceRecordsTask(mContext, mService, rec);
                maintTask.performTask();

                FetchUserBaseExpenseRecordsTask expenseRecordsTask = new FetchUserBaseExpenseRecordsTask(mContext, mService, rec);
                expenseRecordsTask.performTask();

                FetchUserFuelRecordsTask fuelTask = new FetchUserFuelRecordsTask(mContext, mService, rec);
                fuelTask.performTask();
            }
        }

        // Send a message saying we finished syncing
        getContext().sendBroadcast(new Intent(SYNC_FINISHED_BROADCAST));
    }

    private void setAccountName(String accountName) {
        SharedPreferences settings = getContext().getSharedPreferences("CarHubMobile", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Util.PREF_ACCOUNT_NAME, accountName);
        editor.commit();
        mCreds.setSelectedAccountName(accountName);
    }
}
