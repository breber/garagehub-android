package com.worthwhilegames.carhubmobile;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import com.worthwhilegames.carhubmobile.models.CategoryRecord;
import com.worthwhilegames.carhubmobile.models.UserMaintenanceRecord;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;

import java.util.Calendar;
import java.util.List;

/**
 * Created by breber on 6/29/13.
 */
public class AddMaintenanceRecordActivity extends AdActivity {

    private UserVehicleRecord mVehicle;
    private UserMaintenanceRecord mRecord;

    private ArrayAdapter<CategoryRecord> mAdapter;
    private DatePicker mDatePicker;
    private Spinner mCategorySpinner;
    private EditText mLocationEditText;
    private EditText mDescriptionEditText;
    private EditText mAmount;
    private EditText mOdometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmaintenance);

        Long vehicleId = getIntent().getLongExtra(Constants.INTENT_DATA_VEHICLE, -1);
        mVehicle = UserVehicleRecord.findById(UserVehicleRecord.class, vehicleId);

        if (mVehicle == null) {
            Toast.makeText(this, "Vehicle doesn't exist", Toast.LENGTH_LONG).show();
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        // Set up the UI elements
        mDatePicker = (DatePicker) findViewById(R.id.datePicker);
        mCategorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        mLocationEditText = (EditText) findViewById(R.id.locationText);
        mDescriptionEditText = (EditText) findViewById(R.id.descriptionText);
        mAmount = (EditText) findViewById(R.id.amountText);
        mOdometer = (EditText) findViewById(R.id.odometerText);

        // Fill in the categories
        List<CategoryRecord> categoryRecords = CategoryRecord.getMaintenanceCategories(CategoryRecord.class);
        mAdapter = new ArrayAdapter<CategoryRecord>(this, android.R.layout.simple_spinner_dropdown_item, categoryRecords);
        mCategorySpinner.setAdapter(mAdapter);

        Long existingId = getIntent().getLongExtra(Constants.INTENT_DATA_RECORD, -1);
        mRecord = UserMaintenanceRecord.findById(UserMaintenanceRecord.class, existingId);
        if (mRecord != null) {
            // Fill in the UI fields if we are editing a record
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(mRecord.getDate());
            mDatePicker.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            mLocationEditText.setText(mRecord.getLocation());
            mDescriptionEditText.setText(mRecord.getDescription());
            mAmount.setText(mRecord.getAmount() + "");
            mOdometer.setText(mRecord.getOdometer() + "");

            int selectedPosition = mAdapter.getPosition(mRecord.getCategory());
            mCategorySpinner.setSelection(selectedPosition);
        }
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_record, menu);
        return true;
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // This will cover the Android menu button press
        switch (item.getItemId()) {
            case R.id.menu_save:
                Calendar calendar = Calendar.getInstance();
                calendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
                String location = mLocationEditText.getText().toString();
                String description = mDescriptionEditText.getText().toString();
                String amount = mAmount.getText().toString();
                String odometer = mOdometer.getText().toString();

                if (mRecord == null) {
                    mRecord = new UserMaintenanceRecord(this);
                }

                mRecord.setCategoryId(mAdapter.getItem(mCategorySpinner.getSelectedItemPosition()));
                mRecord.setDate(calendar.getTimeInMillis());
                mRecord.setLocation(location);
                mRecord.setDescription(description);
                mRecord.setAmount(Float.parseFloat(amount));
                mRecord.setOdometer(Integer.parseInt(odometer));
                mRecord.setDirty(true);
                mRecord.save();

                finish();
                return true;
            case R.id.menu_delete:
                if (mRecord != null) {
                    // TODO: add delete functionality
                }

                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
