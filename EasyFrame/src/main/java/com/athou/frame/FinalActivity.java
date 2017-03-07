/*
 * Copyright (c) 2016.
 * 丸子地球 版权所有
 * 杭州幻橙网络科技有限公司
 * HuanCheng Network Technology CO. LTD ALL RIGHTS RESERVED
 */
package com.athou.frame;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.athou.crash.path.PagePath;
import com.athou.frame.constants.Constants;
import com.athou.frame.handler.DialogHandlerImpl;
import com.athou.frame.handler.IBaseHandler;
import com.athou.frame.handler.IPermissionHandler;
import com.athou.frame.handler.ToastHandlerImpl;
import com.athou.frame.helper.ScreenManager;
import com.athou.frame.net.NetErrorBean;
import com.athou.frame.statusbar.StatusBarMode;
import com.athou.frame.statusbar.StatusBarUtil;
import com.athou.frame.util.AbAppUtil;
import com.athou.frame.util.ClickUtil;
import com.athou.frame.util.L;
import com.athou.frame.util.OsInfoUtil;
import com.athou.frame.util.ViewFinder;
import com.athou.frame.widget.dialog.handler.ICustomDialog;
import com.athou.frame.widget.dialog.handler.ILoadDialog;
import com.athou.frame.widget.dialog.handler.IProgressDialog;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * 作者：CaiCai on 2016/6/17 11:44
 */
@RuntimePermissions
public abstract class FinalActivity extends AppCompatActivity implements IPermissionHandler, Toolbar.OnMenuItemClickListener, IBaseHandler {

    private ToastHandlerImpl toastHandlerImpl;
    private DialogHandlerImpl dialogHandlerImpl;

    /**
     * 全局的LayoutInflater对象，已经完成初始化.
     */
    public LayoutInflater mInflater;
    /* 根布局 */
    protected View mRootView;
    protected Toolbar mToolbar;
    protected FrameLayout mContainer;

    /* 权限handler */
    private PermissionCallback mHandler;

