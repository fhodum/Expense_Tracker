package com.expensetracker.fhodum.expensetracker.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;

import com.expensetracker.fhodum.expensetracker.R;

/**
 * Created by fhodum on 5/29/16.
 */
public class FullScreenDialog extends Dialog {

    public FullScreenDialog(Context context, int contentId) {
        super(context, R.style.AppTheme);
        setContentView(contentId);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT); //Optional
        getWindow().getAttributes().windowAnimations = R.style.AddNewPersonDialogAnimation;

    }
}