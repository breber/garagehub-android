package com.worthwhilegames.carhubmobile.models;

import android.content.Context;
import com.orm.StringUtil;
import com.orm.SugarRecord;

import java.util.List;

/**
 * An abstract class representing an AppEngine syncable record
 * 
 * @author breber
 */
public abstract class SyncableRecord extends SugarRecord<SyncableRecord> {

	protected String mRemoteId;
	protected boolean mDirty;
	protected long mLastUpdated;

	public SyncableRecord(Context arg0) {
		super(arg0);
	}

    public abstract void fromAPI(Object veh);

    public abstract Object toAPI();

	/**
	 * @return the mRemoteId
	 */
	public String getRemoteId() {
		return mRemoteId;
	}

	/**
	 * @param aRemoteId the mRemoteId to set
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
	 * @param aDirty the mDirty to set
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
	 * @param aLastUpdated the mLastUpdated to set
	 */
	public void setLastUpdated(long aLastUpdated) {
		this.mLastUpdated = aLastUpdated;
	}

    /**
     * Convenience method for finding all dirty records
     *
     * @param type
     * @param <T>
     * @return
     */
    public static <T extends SyncableRecord> List<T> findAllDirty(Class<T> type) {
        return T.find(type, StringUtil.toSQLName("mDirty") + " = 1");
    }

    /**
     * Convenience method for finding by AppEngine remote id
     *
     * @param type
     * @param remoteId
     * @param <T>
     * @return
     */
    public static <T extends SyncableRecord> T findByRemoteId(Class<T> type, String remoteId) {
        if (remoteId != null) {
            List<T> found = T.find(type, StringUtil.toSQLName("mRemoteId") + " = ? LIMIT 1", remoteId);
            if (!found.isEmpty()) {
                return found.get(0);
            }
        }
        return null;
    }
}
