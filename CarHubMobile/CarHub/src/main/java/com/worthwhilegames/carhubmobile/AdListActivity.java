package com.worthwhilegames.carhubmobile;

import android.app.ListActivity;
import android.view.View;

/**
 * Created by breber on 6/22/13.
 */
public class AdListActivity extends ListActivity {

    protected boolean mAdsHidden = Util.shouldHideAds;

    @Override
    protected void onStart() {
        super.onStart();

        if (mAdsHidden) {
            View v = findViewById(R.id.adView);

            if (v != null) {
                v.setVisibility(View.GONE);
            }
        }
    }
}
