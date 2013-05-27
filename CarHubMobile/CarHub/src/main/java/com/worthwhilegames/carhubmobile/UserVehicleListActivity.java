package com.worthwhilegames.carhubmobile;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;
import com.worthwhilegames.carhubmobile.sync.FetchUserVehiclesTask;

/**
 * @author breber
 */
public class UserVehicleListActivity extends AppEngineListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.string.noVehiclesRegistered);

		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				UserVehicleRecord model = (UserVehicleRecord) a.getItemAtPosition(position);
				Intent i = new Intent(UserVehicleListActivity.this, UserVehicleActivity.class);
				i.putExtra(Constants.INTENT_DATA_VEHICLE, model.getId());
				startActivity(i);
			}
		});
	}

	/**
	 * Perform all necessary UI updates, then call execute request
	 */
	@Override
	protected void performUpdate() {
		setProgressBarIndeterminateVisibility(true);

		FetchUserVehiclesTask request = new FetchUserVehiclesTask(this, this);
		request.execute();
	}

	@Override
	public void taskDidFinish() {
		// Get all GasPriceRecords from the database
		List<UserVehicleRecord> vehicles = UserVehicleRecord.listAll(UserVehicleRecord.class);
		Collections.sort(vehicles, new Comparator<UserVehicleRecord>() {
			@Override
			public int compare(UserVehicleRecord lhs, UserVehicleRecord rhs) {
				return lhs.getMake().compareTo(rhs.getMake());
			}
		});

		setListAdapter(new UserVehicleAdapter(this, R.layout.uservehiclerow, vehicles));

		setProgressBarIndeterminateVisibility(false);
	}
}
