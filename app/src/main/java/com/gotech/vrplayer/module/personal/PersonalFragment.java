package com.gotech.vrplayer.module.personal;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gotech.vrplayer.R;
import com.gotech.vrplayer.base.BaseFragment;
import com.gotech.vrplayer.module.personal.update.CheckUpdateThread;
import com.gotech.vrplayer.module.personal.update.DialogCreater;
import com.gotech.vrplayer.module.personal.update.DownloadUpdateThread;
import com.gotech.vrplayer.module.personal.update.MessageID;
import com.gotech.vrplayer.module.personal.update.UpdateController;
import com.gotech.vrplayer.module.personal.update.UpdateService;
import com.gotech.vrplayer.utils.NetworkUtil;
import com.gotech.vrplayer.utils.ToastUtil;
import com.gotech.vrplayer.widget.CustomDialog;
import com.socks.library.KLog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: ZouHaiping on 2017/6/27
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class PersonalFragment extends BaseFragment<PersonalPresenter> implements IPersonalView {

    @BindView(R.id.tv_app_version)
    TextView mTvAppVersion;
    @BindView(R.id.check_update)
    RelativeLayout mCheckUpdate;
    @BindView(R.id.help)
    RelativeLayout mHelp;
    @BindView(R.id.feedback_advice)
    RelativeLayout mFeedbackAdvice;

    private Context mContext;
    private Resources mResources;

    // 自更新Service
    private CustomDialog mCheckingDialog;
    private UpdateService mUpdateService;
    private UpdateController mUpdateController;

    public static PersonalFragment newInstance(String arg) {
        Bundle args = new Bundle();
        args.putString("ARGS", arg);
        PersonalFragment fragment = new PersonalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showVersionName();
        initUpdateService();
    }

    @Override
    public void onDestroyView() {
        if (mUpdateService != null) {
            mUpdateService.setExternalHandler(null);
            mUpdateController.unbindUpdateService();
        }
        mAutoUpdateHandler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.fragment_personal;
    }

    @Override
    protected void createPresenter() {
        mContext = getContext();
        mResources = mContext.getResources();
        mPresenter = new PersonalPresenter(mContext, this);
    }

    @OnClick(R.id.check_update)
    public void checkUpdate() {
        if (!NetworkUtil.checkNetworkConnection(mContext)) {
            ToastUtil.showToast(mContext, R.string.no_network_connect, Toast.LENGTH_SHORT);
            return;
        }
        if (mUpdateService == null) {
            KLog.e("mUpdateService == null");
            return;
        }

        UpdateService.UPDATE_SERVICE_STATE eState = mUpdateService.getServiceState();
        if (eState == UpdateService.UPDATE_SERVICE_STATE.CHECKING) {
            KLog.i("UPDATE_SERVICE_STATE.CHECKING");
            return;
        } else if (eState == UpdateService.UPDATE_SERVICE_STATE.DOWNLOADINIG) {
            ToastUtil.showToast(mContext, R.string.update_downloading, Toast.LENGTH_SHORT);
            return;
        }
        mCheckingDialog = DialogCreater.showWaitingDialog(mContext, mResources.getString(R.string.update_check_tips));
        mCheckingDialog.show();
        // 此Handler用于Service检测更新/下载更新结果回调通知
        mUpdateService.setExternalHandler(mAutoUpdateHandler);
        mUpdateService.startCheckUpdate();
    }

    private void showVersionName() {
        String appVersion = mPresenter.getVersionName();
        mTvAppVersion.setText(appVersion);
    }

    private void initUpdateService() {
        mUpdateController = new UpdateController(mContext);
        // 此Handler用于Service连接回调通知
        mUpdateController.setExternalHandler(mAutoUpdateHandler);
        mUpdateController.startUpdateServiceIfStoped();
        mUpdateController.bindUpdateService();
    }

    private Handler mAutoUpdateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessageID.AUTO_UPDATE_SERVICE_CONNECTED:
                    mUpdateService = mUpdateController.getUpdateServiceInstance();
                    break;

                case MessageID.AUTO_UPDATE_SERVICE_DISCONNECTED:
                    mUpdateService = null;
                    break;

                case MessageID.AUTO_UPDATE_CHECKING_COMPLETE:
                    if (mCheckingDialog != null && mCheckingDialog.isShowing()) {
                        mCheckingDialog.dismiss();
                        mCheckingDialog = null;
                    }
                    CheckUpdateThread.CheckUpdateMsg updateMsg = (CheckUpdateThread.CheckUpdateMsg)msg.obj;
                    if (updateMsg.eResult == CheckUpdateThread.CHECK_UPDATE_RESULT.HAVE_UPDATE) {
                        mUpdateController.initDialogView();
                        mUpdateController.showUpdateDialog(updateMsg.strCheckResult);
                        mUpdateController.saveAppInfo(updateMsg.strAppMd5, updateMsg.appSize);
                    } else if (updateMsg.eResult == CheckUpdateThread.CHECK_UPDATE_RESULT.NO_UPDATE) {
                        ToastUtil.showToast(mContext, R.string.update_already_new, Toast.LENGTH_SHORT);
                    } else if (updateMsg.eResult == CheckUpdateThread.CHECK_UPDATE_RESULT.TIMEOUT) {
                        ToastUtil.showToast(mContext, R.string.update_check_timeout, Toast.LENGTH_SHORT);
                    } else if (updateMsg.eResult == CheckUpdateThread.CHECK_UPDATE_RESULT.EXCEPTION) {
                        ToastUtil.showToast(mContext, R.string.update_check_exception, Toast.LENGTH_SHORT);
                    }
                    break;

                case MessageID.AUTO_UPDATE_DOWNLOADING_COMPLETE:
                    DownloadUpdateThread.DownloadUpdateMsg downloadMsg = (DownloadUpdateThread.DownloadUpdateMsg)msg.obj;
                    ToastUtil.showToast(mContext, downloadMsg.strDownloadResult, Toast.LENGTH_LONG);
                    break;

                default:
                    break;
            }
        }
    };
}
