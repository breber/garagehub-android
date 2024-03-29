package com.worthwhilegames.carhubmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.mobsandgeeks.adapters.InstantAdapter;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    /* (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_vehicle_list, menu);
        return true;
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // This will cover the Android menu button press
        switch (item.getItemId()) {
            case R.id.menu_add_vehicle:
                Intent i = new Intent(this, AddVehicleActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void updateUi() {
        // Get all GasPriceRecords from the database
        List<UserVehicleRecord> vehicles = UserVehicleRecord.listAll(UserVehicleRecord.class);
        Collections.sort(vehicles, new Comparator<UserVehicleRecord>() {
            @Override
            public int compare(UserVehicleRecord lhs, UserVehicleRecord rhs) {
                return lhs.getMake().compareTo(rhs.getMake());
            }
        });

        setListAdapter(new InstantAdapter<UserVehicleRecord>(this, R.layout.simple_list_item, UserVehicleRecord.class, vehicles));
    }
}
