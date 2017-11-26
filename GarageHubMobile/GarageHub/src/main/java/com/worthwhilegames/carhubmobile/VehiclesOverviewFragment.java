package com.worthwhilegames.carhubmobile;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class VehiclesOverviewFragment extends Fragment {
    public VehiclesOverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_list_view, container, false);

        // Get all Vehicles from the database
        List<UserVehicleRecord> vehicles = UserVehicleRecord.listAll(UserVehicleRecord.class);
        Collections.sort(vehicles, new Comparator<UserVehicleRecord>() {
            @Override
            public int compare(UserVehicleRecord lhs, UserVehicleRecord rhs) {
                return lhs.getMake().compareTo(rhs.getMake());
            }
        });

        TextView tv = (TextView) rootView.findViewById(android.R.id.empty);
        if (vehicles.isEmpty()) {
            tv.setText(R.string.noVehiclesRegistered);
        } else {
            tv.setVisibility(View.INVISIBLE);

            ListView listView = (ListView) rootView.findViewById(android.R.id.list);
            listView.setAdapter(new ArrayAdapter<UserVehicleRecord>(getActivity(), android.R.layout.simple_list_item_1, vehicles));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    MainActivity activity = (MainActivity) getActivity();
                    UserVehicleRecord record = (UserVehicleRecord) a.getItemAtPosition(position);
                    activity.selectItem(record);
                }
            });
        }

        return rootView;
    }
}
