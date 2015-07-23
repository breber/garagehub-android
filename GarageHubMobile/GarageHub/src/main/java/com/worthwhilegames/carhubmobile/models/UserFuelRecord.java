package com.worthwhilegames.carhubmobile.models;

import android.content.Context;
import com.appspot.car_hub.garagehub.model.FuelRecord;
import com.mobsandgeeks.adapters.InstantText;
import com.orm.util.NamingHelper;
import com.worthwhilegames.carhubmobile.R;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class UserFuelRecord extends UserBaseExpenseRecord {

    private float mMpg;
    private int mOdometerStart;
    private int mOdometerEnd;
    private float mGallons;
    private float mCostPerGallon;
    private String mFuelGrade;

    @Override
    public void fromAPI(Object rec) {
        FuelRecord oth = (FuelRecord) rec;
        mRemoteId = oth.getServerId();
        mVehicle = UserVehicleRecord.findByRemoteId(UserVehicleRecord.class, "" + oth.getVehicle());
        try {
            mDate = cDateFormat.parse(oth.getDate()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mCategory = CategoryRecord.fuelCategory(CategoryRecord.class);
        mLocation = oth.getLocation();
        mDescription = oth.getDescription();
        mAmount = (float) ((double) oth.getAmount());
        mPictureUrl = oth.getPictureurl();
        mMpg = oth.getMpg().floatValue();
        mOdometerStart = oth.getOdometerStart();
        mOdometerEnd = oth.getOdometerEnd();
        mGallons = oth.getGallons().floatValue();
        mCostPerGallon = oth.getCostPerGallon().floatValue();
        mFuelGrade = oth.getFuelGrade();
    }

    @Override
    public FuelRecord toAPI() {
        FuelRecord toRet = new FuelRecord();
        toRet.setServerId(mRemoteId);
        toRet.setVehicle(Long.parseLong(mVehicle.getRemoteId()));
        toRet.setDate(cDateFormat.format(new Date(mDate)));
        toRet.setCategoryid(Long.parseLong(CategoryRecord.fuelCategory(CategoryRecord.class).getRemoteId()));
        toRet.setLocation(mLocation);
        toRet.setDescription(mDescription);
        toRet.setAmount((double) mAmount);
        toRet.setPictureurl(mPictureUrl);
        toRet.setMpg((double) mMpg);
        toRet.setOdometerStart(mOdometerStart);
        toRet.setOdometerEnd(mOdometerEnd);
        toRet.setGallons((double) mGallons);
        toRet.setCostPerGallon((double) mCostPerGallon);
        toRet.setFuelGrade(mFuelGrade);
        return toRet;
    }

    /**
     * @return the mMpg
     */
    public float getMpg() {
        return mMpg;
    }

    /**
     * @param aMpg the mMpg to set
     */
    public void setMpg(float aMpg) {
        this.mMpg = aMpg;
    }

    /**
     * @return a string odometer reading
     */
    @InstantText(viewId = R.id.odometerLabel)
    public String getOdometerString() {
        if ((getOdometerStart() != -1) &&
            (getOdometerStart() != getOdometerEnd())) {
            return "Odometer: " + getOdometerStart() + " - " + getOdometerEnd();
        } else {
            return "Odometer: " + getOdometerEnd();
        }
    }

    /**
     * @return the mOdometerStart
     */
    public int getOdometerStart() {
        return mOdometerStart;
    }

    /**
     * @param aOdometerStart the mOdometerStart to set
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
     * @param aOdometerEnd the mOdometerEnd to set
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
     * @param aGallons the mGallons to set
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
     * @param aCostPerGallon the mCostPerGallon to set
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
     * @param aFuelGrade the mFuelGrade to set
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
                "select * from " + NamingHelper.toSQLName(UserFuelRecord.class) +
                        " where " + NamingHelper.toSQLNameDefault("mVehicle") + " = '" + vehicle.getId() + "'" +
                        " order by " + NamingHelper.toSQLNameDefault("mOdometerEnd") + " desc limit 1");
        if (!list.isEmpty()) {
            return list.get(0);
        }

        return null;
    }
}
