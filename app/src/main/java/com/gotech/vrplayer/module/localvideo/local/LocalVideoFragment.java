package com.gotech.vrplayer.module.localvideo.local;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gotech.vrplayer.R;
import com.gotech.vrplayer.base.BaseFragment;
import com.socks.library.KLog;

import butterknife.BindView;

/**
 * Author: ZouHaiping on 2017/7/2
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class LocalVideoFragment extends BaseFragment<LocalVideoPresenter> implements ILocalVideoView {

    @BindView(R.id.text_view_text)
    TextView mTextView;

    private Context mContext;

    public static LocalVideoFragment newInstance(String arg) {
        Bundle args = new Bundle();
        args.putString("ARGS", arg);
        LocalVideoFragment fragment = new LocalVideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.fragment_local_video;
    }

    @Override
    protected void createPresenter() {
        mContext = getContext();
        mPresenter = new LocalVideoPresenter(mContext, this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    private void initRecyclerView() {
        String arg = getArguments().getString("ARGS");
        mTextView.setText(arg);
    }
}
