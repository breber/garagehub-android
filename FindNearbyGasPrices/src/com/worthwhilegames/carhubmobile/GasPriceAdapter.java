package com.worthwhilegames.carhubmobile;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GasPriceAdapter extends ArrayAdapter<GasPriceRowModel>{

    Context context; 
    int layoutResourceId;
    List<GasPriceRowModel> data = null;
    
    public GasPriceAdapter(Context context, int layoutResourceId, List<GasPriceRowModel> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        GasPriceHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new GasPriceHolder();
            holder.stationAddress = (TextView)row.findViewById(R.id.stationAddress);
            holder.stationDistance = (TextView)row.findViewById(R.id.stationDistance);
            holder.stationName = (TextView)row.findViewById(R.id.stationName);
            holder.stationPrice = (TextView)row.findViewById(R.id.stationPrice);
            holder.lastUpdated = (TextView)row.findViewById(R.id.stationLastUpdated);
            
            
            row.setTag(holder);
        }
        else
        {
            holder = (GasPriceHolder)row.getTag();
        }
        
        GasPriceRowModel station = data.get(position);
        holder.stationAddress.setText(station.getStationAddress().trim());
        holder.stationDistance.setText("Distance: " + station.getStationDistance().trim());
        holder.stationName.setText(station.getStationName().trim() + " - ");
        holder.stationPrice.setText("Price: " + station.getStationPrice().trim());
        holder.lastUpdated.setText("Last Updated: " + station.getLastUpdated().trim());
        
        return row;
    }
    
    static class GasPriceHolder
    {
        TextView stationName;
        TextView stationAddress;
        TextView stationPrice;
        TextView stationDistance;
        TextView lastUpdated;
    }
}