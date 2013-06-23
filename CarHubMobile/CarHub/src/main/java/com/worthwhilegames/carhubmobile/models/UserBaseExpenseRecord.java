package com.worthwhilegames.carhubmobile.models;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.google.api.services.carhub.model.BaseExpense;

import android.content.Context;

import com.orm.StringUtil;

public class UserBaseExpenseRecord extends SyncableRecord {

	protected UserVehicleRecord mVehicle;
    protected long mDate;
    protected Integer mCategoryId; // TODO: change to category when we sync that
    protected String mLocation;
    protected String mDescription;
    protected float mAmount;
    protected String mPictureUrl;

	public UserBaseExpenseRecord(Context arg0) {
		super(arg0);
	}

    @Override
    public void fromAPI(Object exp) {
        BaseExpense oth = (BaseExpense) exp;
        mRemoteId = oth.getServerId();
        mVehicle = UserVehicleRecord.findByRemoteId(UserVehicleRecord.class, "" + oth.getVehicle());
        mDate = Date.valueOf(oth.getDate()).getTime();
        mCategoryId = oth.getCategoryid();
        mLocation = oth.getLocation();
        mDescription = oth.getDescription();
        mAmount = (float) ((double) oth.getAmount());
        mPictureUrl = oth.getPictureurl();
    }

    @Override
    public Object toAPI() {
        BaseExpense toRet = new BaseExpense();
        toRet.setServerId(mRemoteId);
        toRet.setDate(new Date(mDate).toString());
        toRet.setCategoryid(mCategoryId);
        toRet.setLocation(mLocation);
        toRet.setDescription(mDescription);
        toRet.setAmount((double) mAmount);
        toRet.setPictureurl(mPictureUrl);
        return toRet;
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
	 * @param aDate the mlong to set
	 */
	public void setDate(long aDate) {
		this.mDate = aDate;
	}

	/**
	 * @return the mCategoryId
	 */
	public Integer getCategoryId() {
		return mCategoryId;
	}

	/**
	 * @param aCategoryId the mCategoryId to set
	 */
	public void setCategoryId(Integer aCategoryId) {
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
