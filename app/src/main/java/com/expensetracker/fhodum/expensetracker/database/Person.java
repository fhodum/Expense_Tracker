package com.expensetracker.fhodum.expensetracker.database;

/**
 * Created by fhodum on 5/29/16.
 */
public class Person {

    String name;

    long id = -1;

    double amount;

    public long getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
