package com.worthwhilegames.carhubmobile;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.worthwhilegames.carhubmobile.models.UserMaintenanceRecord;

/**
 * @author breber
 */
public class UserMaintenanceAdapter extends ArrayAdapter<UserMaintenanceRecord> {

	private int layoutResourceId;

	public UserMaintenanceAdapter(Context context, int layoutResourceId, List<UserMaintenanceRecord> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
	}

	@Override
	public View getView(int position, View row, ViewGroup parent) {
		FuelHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new FuelHolder();
			holder.topLeft = (TextView) row.findViewById(R.id.topRowLeft);
			holder.topRight = (TextView) row.findViewById(R.id.topRowRight);
			holder.bottomLeft = (TextView) row.findViewById(R.id.bottomRowLeft);
			holder.bottomRight = (TextView) row.findViewById(R.id.bottomRowRight);

			row.setTag(holder);
		} else {
			holder = (FuelHolder) row.getTag();
		}

		UserMaintenanceRecord record = getItem(position);

		holder.bottomLeft.setText(DateFormat.format("MM/dd/yyyy", new Date(record.getDate() * 1000)));
		holder.topLeft.setText(record.getLocation());
		holder.topRight.setText(record.getOdometer() + "");
		holder.bottomRight.setText(record.getDescription());

		return row;
	}

	static class FuelHolder
	{
		TextView topLeft;
		TextView topRight;
		TextView bottomLeft;
		TextView bottomRight;
	}
}