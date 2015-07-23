package com.worthwhilegames.carhubmobile.sync;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.*;
import android.os.Bundle;
import android.util.Log;
import com.appspot.car_hub.garagehub.Garagehub;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.worthwhilegames.carhubmobile.Util;
import com.worthwhilegames.carhubmobile.garagehubkeys.GarageHubKeys;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;

import java.util.List;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public static String SYNC_STARTED_BROADCAST = "com.worthwhilegames.carhubmobile.sync.SyncAdapter.SYNC_STARTED";
    public static String SYNC_FINISHED_BROADCAST = "com.worthwhilegames.carhubmobile.sync.SyncAdapter.SYNC_FINISHED";

    /**
     * Current credentials
     */
    protected GoogleAccountCredential mCreds;

    /**
     * The GarageHub service for interacting with AppEngine
     */
    protected Garagehub mService;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        Util.updateSyncAutomatically(context);
    }

    @TargetApi(11)
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);

        Util.updateSyncAutomatically(context);
    }

    @Override
    public void onPerformSync(Account account,
                              Bundle bundle,
                              String s,
                              ContentProviderClient contentProviderClient,
                              SyncResult syncResult) {
        if (Util.isDebugBuild) {
            Log.d("SyncAdapter", "onPerformSync - " + account.name);
        }

        // If this sync wasn't started by Util.startSync, make sure that the
        // user has an account selected. If this is from Util.startSync, we
        // know that the user has an account selected
        if (!bundle.getBoolean(Util.FROM_START_SYNC)) {
            // Only do the sync if the account given is the same one
            // the user selected to sync
            String accountName = Util.getAccountName(getContext());
            if ((accountName == null) ||
                (!account.name.equals(accountName))) {
                if (Util.isDebugBuild) {
                    Log.d("SyncAdapter", "onPerformSync - " + account.name + " - CANCELLED: " + accountName);
                }

                return;
            }
        }

        // Send a message saying we finished syncing
        getContext().sendBroadcast(new Intent(SYNC_STARTED_BROADCAST));

        // Inside your Activity class onCreate method
        mCreds = GoogleAccountCredential.usingAudience(getContext(), GarageHubKeys.GARAGEHUB_KEY);
        mCreds.setSelectedAccountName(account.name);

        if (Util.isDebugBuild) {
            Log.d("SyncAdapter", "AccountName: " + mCreds.getSelectedAccountName());
        }

        Garagehub.Builder bl = new Garagehub.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), mCreds);
        bl.setApplicationName("GarageHub");
        mService = bl.build();

        if (mCreds.getSelectedAccountName() != null) {
            FetchCategoryRecordsTask request = new FetchCategoryRecordsTask(getContext(), mService);
            request.performTask();

            if (Util.isDebugBuild) {
                Log.d("SyncAdapter", "fetched categories");
            }

            // Fetch Vehicles
            FetchUserVehiclesTask vehiclesTask = new FetchUserVehiclesTask(getContext(), mService);
            vehiclesTask.performTask();

            if (Util.isDebugBuild) {
                Log.d("SyncAdapter", "fetched vehicles");
            }

            List<UserVehicleRecord> vehicles = UserVehicleRecord.listAll(UserVehicleRecord.class);

            for (UserVehicleRecord rec : vehicles) {
                // Once we have vehicles, sync Fuel, Maintenance and Expenses
                FetchUserMaintenanceRecordsTask maintTask = new FetchUserMaintenanceRecordsTask(getContext(), mService, rec);
                maintTask.performTask();

                FetchUserBaseExpenseRecordsTask expenseRecordsTask = new FetchUserBaseExpenseRecordsTask(getContext(), mService, rec);
                expenseRecordsTask.performTask();

                FetchUserFuelRecordsTask fuelTask = new FetchUserFuelRecordsTask(getContext(), mService, rec);
                fuelTask.performTask();
            }
        }

        // Send a message saying we finished syncing
        getContext().sendBroadcast(new Intent(SYNC_FINISHED_BROADCAST));
    }
}
