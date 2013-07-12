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

    /**
     * Utility method for getting the SharedPreferences instance for the app
     *
     * @param ctx
     * @return
     */
    public static SharedPreferences getSharedPrefs(Context ctx) {
        return ctx.getSharedPreferences("Preferences", 0);
    }
}
