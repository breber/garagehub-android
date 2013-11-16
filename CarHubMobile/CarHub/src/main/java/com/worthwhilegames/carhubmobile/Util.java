package com.worthwhilegames.carhubmobile;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author jamiekujawa
 */
public class Util {

    /**
     * A boolean to represent if the current build is a debug build
     */
    public static final boolean isDebugBuild = BuildConfig.DEBUG;

    /**
     * Should we hide ads?
     */
    public static final boolean shouldHideAds = isDebugBuild;

    public static final String PREF_ACCOUNT_NAME = "accountName";

    /**
     * The pref key for account name
     */
    public static final String PREF_ACCOUNT_NAME = "accountName";

    /**
     * Utility method for getting the SharedPreferences instance for the app
     *
     * @param ctx
     * @return
     */
    public static SharedPreferences getSharedPrefs(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences("Preferences", 0);

        // Migrate old preferences to new consolidated preferences
        SharedPreferences oldPrefs = ctx.getSharedPreferences("CarHubMobile", 0);
        if (oldPrefs.contains(PREF_ACCOUNT_NAME)) {
            // Add key to new preferences
            prefs.edit().putString(PREF_ACCOUNT_NAME, oldPrefs.getString(PREF_ACCOUNT_NAME, "")).commit();

            // Clear the old preferences
            oldPrefs.edit().clear().commit();
        }

        return prefs;
    }
}
