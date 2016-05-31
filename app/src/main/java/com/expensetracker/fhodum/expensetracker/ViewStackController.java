package com.expensetracker.fhodum.expensetracker;

/**
 * Created by fhodum on 5/30/16.
 */
public interface ViewStackController {

    boolean handleBackButton();

    void pushShowSummaryOnStack();

    void pushShowPersonOnStack();
}
