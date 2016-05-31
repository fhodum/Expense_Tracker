package com.expensetracker.fhodum.expensetracker;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expensetracker.fhodum.expensetracker.database.Expense;
import com.expensetracker.fhodum.expensetracker.database.ExpenseTrackerDatabase;
import com.expensetracker.fhodum.expensetracker.database.Person;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created by fhodum on 5/30/16.
 */
public class SummaryRecyclerViewAdapter extends
        RecyclerView.Adapter <RecyclerView.ViewHolder> implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    List<Pair<Person,Double>> items = new Vector<>();

    private Context ctxt;

    private static final int HEADER = 0;
    private static final int ITEM = 1;

    public SummaryRecyclerViewAdapter(Context ctxt, List<Person> people){

        this.ctxt = ctxt;
        items = new Vector<>();
        //These should be ordered by Date so I can just go through them and put
        //them in a new List
        Date date = null;
        for(Person person:people) {

            List<Expense> expenses = ExpenseTrackerDatabase.instance(ctxt).getExpensesForPerson(person, true);
            double totalSpent = 0.0;
            for (Expense expense : expenses) {
                totalSpent += expense.getExpense();
            }
            items.add(new Pair<>(person,Double.valueOf(totalSpent)));
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctxt);

                return new RecyclerItemViewHolder(inflater.inflate(R.layout.summary_single_line, parent, false));


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        RecyclerItemViewHolder itemHolder = (RecyclerItemViewHolder)holder;
        Pair<Person,Double> item = items.get(position);
        ((RecyclerItemViewHolder) holder).summaryCategoryName.setText(item.first.getName());
        NumberFormat format = NumberFormat.getCurrencyInstance();
        double remaining = item.first.getAmount()-item.second;
        ((RecyclerItemViewHolder) holder).summaryRemaining.setText("Remaining: " +format.format(remaining));
        if(remaining<50){
            ((RecyclerItemViewHolder) holder).summaryRemaining.setTextColor(ctxt.getResources().getColor(R.color.red));
        }
        ((RecyclerItemViewHolder) holder).summaryBudget.setText("Budget: " +format.format(item.first.getAmount()));
        ((RecyclerItemViewHolder) holder).summarySpent.setText("Spent: " +format.format(item.second));

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
    public int getItemCount() {
        return items.size();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    public final static class RecyclerItemViewHolder extends RecyclerView.ViewHolder{
        TextView summaryCategoryName;
        TextView summaryBudget;
        TextView summaryRemaining;
        TextView summarySpent;


        public RecyclerItemViewHolder(View itemView) {
            super(itemView);
            summaryCategoryName = (TextView)((ViewGroup) itemView).findViewById(R.id.summary_category_name);
            summaryBudget = (TextView)((ViewGroup) itemView).findViewById(R.id.summary_budget);
            summaryRemaining = (TextView)((ViewGroup) itemView).findViewById(R.id.summary_remaining);
            summarySpent = (TextView)((ViewGroup) itemView).findViewById(R.id.summary_spent);
        }


    }

}
