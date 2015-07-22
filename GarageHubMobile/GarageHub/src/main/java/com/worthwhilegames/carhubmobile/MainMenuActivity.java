package com.worthwhilegames.carhubmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.worthwhilegames.carhubmobile.adapters.MenuImageAdapter;
import com.worthwhilegames.carhubmobile.adapters.MenuImageAdapter.ImageTextWrapper;

/**
 * @author jamiekujawa
 */
public class MainMenuActivity extends AdActivity {

    private ImageTextWrapper[] mImageTextWrappers = {
            new MenuImageAdapter.ImageTextWrapper(R.raw.fuel, R.string.findNearbyGasPrices, FindGasPricesActivity.class),
            new MenuImageAdapter.ImageTextWrapper(R.raw.vehicle, R.string.userVehicleList, UserVehicleListActivity.class),
            new MenuImageAdapter.ImageTextWrapper(R.raw.info, R.string.about, AboutActivity.class),
    };

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        Util.updateSyncAutomatically(this);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new MenuImageAdapter(this, mImageTextWrappers));

        gridview.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageTextWrapper item = (ImageTextWrapper) parent.getItemAtPosition(position);
                Intent i = new Intent(MainMenuActivity.this, item.mIntent);
                startActivity(i);
            }
        });

        // Temporarily clear preferences if first start up
        // This should fix syncing issues after database upgrade
        SharedPreferences prefs = Util.getSharedPrefs(this);
        try {
            int versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            if (prefs.getInt("PACKAGE_VERSION", 0) != versionCode) {
                SharedPreferences.Editor editor = prefs.edit();

                // Migrate old preferences to new consolidated preferences
                SharedPreferences oldPrefs = getSharedPreferences("CarHubMobile", 0);
                if (oldPrefs.contains(Util.PREF_ACCOUNT_NAME)) {
                    // Add key to new preferences
                    editor.putString(Util.PREF_ACCOUNT_NAME, oldPrefs.getString(Util.PREF_ACCOUNT_NAME, ""));

                    // Clear the old preferences
                    oldPrefs.edit().clear().commit();
                }
                editor.putInt("PACKAGE_VERSION", versionCode);
                editor.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
