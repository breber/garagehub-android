package com.worthwhilegames.carhubmobile.models;

import android.content.Context;

import com.orm.SugarRecord;

@SuppressWarnings("rawtypes")
public class GasPriceRecord extends SugarRecord {

	private String mStationId;
	private String mStation;
	private String mAddress;
	private String mPrice;
	private String mDistance;
	private String mLastUpdated;

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
	public String getStation() {
		return mStation;
	}

	/**
	 * @param aStationId the mStation to set
	 */
	public void setStation(String aStation) {
		this.mStation = aStation;
	}

	/**
	 * @return the Address
	 */
	public String getAddress() {
		return mAddress;
	}

	/**
	 * @param mAddress the mAddress to set
	 */
	public void setAddress(String aStationId) {
		this.mAddress = aStationId;
	}

	/**
	 * @return the mPrice
	 */
	public String getPrice() {
		return mPrice;
	}

	/**
	 * @param mPrice the mPrice to set
	 */
	public void setPrice(String aPrice) {
		this.mPrice = aPrice;
	}

	/**
	 * @return the mDistance
	 */
	public String getDistance() {
		return mDistance;
	}

	/**
	 * @param mDistance the mDistance to set
	 */
	public void setDistance(String aDistance) {
		this.mDistance = aDistance;
	}

	/**
	 * @return the mLastUpdated
	 */
	public String getLastUpdated() {
		return mLastUpdated;
	}

	/**
	 * @param mLastUpdated the mLastUpdated to set
	 */
	public void setLastUpdated(String aLastUpdated) {
		this.mLastUpdated = aLastUpdated;
	}
}
