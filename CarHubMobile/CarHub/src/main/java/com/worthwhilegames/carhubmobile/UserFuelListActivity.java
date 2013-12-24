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
import com.worthwhilegames.carhubmobile.models.UserFuelRecord;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;

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

                Intent i = new Intent(UserFuelListActivity.this, AddUserFuelRecordActivity.class);
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
        getMenuInflater().inflate(R.menu.activity_fuel_list, menu);
        return true;
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // This will cover the Android menu button press
        switch (item.getItemId()) {
            case R.id.menu_add_fuel:
                Intent i = new Intent(this, AddUserFuelRecordActivity.class);
                i.putExtra(Constants.INTENT_DATA_VEHICLE, mVehicle.getId());
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateUi() {
        // Get all GasPriceRecords from the database
        List<UserFuelRecord> fuelRecords = UserFuelRecord.getRecordsForVehicle(UserFuelRecord.class, mVehicle);
        Collections.sort(fuelRecords, new Comparator<UserFuelRecord>() {
            @Override
            public int compare(UserFuelRecord lhs, UserFuelRecord rhs) {
                return rhs.getOdometerEnd() - lhs.getOdometerEnd();
            }
        });

        setListAdapter(new InstantAdapter<UserFuelRecord>(this, R.layout.fuelrow, UserFuelRecord.class, fuelRecords));
    }
}
