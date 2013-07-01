package com.worthwhilegames.carhubmobile.util;

import android.content.Context;
import android.os.AsyncTask;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.carhub.Carhub;

/**
 * @author breber
 */
public abstract class AuthenticatedHttpRequest extends AsyncTask<Void, Void, Object> {

    public interface AuthenticatedHttpRequestCallback {
        void taskDidFinish();
    }

    /**
     * The context to get preferences with
     */
    protected Context mContext;

    /**
     * Current credentials
     */
    protected GoogleAccountCredential mCreds;

    /**
     * Current Carhub service
     */
    protected Carhub mService;

    /**
     * The delegate to notify when the task is complete
     */
    protected AuthenticatedHttpRequestCallback mDelegate;

    public AuthenticatedHttpRequest(Context ctx, Carhub service) {
        this(ctx, service, null);
    }

    public AuthenticatedHttpRequest(Context ctx, Carhub service, AuthenticatedHttpRequestCallback delegate) {
        this.mContext = ctx;
        this.mDelegate = delegate;
        this.mService = service;
    }
}
