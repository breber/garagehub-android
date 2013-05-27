package com.worthwhilegames.carhubmobile;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.worthwhilegames.carhubmobile.models.UserBaseExpenseRecord;
import com.worthwhilegames.carhubmobile.models.UserFuelRecord;
import com.worthwhilegames.carhubmobile.models.UserMaintenanceRecord;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;
import com.worthwhilegames.carhubmobile.sync.FetchUserBaseExpenseRecordsTask;
import com.worthwhilegames.carhubmobile.sync.FetchUserFuelRecordsTask;
import com.worthwhilegames.carhubmobile.sync.FetchUserMaintenanceRecordsTask;

/**
 * @author breber
 */
public class UserExpenseManagerActivity extends AppEngineListActivity {

	private UserVehicleRecord mVehicle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Long vehicleId = getIntent().getLongExtra(Constants.INTENT_DATA_VEHICLE, 0);
		mVehicle = UserVehicleRecord.findById(UserVehicleRecord.class, vehicleId);

		if (mVehicle == null) {
			Toast.makeText(this, "Vehicle doesn't exist", Toast.LENGTH_LONG).show();
			setResult(RESULT_CANCELED);
			finish();
			return;
		}

		super.onCreate(savedInstanceState, R.string.noFuelRecords);

		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				UserBaseExpenseRecord model = (UserBaseExpenseRecord) a.getItemAtPosition(position);
				if (Util.isDebugBuild) {
					Toast.makeText(UserExpenseManagerActivity.this, model.getId() + "", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	/**
	 * Perform all necessary UI updates, then call execute request
	 */
	@Override
	protected void performUpdate() {
		setProgressBarIndeterminateVisibility(true);

		// Fetch Base Expense
		FetchUserBaseExpenseRecordsTask request = new FetchUserBaseExpenseRecordsTask(this, mVehicle, this);
		request.execute();

		// Fetch Maintenance
		FetchUserMaintenanceRecordsTask requestMaint = new FetchUserMaintenanceRecordsTask(this, mVehicle, this);
		requestMaint.execute();

		// Fetch Fuel
		FetchUserFuelRecordsTask requestFuel = new FetchUserFuelRecordsTask(this, mVehicle, this);
		requestFuel.execute();
	}

	@Override
	public void taskDidFinish() {
		// Get all GasPriceRecords from the database
		List<UserBaseExpenseRecord> expenseRecords = UserBaseExpenseRecord.getRecordsForVehicle(UserBaseExpenseRecord.class, mVehicle);
		expenseRecords.addAll(UserFuelRecord.getRecordsForVehicle(UserFuelRecord.class, mVehicle));
		expenseRecords.addAll(UserMaintenanceRecord.getRecordsForVehicle(UserMaintenanceRecord.class, mVehicle));
		Collections.sort(expenseRecords, new Comparator<UserBaseExpenseRecord>() {
			@Override
			public int compare(UserBaseExpenseRecord lhs, UserBaseExpenseRecord rhs) {
				return (int) (rhs.getDate() - lhs.getDate());
			}
		});

		UserExpenseAdapter adapter = new UserExpenseAdapter(UserExpenseManagerActivity.this, R.layout.fouritemrow, expenseRecords);
		setListAdapter(adapter);

		setProgressBarIndeterminateVisibility(false);
	}
}
