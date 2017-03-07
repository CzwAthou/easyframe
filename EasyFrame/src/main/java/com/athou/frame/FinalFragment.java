package com.athou.frame;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.athou.crash.path.PagePath;
import com.athou.frame.handler.DialogHandlerImpl;
import com.athou.frame.handler.IBaseHandler;
import com.athou.frame.handler.ToastHandlerImpl;
import com.athou.frame.net.NetErrorBean;
import com.athou.frame.util.L;
import com.athou.frame.util.ViewFinder;
import com.athou.frame.widget.dialog.handler.ICustomDialog;
import com.athou.frame.widget.dialog.handler.ILoadDialog;
import com.athou.frame.widget.dialog.handler.IProgressDialog;

/**
 * Created by cai on 2016/9/25.
 */

public abstract class FinalFragment extends Fragment implements IBaseHandler {

    protected View rootView = null;

    private ToastHandlerImpl toastHandlerImpl;
    private DialogHandlerImpl dialogHandlerImpl;

    /**
     * 合并外部数据和内部参数
     */
    public void setArgumentsData(Bundle data) {
        Bundle bundle = getArguments();
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putAll(data);
        setArguments(bundle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutId = requestLayoutId();
        if (layoutId <= 0) {
            L.e("the fragment layoutid is illegal");
            return null;
        }
        rootView = inflater.inflate(layoutId, null);
        if (rootView == null) {
            L.e("the fragment view inflater error");
            return null;
        }
        fragmentCreateStart();
        toastHandlerImpl = new ToastHandlerImpl(getActivity(), rootView);
        dialogHandlerImpl = new DialogHandlerImpl(getActivity());

        initView(rootView);
        setViewData(savedInstanceState);

        PagePath.onCreate(this);
        fragmentCreateEnd();
        return rootView;
    }

    public View getRootView() {
        return rootView;
    }

    /**
     * 设置fragment的layout
     */
    protected abstract int requestLayoutId();

    /**
     * 初始化view
     *
     * @param rootView
     */
    protected abstract void initView(View rootView);

    /**
     * 初始化view相关联的数据
     */
    protected abstract void setViewData(Bundle savedInstanceState);

    protected void fragmentCreateStart() {
    }

    protected void fragmentCreateEnd() {
    }

    /**
     * 查找view
     *
     * @param id view的ID
     * @return view
     * @author 菜菜
     */
    @SuppressWarnings("hiding")
    protected <T extends View> T findView(int id) {
        return ViewFinder.findViewById(rootView, id);
    }

    /**
     * 通过父View查找子View
     *
     * @param v  父View
     * @param id 子View的ID
     * @return 子View
     * @author 菜菜
     */
    @SuppressWarnings("hiding")
    protected <T extends View> T findView(View v, int id) {
        return ViewFinder.findViewById(v, id);
    }

    /**
     * 返回键处理
     *
     * @return true：表示此fragment消化了返回键 false：表示没有消化返回键，交给activity处理
     */
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onResume() {
        PagePath.onResume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        PagePath.onPause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        PagePath.onDestroy(this);
        super.onDestroy();
        L.i("onDestroy ==>>" + getClass().getSimpleName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        L.i("onDestroyView ==>>" + getClass().getSimpleName());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        L.i("onSaveInstanceState ==>>" + getClass().getSimpleName());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        L.i("onViewStateRestored ==>>" + getClass().getSimpleName());
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        L.i("setMenuVisibility ==>>" + getClass().getSimpleName());
        if (this.getView() != null)
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public Activity getActivityContext() {
        return getActivity();
    }

    @Override
    public void startActivityDelegate(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void startActivityForResultDelegate(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public boolean isDestroyDelegate() {
        return isDetached();
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

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getActivity().getCurrentFocus() != null) {
            manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void hideSoftInputView(View view) {
        InputMethodManager manager = ((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (view != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showSoftInputView(View view) {
        if (view != null) {
            InputMethodManager manager = ((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE));
            manager.showSoftInput(view, 0);
        }
    }
}
