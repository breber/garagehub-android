package com.worthwhilegames.carhubmobile;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.worthwhilegames.carhubmobile.models.UserFuelRecord;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Created by breber on 6/29/13.
 */
public class AddUserFuelRecordActivity extends AdActivity {

    private UserVehicleRecord mVehicle;
    private UserFuelRecord mRecord;

    @BindView(R.id.datePicker) protected DatePicker mDatePicker;
    @BindView(R.id.locationText) protected EditText mLocationEditText;
    @BindView(R.id.amountText) protected EditText mAmount;
    @BindView(R.id.odometerText) protected EditText mOdometerEndEditText;
    @BindView(R.id.costPerGallonText) protected EditText mCostPerGallonEditText;
    @BindView(R.id.numGallonText) protected EditText mNumGallonEditText;
    @BindView(R.id.fuelGradeSpinner) protected Spinner mFuelGradeSpinner;

    private TextWatcher mTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // Nothing
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // Nothing
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String amountText = mAmount.getText().toString();
            String cpgText = mCostPerGallonEditText.getText().toString();

            if (!"".equals(amountText) && !"".equals(cpgText)) {
                float amountVal = 0;
                float cpgVal = 0;
                try {
                    amountVal = Float.parseFloat(amountText);
                    cpgVal = Float.parseFloat(cpgText);
                } catch (Exception e) {
                    // Ignore this, just don't try calculating
                    return;
                }

                mNumGallonEditText.setText(String.format("%.2f", amountVal / cpgVal));
            }
        }
    };

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

        ButterKnife.bind(this);

        // Add onChange listeners to the amount and cost per gallon fields
        // so that we can update the num gallons field
        mAmount.addTextChangedListener(mTextChangedListener);
        mCostPerGallonEditText.addTextChangedListener(mTextChangedListener);


        // Fill in the fuel types
        String[] fuelTypesArray = getResources().getStringArray(R.array.fuelGradeArray);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, fuelTypesArray);
        mFuelGradeSpinner.setAdapter(adapter);

        Long existingId = getIntent().getLongExtra(Constants.INTENT_DATA_RECORD, -1);
        mRecord = UserFuelRecord.findById(UserFuelRecord.class, existingId);
        if (mRecord != null) {
            // Fill in the UI fields if we are editing a record
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(mRecord.getDate());
            mDatePicker.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            mLocationEditText.setText(mRecord.getLocation());
            mAmount.setText(String.format("%.2f", mRecord.getAmount()));
            mOdometerEndEditText.setText(mRecord.getOdometerEnd() + "");
            mCostPerGallonEditText.setText(String.format("%.2f", mRecord.getCostPerGallon()));
            mNumGallonEditText.setText(String.format("%.2f", mRecord.getAmount() / mRecord.getCostPerGallon()));

            int selectedPosition = adapter.getPosition(mRecord.getFuelGrade());
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
                    mRecord = new UserFuelRecord();
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
