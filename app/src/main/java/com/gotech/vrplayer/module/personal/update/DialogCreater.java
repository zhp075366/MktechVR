package com.gotech.vrplayer.module.personal.update;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gotech.vrplayer.R;
import com.gotech.vrplayer.widget.CustomDialog;

public class DialogCreater {

    public static CustomDialog showDownloadDialog(Context context, View view, String updateInfo) {
        TextView textContent = (TextView)view.findViewById(R.id.text_content);
        textContent.setText(updateInfo);
        CustomDialog dialogWaiting = new CustomDialog(context, view, R.style.UpdateDialog);
        dialogWaiting.show();
        dialogWaiting.setCancelable(true);
        return dialogWaiting;
    }

    public static CustomDialog showWaitingDialog(Context context, String tip) {
        View view = View.inflate(context, R.layout.dialog_waitting, null);
        if (!TextUtils.isEmpty(tip)) {
            ((TextView)view.findViewById(R.id.tvTip)).setText(tip);
        } else {
            view.findViewById(R.id.tvTip).setVisibility(View.GONE);
        }
        CustomDialog dialogWaiting = new CustomDialog(context, view, R.style.WaitDialog);
        dialogWaiting.show();
        dialogWaiting.setCancelable(false);
        return dialogWaiting;
    }
}