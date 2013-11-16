package com.worthwhilegames.carhubmobile.sync;

import android.content.Context;
import com.appspot.car_hub.carhub.Carhub;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;
import com.worthwhilegames.carhubmobile.util.AuthenticatedHttpRequest;

import java.util.List;

/**
 * Created by breber on 8/2/13.
 */
public class OldSyncAdapter implements AuthenticatedHttpRequest.AuthenticatedHttpRequestCallback {

    private static final int NONE = 0;
    private static final int MAINTENANCE = (1 << 0);
    private static final int FUEL = (1 << 1);
    private static final int EXPENSE = (1 << 2);
    private static final int ALL = (MAINTENANCE | FUEL | EXPENSE);

    private Context mContext;
    private Carhub mService;
    private AuthenticatedHttpRequest.AuthenticatedHttpRequestCallback mDelegate;
    private int mFinished = NONE;

    public static void performSync(Context ctx, Carhub service, AuthenticatedHttpRequest.AuthenticatedHttpRequestCallback delegate) {
        OldSyncAdapter adapter = new OldSyncAdapter(ctx, service, delegate);
        adapter.startSync();
    }

    private OldSyncAdapter(Context ctx, Carhub service, AuthenticatedHttpRequest.AuthenticatedHttpRequestCallback delegate) {
        this.mContext = ctx;
        this.mService = service;
        this.mDelegate = delegate;
    }

    private void startSync() {
        // Fetch Categories
        FetchCategoryRecordsTask request = new FetchCategoryRecordsTask(mContext, mService);
        request.execute();

        // Fetch Vehicles
        FetchUserVehiclesTask vehiclesTask = new FetchUserVehiclesTask(mContext, mService, this);
        vehiclesTask.execute();
    }

    private void handleFinished() {
        if (mFinished == ALL) {
            mFinished = NONE;
            mDelegate.taskDidFinish(null);
        }
    }

    @Override
    public void taskDidFinish(Class<? extends AuthenticatedHttpRequest> cls) {
        if (cls == FetchUserVehiclesTask.class) {
            List<UserVehicleRecord> vehicles = UserVehicleRecord.listAll(UserVehicleRecord.class);

            for (UserVehicleRecord rec : vehicles) {
                // Once we have vehicles, sync Fuel, Maintenance and Expenses
                FetchUserMaintenanceRecordsTask maintTask = new FetchUserMaintenanceRecordsTask(mContext, mService, this, rec);
                maintTask.execute();

                FetchUserBaseExpenseRecordsTask expenseRecordsTask = new FetchUserBaseExpenseRecordsTask(mContext, mService, this, rec);
                expenseRecordsTask.execute();

                FetchUserFuelRecordsTask fuelTask = new FetchUserFuelRecordsTask(mContext, mService, this, rec);
                fuelTask.execute();
            }
        } else if (cls == FetchUserMaintenanceRecordsTask.class) {
            mFinished |= MAINTENANCE;
            handleFinished();
        } else if (cls == FetchUserFuelRecordsTask.class) {
            mFinished |= FUEL;
            handleFinished();
        } else if (cls == FetchUserBaseExpenseRecordsTask.class) {
            mFinished |= EXPENSE;
            handleFinished();
        }
    }
}
