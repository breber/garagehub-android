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

import java.util.Date;
import java.util.List;

/**
 * @author breber
 */
public class UserExpenseAdapter extends ArrayAdapter<UserBaseExpenseRecord> {

    private int layoutResourceId;

    public UserExpenseAdapter(Context context, List<UserBaseExpenseRecord> data) {
        super(context, R.layout.fouritemrow, data);
        this.layoutResourceId = R.layout.fouritemrow;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        ExpenseHolder holder;

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
        holder.bottomLeft.setText(DateFormat.format("MM/dd/yyyy", new Date(expense.getDate())));
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
