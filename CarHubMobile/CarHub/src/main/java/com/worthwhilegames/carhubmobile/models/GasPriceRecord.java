package com.worthwhilegames.carhubmobile.models;

import android.content.Context;
import com.mobsandgeeks.adapters.InstantText;
import com.orm.SugarRecord;
import com.worthwhilegames.carhubmobile.R;

public class GasPriceRecord extends SugarRecord<GasPriceRecord> {

    private String mStationId;
    private String mStation;
    private String mAddress;
    private String mPrice;
    private String mDistance;
    private String mLastUpdated;
    private String mCity;
    private String mRegion;
    private String mLat;
    private String mLng;

    public GasPriceRecord(Context arg0) {
        super(arg0);
    }

    /**
     * @return the stationId
     */
    public String getStationId() {
        return mStationId;
    }

    /**
     * @param aStationId the mStation to set
     */
    public void setStationId(String aStationId) {
        this.mStationId = aStationId;
    }

    /**
     * @return the station
     */
    @InstantText(viewId = R.id.gasStationName)
    public String getStation() {
        return mStation;
    }

    /**
     * @param aStation the mStation to set
     */
    public void setStation(String aStation) {
        this.mStation = aStation;
    }

    /**
     * @return the Address
     */
    @InstantText(viewId = R.id.gasStationAddress, formatString = "%s, ")
    public String getAddress() {
        return mAddress;
    }

    /**
     * @param aAddress the mAddress to set
     */
    public void setAddress(String aAddress) {
        this.mAddress = aAddress;
    }

    /**
     * @return the mPrice
     */
    @InstantText(viewId = R.id.gasStationPrice)
    public String getPrice() {
        return mPrice;
    }

    /**
     * @param aPrice the mPrice to set
     */
    public void setPrice(String aPrice) {
        this.mPrice = aPrice;
    }

    /**
     * @return the mDistance
     */
    @InstantText(viewId = R.id.gasStationDistance, formatString = "%s away - ")
    public String getDistance() {
        return mDistance;
    }

    /**
     * @param aDistance the mDistance to set
     */
    public void setDistance(String aDistance) {
        this.mDistance = aDistance;
    }

    /**
     * @return the mLastUpdated
     */
    @InstantText(viewId = R.id.gasStationLastUpdated, formatString = "Updated: %s")
    public String getLastUpdated() {
        return mLastUpdated;
    }

    /**
     * @param aLastUpdated the mLastUpdated to set
     */
    public void setLastUpdated(String aLastUpdated) {
        this.mLastUpdated = aLastUpdated;
    }

    /**
     * @return the mLat
     */
    public String getLat() {
        return mLat;
    }

    /**
     * @param aLat the mLat to set
     */
    public void setLat(String aLat) {
        this.mLat = aLat;
    }

    /**
     * @return the mLng
     */
    public String getLng() {
        return mLng;
    }

    /**
     * @param aLng the mLng to set
     */
    public void setLng(String aLng) {
        this.mLng = aLng;
    }

    /**
     * @return the mCity
     */
    @InstantText(viewId = R.id.gasStationAddressCity, formatString = "%s, ")
    public String getCity() {
        return mCity;
    }

    /**
     * @param aCity the mCity to set
     */
    public void setCity(String aCity) {
        this.mCity = aCity;
    }

    /**
     * @return the mRegion
     */
    @InstantText(viewId = R.id.gasStationAddressRegion)
    public String getRegion() {
        return mRegion;
    }

    /**
     * @param aRegion the mRegion to set
     */
    public void setRegion(String aRegion) {
        this.mRegion = aRegion;
    }
}
