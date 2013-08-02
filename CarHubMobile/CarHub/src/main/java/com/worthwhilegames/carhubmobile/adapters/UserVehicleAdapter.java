package com.worthwhilegames.carhubmobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.worthwhilegames.carhubmobile.models.UserVehicleRecord;

import java.util.List;

/**
 * @author breber
 */
public class UserVehicleAdapter extends ArrayAdapter<UserVehicleRecord> {

    private int layoutResourceId;

    public UserVehicleAdapter(Context context, int layoutResourceId, List<UserVehicleRecord> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        VehicleHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new VehicleHolder();
            holder.vehicleName = (TextView) row.findViewById(android.R.id.text1);

            row.setTag(holder);
        } else {
            holder = (VehicleHolder) row.getTag();
        }

        UserVehicleRecord vehicle = getItem(position);

        holder.vehicleName.setText(vehicle.getYear() + " " + vehicle.getMake() + " " + vehicle.getModel());

        return row;
    }

    static class VehicleHolder
    {
        TextView vehicleName;
    }
}
