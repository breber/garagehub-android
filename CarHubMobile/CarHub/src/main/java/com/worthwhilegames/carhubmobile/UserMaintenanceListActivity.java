package com.worthwhilegames.carhubmobile;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.worthwhilegames.carhubmobile.models.UserMaintenanceRecord;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;
import com.worthwhilegames.carhubmobile.sync.FetchUserMaintenanceRecordsTask;

/**
 * @author breber
 */
public class UserMaintenanceListActivity extends AppEngineListActivity {

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

		super.onCreate(savedInstanceState, R.string.noMaintRecords);

		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				UserMaintenanceRecord model = (UserMaintenanceRecord) a.getItemAtPosition(position);
				if (Util.isDebugBuild) {
					Toast.makeText(UserMaintenanceListActivity.this, model.getId() + "", Toast.LENGTH_LONG).show();
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

		FetchUserMaintenanceRecordsTask request = new FetchUserMaintenanceRecordsTask(this, mVehicle, this);
		request.execute();
	}

	@Override
	public void taskDidFinish() {
		// Get all GasPriceRecords from the database
		List<UserMaintenanceRecord> maintRecords = UserMaintenanceRecord.getRecordsForVehicle(UserMaintenanceRecord.class, mVehicle);
		Collections.sort(maintRecords, new Comparator<UserMaintenanceRecord>() {
			@Override
			public int compare(UserMaintenanceRecord lhs, UserMaintenanceRecord rhs) {
				return rhs.getOdometer() - lhs.getOdometer();
			}
		});

		UserMaintenanceAdapter adapter = new UserMaintenanceAdapter(UserMaintenanceListActivity.this, R.layout.fouritemrow, maintRecords);
		setListAdapter(adapter);

		setProgressBarIndeterminateVisibility(false);
	}

}