    protected Handler baseHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.SHOW_TOAST:
                    showToast(msg.getData().getString("Msg"));
                    break;
                case Constants.SHOW_SNACK:
                    showSnacke(msg.getData().getString("Msg"));
                    break;
                case Constants.SHOW_CUSTOM_DIALOG:
                    showDialog(msg.getData().getString("Title"), msg.getData().getString("Msg"));
                    break;
                case Constants.HIDE_CUSTOM_DIALOG:
                    hideDialog();
                    break;
                case Constants.SHOW_LOAD_DIALOG:
                    showLoadDialog();
                    break;
                case Constants.HIDE_LOAD_DIALOG:
                    hideLoadDialog();
                    break;
                case Constants.SHOW_PROGRESS:
                    showProgressDialog(msg.getData().getLong("Total", 0), msg.getData().getLong("Progress", 0));
                    break;
                case Constants.HIDE_PROGRESS:
                    hideProgressDialog();
                    break;
            }
        }
    };

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.i("onCreate ==>>" + getClass().getSimpleName());
        if (!initData(savedInstanceState)) { //初始化数据失败的话 直接finish
            showToast("数据出错");
            finish();
            return;
        }
        int layoutId = requestLayoutId();
        if (layoutId <= 0) {
            L.e("the activity layoutid is illegal");
            finish();
            return;
        }
        mInflater = getLayoutInflater();
        View contentView = createContainerView(mInflater, layoutId);
        if (contentView == null) {
            L.e("the activity view inflater error");
            finish();
            return;
        }
        activityCreateStart();
        ScreenManager.getInstance().pushActivity(this);

        setBaseContentView(R.layout.frame_base_container);
        mRootView = findView(R.id.frame_base_root);
        mToolbar = findView(R.id.frame_base_container_toolbar);
        mContainer = findView(R.id.frame_base_container);

        setupToolbar();

        // 设置ContentView
        mContainer.addView(contentView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        if (OsInfoUtil.isM()) {
            mRootView.setFitsSystemWindows(true);
        }
        setupTransparentStatus();

        toastHandlerImpl = new ToastHandlerImpl(this, mRootView);
        dialogHandlerImpl = new DialogHandlerImpl(this);

        initView(contentView);
        setViewData(savedInstanceState);

        PagePath.onCreate(this);
        activityCreateEnd();
    }

    /**
     * 创建containerview
     *
     * @param inflater
     * @param layoutResID
     * @return
     */
    protected View createContainerView(LayoutInflater inflater, @LayoutRes int layoutResID) {
        return inflater.inflate(layoutResID, null);
    }

    /**
     * 设置contentview<br>
     * 采用代理的方式  供子类进行重写
     */
    protected void setBaseContentView(@LayoutRes int layoutResID) {
        setContentView(layoutResID);
    }

    /**
     * 初始化数据，若失败，则finish
     *
     * @param bundle
     * @return
     */
    protected abstract boolean initData(Bundle bundle);

    /**
     * 开始创建activity界面
     */
    protected void activityCreateStart() {
    }

    /**
     * 设置fragment的layout
     */
    protected abstract int requestLayoutId();

    /**
     * 初始化view
     */
    protected abstract void initView(View contentView);

    /**
     * 初始化view相关联的数据
     */
    protected abstract void setViewData(Bundle savedInstanceState);

    /**
     * 结束activity创建
     */
    protected void activityCreateEnd() {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        ScreenManager.screenWidth = metrics.widthPixels;
        ScreenManager.screenHeight = metrics.heightPixels;
    }

    /**
     * 设置沉浸式状态栏
     */
    protected void setupTransparentStatus() {
        if (requestStatusBarMode() == StatusBarMode.Shade_Color) {
            StatusBarUtil.setColor(this, getResources().getColor(requestStatusbarColorId()));
        } else if (requestStatusBarMode() == StatusBarMode.Full_Screen) {
            StatusBarUtil.setTranslucentStatus(this, true);
        }
    }

    /**
     * 是否设置statusbar的文字及图标变深色
     *
     * @param darkMode
     */
    protected void setStatusBarDarkMode(boolean darkMode) {
        if (darkMode) { //开启状态栏文字颜色加深，背景色变浅
            if (StatusBarUtil.setStatusBarTextColor(this, true) > 0) {
                //状态栏文字颜色加深成功，则更改状态栏背景
                StatusBarUtil.setColor(this, getResources().getColor(requestStatusbarColorId()));
            } else {
                //状态栏文字颜色加深失败，则更换样式（背景色改为主题色，状态栏文字颜色还是保留白色）
                StatusBarUtil.setColor(this, Color.BLACK);
            }
        } else {
            StatusBarUtil.setColor(this, getResources().getColor(requestThemeColorId()));
        }
    }

    /**
     * 设置沉浸式状态栏模式（默认不添加）
     */
    protected StatusBarMode requestStatusBarMode() {
        return StatusBarMode.None;
    }

    /**
     * 设置沉浸式状态栏的背景色
     */
    protected abstract int requestStatusbarColorId();

    /**
     * 主题颜色
     *
     * @return
     */
    protected abstract int requestThemeColorId();

    /**
     * 是否开启沉浸式状态栏darkmode
     */
    protected abstract boolean enableStatusbarDarkMode();

    public View getRootView() {
        return mRootView;
    }

    public FrameLayout getContainer() {
        return mContainer;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    /* replace the inner toolbar, if the new toolbar is same as inner's , it will do nothing*/
    public void setToolbar(Toolbar toolbar) {
        if (mToolbar != toolbar) {
            mToolbar.setVisibility(View.GONE);

            this.mToolbar = toolbar;
            setupToolbar();
        }
    }

    /* setup  toolbar  */
    private void setupToolbar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //显示返回键
            getSupportActionBar().setDisplayShowHomeEnabled(true); //可响应返回键点击事件
            mToolbar.setOnTouchListener(toolbarTouch);
            mToolbar.setOnMenuItemClickListener(this);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ClickUtil.isClickAvalible()) {
                        onBackPressed();
                    }
                }
            });
        }
    }

    /**
     * 查找view
     */
    @SuppressWarnings("hiding")
    protected <T extends View> T findView(int id) {
        return ViewFinder.findViewById(this, id);
    }

    /**
     * 通过父View查找子View
     */
    @SuppressWarnings("hiding")
    protected <T extends View> T findView(View v, int id) {
        return ViewFinder.findViewById(v, id);
    }

    /* 监听toolbar的touch事件，隐藏键盘 */
    private View.OnTouchListener toolbarTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            hideSoftInputView();
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (-1 != requestMenuId()) {
            getMenuInflater().inflate(requestMenuId(), menu);
        }
        return true;
    }

    public int requestMenuId() {
        return -1;
    }

    /* 监听toolbar的点击事件 */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        PagePath.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        PagePath.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        PagePath.onDestroy(this);

        hideLoadDialog();
        ScreenManager.getInstance().removeActivity(this);

        hideSoftInputView();
        super.onDestroy();

        L.i("onDestroy ==>>" + getClass().getSimpleName());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        L.i("onSaveInstanceState ==>>" + getClass().getSimpleName());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        L.i("onRestoreInstanceState ==>>" + getClass().getSimpleName());
    }

    @Override
    public Activity getActivityContext() {
        return this;
    }

    @Override
    public void startActivityDelegate(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void startActivityForResultDelegate(Intent intent, int requestCode) {
        startActivityForResultDelegate(intent, requestCode);
    }

    @Override
    public boolean isDestroyed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return super.isDestroyed();
        } else {
            if (getSupportFragmentManager().isDestroyed()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isDestroyDelegate() {
        return isDestroyed();
    }

    /**
     * 在线程中提示文本信息.
     */
    public void showToastInThread(String msgStr) {
        toastHandlerImpl.showToastInThread(msgStr);
    }

    /**
     * 在线程中提示文本信息.
     */
    public void showSnackInThread(String msgStr) {
        toastHandlerImpl.showSnackInThread(msgStr);
    }

    @Override
    public Toast showToast(int resId) {
        return toastHandlerImpl.showToast(resId);
    }

    @Override
    public Toast showToast(String msg) {
        return toastHandlerImpl.showToast(msg);
    }

    @Override
    public Toast showToast(NetErrorBean msg) {
        return toastHandlerImpl.showToast(msg);
    }

    @Override
    public Toast showToastLong(String msg) {
        return toastHandlerImpl.showToastLong(msg);
    }

    @Override
    public Snackbar showSnacke(String msg) {
        return toastHandlerImpl.showSnacke(msg);
    }

    @Override
    public Snackbar showSnackeLong(String msg) {
        return toastHandlerImpl.showSnackeLong(msg);
    }

    @Override
    public Snackbar showSnacke(String msg, String action, View.OnClickListener actionClick) {
        return toastHandlerImpl.showSnacke(msg, action, actionClick);
    }

    public void showDialogInThread(String title, String msgStr) {
        dialogHandlerImpl.showDialogInThread(title, msgStr);
    }

    public void hideDialogInThread() {
        dialogHandlerImpl.hideDialogInThread();
    }

    @Override
    public ICustomDialog showDialog(String title, String msg) {
        return dialogHandlerImpl.showDialog(title, msg);
    }

    @Override
    public ICustomDialog showDialog(String title, String msg, DialogInterface.OnClickListener ok) {
        return dialogHandlerImpl.showDialog(title, msg, ok);
    }

    @Override
    public ICustomDialog showDialog(String title, String msg, DialogInterface.OnClickListener ok, DialogInterface.OnClickListener cancel) {
        return dialogHandlerImpl.showDialog(title, msg, ok, cancel);
    }

    public ICustomDialog showDialog(String title, String msg, String okStr, DialogInterface.OnClickListener ok, String cancelStr, DialogInterface.OnClickListener cancel) {
        return dialogHandlerImpl.showDialog(title, msg, okStr, ok, cancelStr, cancel);
    }

    @Override
    public void hideDialog() {
        dialogHandlerImpl.hideDialog();
    }

    public void showLoadDialogInThread() {
        dialogHandlerImpl.showLoadDialogInThread();
    }

    public void hideLoadDialogInThread() {
        dialogHandlerImpl.hideLoadDialogInThread();
    }

    @Override
    public ILoadDialog showLoadDialog() {
        return dialogHandlerImpl.showLoadDialog();
    }

    @Override
    public ILoadDialog showLoadDialog(String title, String msg) {
        return dialogHandlerImpl.showLoadDialog(title, msg);
    }

    @Override
    public void hideLoadDialog() {
        dialogHandlerImpl.hideLoadDialog();
    }

    public void showProgressDialogInThread(long total, long progress) {
        dialogHandlerImpl.showProgressDialogInThread(total, progress);
    }

    public void hideProgressDialogInThread() {
        dialogHandlerImpl.hideProgressDialogInThread();
    }

    @Override
    public IProgressDialog showProgressDialog(long total, long progress) {
        return dialogHandlerImpl.showProgressDialog(total, progress);
    }

    @Override
    public void hideProgressDialog() {
        dialogHandlerImpl.hideProgressDialog();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                hideSoftInputView();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getCurrentFocus() != null) {
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void hideSoftInputView(View view) {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (view != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showSoftInputView(View view) {
        if (view != null) {
            InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
            manager.showSoftInput(view, 0);
        }
    }

    //-----------------------------------------------------------

    /**
     * 请求相机权限
     *
     * @param permissionHandler
     */
    @Override
    public void requestSDCardPermission(PermissionCallback permissionHandler) {
        this.mHandler = permissionHandler;
        FinalActivityPermissionsDispatcher.handleSDCardPermissionWithCheck(this);
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @Override
    public void handleSDCardPermission() {
        if (mHandler != null)
            mHandler.onGranted();
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @Override
    public void showWhyNeedSDCardPermission(PermissionRequest request) {
        L.i("showWhyNeedSDCardPermission");
        showWhyPermissionDialog(request, "[存储]");
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @Override
    public void deniedSDCardPermission() {
        if (mHandler != null)
            mHandler.onDenied();
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @Override
    public void OnSDCardNeverAskAgain() {
        showPermissionDialog("[存储]");
    }

    //-----------------------------------------------------------

    /**
     * 请求相机权限
     *
     * @param permissionHandler
     */
    @Override
    public void requestRecordPermission(PermissionCallback permissionHandler) {
        this.mHandler = permissionHandler;
        FinalActivityPermissionsDispatcher.handleRecordPermissionWithCheck(this);
    }

    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    @Override
    public void handleRecordPermission() {
        L.i("handleRecordPermission");
        if (mHandler != null)
            mHandler.onGranted();
    }

    @OnShowRationale(Manifest.permission.RECORD_AUDIO)
    @Override
    public void showWhyNeedRecordPermission(PermissionRequest request) {
        L.i("showWhyNeedRecordPermission");
        showWhyPermissionDialog(request, "[录音]");
    }

    @OnPermissionDenied(Manifest.permission.RECORD_AUDIO)
    @Override
    public void deniedRecordPermission() {
        L.i("deniedRecordPermission");
        if (mHandler != null)
            mHandler.onDenied();
    }

    @OnNeverAskAgain(Manifest.permission.RECORD_AUDIO)
    @Override
    public void OnRecordNeverAskAgain() {
        L.i("OnRecordNeverAskAgain");
        showPermissionDialog("[录音]");
    }

    //-----------------------------------------------------------

    /**
     * 请求相机权限
     *
     * @param permissionHandler
     */
    @Override
    public void requestCameraPermission(PermissionCallback permissionHandler) {
        this.mHandler = permissionHandler;
        FinalActivityPermissionsDispatcher.handleCameraPermissionWithCheck(this);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    @Override
    public void handleCameraPermission() {
        L.i("handleCameraPermission");
        if (mHandler != null)
            mHandler.onGranted();
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    @Override
    public void showWhyNeedCameraPermission(PermissionRequest request) {
        L.i("showWhyNeedCameraPermission");
        showWhyPermissionDialog(request, "[相机]");
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    @Override
    public void deniedCameraPermission() {
        L.i("deniedCameraPermission");
        if (mHandler != null)
            mHandler.onDenied();
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    @Override
    public void OnCameraNeverAskAgain() {
        L.i("OnCameraNeverAskAgain");
        showPermissionDialog("[相机]");
    }

    //-----------------------------------------------------------

    @Override
    public void requestPhoneStatePermission(PermissionCallback permissionHandler) {
        this.mHandler = permissionHandler;
        FinalActivityPermissionsDispatcher.handlePhoneStatePermissionWithCheck(this);
    }

    @NeedsPermission(Manifest.permission.READ_PHONE_STATE)
    @Override
    public void handlePhoneStatePermission() {
        L.i("handlePhoneStatePermission");
        if (mHandler != null)
            mHandler.onGranted();
    }

    @OnShowRationale(Manifest.permission.READ_PHONE_STATE)
    @Override
    public void showWhyNeedPhoneStatePermission(PermissionRequest request) {
        L.i("showWhyNeedPhoneStatePermission");
        showWhyPermissionDialog(request, "[电话]");
    }

    @OnPermissionDenied(Manifest.permission.READ_PHONE_STATE)
    @Override
    public void deniedPhoneStatePermission() {
        L.i("deniedPhoneStatePermission");
        if (mHandler != null)
            mHandler.onDenied();
    }

    @OnNeverAskAgain(Manifest.permission.READ_PHONE_STATE)
    @Override
    public void OnPhoneStateNeverAskAgain() {
        L.i("OnPhoneStateNeverAskAgain");
        showPermissionDialog("[相机]");
    }


    //-----------------------------------------------------------

    /**
     * 请求电话权限
     *
     * @param permissionHandler
     */
    @Override
    public void requestCallPermission(PermissionCallback permissionHandler) {
        this.mHandler = permissionHandler;
        FinalActivityPermissionsDispatcher.handleCallPermissionWithCheck(this);
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    @Override
    public void handleCallPermission() {
        L.i("handleCallPermission");
        if (mHandler != null)
            mHandler.onGranted();
    }

    @OnShowRationale(Manifest.permission.CALL_PHONE)
    @Override
    public void showWhyNeedCallPermission(PermissionRequest request) {
        L.i("showWhyNeedCallPermission");
        showWhyPermissionDialog(request, "[电话]");
    }

    @OnPermissionDenied(Manifest.permission.CALL_PHONE)
    @Override
    public void deniedCallPermission() {
        L.i("deniedCallPermission");
        if (mHandler != null)
            mHandler.onDenied();
    }

    @OnNeverAskAgain(Manifest.permission.CALL_PHONE)
    @Override
    public void OnCallNeverAskAgain() {
        L.i("OnCallNeverAskAgain");
        showPermissionDialog("[电话]");
    }

    /**
     * 当用户拒绝权限后，弹出提示框
     *
     * @param permission
     */
    @Override
    public void showPermissionDialog(String permission) {
        showDialog("权限申请", String.format("请在设置-应用-%s-权限中开启%s权限", getString(getApplicationInfo().labelRes), permission),
                "去开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AbAppUtil.showInstalledAppDetails(FinalActivity.this);
                        dialog.dismiss();
                    }
                }, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mHandler != null) {
                            mHandler.onDenied();
                        }
                        dialog.dismiss();
                    }
                });
    }

    /**
     * 提示用户为什么需要此权限
     */
    @Override
    public void showWhyPermissionDialog(final PermissionRequest request, String label) {
        showDialog(null, String.format("你必须开启%s权限，才能正常使用它此功能！", label), "去开启", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                request.proceed();//再次执行请求
            }
        }, null, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        FinalActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
