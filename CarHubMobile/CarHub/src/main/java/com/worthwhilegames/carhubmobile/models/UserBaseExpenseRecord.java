package com.worthwhilegames.carhubmobile.models;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.orm.StringUtil;

public class UserBaseExpenseRecord extends SyncableRecord {

	private UserVehicleRecord mVehicle;
	private long mDate;
	private String mCategoryId; // TODO: change to category when we sync that
	private String mLocation;
	private String mDescription;
	private float mAmount;
	private String mPictureUrl;

	public UserBaseExpenseRecord(Context arg0) {
		super(arg0);
	}

	/**
	 * @return the mVehicle
	 */
	public UserVehicleRecord getVehicle() {
		return mVehicle;
	}

	/**
	 * @param mVehicle the mVehicle to set
	 */
	public void setVehicle(UserVehicleRecord aVehicle) {
		this.mVehicle = aVehicle;
	}

	/**
	 * @return the mDate
	 */
	public long getDate() {
		return mDate;
	}

	/**
	 * @param mlong the mlong to set
	 */
	public void setDate(long aDate) {
		this.mDate = aDate;
	}

	/**
	 * @return the mCategoryId
	 */
	public String getCategoryId() {
		return mCategoryId;
	}

	/**
	 * @param mCategoryId the mCategoryId to set
	 */
	public void setCategoryId(String aCategoryId) {
		this.mCategoryId = aCategoryId;
	}

	/**
	 * @return the mLocation
	 */
	public String getLocation() {
		return mLocation;
	}

	/**
	 * @param mLocation the mLocation to set
	 */
	public void setLocation(String aLocation) {
		this.mLocation = aLocation;
	}

	/**
	 * @return the mDescription
	 */
	public String getDescription() {
		return mDescription;
	}

	/**
	 * @param mDescription the mDescription to set
	 */
	public void setDescription(String aDescription) {
		this.mDescription = aDescription;
	}

	/**
	 * @return the mAmount
	 */
	public float getAmount() {
		return mAmount;
	}

	/**
	 * @param mAmount the mAmount to set
	 */
	public void setAmount(float aAmount) {
		this.mAmount = aAmount;
	}

	/**
	 * @return the mPictureUrl
	 */
	public String getPictureUrl() {
		return mPictureUrl;
	}

	/**
	 * @param mPictureUrl the mPictureUrl to set
	 */
	public void setPictureUrl(String aPictureUrl) {
		this.mPictureUrl = aPictureUrl;
	}

	/**
	 * Gets all records for a given vehicle
	 * 
	 * @param type
	 * @param vehicle
	 * @return
	 */
	public static <T extends UserBaseExpenseRecord> List<T> getRecordsForVehicle(Class<T> type, UserVehicleRecord vehicle) {
		if (vehicle != null) {
			return UserBaseExpenseRecord.find(type, StringUtil.toSQLName("mVehicle") + " = ?", vehicle.getId() + "");
		} else {
			return new ArrayList<T>();
		}
	}
}
