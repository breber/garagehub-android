package com.worthwhilegames.carhubmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import com.mobsandgeeks.adapters.InstantAdapter;
import com.worthwhilegames.carhubmobile.models.UserMaintenanceRecord;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

                Intent i = new Intent(UserMaintenanceListActivity.this, AddMaintenanceRecordActivity.class);
                i.putExtra(Constants.INTENT_DATA_VEHICLE, mVehicle.getId());
                i.putExtra(Constants.INTENT_DATA_RECORD, model.getId());
                startActivity(i);
            }
        });
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_maintenance_list, menu);
        return true;
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // This will cover the Android menu button press
        switch (item.getItemId()) {
            case R.id.menu_add_maintenance:
                Intent i = new Intent(this, AddMaintenanceRecordActivity.class);
                i.putExtra(Constants.INTENT_DATA_VEHICLE, mVehicle.getId());
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateUi() {
        // Get all GasPriceRecords from the database
        List<UserMaintenanceRecord> maintRecords = UserMaintenanceRecord.getRecordsForVehicle(UserMaintenanceRecord.class, mVehicle);
        Collections.sort(maintRecords, new Comparator<UserMaintenanceRecord>() {
            @Override
            public int compare(UserMaintenanceRecord lhs, UserMaintenanceRecord rhs) {
                return rhs.getOdometer() - lhs.getOdometer();
            }
        });

        setListAdapter(new InstantAdapter<UserMaintenanceRecord>(this, R.layout.maintenancerow, UserMaintenanceRecord.class, maintRecords));
    }

}
