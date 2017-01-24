package com.worthwhilegames.carhubmobile.models;

import com.appspot.car_hub.garagehub.model.UserVehicle;
import com.mobsandgeeks.adapters.InstantText;
import com.worthwhilegames.carhubmobile.R;

import java.util.LinkedList;
import java.util.List;

public class UserVehicleRecord extends SyncableRecord {

    private String mMake;
    private String mModel;
    private String mYear;
    private String mColor;
    private String mPlates;

    @Override
    public void fromAPI(Object oth) {
        UserVehicle veh = (UserVehicle) oth;
        mRemoteId = veh.getServerId();
        mMake = veh.getMake();
        mModel = veh.getModel();
        mYear = veh.getYear();
        mColor = veh.getColor();
        mPlates = veh.getPlates();
    }

    @Override
    public UserVehicle toAPI() {
        UserVehicle veh = new UserVehicle();
        veh.setServerId(mRemoteId);
        veh.setMake(mMake);
        veh.setModel(mModel);
        veh.setYear(mYear);
        veh.setColor(mColor);
        veh.setPlates(mPlates);
        return veh;
    }

    /**
     * @return a vehicle name
     */
    @InstantText(viewId = R.id.text1)
    public String getName() {
        return getYear() + " " + getMake() + " " + getModel();
    }

    /**
     * @return the mMake
     */
    public String getMake() {
        return mMake;
    }

    /**
     * @param aMake the mMake to set
     */
    public void setMake(String aMake) {
        this.mMake = aMake;
    }

    /**
     * @return the mModel
     */
    public String getModel() {
        return mModel;
    }

    /**
     * @param aModel the mModel to set
     */
    public void setModel(String aModel) {
        this.mModel = aModel;
    }

    /**
     * @return the mYear
     */
    public String getYear() {
        return mYear;
    }

    /**
     * @param aYear the mYear to set
     */
    public void setYear(String aYear) {
        this.mYear = aYear;
    }

    /**
     * @return the mColor
     */
    public String getColor() {
        return mColor;
    }

    /**
     * @param aColor the mColor to set
     */
    public void setColor(String aColor) {
        this.mColor = aColor;
    }

    /**
     * @return the mPlates
     */
    public String getPlates() {
        return mPlates;
    }

    /**
     * @param aPlates the mPlates to set
     */
    public void setPlates(String aPlates) {
        this.mPlates = aPlates;
    }

    /**
     * @return the latest odometer reading
     */
    public int getLatestOdometer() {
        int odometer = -1;

        // Check fuel records
        UserFuelRecord latestFuel = UserFuelRecord.getLatest(this);
        if (latestFuel != null) {
            if (latestFuel.getOdometerEnd() > odometer) {
                odometer = latestFuel.getOdometerEnd();
            }
        }

        // Check Maintenance records
        UserMaintenanceRecord latestMaint = UserMaintenanceRecord.getLatest(this);
        if (latestMaint != null) {
            if (latestMaint.getOdometer() > odometer) {
                odometer = latestMaint.getOdometer();
            }
        }

        return odometer;
    }

    /**
     * @return the total amount spent on expenses
     */
    public float getTotalCost() {
        List<UserFuelRecord> fuel = UserFuelRecord.getRecordsForVehicle(UserFuelRecord.class, this);
        List<UserBaseExpenseRecord> base = UserBaseExpenseRecord.getRecordsForVehicle(UserBaseExpenseRecord.class, this);
        List<UserMaintenanceRecord> maint = UserMaintenanceRecord.getRecordsForVehicle(UserMaintenanceRecord.class, this);

        List<UserBaseExpenseRecord> all = new LinkedList<UserBaseExpenseRecord>(fuel);
        all.addAll(base);
        all.addAll(maint);

        float totalCost = 0;

        for (UserBaseExpenseRecord r : all) {
            totalCost += r.getAmount();
        }

        return totalCost;
    }
}
