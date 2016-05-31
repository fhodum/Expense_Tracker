package com.expensetracker.fhodum.expensetracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.expensetracker.fhodum.expensetracker.Constants;

import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created by fhodum on 5/29/16.
 */
public class ExpenseTrackerDatabase {

    private static ExpenseTrackerDatabase sExpenseTrackerDatabase = null;

    private DBHelper database = null;

    private Context context;


    public static ExpenseTrackerDatabase instance(Context ctxt){
        if(sExpenseTrackerDatabase == null){
            sExpenseTrackerDatabase = new ExpenseTrackerDatabase(ctxt);
        }
        return sExpenseTrackerDatabase;
    }

    private ExpenseTrackerDatabase(Context ctxt) {
        database = new DBHelper(ctxt);
        context = ctxt;
    }


    public List<Person> getPeople(){
        List<Person> retVal = new Vector<>();
        Cursor res =  database.getReadableDatabase().rawQuery( "select * from " + ExpenseTrackerContract.PersonTable.TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false) {
            Person newPerson = new Person();
            newPerson.id = res.getLong(res.getColumnIndex(ExpenseTrackerContract.PersonTable._ID));
            newPerson.name = res.getString(res.getColumnIndex(ExpenseTrackerContract.PersonTable.COLUMN_NAME_PERSON_NAME));
            newPerson.amount = res.getDouble(res.getColumnIndex(ExpenseTrackerContract.PersonTable.COLUMN_NAME_PERSON_BUDGET));
            retVal.add(newPerson);
            res.moveToNext();
        }
        res.close();
        return retVal;
    }

