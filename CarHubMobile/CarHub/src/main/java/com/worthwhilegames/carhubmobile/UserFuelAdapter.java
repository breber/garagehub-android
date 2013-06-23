package com.worthwhilegames.carhubmobile;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.worthwhilegames.carhubmobile.models.UserFuelRecord;

import java.util.Date;
import java.util.List;

/**
 * @author breber
 */
public class UserFuelAdapter extends ArrayAdapter<UserFuelRecord> {

	private int layoutResourceId;

	public UserFuelAdapter(Context context, int layoutResourceId, List<UserFuelRecord> data) {
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

		UserFuelRecord fuelRecord = getItem(position);

		holder.bottomLeft.setText(DateFormat.format("MM/dd/yyyy", new Date(fuelRecord.getDate())));
		holder.topLeft.setText(fuelRecord.getLocation());
		if (fuelRecord.getOdometerStart() != -1) {
			holder.topRight.setText(fuelRecord.getOdometerStart() + " - " + fuelRecord.getOdometerEnd());
		} else {
			holder.topRight.setText(fuelRecord.getOdometerEnd() + "");
		}
		holder.bottomRight.setText(String.format("$%.2f", fuelRecord.getCostPerGallon()));

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
