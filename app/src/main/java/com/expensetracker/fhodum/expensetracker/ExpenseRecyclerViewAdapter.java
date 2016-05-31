package com.expensetracker.fhodum.expensetracker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expensetracker.fhodum.expensetracker.database.Expense;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created by fhodum on 5/30/16.
 */
public class ExpenseRecyclerViewAdapter extends
        RecyclerView.Adapter <RecyclerView.ViewHolder> implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    List<Object> items;

    private Context ctxt;

    private static final int HEADER = 0;
    private static final int ITEM = 1;

    public ExpenseRecyclerViewAdapter(Context ctxt, List<Expense> expenses){

        this.ctxt = ctxt;
        items = new Vector<>();
        //These should be ordered by Date so I can just go through them and put
        //them in a new List
        Date date = null;
        for(Expense expense: expenses){
            if(date == null || compareDatePortionOnly(date, expense.getDate()) !=0){
                date = expense.getDate();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
                items.add(simpleDateFormat.format(date));
                items.add(expense);
            }else {
                items.add(expense);
            }
        }
    }

    private int compareDatePortionOnly(Date d1, Date d2){

        if (d1.getYear() != d2.getYear())
            return d1.getYear() - d2.getYear();
        if (d1.getMonth() != d2.getMonth())
            return d1.getMonth() - d2.getMonth();
        return d1.getDate() - d2.getDate();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctxt);
        switch (viewType) {
            case 0:
                return new RecyclerHeaderViewHolder(inflater.inflate(R.layout.date_item_line, parent, false));
            case 1:
                return new RecyclerItemViewHolder(inflater.inflate(R.layout.expense_item_line, parent, false));
            default: return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof RecyclerItemViewHolder && items.get(position) instanceof Expense){

            RecyclerItemViewHolder itemHolder = (RecyclerItemViewHolder)holder;
            Expense expense = (Expense)items.get(position);
            ((RecyclerItemViewHolder) holder).expenseItemName.setText(expense.getName());
            NumberFormat format = NumberFormat.getCurrencyInstance();
            ((RecyclerItemViewHolder) holder).expenseItemAmount.setText(format.format(expense.getExpense()));
            itemHolder.menu.setOnClickListener(this);

        }else if(holder instanceof RecyclerHeaderViewHolder && items.get(position) instanceof String){

            RecyclerHeaderViewHolder itemHolder = (RecyclerHeaderViewHolder)holder;
            itemHolder.dateView.setText((String)items.get(position));

        }
    }

    public void onClick(View v) {
        if (true) {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.inflate(R.menu.activity_main_drawer);
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        int viewType;
        if (items.get(position) instanceof String){
            viewType = HEADER;
        }else{
            viewType = ITEM;
        }
        return viewType;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    public final static class RecyclerItemViewHolder extends RecyclerView.ViewHolder{
        TextView expenseItemName;
        TextView expenseItemAmount;
        View menu;

        public RecyclerItemViewHolder(View itemView) {
            super(itemView);
            expenseItemName = (TextView)((ViewGroup) itemView).findViewById(R.id.expense_item_name);
            expenseItemAmount = (TextView)((ViewGroup) itemView).findViewById(R.id.expense_item_amount);
            menu = ((ViewGroup)itemView).findViewById(R.id.expense_item_line_menu);
        }


    }

    public final static class RecyclerHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView dateView;

        public RecyclerHeaderViewHolder(View itemView) {
            super(itemView);
            dateView = (TextView) ((ViewGroup) itemView).findViewById(R.id.dateView);

        }


    }


}
