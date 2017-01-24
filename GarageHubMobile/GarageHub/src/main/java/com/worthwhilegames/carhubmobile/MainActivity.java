package com.worthwhilegames.carhubmobile;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mobsandgeeks.adapters.InstantAdapter;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author breber
 */
public class MainActivity extends AppEngineActivity {

    @BindView(R.id.drawer_layout) protected DrawerLayout mDrawerLayout;
    @BindView(R.id.left_drawer) protected ListView mDrawerList;

    private List<UserVehicleRecord> mVehicles;

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        ButterKnife.bind(this);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayUseLogoEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();

        updateUi();
    }

    private void selectItem(int position) {
        if (mVehicles == null || position >= mVehicles.size()) {
            setTitle("GarageHub");
        } else {
            UserVehicleRecord record = mVehicles.get(position);

            // update the main content by replacing fragments
            Fragment fragment = UserVehicleFragment.newInstance(record.getId());

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            setTitle(record.getName());
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    public void updateUi() {
        if (mDrawerList != null) {
            // Get all Vehicles from the database
            mVehicles = UserVehicleRecord.listAll(UserVehicleRecord.class);
            Collections.sort(mVehicles, new Comparator<UserVehicleRecord>() {
                @Override
                public int compare(UserVehicleRecord lhs, UserVehicleRecord rhs) {
                    return lhs.getMake().compareTo(rhs.getMake());
                }
            });

            mDrawerList.setAdapter(new InstantAdapter<UserVehicleRecord>(this, R.layout.simple_list_item, UserVehicleRecord.class, mVehicles));

            selectItem(mDrawerList.getSelectedItemPosition());
        }
    }

    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
}
