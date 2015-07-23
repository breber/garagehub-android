package com.worthwhilegames.carhubmobile.models;

import android.content.Context;
import com.appspot.car_hub.garagehub.model.MaintenanceRecord;
import com.mobsandgeeks.adapters.InstantText;
import com.orm.util.NamingHelper;
import com.worthwhilegames.carhubmobile.R;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class UserMaintenanceRecord extends UserBaseExpenseRecord {

    private int mOdometer;

    @Override
    public void fromAPI(Object rec) {
        MaintenanceRecord oth = (MaintenanceRecord) rec;
        mRemoteId = oth.getServerId();
        mVehicle = UserVehicleRecord.findByRemoteId(UserVehicleRecord.class, "" + oth.getVehicle());
        try {
            mDate = cDateFormat.parse(oth.getDate()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mCategory = CategoryRecord.findByRemoteId(CategoryRecord.class, "" + oth.getCategoryid());
        mLocation = oth.getLocation();
        mDescription = oth.getDescription();
        mAmount = oth.getAmount().floatValue();
        mPictureUrl = oth.getPictureurl();
        mOdometer = oth.getOdometer();
    }

    @Override
    public MaintenanceRecord toAPI() {
        MaintenanceRecord toRet = new MaintenanceRecord();
        toRet.setServerId(mRemoteId);
        toRet.setVehicle(Long.parseLong(mVehicle.getRemoteId()));
        toRet.setDate(cDateFormat.format(new Date(mDate)));
        toRet.setCategoryid(Long.parseLong(mCategory.getRemoteId()));
        toRet.setLocation(mLocation);
        toRet.setDescription(mDescription);
        toRet.setAmount((double) mAmount);
        toRet.setPictureurl(mPictureUrl);
        toRet.setOdometer(mOdometer);
        return toRet;
    }

    /**
     * @return the mOdometer
     */
    @InstantText(viewId = R.id.odometerLabel, formatString = "Odometer: %d")
    public int getOdometer() {
        return mOdometer;
    }

    /**
     * @param aOdometer the mOdometer to set
     */
    public void setOdometer(int aOdometer) {
        this.mOdometer = aOdometer;
    }


    /* UTILITY METHODS */

    /**
     * Get the most recent UserMaintenanceRecord for the given vehicle
     *
     * @param vehicle
     * @return
     */
    public static UserMaintenanceRecord getLatest(UserVehicleRecord vehicle) {
        List<UserMaintenanceRecord> list = UserFuelRecord.findWithQuery(UserMaintenanceRecord.class,
                "select * from " + NamingHelper.toSQLName(UserMaintenanceRecord.class) +
                        " where " + NamingHelper.toSQLNameDefault("mVehicle") + " = '" + vehicle.getId() + "'" +
                        " order by " + NamingHelper.toSQLNameDefault("mOdometer") + " desc limit 1");
        if (!list.isEmpty()) {
            return list.get(0);
        }

        return null;
    }
}
