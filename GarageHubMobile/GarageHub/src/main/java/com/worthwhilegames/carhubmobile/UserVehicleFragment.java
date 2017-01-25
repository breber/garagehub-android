package com.worthwhilegames.carhubmobile;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserVehicleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserVehicleFragment extends Fragment {
    private static final String ARG_VEHICLE_ID = "vehicleId";
    private UserVehicleRecord mVehicle;

    public UserVehicleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param vehicleId Vehicle ID
     * @return A new instance of fragment UserVehicleFragment.
     */
    public static UserVehicleFragment newInstance(Long vehicleId) {
        UserVehicleFragment fragment = new UserVehicleFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_vehicle, container, false);

        if (mVehicle != null) {
            TextView currentField = (TextView) rootView.findViewById(R.id.vehicleColor);
            String color = mVehicle.getColor();
            if (color != null && !"".equals(color)) {
                currentField.setText(color);
            } else {
                currentField.setText("N/A");
            }

            currentField = (TextView) rootView.findViewById(R.id.vehiclePlates);
            String plates = mVehicle.getPlates();
            if (plates != null && !"".equals(plates)) {
                currentField.setText(plates);
            } else {
                currentField.setText("N/A");
            }

            currentField = (TextView) rootView.findViewById(R.id.currentOdometer);
            int odometer = mVehicle.getLatestOdometer();
            if (odometer >= 0) {
                currentField.setText(odometer + "");
            } else {
                currentField.setText("Unknown");
            }

            currentField = (TextView) rootView.findViewById(R.id.totalCost);
            float totalCost = mVehicle.getTotalCost();
            if (totalCost >= 0) {
                currentField.setText(String.format("$%.02f", totalCost));
            } else {
                currentField.setText("Unknown");
            }
        }

        return rootView;
    }
}
