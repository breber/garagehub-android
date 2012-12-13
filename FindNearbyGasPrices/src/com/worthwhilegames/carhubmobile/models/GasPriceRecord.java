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
	
	public GasPriceRecord(Context arg0) {
		super(arg0);
	}
	
	/**
	 * @return the mStationId
	 */
	public String getmStationId() {
		return mStationId;
	}
	
	/**
	 * @param mStationId the mStationId to set
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
	public void setStation(String aStationId) {
		this.mStation = aStationId;
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
	
}
