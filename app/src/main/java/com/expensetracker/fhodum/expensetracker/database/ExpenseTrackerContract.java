package com.expensetracker.fhodum.expensetracker.database;

import android.provider.BaseColumns;

/**
 * Created by fhodum on 5/29/16.
 */
public final class ExpenseTrackerContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public ExpenseTrackerContract() {}

    /* Inner class that defines the table contents */
    public static abstract class PersonTable implements BaseColumns {
        public static final String TABLE_NAME = "person";
        public static final String COLUMN_NAME_PERSON_NAME = "name";
        public static final String COLUMN_NAME_PERSON_BUDGET = "budget";
    }


    public static abstract class ExpenseItemTable implements BaseColumns {
        public static final String TABLE_NAME = "espenseitem";
        public static final String COLUMN_NAME_EXPENSE_ITEM_ID = "expenseitemid";
        public static final String COLUMN_NAME_EXPENSE_ITEM = "item";
        public static final String COLUMN_NAME_EXPENSE_ITEM_AMOUNT = "amount";
        public static final String COLUMN_NAME_EXPENSE_ITEM_DATE = "date";
        public static final String COLUMN_NAME_EXPENSE_ITEM_RECEIPTE_FILE = "receiptfile";
        public static final String COLUMN_NAME_PERSON_REFERNCE = "person_reference";
    }

}
