package com.worthwhilegames.carhubmobile.models;

import android.content.Context;

import com.orm.SugarRecord;

/**
 * An abstract class representing an AppEngine syncable record
 * 
 * @author breber
 */
public abstract class SyncableRecord extends SugarRecord<SyncableRecord> {

	private String mRemoteId;
	protected boolean mDirty;
	protected long mLastUpdated;

	public SyncableRecord(Context arg0) {
		super(arg0);
	}

	/**
	 * @return the mRemoteId
	 */
	public String getRemoteId() {
		return mRemoteId;
	}

	/**
	 * @param mRemoteId the mRemoteId to set
	 */
	public void setRemoteId(String aRemoteId) {
		this.mRemoteId = aRemoteId;
	}

	/**
	 * @return the mDirty
	 */
	public boolean isDirty() {
		return mDirty;
	}

	/**
	 * @param mDirty the mDirty to set
	 */
	public void setDirty(boolean aDirty) {
		this.mDirty = aDirty;
	}

	/**
	 * @return the mLastUpdated
	 */
	public long getLastUpdated() {
		return mLastUpdated;
	}

	/**
	 * @param mLastUpdated the mLastUpdated to set
	 */
	public void setLastUpdated(long aLastUpdated) {
		this.mLastUpdated = aLastUpdated;
	}
}
