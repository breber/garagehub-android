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
import com.worthwhilegames.carhubmobile.models.UserFuelRecord;

import java.util.Date;
import java.util.List;

/**
 * @author breber
 */
public class UserFuelAdapter extends ArrayAdapter<UserFuelRecord> {

    private int layoutResourceId;

    public UserFuelAdapter(Context context, List<UserFuelRecord> data) {
        super(context, R.layout.fuelrow, data);
        this.layoutResourceId = R.layout.fuelrow;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        FuelHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new FuelHolder();
            holder.locationName = (TextView) row.findViewById(R.id.locationName);
            holder.priceLabel = (TextView) row.findViewById(R.id.priceLabel);
            holder.dateLabel = (TextView) row.findViewById(R.id.dateLabel);
            holder.odometer = (TextView) row.findViewById(R.id.odometerLabel);

            row.setTag(holder);
        } else {
            holder = (FuelHolder) row.getTag();
        }

        UserFuelRecord fuelRecord = getItem(position);

        holder.locationName.setText(fuelRecord.getLocation());
        holder.priceLabel.setText(String.format("$%.2f", fuelRecord.getCostPerGallon()));
        holder.dateLabel.setText(DateFormat.format("MM/dd/yyyy", new Date(fuelRecord.getDate())));
        if ((fuelRecord.getOdometerStart() != -1) &&
                (fuelRecord.getOdometerStart() != fuelRecord.getOdometerEnd())) {
            holder.odometer.setText("Odometer: " + fuelRecord.getOdometerStart() + " - " + fuelRecord.getOdometerEnd());
        } else {
            holder.odometer.setText("Odometer: " + fuelRecord.getOdometerEnd());
        }

        return row;
    }

    static class FuelHolder
    {
        TextView locationName;
        TextView priceLabel;
        TextView dateLabel;
        TextView odometer;
    }
}
