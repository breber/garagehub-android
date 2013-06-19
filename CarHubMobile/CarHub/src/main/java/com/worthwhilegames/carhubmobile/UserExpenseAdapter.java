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

import com.worthwhilegames.carhubmobile.models.UserBaseExpenseRecord;

/**
 * @author breber
 */
public class UserExpenseAdapter extends ArrayAdapter<UserBaseExpenseRecord> {

	private int layoutResourceId;

	public UserExpenseAdapter(Context context, int layoutResourceId, List<UserBaseExpenseRecord> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
	}

	@Override
	public View getView(int position, View row, ViewGroup parent) {
		ExpenseHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new ExpenseHolder();
			holder.topLeft = (TextView) row.findViewById(R.id.topRowLeft);
			holder.topRight = (TextView) row.findViewById(R.id.topRowRight);
			holder.bottomLeft = (TextView) row.findViewById(R.id.bottomRowLeft);
			holder.bottomRight = (TextView) row.findViewById(R.id.bottomRowRight);

			row.setTag(holder);
		} else {
			holder = (ExpenseHolder) row.getTag();
		}

		UserBaseExpenseRecord expense = getItem(position);

		holder.topLeft.setText(expense.getLocation());
		holder.topRight.setText(expense.getDescription());
		holder.bottomLeft.setText(DateFormat.format("MM/dd/yyyy", new Date(expense.getDate() * 1000)));
		holder.bottomRight.setText(String.format("$%.2f", expense.getAmount()));

		return row;
	}

	static class ExpenseHolder
	{
		TextView topLeft;
		TextView topRight;
		TextView bottomLeft;
		TextView bottomRight;
	}
}