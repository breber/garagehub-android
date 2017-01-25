package com.worthwhilegames.carhubmobile;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mobsandgeeks.adapters.InstantAdapter;
import com.worthwhilegames.carhubmobile.models.UserFuelRecord;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFuelListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFuelListFragment extends Fragment {
    private static final String ARG_VEHICLE_ID = "vehicleId";
    private UserVehicleRecord mVehicle;

    public UserFuelListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param vehicleId Vehicle ID
     * @return A new instance of fragment UserVehicleFragment.
     */
    public static UserFuelListFragment newInstance(Long vehicleId) {
        UserFuelListFragment fragment = new UserFuelListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_VEHICLE_ID, vehicleId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVehicle = UserVehicleRecord.findById(UserVehicleRecord.class, getArguments().getLong(ARG_VEHICLE_ID));
        }
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_fuel_list, menu);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // This will cover the Android menu button press
        switch (item.getItemId()) {
            case R.id.menu_add_fuel:
                Intent i = new Intent(getActivity(), AddUserFuelRecordActivity.class);
                i.putExtra(Constants.INTENT_DATA_VEHICLE, mVehicle.getId());
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_list_view, container, false);

        if (mVehicle != null) {
            setHasOptionsMenu(true);

            List<UserFuelRecord> fuelRecords = UserFuelRecord.getRecordsForVehicle(UserFuelRecord.class, mVehicle);
            Collections.sort(fuelRecords, new Comparator<UserFuelRecord>() {
                @Override
                public int compare(UserFuelRecord lhs, UserFuelRecord rhs) {
                    return rhs.getOdometerEnd() - lhs.getOdometerEnd();
                }
            });

            TextView tv = (TextView) rootView.findViewById(android.R.id.empty);
            if (fuelRecords.isEmpty()) {
                tv.setText(R.string.noFuelRecords);
            } else {
                tv.setVisibility(View.INVISIBLE);

                ListView listView = (ListView) rootView.findViewById(android.R.id.list);
                listView.setAdapter(new InstantAdapter<>(getActivity(), R.layout.fuelrow, UserFuelRecord.class, fuelRecords));

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                        UserFuelRecord model = (UserFuelRecord) a.getItemAtPosition(position);

                        Intent i = new Intent(getActivity(), AddUserFuelRecordActivity.class);
                        i.putExtra(Constants.INTENT_DATA_VEHICLE, mVehicle.getId());
                        i.putExtra(Constants.INTENT_DATA_RECORD, model.getId());
                        startActivity(i);
                    }
                });
            }
        }

        return rootView;
    }
}
