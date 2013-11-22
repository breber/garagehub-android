package com.worthwhilegames.carhubmobile;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import com.worthwhilegames.carhubmobile.models.UserFuelRecord;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;

import java.util.Calendar;

/**
 * Created by breber on 6/29/13.
 */
public class AddUserFuelRecordActivity extends AdActivity {

    private UserVehicleRecord mVehicle;
    private UserFuelRecord mRecord;

    private ArrayAdapter<String> mAdapter;
    private DatePicker mDatePicker;
    private EditText mLocationEditText;
    private EditText mAmount;
    private EditText mOdometerEndEditText;
    private EditText mCostPerGallonEditText;
    private Spinner mFuelGradeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfuel);

        Long vehicleId = getIntent().getLongExtra(Constants.INTENT_DATA_VEHICLE, 0);
        mVehicle = UserVehicleRecord.findById(UserVehicleRecord.class, vehicleId);

        if (mVehicle == null) {
            Toast.makeText(this, "Vehicle doesn't exist", Toast.LENGTH_LONG).show();
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        // Set up the UI elements
        mDatePicker = (DatePicker) findViewById(R.id.datePicker);
        mLocationEditText = (EditText) findViewById(R.id.locationText);
        mAmount = (EditText) findViewById(R.id.amountText);
        mOdometerEndEditText = (EditText) findViewById(R.id.odometerText);
        mCostPerGallonEditText = (EditText) findViewById(R.id.costPerGallonText);
        mFuelGradeSpinner = (Spinner) findViewById(R.id.fuelGradeSpinner);

        // Fill in the fuel types
        String[] fuelTypesArray = getResources().getStringArray(R.array.fuelGradeArray);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, fuelTypesArray);
        mFuelGradeSpinner.setAdapter(mAdapter);

        Long existingId = getIntent().getLongExtra(Constants.INTENT_DATA_RECORD, -1);
        mRecord = UserFuelRecord.findById(UserFuelRecord.class, existingId);
        if (mRecord != null) {
            // Fill in the UI fields if we are editing a record
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(mRecord.getDate());
            mDatePicker.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            mLocationEditText.setText(mRecord.getLocation());
            mAmount.setText(mRecord.getAmount() + "");
            mOdometerEndEditText.setText(mRecord.getOdometerEnd() + "");
            mCostPerGallonEditText.setText(mRecord.getCostPerGallon() + "");

            int selectedPosition = mAdapter.getPosition(mRecord.getFuelGrade());
            mFuelGradeSpinner.setSelection(selectedPosition);
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
                UserFuelRecord mostRecent = UserFuelRecord.getLatest(mVehicle);
                Calendar calendar = Calendar.getInstance();
                calendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
                String location = mLocationEditText.getText().toString();
                String amount = mAmount.getText().toString();
                Integer odometerStart = -1;
                if (mostRecent != null) {
                    // TODO: at some point this should be customizable
                    odometerStart = mostRecent.getOdometerEnd();
                }
                String odometerEnd = mOdometerEndEditText.getText().toString();
                String costPerGallon = mCostPerGallonEditText.getText().toString();
                String fuelGrade = mFuelGradeSpinner.getSelectedItem().toString();

                if ("".equals(costPerGallon) || "".equals(amount) || "".equals(odometerEnd)) {
                    return false;
                }

                if (mRecord == null) {
                    mRecord = new UserFuelRecord(this);
                }

                mRecord.setDate(calendar.getTimeInMillis());
                mRecord.setLocation(location);
                mRecord.setAmount(Float.parseFloat(amount));
                mRecord.setOdometerStart(odometerStart);
                mRecord.setOdometerEnd(Integer.parseInt(odometerEnd));
                mRecord.setCostPerGallon(Float.parseFloat(costPerGallon));
                mRecord.setGallons(mRecord.getAmount() / mRecord.getCostPerGallon());
                if (mRecord.getOdometerStart() != -1) {
                    mRecord.setMpg((mRecord.getOdometerEnd() - mRecord.getOdometerStart()) / mRecord.getGallons());
                }
                mRecord.setFuelGrade(fuelGrade);
                mRecord.setVehicle(mVehicle);
                mRecord.setDirty(true);
                mRecord.save();

                Util.startSync(this, false);
                finish();
                return true;
            case R.id.menu_delete:
                if (mRecord != null) {
                    // TODO: add delete functionality
                }

                Util.startSync(this, false);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
