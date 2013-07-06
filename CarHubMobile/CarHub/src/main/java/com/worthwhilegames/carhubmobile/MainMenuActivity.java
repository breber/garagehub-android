package com.worthwhilegames.carhubmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import com.worthwhilegames.carhubmobile.adapters.MenuImageAdapter;
import com.worthwhilegames.carhubmobile.adapters.MenuImageAdapter.ImageTextWrapper;

/**
 * @author jamiekujawa
 */
public class MainMenuActivity extends AdActivity {

    private ImageTextWrapper[] mImageTextWrappers = {
            new MenuImageAdapter.ImageTextWrapper(R.drawable.ic_menu_gas, R.string.findNearbyGasPrices, FindGasPricesActivity.class),
            new MenuImageAdapter.ImageTextWrapper(R.drawable.ic_menu_vehicle, R.string.userVehicleList, UserVehicleListActivity.class),
            new MenuImageAdapter.ImageTextWrapper(R.drawable.ic_menu_about, R.string.about, AboutActivity.class),
    };

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new MenuImageAdapter(this, mImageTextWrappers));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageTextWrapper item = (ImageTextWrapper) parent.getItemAtPosition(position);
                Intent i = new Intent(MainMenuActivity.this, item.mIntent);
                startActivity(i);
            }
        });
    }
}
