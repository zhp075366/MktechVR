package com.gotech.vrplayer.module.personal.update;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gotech.vrplayer.R;
import com.gotech.vrplayer.widget.CustomDialog;

public class DialogCreater {

    public static Dialog showDownloadDialog(Context context, int style, View view) {
        DialogWrapper dialog = new DialogWrapper(context, style, view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.alpha = 0.9f;
        window.setAttributes(layoutParams);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static CustomDialog showWaitingDialog(Context context, String tip) {
        View view = View.inflate(context, R.layout.dialog_waitting, null);
        if (!TextUtils.isEmpty(tip)) {
            ((TextView)view.findViewById(R.id.tvTip)).setText(tip);
        } else {
            view.findViewById(R.id.tvTip).setVisibility(View.GONE);
        }
        CustomDialog dialogWaiting = new CustomDialog(context, view, R.style.MyDialog);
        dialogWaiting.show();
        dialogWaiting.setCancelable(false);
        return dialogWaiting;
    }
}