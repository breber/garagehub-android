package com.worthwhilegames.carhubmobile;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import com.worthwhilegames.carhubmobile.models.UserFuelRecord;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;
import com.worthwhilegames.carhubmobile.sync.FetchUserFuelRecordsTask;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author breber
 */
public class UserFuelListActivity extends AppEngineListActivity {

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
				UserFuelRecord model = (UserFuelRecord) a.getItemAtPosition(position);
				if (Util.isDebugBuild) {
					Toast.makeText(UserFuelListActivity.this, model.getId() + "", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	/**
	 * Perform all necessary UI updates, then call execute request
	 */
	protected void performUpdate() {
		setProgressBarIndeterminateVisibility(true);

        FetchUserFuelRecordsTask request = new FetchUserFuelRecordsTask(this, mService, this, mVehicle);
        request.execute();
	}

	public void taskDidFinish() {
		// Get all GasPriceRecords from the database
		List<UserFuelRecord> fuelRecords = UserFuelRecord.getRecordsForVehicle(UserFuelRecord.class, mVehicle);
		Collections.sort(fuelRecords, new Comparator<UserFuelRecord>() {
			@Override
			public int compare(UserFuelRecord lhs, UserFuelRecord rhs) {
				return rhs.getOdometerEnd() - lhs.getOdometerEnd();
			}
		});

		UserFuelAdapter adapter = new UserFuelAdapter(UserFuelListActivity.this, R.layout.fouritemrow, fuelRecords);
		setListAdapter(adapter);

		setProgressBarIndeterminateVisibility(false);
	}
}
