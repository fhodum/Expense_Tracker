package com.expensetracker.fhodum.expensetracker;


import android.app.Activity;
import android.content.Context;

/**
 * Created by fhodum on 5/30/16.
 */
public class ControllerBase  {
    protected Activity ctxt;

    public ControllerBase(Activity context){
        this.ctxt = context;
    }

}
