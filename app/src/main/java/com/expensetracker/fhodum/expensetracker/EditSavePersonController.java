package com.expensetracker.fhodum.expensetracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.renderscript.Double2;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.expensetracker.fhodum.expensetracker.R;
import com.expensetracker.fhodum.expensetracker.database.ExpenseTrackerDatabase;
import com.expensetracker.fhodum.expensetracker.database.Person;
import com.expensetracker.fhodum.expensetracker.widget.FullScreenDialog;

/**
 * Created by fhodum on 5/29/16.
 */
public class EditSavePersonController extends ControllerBase implements Toolbar.OnMenuItemClickListener{

    private FullScreenDialog dialog;

    public EditSavePersonController(Activity ctxt){
        super(ctxt);


    }

    public void showSaveScreen(){

        dialog= new FullScreenDialog(ctxt, R.layout.add_new_person);
        Toolbar toolbar = (Toolbar)dialog.findViewById(R.id.add_person_tooolbar);
        toolbar.inflateMenu(R.menu.add_new_person_menu);
        toolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
        toolbar.setOnMenuItemClickListener(this);
        dialog.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        EditText text = (EditText)dialog.findViewById(R.id.input_name);
        Person person = new Person();
        String name = text.getText().toString();
        if(name.length() == 0){
            Snackbar.make(dialog.findViewById(R.id.add_new_person_main_view),"Name cannot be empty", Snackbar.LENGTH_LONG).show();
        }
        else {
            person.setName(name);

            text = (EditText) dialog.findViewById(R.id.input_budget);
            String budget = text.getText().toString();
            if (name.length() == 0) {
                Snackbar.make(dialog.findViewById(R.id.add_new_person_main_view), "Budget cannot be empty", Snackbar.LENGTH_LONG).show();
            }else {
                double budgetVal = Double.parseDouble(budget);
                person.setAmount(budgetVal);
                ExpenseTrackerDatabase.instance(ctxt).addPerson(person);
                dialog.dismiss();
            }
        }
        return true;
    }
}
