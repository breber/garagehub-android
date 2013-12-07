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
import com.worthwhilegames.carhubmobile.models.UserBaseExpenseRecord;

import java.util.List;
import java.util.Date;

/**
 * @author breber
 */
public class UserExpenseAdapter extends ArrayAdapter<UserBaseExpenseRecord> {

    private int layoutResourceId;

    public UserExpenseAdapter(Context context, List<UserBaseExpenseRecord> data) {
        super(context, R.layout.expenserow, data);
        this.layoutResourceId = R.layout.expenserow;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        ExpenseHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ExpenseHolder();
            holder.locationName = (TextView) row.findViewById(R.id.locationName);
            holder.priceLabel = (TextView) row.findViewById(R.id.priceLabel);
            holder.dateLabel = (TextView) row.findViewById(R.id.dateLabel);
            holder.descriptionLabel = (TextView) row.findViewById(R.id.descriptionLabel);

            row.setTag(holder);
        } else {
            holder = (ExpenseHolder) row.getTag();
        }

        UserBaseExpenseRecord expense = getItem(position);

        holder.locationName.setText(expense.getLocation());
        holder.priceLabel.setText(String.format("$%.2f", expense.getAmount()));
        holder.dateLabel.setText(DateFormat.format("MM/dd/yyyy", new Date(expense.getDate())));
        holder.descriptionLabel.setText(expense.getDescription());

        return row;
    }

    static class ExpenseHolder
    {
        TextView locationName;
        TextView priceLabel;
        TextView dateLabel;
        TextView descriptionLabel;
    }
}
