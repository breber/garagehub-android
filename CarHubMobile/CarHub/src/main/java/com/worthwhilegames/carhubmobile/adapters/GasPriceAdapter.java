package com.worthwhilegames.carhubmobile.adapters;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.worthwhilegames.carhubmobile.R;
import com.worthwhilegames.carhubmobile.Util;
import com.worthwhilegames.carhubmobile.models.GasPriceRecord;

public class GasPriceAdapter extends ArrayAdapter<GasPriceRecord>{

    private Context context;
    private int layoutResourceId;
    private List<GasPriceRecord> data = null;

    public GasPriceAdapter(Context context, int layoutResourceId, List<GasPriceRecord> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        GasPriceHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new GasPriceHolder();
            holder.stationAddress = (TextView) row.findViewById(R.id.gasStationAddress);
            holder.stationDistance = (TextView) row.findViewById(R.id.gasStationDistance);
            holder.stationName = (TextView) row.findViewById(R.id.gasStationName);
            holder.stationPrice = (TextView) row.findViewById(R.id.gasStationPrice);
            holder.lastUpdated = (TextView) row.findViewById(R.id.gasStationLastUpdated);
            holder.stationCity = (TextView) row.findViewById(R.id.gasStationAddressCity);
            holder.stationRegion = (TextView) row.findViewById(R.id.gasStationAddressRegion);
            holder.stationPriceGrade = (TextView) row.findViewById(R.id.gasStationPriceGrade);

            row.setTag(holder);
        } else {
            holder = (GasPriceHolder) row.getTag();
        }

        GasPriceRecord station = data.get(position);

        holder.stationAddress.setText(station.getAddress().trim() + " ");
        holder.stationDistance.setText(station.getDistance().trim() + " away - ");
        holder.stationName.setText(station.getStation().trim());
        holder.stationPrice.setText(station.getPrice().trim());
        holder.lastUpdated.setText("Updated " + station.getLastUpdated().trim());
        holder.stationCity.setText(station.getCity() + ", ");
        holder.stationRegion.setText(station.getRegion());

        SharedPreferences sharedPref = Util.getSharedPrefs(context);
        String grade = sharedPref.getString("Fuel Type", "Mid");

        if ("reg".equalsIgnoreCase(grade)) {
            holder.stationPriceGrade.setText("Fuel Grade: Regular");
        } else if ("mid".equalsIgnoreCase(grade)) {
            holder.stationPriceGrade.setText("Fuel Grade: Mid");
        } else if ("pre".equalsIgnoreCase(grade)) {
            holder.stationPriceGrade.setText("Fuel Grade: Premium");
        } else if ("diesel".equalsIgnoreCase(grade)) {
            holder.stationPriceGrade.setText("Fuel Grade: Diesel");
        } else {
            holder.stationPriceGrade.setText("");
        }

        return row;
    }

    static class GasPriceHolder
    {
        TextView stationName;
        TextView stationAddress;
        TextView stationPrice;
        TextView stationDistance;
        TextView lastUpdated;
        TextView stationCity;
        TextView stationRegion;
        TextView stationPriceGrade;
    }
}
