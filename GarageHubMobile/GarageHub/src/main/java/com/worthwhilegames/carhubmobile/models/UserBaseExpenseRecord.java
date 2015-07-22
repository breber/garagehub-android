package com.worthwhilegames.carhubmobile.models;

import android.content.Context;
import android.text.format.DateFormat;
import com.appspot.car_hub.carhub.model.UserExpenseRecord;
import com.mobsandgeeks.adapters.InstantText;
import com.orm.dsl.Ignore;
import com.orm.util.NamingHelper;
import com.worthwhilegames.carhubmobile.R;

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
    protected CategoryRecord mCategory;
    protected String mLocation;
    protected String mDescription;
    protected float mAmount;
    protected String mPictureUrl;

    public UserBaseExpenseRecord() {
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
        mCategory = CategoryRecord.findByRemoteId(CategoryRecord.class, "" + oth.getCategoryid());
        mLocation = oth.getLocation();
        mDescription = oth.getDescription();
        mAmount = (float) ((double) oth.getAmount());
        mPictureUrl = oth.getPictureurl();
    }

    @Override
    public Object toAPI() {
        UserExpenseRecord toRet = new UserExpenseRecord();
        toRet.setServerId(mRemoteId);
        toRet.setVehicle(Long.parseLong(mVehicle.getRemoteId()));
        toRet.setDate(cDateFormat.format(new Date(mDate)));
        toRet.setCategoryid(Long.parseLong(mCategory.getRemoteId()));
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
     * @return the date as a string
     */
    @InstantText(viewId = R.id.dateLabel)
    public String getFormattedDate() {
        return DateFormat.format("MM/dd/yyyy", new Date(getDate())).toString();
    }

    /**
     * @param aDate the mlong to set
     */
    public void setDate(Long aDate) {
        this.mDate = aDate;
    }

    /**
     * @return the mCategory
     */
    public CategoryRecord getCategory() {
        return mCategory;
    }

    /**
     * @param aCategory the mCategory to set
     */
    public void setCategoryId(CategoryRecord aCategory) {
        this.mCategory = aCategory;
    }

    /**
     * @return the mLocation
     */
    @InstantText(viewId = R.id.locationName)
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
    @InstantText(viewId = R.id.descriptionLabel)
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
    @InstantText(viewId = R.id.priceLabel, formatString = "$%.2f")
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
        String sqlName = null;
        try {
            sqlName = NamingHelper.toSQLName(type.getField("mVehicle"));
        } catch (Exception e) {
            // Nothing to do
        }

        if (sqlName != null && vehicle != null) {
            return UserBaseExpenseRecord.find(type, sqlName + " = ?", vehicle.getId() + "");
        } else {
            return new ArrayList<T>();
        }
    }
}
