package com.worthwhilegames.carhubmobile;

public class GasPriceRowModel {

	
	private String stationName;
	private String sationID;
	private String stationAddress;
	private String stationPrice;
	private String stationDistance;
	private String lastUpdated;
	
	public GasPriceRowModel(String stationName, String sationID,
			String stationAddress, String stationPrice, String stationDistance,
			String lastUpdated) {
		super();
		this.stationName = stationName;
		this.sationID = sationID;
		this.stationAddress = stationAddress;
		this.stationPrice = stationPrice;
		this.stationDistance = stationDistance;
		this.lastUpdated = lastUpdated;
	}

	public GasPriceRowModel() {
		super();
	}
	
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getSationID() {
		return sationID;
	}
	public void setSationID(String sationID) {
		this.sationID = sationID;
	}
	public String getStationAddress() {
		return stationAddress;
	}
	public void setStationAddress(String stationAddress) {
		this.stationAddress = stationAddress;
	}
	public String getStationPrice() {
		return stationPrice;
	}
	public void setStationPrice(String stationPrice) {
		this.stationPrice = stationPrice;
	}
	public String getStationDistance() {
		return stationDistance;
	}
	public void setStationDistance(String stationDistance) {
		this.stationDistance = stationDistance;
	}
	public String getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	
}
