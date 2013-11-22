package com.worthwhilegames.carhubmobile;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

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

    // Content provider authority
    public static final String AUTHORITY = "com.worthwhilegames.carhubmobile.provider";

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

    /**
     * Reads an entire input stream as a String. Closes the input stream.
     */
    public static String readStreamAsString(InputStream in) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];
            int count;
            do {
                count = in.read(buffer);
                if (count > 0) {
                    out.write(buffer, 0, count);
                }
            } while (count >= 0);
            return out.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("The JVM does not support the compiler's default encoding.", e);
        } catch (IOException e) {
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException ignored) {    }
        }
    }

    public static void startSync(Context ctx, boolean important) {
        String accountName = getAccountName(ctx);
        AccountManager manager = AccountManager.get(ctx);
        if (manager != null) {
            Account[] accounts = manager.getAccountsByType("com.google");
            for (Account a : accounts) {
                if (a.name.equals(accountName)) {
                    Bundle b = new Bundle();
                    b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
                    if (important) {
                        // Disable sync backoff and ignore sync preferences. In other words...perform sync NOW!
                        b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                    }
                    ContentResolver.requestSync(a, Util.AUTHORITY, b);
                    ContentResolver.setSyncAutomatically(a, AUTHORITY, true);
                }
            }
        }
    }

    public static String getAccountName(Context ctx) {
        SharedPreferences settings = getSharedPrefs(ctx);
        return settings.getString(Util.PREF_ACCOUNT_NAME, "");
    }

}
