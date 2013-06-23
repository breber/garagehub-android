package com.worthwhilegames.carhubmobile.models;

import java.sql.Date;
import java.util.List;

import android.content.Context;

import com.google.api.services.carhub.model.MaintenanceRecord;
import com.orm.StringUtil;

public class UserMaintenanceRecord extends UserBaseExpenseRecord {

	private int mOdometer;

	public UserMaintenanceRecord(Context arg0) {
		super(arg0);
	}

    @Override
    public void fromAPI(Object rec) {
        MaintenanceRecord oth = (MaintenanceRecord) rec;
        mRemoteId = oth.getServerId();
        mVehicle = UserVehicleRecord.findByRemoteId(UserVehicleRecord.class, "" + oth.getVehicle());
        mDate = Date.valueOf(oth.getDate()).getTime();
        mCategoryId = oth.getCategoryid();
        mLocation = oth.getLocation();
        mDescription = oth.getDescription();
        mAmount = (float) ((double) oth.getAmount());
        mPictureUrl = oth.getPictureurl();
        mOdometer = oth.getOdometer();
    }

    @Override
    public MaintenanceRecord toAPI() {
        MaintenanceRecord toRet = new MaintenanceRecord();
        toRet.setServerId(mRemoteId);
        toRet.setDate(new Date(mDate).toString());
        toRet.setCategoryid(mCategoryId);
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
	public int getOdometer() {
		return mOdometer;
	}

	/**
	 * @param mOdometer the mOdometer to set
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
				"select * from " + StringUtil.toSQLName(UserMaintenanceRecord.class.getSimpleName()) + " order by " + StringUtil.toSQLName("mOdometer") + " desc limit 1");
		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}
}
