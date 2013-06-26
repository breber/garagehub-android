package com.worthwhilegames.carhubmobile.models;

import android.content.Context;
import com.google.api.services.carhub.model.UserExpenseRecord;
import com.orm.StringUtil;
import com.orm.dsl.Ignore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class UserBaseExpenseRecord extends SyncableRecord {

    @Ignore
    protected static final SimpleDateFormat cDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    protected UserVehicleRecord mVehicle;
    protected long mDate;
    protected int mCategoryId; // TODO: change to category when we sync that
    protected String mLocation;
    protected String mDescription;
    protected float mAmount;
    protected String mPictureUrl;

    public UserBaseExpenseRecord(Context arg0) {
        super(arg0);
        cDateFormat.setTimeZone(TimeZone.getDefault());
    }

    @Override
    public void fromAPI(Object exp) {
        UserExpenseRecord oth = (UserExpenseRecord) exp;
        mRemoteId = oth.getServerId();
        mVehicle = UserVehicleRecord.findByRemoteId(UserVehicleRecord.class, "" + oth.getVehicle());
        try {
            mDate = cDateFormat.parse(oth.getDate()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mCategoryId = oth.getCategoryid();
        mLocation = oth.getLocation();
        mDescription = oth.getDescription();
        mAmount = (float) ((double) oth.getAmount());
        mPictureUrl = oth.getPictureurl();
    }

    @Override
    public Object toAPI() {
        UserExpenseRecord toRet = new UserExpenseRecord();
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
     * @param aVehicle the mVehicle to set
     */
    public void setVehicle(UserVehicleRecord aVehicle) {
        this.mVehicle = aVehicle;
    }

    /**
     * @return the mDate
     */
    public Long getDate() {
        return mDate;
    }

    /**
     * @param aDate the mlong to set
     */
    public void setDate(Long aDate) {
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
     * @param aLocation the mLocation to set
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
     * @param aDescription the mDescription to set
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
     * @param aAmount the mAmount to set
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
     * @param aPictureUrl the mPictureUrl to set
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
