package com.mktech.smarthome.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mktech.smarthome.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class VrActionBar extends RelativeLayout {

    @BindView(R.id.actionbar_search)
    TextView mTextView;

    private View mView;
    private Context mContext;
    private Unbinder Unbinder;

    public enum RIGHT_BUTTON {
        SEARCH_ICON, EDIT_ICON, NONE_ICON
    }

    public VrActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setWillNotDraw(false);
        init();
    }

    private void init() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.widget_actionbar, this, true);
        Unbinder = ButterKnife.bind(mView);
    }

    public void setRightButton(RIGHT_BUTTON rightButton) {
        switch(rightButton) {
            case SEARCH_ICON:
                mTextView.setVisibility(VISIBLE);
                mTextView.setBackgroundResource(R.mipmap.ic_search);
                mTextView.setText("");
                break;
            case EDIT_ICON:
                mTextView.setVisibility(VISIBLE);
                mTextView.setBackgroundColor(getResources().getColor(R.color.transparent));
                mTextView.setText("编辑");
                break;
            case NONE_ICON:
                mTextView.setVisibility(GONE);
                break;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Unbinder.unbind();
    }

    public void setOnRightButtonClickListner(OnClickListener onClickListener) {
        mTextView.setOnClickListener(onClickListener);
    }

    private boolean bEditable = false;

    public void setEditable(boolean b) {
        bEditable = b;
        if(b) {
            mTextView.setText("删除");
        } else {
            mTextView.setText("编辑");
        }
    }

    public boolean getEditable() {
        return bEditable;
    }
}
