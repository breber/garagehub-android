package com.worthwhilegames.carhubmobile.sync;

import android.content.Context;
import com.appspot.car_hub.carhub.Carhub;
import com.appspot.car_hub.carhub.model.ExpenseCategory;
import com.appspot.car_hub.carhub.model.ExpenseCategoryCollection;
import com.worthwhilegames.carhubmobile.Util;
import com.worthwhilegames.carhubmobile.models.CategoryRecord;

import java.io.IOException;

public class FetchCategoryRecordsTask implements ISyncTask {

    /**
     * The context
     */
    private Context mContext;

    /**
     * The GarageHub service for interacting with AppEngine
     */
    protected Carhub mService;

    public FetchCategoryRecordsTask(Context ctx, Carhub service) {
        mContext = ctx;
        mService = service;
    }

    @Override
    public boolean performTask() {
        ExpenseCategoryCollection records;
        long prevLastModified = Util.getSharedPrefs(mContext).getLong(FetchCategoryRecordsTask.class.getSimpleName() + "_lastUpdate", 0);
        long currentTime = System.currentTimeMillis();

        if ((currentTime - prevLastModified) < (24 * 60 * 60 * 1000)) {
            // Skip the update for now
            return false; // TODO: should be false?
        }

        try {
            String pageToken = null;

            do {
                Carhub.Category.List query = mService.category().list();

                query = query.setPageToken(pageToken);

                // Get a list of all records currently on the server
                records = query.execute();
                if (records != null) {
                    for (ExpenseCategory r : records.getItems()) {
                        // Try and find a record locally to update
                        CategoryRecord toUpdate = CategoryRecord.findByRemoteId(CategoryRecord.class, r.getServerId());

                        // If one can't be found, create a new one
                        if (toUpdate == null) {
                            toUpdate = new CategoryRecord();
                        }

                        // Update the local copy with the server information
                        toUpdate.fromAPI(r);
                        toUpdate.save();
                    }

                    pageToken = records.getNextPageToken();
                }
            } while (pageToken != null);

            Util.getSharedPrefs(mContext).edit().putLong(FetchCategoryRecordsTask.class.getSimpleName() + "_lastUpdate", currentTime).commit();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