    public boolean addPerson(Person person){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ExpenseTrackerContract.PersonTable.COLUMN_NAME_PERSON_NAME, person.name);
        contentValues.put(ExpenseTrackerContract.PersonTable.COLUMN_NAME_PERSON_BUDGET, person.getAmount());
        SQLiteDatabase db = database.getWritableDatabase();
        long id = db.insert(ExpenseTrackerContract.PersonTable.TABLE_NAME,
                null,
                contentValues);
        boolean retVal = false;
        if (id>0){
            retVal = true;
        }
        person.id = id;
        sendUpdateMessage(Constants.ADDED_PERSON);
        return retVal;
    }

    public boolean addPerson(String name){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ExpenseTrackerContract.PersonTable.COLUMN_NAME_PERSON_NAME, name);
        SQLiteDatabase db = database.getWritableDatabase();
        long id = db.insert(ExpenseTrackerContract.PersonTable.TABLE_NAME,
                null,
                contentValues);
        boolean retVal = false;
        if (id>0){
            retVal = true;
        }
        sendUpdateMessage(Constants.ADDED_PERSON);
        return retVal;
    }

    public boolean addExpense(Expense expense){
        boolean retVal = false;
        if(expense.person == null ){
            throw new IllegalArgumentException("Person cannot be null");
        }else if(expense.person.id == -1){
            throw new IllegalArgumentException("Person must have been saved and have an id");
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(ExpenseTrackerContract.ExpenseItemTable.COLUMN_NAME_EXPENSE_ITEM,expense.name);
        contentValues.put(ExpenseTrackerContract.ExpenseItemTable.COLUMN_NAME_EXPENSE_ITEM_AMOUNT,expense.expense);
        contentValues.put(ExpenseTrackerContract.ExpenseItemTable.COLUMN_NAME_EXPENSE_ITEM_DATE,expense.date.getTime());
        contentValues.put(ExpenseTrackerContract.ExpenseItemTable.COLUMN_NAME_EXPENSE_ITEM_RECEIPTE_FILE,expense.fileNameOfReceipt);
        contentValues.put(ExpenseTrackerContract.ExpenseItemTable.COLUMN_NAME_PERSON_REFERNCE,expense.person.id);
        SQLiteDatabase db = database.getWritableDatabase();
        long id = db.insert(ExpenseTrackerContract.ExpenseItemTable.TABLE_NAME,
                null,
                contentValues);
        expense.id = id;

        sendUpdateMessage(Constants.ADDED_EXPENSE);
        return retVal;
    }


    public List<Expense> getExpensesForPerson(Person person, boolean ascending){
        List<Expense> retVal = new Vector<>();
        Cursor res =  database.getReadableDatabase().query(ExpenseTrackerContract.ExpenseItemTable.TABLE_NAME,
                null, ExpenseTrackerContract.ExpenseItemTable.COLUMN_NAME_PERSON_REFERNCE +"=?",
                new String[] {String.valueOf(person.id)},null,null,
                ExpenseTrackerContract.ExpenseItemTable.COLUMN_NAME_EXPENSE_ITEM_DATE + (ascending?" ASC":" DESC"));
        res.moveToFirst();

        while(res.isAfterLast() == false) {
            Expense newExpense = new Expense();
            newExpense.id = res.getLong(res.getColumnIndex(ExpenseTrackerContract.ExpenseItemTable._ID));
            newExpense.name = res.getString(res.getColumnIndex(ExpenseTrackerContract.ExpenseItemTable.COLUMN_NAME_EXPENSE_ITEM));
            newExpense.expense = res.getDouble(res.getColumnIndex(ExpenseTrackerContract.ExpenseItemTable.COLUMN_NAME_EXPENSE_ITEM_AMOUNT));
            newExpense.date = new Date(res.getLong(res.getColumnIndex(ExpenseTrackerContract.ExpenseItemTable.COLUMN_NAME_EXPENSE_ITEM_DATE)));
            newExpense.fileNameOfReceipt = res.getString(res.getColumnIndex(ExpenseTrackerContract.ExpenseItemTable.COLUMN_NAME_EXPENSE_ITEM_RECEIPTE_FILE));
            newExpense.person = person;
            retVal.add(newExpense);
            res.moveToNext();
        }
        res.close();
        return retVal;
    }

    private void sendUpdateMessage(String message) {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent(message);
        // You can also include some extra data.
        intent.putExtra("message", "This is my message!");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }



    private static final String DATABASE_NAME = "expense_tracking";

    private static final String TEXT_TYPE = " TEXT";
    private static final String DECIMAL_TYPE = " REAL";
    private static final String LONG_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_PERSON_TABLE =
            "CREATE TABLE " + ExpenseTrackerContract.PersonTable.TABLE_NAME + " (" +
                    ExpenseTrackerContract.PersonTable._ID + " INTEGER PRIMARY KEY," +
                    ExpenseTrackerContract.PersonTable.COLUMN_NAME_PERSON_NAME + TEXT_TYPE + COMMA_SEP +
                    ExpenseTrackerContract.PersonTable.COLUMN_NAME_PERSON_BUDGET + DECIMAL_TYPE +
            " )";

    private static final String SQL_CREATE_EXPENSE_ITEM_TABLE =
            "CREATE TABLE " + ExpenseTrackerContract.ExpenseItemTable.TABLE_NAME + " (" +
                    ExpenseTrackerContract.ExpenseItemTable._ID + " INTEGER PRIMARY KEY," +
                    ExpenseTrackerContract.ExpenseItemTable.COLUMN_NAME_EXPENSE_ITEM + TEXT_TYPE + COMMA_SEP +
                    ExpenseTrackerContract.ExpenseItemTable.COLUMN_NAME_EXPENSE_ITEM_AMOUNT + DECIMAL_TYPE + COMMA_SEP +
                    ExpenseTrackerContract.ExpenseItemTable.COLUMN_NAME_EXPENSE_ITEM_DATE + LONG_TYPE + COMMA_SEP +
                    ExpenseTrackerContract.ExpenseItemTable.COLUMN_NAME_EXPENSE_ITEM_RECEIPTE_FILE + TEXT_TYPE + COMMA_SEP +
                    ExpenseTrackerContract.ExpenseItemTable.COLUMN_NAME_PERSON_REFERNCE + LONG_TYPE + COMMA_SEP +
                    "FOREIGN KEY(" + ExpenseTrackerContract.ExpenseItemTable.COLUMN_NAME_PERSON_REFERNCE + ") REFERENCES " +
                    ExpenseTrackerContract.PersonTable.TABLE_NAME + "(" + ExpenseTrackerContract.PersonTable._ID +") "  +
                    " )";


    private static final String SQL_DELETE_PERSON_TABLE =
            "DROP TABLE IF EXISTS " + ExpenseTrackerContract.PersonTable.TABLE_NAME;

    private static final String SQL_DELETE_EXPENSE_ITEM_TABLE =
            "DROP TABLE IF EXISTS " + ExpenseTrackerContract.ExpenseItemTable.TABLE_NAME;


    private class DBHelper extends SQLiteOpenHelper {


        public DBHelper(Context context)
        {
            super(context, DATABASE_NAME , null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_PERSON_TABLE);
            db.execSQL(SQL_CREATE_EXPENSE_ITEM_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
