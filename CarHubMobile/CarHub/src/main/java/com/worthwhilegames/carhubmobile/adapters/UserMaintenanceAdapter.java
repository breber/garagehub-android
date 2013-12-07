package com.worthwhilegames.carhubmobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.worthwhilegames.carhubmobile.R;
import com.worthwhilegames.carhubmobile.models.UserMaintenanceRecord;

import java.util.List;
import java.util.Date;

/**
 * @author breber
 */
public class UserMaintenanceAdapter extends ArrayAdapter<UserMaintenanceRecord> {

    private int layoutResourceId;

    public UserMaintenanceAdapter(Context context, List<UserMaintenanceRecord> data) {
        super(context, R.layout.maintenancerow, data);
        this.layoutResourceId = R.layout.maintenancerow;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        MaintenanceHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new MaintenanceHolder();
            holder.locationName = (TextView) row.findViewById(R.id.locationName);
            holder.priceLabel = (TextView) row.findViewById(R.id.priceLabel);
            holder.dateLabel = (TextView) row.findViewById(R.id.dateLabel);
            holder.descriptionLabel = (TextView) row.findViewById(R.id.descriptionLabel);
            holder.odometerLabel = (TextView) row.findViewById(R.id.odometerLabel);

            row.setTag(holder);
        } else {
            holder = (MaintenanceHolder) row.getTag();
        }

        UserMaintenanceRecord record = getItem(position);

        holder.locationName.setText(record.getLocation());
        holder.dateLabel.setText(DateFormat.format("MM/dd/yyyy", new Date(record.getDate())));
        holder.descriptionLabel.setText(record.getDescription());
        holder.odometerLabel.setText("Odometer: " + record.getOdometer());
        holder.priceLabel.setText(String.format("$%.2f", record.getAmount()));

        return row;
    }

    static class MaintenanceHolder
    {
        TextView locationName;
        TextView priceLabel;
        TextView dateLabel;
        TextView odometerLabel;
        TextView descriptionLabel;
    }
}
