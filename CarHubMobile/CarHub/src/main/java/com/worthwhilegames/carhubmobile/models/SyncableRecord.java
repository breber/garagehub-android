package com.worthwhilegames.carhubmobile.models;

import android.util.Log;

import com.orm.StringUtil;
import com.orm.SugarRecord;
import com.worthwhilegames.carhubmobile.Util;

import java.util.List;

/**
 * An abstract class representing an AppEngine syncable record
 *
 * @author breber
 */
public abstract class SyncableRecord extends SugarRecord<SyncableRecord> {

    protected String mRemoteId;
    protected int mDirty;

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
        return mDirty != 0;
    }

    /**
     * @param aDirty the mDirty to set
     */
    public void setDirty(boolean aDirty) {
        this.mDirty = aDirty ? 1 : 0;
    }

    /**
     * Convenience method for finding all dirty records
     *
     * @param type
     * @param <T>
     * @return
     */
    public static <T extends SyncableRecord> List<T> findAllDirty(Class<T> type) {
        return T.find(type, StringUtil.toSQLName("mDirty") + " != 0");
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
                if (found.size() != 1) {
                    for (int i = 1; i < found.size(); i++) {
                        T t = found.get(i);
                        if (Util.isDebugBuild) {
                            Log.d("SyncableRecord", "More than one remote id. Deleting " + type.getSimpleName() + " Remote: " + t.getRemoteId() + " Local: " + t.getId());
                        }
                        t.delete();
                    }
                }
                return found.get(0);
            }
        }
        return null;
    }

    /**
     * Delete all records passed in
     * @param type
     * @param <T>
     * @return
     */
    public static <T extends SyncableRecord> void deleteAllInList(Class<T> type, List<T> toDelete) {
        for (T t : toDelete) {
            if (Util.isDebugBuild) {
                Log.d("SyncableRecord", "Deleting " + type.getSimpleName() + " Remote: " + t.getRemoteId() + " Local: " + t.getId());
            }
            t.delete();
        }
    }
}
