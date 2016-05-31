package com.expensetracker.fhodum.expensetracker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.expensetracker.fhodum.expensetracker.database.Expense;
import com.expensetracker.fhodum.expensetracker.database.ExpenseTrackerDatabase;
import com.expensetracker.fhodum.expensetracker.database.Person;
import com.expensetracker.fhodum.expensetracker.widget.FullScreenDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by fhodum on 5/29/16.
 */
public class EditSaveExpenseController extends ControllerBase implements Toolbar.OnMenuItemClickListener,
        DatePickerDialog.OnDateSetListener{

    private FullScreenDialog dialog;

    private EditText dateEdit;

    private Person person;

    public EditSaveExpenseController(Activity ctxt, Person person){
        super(ctxt);
        this.person = person;
    }

    public void showSaveScreen(){

        dialog= new FullScreenDialog(ctxt, R.layout.add_new_expense);
        Toolbar toolbar = (Toolbar)dialog.findViewById(R.id.add_person_tooolbar);
        toolbar.inflateMenu(R.menu.add_new_person_menu);
        toolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
        toolbar.setOnMenuItemClickListener(this);



        dateEdit = (EditText)dialog.findViewById(R.id.expense_date);
        dateEdit.setOnClickListener(new DateItemClickListener());


        Date dt = new Date(System.currentTimeMillis());
        dateEdit.setText(new SimpleDateFormat(Constants.DATE_FORMAT).format(dt));
        dateEdit.setTag(dt);
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(year,monthOfYear,dayOfMonth);
        Date dt = cal.getTime();
        dateEdit.setText(new SimpleDateFormat(Constants.DATE_FORMAT).format(dt));
        dateEdit.setTag(dt);
    }

    private final class DateItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Calendar cal = Calendar.getInstance();
            DatePickerDialog datePicker = new DatePickerDialog(ctxt, EditSaveExpenseController.this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            datePicker.show();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        EditText text = (EditText)dialog.findViewById(R.id.input_expense_name);
        Expense expense = new Expense();
        expense.setPerson(person);
        String name = text.getText().toString();
        if(name.length() == 0){
            Snackbar.make(dialog.findViewById(R.id.add_new_person_main_view),"Expense name cannot be empty", Snackbar.LENGTH_LONG).show();
        }else{
            expense.setName(name);
        }

        text = (EditText) dialog.findViewById(R.id.input_amount);
        String amount = text.getText().toString();

        if (amount.length() ==0) {
            Snackbar.make(dialog.findViewById(R.id.add_new_expense_main_view), "Amount cannot be empty", Snackbar.LENGTH_LONG).show();
        }else{
            double amountVal = Double.parseDouble(amount);
            expense.setExpense(amountVal);
        }

        if(dateEdit.getTag()!=null){
            Date date = (Date)dateEdit.getTag();
            expense.setDate(date);
        }

        ExpenseTrackerDatabase.instance(ctxt).addExpense(expense);
        dialog.dismiss();
        return true;
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(ctxt.getPackageManager()) != null) {
            ctxt.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}
