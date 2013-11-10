package com.worthwhilegames.carhubmobile;

import android.app.ListActivity;
import android.view.View;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by breber on 6/22/13.
 */
public class AdListActivity extends ListActivity {

    private boolean mAdsHidden = Util.shouldHideAds;

    @Override
    protected void onStart() {
        super.onStart();

        if (mAdsHidden) {
            View v = findViewById(R.id.adView);

            if (v != null) {
                v.setVisibility(View.GONE);
            }
        } else {
            AdView adView = (AdView)this.findViewById(R.id.adView);
            if (adView != null) {
                AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build();
                adView.loadAd(adRequest);
            }
        }
    }

    @Override
    protected void onPause() {
        if (!mAdsHidden) {
            AdView adView = (AdView)this.findViewById(R.id.adView);
            if (adView != null) {
                adView.pause();
            }
        }

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mAdsHidden) {
            AdView adView = (AdView)this.findViewById(R.id.adView);
            if (adView != null) {
                adView.resume();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (!mAdsHidden) {
            AdView adView = (AdView)this.findViewById(R.id.adView);
            if (adView != null) {
                adView.destroy();
            }
        }

        super.onDestroy();
    }
}
