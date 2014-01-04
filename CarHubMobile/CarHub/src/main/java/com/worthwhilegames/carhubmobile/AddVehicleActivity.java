package com.worthwhilegames.carhubmobile;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;

/**
 * Created by breber on 6/29/13.
 */
public class AddVehicleActivity extends AdActivity {

    private UserVehicleRecord mVehicle;

    @InjectView(R.id.yearText) protected EditText mYearEditText;
    @InjectView(R.id.makeText) protected EditText mMakeEditText;
    @InjectView(R.id.modelText) protected EditText mModelEditText;
    @InjectView(R.id.colorText) protected EditText mColorEditText;
    @InjectView(R.id.plateText) protected EditText mPlateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addvehicle);
        ButterKnife.inject(this);

        Long vehicleId = getIntent().getLongExtra(Constants.INTENT_DATA_VEHICLE, -1);
        mVehicle = UserVehicleRecord.findById(UserVehicleRecord.class, vehicleId);

        if (mVehicle != null) {
            // Fill in the UI fields if we are editing a record
            mYearEditText.setText(mVehicle.getYear());
            mMakeEditText.setText(mVehicle.getMake());
            mModelEditText.setText(mVehicle.getModel());
            mColorEditText.setText(mVehicle.getColor());
            mPlateEditText.setText(mVehicle.getPlates());
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
                String year = mYearEditText.getText().toString();
                String make = mMakeEditText.getText().toString();
                String model = mModelEditText.getText().toString();
                String color = mColorEditText.getText().toString();
                String plates = mPlateEditText.getText().toString();

                if (!"".equals(year) && !"".equals(make) && !"".equals(model)) {

                }

                if (mVehicle == null) {
                    mVehicle = new UserVehicleRecord(this);
                }

                mVehicle.setYear(year);
                mVehicle.setMake(make);
                mVehicle.setModel(model);
                mVehicle.setColor(color);
                mVehicle.setPlates(plates);
                mVehicle.setDirty(true);
                mVehicle.save();

                Util.startSync(this, false);
                finish();
                return true;
            case R.id.menu_delete:
                if (mVehicle != null) {
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
