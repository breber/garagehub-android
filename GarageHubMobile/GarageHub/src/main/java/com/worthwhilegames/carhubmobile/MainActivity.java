package com.worthwhilegames.carhubmobile;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;

import java.util.ArrayList;
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
    private ActionBarDrawerToggle mDrawerToggle;
    private int mSelectedItem = 0;
    private List<ListItem> mVehicles;

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
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)  {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
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
            ListItem record = mVehicles.get(position);

            if (record.fragment != null) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, record.fragment).commit();

                // update selected item and title, then close the drawer
                if (record.vehicle.getName().equals(record.name.trim())) {
                    setTitle(record.name.trim());
                } else {
                    setTitle(record.vehicle.getName() + " " + record.name.trim());
                }
                mDrawerLayout.closeDrawer(mDrawerList);

                mSelectedItem = position;
            } else {
                // Assume that if the fragment is non-null we want to add a vehicle
                Intent i = new Intent(this, AddVehicleActivity.class);
                startActivity(i);
            }
        }
    }

    class ListItem
    {
        UserVehicleRecord vehicle;
        Fragment fragment;
        String name;

        public String toString() {
            return name;
        }
    }

    public void updateUi() {
        if (mDrawerList != null && mCreds.getSelectedAccountName() != null) {
            // Get all Vehicles from the database
            List<UserVehicleRecord> vehicles = UserVehicleRecord.listAll(UserVehicleRecord.class);
            Collections.sort(vehicles, new Comparator<UserVehicleRecord>() {
                @Override
                public int compare(UserVehicleRecord lhs, UserVehicleRecord rhs) {
                    return lhs.getMake().compareTo(rhs.getMake());
                }
            });

            mVehicles = new ArrayList<ListItem>(vehicles.size() * 4);

            for (UserVehicleRecord v : vehicles) {
                ListItem infoItem = new ListItem();
                infoItem.vehicle = v;
                infoItem.fragment = UserVehicleFragment.newInstance(v.getId());
                infoItem.name = v.getName();
                mVehicles.add(infoItem);

                ListItem fuelItem = new ListItem();
                fuelItem.vehicle = v;
                fuelItem.fragment = UserFuelListFragment.newInstance(v.getId());
                fuelItem.name = "  Fuel";
                mVehicles.add(fuelItem);

                ListItem maintItem = new ListItem();
                maintItem.vehicle = v;
                maintItem.fragment = UserMaintenanceListFragment.newInstance(v.getId());
                maintItem.name = "  Maintenance";
                mVehicles.add(maintItem);

                ListItem expenseItem = new ListItem();
                expenseItem.vehicle = v;
                expenseItem.fragment = UserExpenseManagerFragment.newInstance(v.getId());
                expenseItem.name = "  Expenses";
                mVehicles.add(expenseItem);
            }

            ListItem addVehicleItem = new ListItem();
            addVehicleItem.name = getResources().getString(R.string.addVehicle);
            mVehicles.add(addVehicleItem);

            mDrawerList.setAdapter(new ArrayAdapter<ListItem>(this, android.R.layout.simple_list_item_1, mVehicles));

            if (mDrawerList.getSelectedItemPosition() == -1) {
                selectItem(mSelectedItem);
            } else {
                selectItem(mDrawerList.getSelectedItemPosition());
            }
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
