package com.worthwhilegames.carhubmobile.models;

import android.content.Context;
import com.appspot.car_hub.garagehub.model.ExpenseCategory;
import com.orm.util.NamingHelper;

import java.util.List;

public class CategoryRecord extends SyncableRecord {

    private String mCategory;
    private String mSubcategory;

    @Override
    public void fromAPI(Object rec) {
        ExpenseCategory oth = (ExpenseCategory) rec;
        mRemoteId = oth.getServerId();
        mCategory = oth.getCategory();
        mSubcategory = oth.getSubcategory();
    }

    @Override
    public ExpenseCategory toAPI() {
        ExpenseCategory toRet = new ExpenseCategory();
        toRet.setServerId(mRemoteId);
        toRet.setCategory(mCategory);
        toRet.setSubcategory(mSubcategory);
        return toRet;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String aCategory) {
        this.mCategory = aCategory;
    }

    public String getSubcategory() {
        return mSubcategory;
    }

    public void setSubcategory(String aSubcategory) {
        this.mSubcategory = aSubcategory;
    }

    public String toString() {
        if (mSubcategory != null && !"".equals(mSubcategory)) {
            return mSubcategory;
        } else {
            return mCategory;
        }
    }

    /**
     * Convenience method for finding the fuel category
     *
     * @param type
     * @param <T>
     * @return
     */
    public static <T extends SyncableRecord> T fuelCategory(Class<T> type) {
        List<T> toRet = T.find(type, NamingHelper.toSQLNameDefault("mCategory") + " = 'Fuel Up'");
        if (!toRet.isEmpty()) {
            return toRet.get(0);
        }
        return null;
    }

    /**
     * Convenience method for finding the expense categories
     *
     * @param type
     * @param <T>
     * @return
     */
    public static <T extends SyncableRecord> List<T> getExpenseCategories(Class<T> type) {
        return T.find(type, NamingHelper.toSQLNameDefault("mCategory") + " != 'Maintenance'");
    }

    /**
     * Convenience method for finding the maintenance categories
     *
     * @param type
     * @param <T>
     * @return
     */
    public static <T extends SyncableRecord> List<T> getMaintenanceCategories(Class<T> type) {
        return T.find(type, NamingHelper.toSQLNameDefault("mCategory") + " == 'Maintenance'");
    }
}
