package com.amany.chat.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;

import com.amany.chat.R;

public class LoadingDialog {
    private Activity  activity;
     private  AlertDialog dialog;
    public LoadingDialog(Activity myActivity){
        activity=myActivity;
    }

    public void startLoadingDialog(){
        AlertDialog.Builder builder= new AlertDialog.Builder(activity);

        LayoutInflater inflater=activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.full_screen,null));
        builder.setCancelable(false);
        dialog= builder.create();
        dialog.show();

    }
     public void dismissDialog(){

        dialog.dismiss();

    }
}
