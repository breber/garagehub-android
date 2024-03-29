package com.worthwhilegames.carhubmobile.sync;

import android.content.Context;
import com.appspot.car_hub.garagehub.Garagehub;
import com.appspot.car_hub.garagehub.model.ModelsActiveRecords;
import com.appspot.car_hub.garagehub.model.UserExpenseRecord;
import com.appspot.car_hub.garagehub.model.UserExpenseRecordCollection;
import com.worthwhilegames.carhubmobile.Util;
import com.worthwhilegames.carhubmobile.models.UserBaseExpenseRecord;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FetchUserBaseExpenseRecordsTask implements ISyncTask {

    /**
     * The Vehicle to get expense records for
     */
    private UserVehicleRecord mVehicle;

    /**
     * The context
     */
    private Context mContext;

    /**
     * The GarageHub service for interacting with AppEngine
     */
    protected Garagehub mService;

    public FetchUserBaseExpenseRecordsTask(Context ctx, Garagehub service, UserVehicleRecord vehicle) {
        mContext = ctx;
        mService = service;
        mVehicle = vehicle;
    }

    @Override
    public boolean performTask() {
        UserExpenseRecordCollection records;
        long prevLastModified = Util.getSharedPrefs(mContext).getLong(FetchUserBaseExpenseRecordsTask.class.getSimpleName() + "_" + mVehicle.getRemoteId() + "_lastUpdate", 0);

        try {
            // Handle all records deleted on the server
            List<UserBaseExpenseRecord> allLocal = UserBaseExpenseRecord.getRecordsForVehicle(UserBaseExpenseRecord.class, mVehicle);
            List<UserBaseExpenseRecord> toDelete = new ArrayList<UserBaseExpenseRecord>();
            ModelsActiveRecords active = mService.expense().active(Long.parseLong(mVehicle.getRemoteId())).execute();
            List<String> activeList = active.getActive();
            if (activeList != null) {
                for (UserBaseExpenseRecord rec : allLocal) {
                    String remoteId = rec.getRemoteId();
                    if (remoteId != null && !"".equals(remoteId) && !activeList.contains(remoteId)) {
                        toDelete.add(rec);
                    }
                }

                UserBaseExpenseRecord.deleteAllInList(UserBaseExpenseRecord.class, toDelete);
            }

            // Get a list of all records currently on the server
            String pageToken = null;
            do {
                Garagehub.Expense.List query = mService.expense().list(Long.parseLong(mVehicle.getRemoteId()));
                if (prevLastModified != 0) {
                    query = query.setModifiedSince(prevLastModified + "");
                }
                query = query.setPageToken(pageToken);

                // Get a list of all records currently on the server
                records = query.execute();
                if (records != null && records.getItems() != null) {
                    for (UserExpenseRecord r : records.getItems()) {
                        // Try and find a record locally to update
                        UserBaseExpenseRecord toUpdate = UserBaseExpenseRecord.findByRemoteId(UserBaseExpenseRecord.class, r.getServerId());

                        // If one can't be found, create a new one
                        if (toUpdate == null) {
                            toUpdate = new UserBaseExpenseRecord();
                        }

                        // Update the local copy with the server information
                        toUpdate.fromAPI(r);
                        toUpdate.setDirty(false);
                        toUpdate.save();
                    }

                    pageToken = records.getNextPageToken();
                }
            } while (pageToken != null);


            // Send all records that are dirty
            for (UserBaseExpenseRecord rec : UserBaseExpenseRecord.findAllDirty(UserBaseExpenseRecord.class)) {
                // Convert to UserVehicle
                UserExpenseRecord toSend = (UserExpenseRecord) rec.toAPI();

                // Send to AppEngine
                UserExpenseRecord sent;

                if (toSend.getServerId() != null &&
                        !"".equals(toSend.getServerId())) {
                    sent = mService.expense().update(toSend).execute();
                } else {
                    sent = mService.expense().add(toSend).execute();
                }

                // Update our local copy (last updated, remote id, etc)
                rec.fromAPI(sent);

                // Save the record
                rec.setDirty(false);
                rec.save();
            }

            long currentTime = System.currentTimeMillis();
            Util.getSharedPrefs(mContext).edit().putLong(FetchUserBaseExpenseRecordsTask.class.getSimpleName() + "_" + mVehicle.getRemoteId() + "_lastUpdate", currentTime).commit();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
