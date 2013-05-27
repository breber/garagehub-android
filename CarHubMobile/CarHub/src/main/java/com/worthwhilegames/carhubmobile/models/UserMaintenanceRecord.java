package com.worthwhilegames.carhubmobile.models;

import java.util.List;

import android.content.Context;

import com.orm.StringUtil;

public class UserMaintenanceRecord extends UserBaseExpenseRecord {

	private int mOdometer;

	public UserMaintenanceRecord(Context arg0) {
		super(arg0);
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
