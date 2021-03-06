package com.expensetracker.fhodum.expensetracker;

import android.app.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expensetracker.fhodum.expensetracker.database.Expense;
import com.expensetracker.fhodum.expensetracker.database.ExpenseTrackerDatabase;
import com.expensetracker.fhodum.expensetracker.database.Person;

import java.text.NumberFormat;
import java.util.List;


/**
 * Created by fhodum on 5/30/16.
 */
public class PersonViewController extends ControllerBase {

    private Person person;

    private RecyclerView recyclerView;

    private ExpenseRecyclerViewAdapter adapter;

    private List<Expense> expenses;

    ViewGroup parentView;

    ViewGroup myMainView;

    public PersonViewController(Activity ctxt, Person person){
        super(ctxt);
        this.person = person;



        LocalBroadcastManager.getInstance(ctxt).registerReceiver(mMessageReceiver,
                new IntentFilter(Constants.ADDED_EXPENSE));

        LocalBroadcastManager.getInstance(ctxt).registerReceiver(mMessageReceiver,
                new IntentFilter(Constants.EDITED_EXPENSE));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            parentView.removeView(myMainView);
            showPerson(parentView);
            Log.d("receiver", "Got message");
        }
    };

    private void setupTopRow(ViewGroup vg){
        TextView tv = (TextView)vg.findViewById(R.id.budget_view);
        NumberFormat format = NumberFormat.getCurrencyInstance();
        tv.setText("Budget: " + format.format(person.getAmount()));
        double totalSpent = 0.0;
        for(Expense expense:expenses){
            totalSpent += expense.getExpense();
        }
        tv =  (TextView)vg.findViewById(R.id.spent_view);
        tv.setText("Spent: "+ format.format(totalSpent));

        double remaining = person.getAmount() - totalSpent;

        tv = (TextView)vg.findViewById(R.id.remaining_view);
        if(remaining < 50){
            tv.setTextColor(ctxt.getResources().getColor(R.color.red));
        }
        tv.setText("Remianing: " + format.format(remaining));

    }

    public void showPerson(ViewGroup parentView){
        this.parentView = parentView;
        myMainView = (ViewGroup)LayoutInflater.from(ctxt).inflate(R.layout.expenses_recycler_layout,parentView,false);
        expenses = ExpenseTrackerDatabase.instance(ctxt).getExpensesForPerson(person, false);
        adapter = new ExpenseRecyclerViewAdapter(ctxt,expenses);
        setupTopRow(myMainView);

        recyclerView = (RecyclerView)myMainView.findViewById(R.id.expense_list);

        FloatingActionButton fab =  (FloatingActionButton)ctxt.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EditSaveExpenseController(ctxt, person).showSaveScreen();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(ctxt);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(ctxt));
        parentView.addView(myMainView);

    }

    public class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private final int[] ATTRS = new int[]{android.R.attr.listDivider};

        private Drawable mDivider;

        /**
         * Default divider will be used
         */
        public DividerItemDecoration(Context context) {
            final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
            mDivider = styledAttributes.getDrawable(0);
            styledAttributes.recycle();
        }

        /**
         * Custom divider will be used
         */
        public DividerItemDecoration(Context context, int resId) {
            mDivider = ContextCompat.getDrawable(context, resId);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}
