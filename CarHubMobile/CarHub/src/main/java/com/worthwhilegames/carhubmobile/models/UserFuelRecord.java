package com.worthwhilegames.carhubmobile.models;

import java.util.List;

import android.content.Context;

import com.orm.StringUtil;

public class UserFuelRecord extends UserBaseExpenseRecord {

	private float mMpg;
	private int mOdometerStart;
	private int mOdometerEnd;
	private float mGallons;
	private float mCostPerGallon;
	private String mFuelGrade;

	public UserFuelRecord(Context arg0) {
		super(arg0);
	}

	/**
	 * @return the mMpg
	 */
	public float getMpg() {
		return mMpg;
	}

	/**
	 * @param mMpg the mMpg to set
	 */
	public void setMpg(float aMpg) {
		this.mMpg = aMpg;
	}

	/**
	 * @return the mOdometerStart
	 */
	public int getOdometerStart() {
		return mOdometerStart;
	}

	/**
	 * @param mOdometerStart the mOdometerStart to set
	 */
	public void setOdometerStart(int aOdometerStart) {
		this.mOdometerStart = aOdometerStart;
	}

	/**
	 * @return the mOdometerEnd
	 */
	public int getOdometerEnd() {
		return mOdometerEnd;
	}

	/**
	 * @param mOdometerEnd the mOdometerEnd to set
	 */
	public void setOdometerEnd(int aOdometerEnd) {
		this.mOdometerEnd = aOdometerEnd;
	}

	/**
	 * @return the mGallons
	 */
	public float getGallons() {
		return mGallons;
	}

	/**
	 * @param mGallons the mGallons to set
	 */
	public void setGallons(float aGallons) {
		this.mGallons = aGallons;
	}

	/**
	 * @return the mCostPerGallon
	 */
	public float getCostPerGallon() {
		return mCostPerGallon;
	}

	/**
	 * @param mCostPerGallon the mCostPerGallon to set
	 */
	public void setCostPerGallon(float aCostPerGallon) {
		this.mCostPerGallon = aCostPerGallon;
	}

	/**
	 * @return the mFuelGrade
	 */
	public String getFuelGrade() {
		return mFuelGrade;
	}

	/**
	 * @param mFuelGrade the mFuelGrade to set
	 */
	public void setFuelGrade(String aFuelGrade) {
		this.mFuelGrade = aFuelGrade;
	}

	/* UTILITY METHODS */

	/**
	 * Get the most recent UserFuelRecord for the given vehicle
	 * 
	 * @param vehicle
	 * @return
	 */
	public static UserFuelRecord getLatest(UserVehicleRecord vehicle) {
		List<UserFuelRecord> list = UserFuelRecord.findWithQuery(UserFuelRecord.class,
				"select * from " + StringUtil.toSQLName(UserFuelRecord.class.getSimpleName()) + " order by " + StringUtil.toSQLName("mOdometerEnd") + " desc limit 1");
		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}
}
