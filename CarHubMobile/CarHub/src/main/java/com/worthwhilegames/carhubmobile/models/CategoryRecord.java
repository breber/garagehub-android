package com.worthwhilegames.carhubmobile.models;

import android.content.Context;
import com.google.api.services.carhub.model.ExpenseCategory;

public class CategoryRecord extends SyncableRecord {

    private String mCategory;
    private String mSubcategory;

    public CategoryRecord(Context arg0) {
        super(arg0);
    }

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

}
