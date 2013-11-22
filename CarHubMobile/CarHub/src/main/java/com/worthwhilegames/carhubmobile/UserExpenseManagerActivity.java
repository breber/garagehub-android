package com.worthwhilegames.carhubmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import com.worthwhilegames.carhubmobile.adapters.UserExpenseAdapter;
import com.worthwhilegames.carhubmobile.models.UserBaseExpenseRecord;
import com.worthwhilegames.carhubmobile.models.UserFuelRecord;
import com.worthwhilegames.carhubmobile.models.UserMaintenanceRecord;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

        super.onCreate(savedInstanceState, R.string.noExpenseRecords);

        getListView().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                UserBaseExpenseRecord model = (UserBaseExpenseRecord) a.getItemAtPosition(position);
                if (Util.isDebugBuild) {
                    Toast.makeText(UserExpenseManagerActivity.this, model.getId() + "", Toast.LENGTH_LONG).show();
                }

                Intent i;

                if (model.getClass() == UserFuelRecord.class) {
                    i = new Intent(UserExpenseManagerActivity.this, AddUserFuelRecordActivity.class);
                } else if (model.getClass() == UserMaintenanceRecord.class) {
                    i = new Intent(UserExpenseManagerActivity.this, AddMaintenanceRecordActivity.class);
                } else {
                    i = new Intent(UserExpenseManagerActivity.this, AddExpenseRecordActivity.class);
                }
                i.putExtra(Constants.INTENT_DATA_VEHICLE, mVehicle.getId());
                i.putExtra(Constants.INTENT_DATA_RECORD, model.getId());
                startActivity(i);
            }
        });

        updateUi();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_expense_manager, menu);
        return true;
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // This will cover the Android menu button press
        switch (item.getItemId()) {
            case R.id.menu_add_expense:
                Intent i = new Intent(this, AddExpenseRecordActivity.class);
                i.putExtra(Constants.INTENT_DATA_VEHICLE, mVehicle.getId());
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateUi() {
        // Get all GasPriceRecords from the database
        List<UserBaseExpenseRecord> expenseRecords = UserBaseExpenseRecord.getRecordsForVehicle(UserBaseExpenseRecord.class, mVehicle);
        expenseRecords.addAll(UserFuelRecord.getRecordsForVehicle(UserFuelRecord.class, mVehicle));
        expenseRecords.addAll(UserMaintenanceRecord.getRecordsForVehicle(UserMaintenanceRecord.class, mVehicle));
        Collections.sort(expenseRecords, new Comparator<UserBaseExpenseRecord>() {
            @Override
            public int compare(UserBaseExpenseRecord lhs, UserBaseExpenseRecord rhs) {
                return rhs.getDate().compareTo(lhs.getDate());
            }
        });

        UserExpenseAdapter adapter = new UserExpenseAdapter(UserExpenseManagerActivity.this, expenseRecords);
        setListAdapter(adapter);
    }
}
