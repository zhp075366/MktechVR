package com.gotech.vrplayer.module.personal.update;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class DialogWrapper extends Dialog {

    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView);
    }

    public DialogWrapper(Context context, int theme, View view) {
        super(context, theme);
        mView = view;
    }
}