package com.worthwhilegames.carhubmobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.worthwhilegames.carhubmobile.R;
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
            holder.vehicleName = (TextView) row.findViewById(R.id.vehicleName);
            holder.vehicleColor = (TextView) row.findViewById(R.id.vehicleColor);
            holder.vehicleLicensePlates = (TextView) row.findViewById(R.id.vehicleLicensePlates);
            holder.vehicleYear = (TextView) row.findViewById(R.id.vehicleModelYear);

            row.setTag(holder);
        } else {
            holder = (VehicleHolder) row.getTag();
        }

        UserVehicleRecord vehicle = getItem(position);

        holder.vehicleName.setText(vehicle.getMake().trim() + " " + vehicle.getModel().trim());
        String color = vehicle.getColor();
        if (color != null && !"".equals(color.trim())) {
            holder.vehicleColor.setText(color.trim());
        } else {
            holder.vehicleColor.setText("N/A");
        }
        String plates = vehicle.getPlates();
        if (plates != null && !"".equals(plates.trim())) {
            holder.vehicleLicensePlates.setText(plates.trim());
        } else {
            holder.vehicleLicensePlates.setText("N/A");
        }
        holder.vehicleYear.setText(vehicle.getYear().trim());

        return row;
    }

    static class VehicleHolder
    {
        TextView vehicleName;
        TextView vehicleColor;
        TextView vehicleLicensePlates;
        TextView vehicleYear;
    }
}
