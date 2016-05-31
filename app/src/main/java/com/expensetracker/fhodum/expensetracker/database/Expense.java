package com.expensetracker.fhodum.expensetracker.database;

import java.util.Date;

/**
 * Created by fhodum on 5/30/16.
 */
public class Expense {


    long id;
    String name;
    double expense;
    Date date;
    String fileNameOfReceipt;
    Person person;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFileNameOfReceipt() {
        return fileNameOfReceipt;
    }

    public void setFileNameOfReceipt(String fileNameOfReceipt) {
        this.fileNameOfReceipt = fileNameOfReceipt;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person){
        this.person = person;
    }
}
