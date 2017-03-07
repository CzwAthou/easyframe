/*
 * Copyright (c) 2016  athou（cai353974361@163.com）.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.athou.frame.web;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.athou.frame.FinalActivity;
import com.athou.frame.R;
import com.athou.frame.util.AbStrUtil;
import com.athou.frame.util.L;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public abstract class FinalWebActivity extends FinalActivity implements View.OnLongClickListener, DownloadListener {

    private static final int FILECHOOSER_RESULTCODE = 0x1001;

    protected final String URL_EMPTY = "about:blank";

    private View contentView;
    protected WebView web;
    private ProgressBar mProgressBar = null;
    private TextView mEmptyView;

    private boolean hasError = false;
    /**
     * 是否展示ToolbarMenu
     */
    private boolean showToolbarMenu = false;

    private WebSharefUtil webSharefUtil;

    private ValueCallback<Uri> mUploadMessage;

    @Override
    protected boolean initData(Bundle paramBundle) {
        showToolbarMenu = getIntent().getBooleanExtra("showToolbarMenu", false);
        webSharefUtil = new WebSharefUtil(this, "webconfig");
        return true;
    }

    @Override
    protected int requestLayoutId() {
        return R.layout.frame_webview;
    }

    @Override
    protected void initView(View rootView) {
        contentView = findView(R.id.frame_webview_contentview);
        mProgressBar = findView(R.id.frame_webview_progressbar);
        web = findView(R.id.frame_webview_webView);

        mEmptyView = findView(R.id.frame_webview_emptyview);
        mEmptyView.setOnClickListener(errOnClickListener);

        web.setOnLongClickListener(this);
    }

    @Override
    protected void setViewData(Bundle savedInstanceState) {
        WebSettings webSettings = web.getSettings();
        // 设置支持JavaScript脚本
        webSettings.setJavaScriptEnabled(true);
        webSettings.setPluginState(PluginState.ON);
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 设置默认缩放方式尺寸是far
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDefaultFontSize(20);
        if (webSharefUtil.getAutoFitScreen() || !showToolbarMenu) { //如果不显示菜单栏，则默认用单列显示
            webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); // 单列显示
        }

        // 设置可以有数据库
        webSettings.setDatabaseEnabled(true);
        // 设置可以使用localStorage
        webSettings.setDomStorageEnabled(true);
        // 设置可以有缓存
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        web.removeJavascriptInterface("searchBoxJavaBridge_");
        web.removeJavascriptInterface("accessibility");
        web.removeJavascriptInterface("accessibilityTraversal");
        web.setScrollContainer(false);
        web.setScrollbarFadingEnabled(false);
        web.setDownloadListener(this);
        // 设置WebChromeClient
        web.setWebChromeClient(finalWebChromeClient);
        // 设置WebViewClient
        web.setWebViewClient(finalWebViewClient);

        loadUrl(requestLoadUrl());
    }

    /* 请求网络链接地址 */
    protected abstract String requestLoadUrl();

    private WebChromeClient finalWebChromeClient = new WebChromeClient() {
        // Android > 4.1.1 调用这个方法
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(Intent.createChooser(intent, "File Choose"), FILECHOOSER_RESULTCODE);
        }

        // 3.0 + 调用这个方法
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(Intent.createChooser(intent, "File Choose"), FILECHOOSER_RESULTCODE);
        }

        // Android < 3.0 调用这个方法
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMessage = uploadMsg;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(Intent.createChooser(intent, "File Choose"), FILECHOOSER_RESULTCODE);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mProgressBar.setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }

        // 设置程序的Title
        @Override
        public void onReceivedTitle(WebView view, String title) {
            setTitle(title);
            super.onReceivedTitle(view, title);
        }
    };

    private WebViewClient finalWebViewClient = new WebViewClient() {

        //  在点击请求的是链接是才会调用，重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边.
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            L.i("shouldOverrideUrlLoading:" + request.getUrl());
            if (FinalWebActivity.this.interceptHyperlink(request.getUrl().getPath())) {
                return true;
            }
            // 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器器加载页面
            view.loadUrl(request.getUrl().getPath());
            // 相应完成返回true
            return true;
        }

        //  在点击请求的是链接是才会调用，重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边.
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            L.i("shouldOverrideUrlLoading:" + url);
            if (FinalWebActivity.this.interceptHyperlink(url)) {
                return true;
            }
            // 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器器加载页面
            view.loadUrl(url);
            // 相应完成返回true
            return true;
        }

        // 页面开始加载
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            L.i("onPageStarted:" + url);

            mEmptyView.setVisibility(View.GONE);
            contentView.setVisibility(View.VISIBLE);

            if (FinalWebActivity.this.interceptUrl(url)) {
                return;
            }
            mProgressBar.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        // WebView加载的所有资源url
        @Override
        public void onLoadResource(WebView view, String url) {
            L.i("onLoadResource:" + url);
            if (FinalWebActivity.this.onLoadResource(view, url)) {
                return;
            }
            super.onLoadResource(view, url);
        }

        // 页面加载完成
        @Override
        public void onPageFinished(WebView view, String url) {
            mProgressBar.setVisibility(View.GONE);
            if (!hasError) {
                hasError = false;
                mEmptyView.setVisibility(View.GONE);
                contentView.setVisibility(View.VISIBLE);
            }
            FinalWebActivity.this.onPageFinished(view, url);
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            //handler.cancel(); // Android默认的处理方式
            handler.proceed();  // 接受所有网站的证书
            //handleMessage(Message msg); // 进行其他处理
        }

        @Override
        public void onReceivedError(final WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            hasError = true;

            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyView.setText(description + "\n点击我重新加载!");
            contentView.setVisibility(View.GONE);
        }

        @TargetApi(23)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);

            hasError = true;

            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyView.setText(error.getDescription() + "\n点击我重新加载!");
            mEmptyView.setOnClickListener(errOnClickListener);

            contentView.setVisibility(View.GONE);
        }

        /**
         * 重写该方法，拦截域名，除自己配置好的域名外，其他域名均不通过。（解决网络劫持)
         * @param view
         * @param request
         * @return
         */
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }
    };

    private OnClickListener errOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            web.reload();
            hasError = false;
            mEmptyView.setText("正在加载中...");
            mEmptyView.setOnClickListener(null);
        }
    };

    /**
     * 点击超链接，拦截请求
     *
     * @param url
     * @return
     */
    protected boolean interceptHyperlink(String url) {
        return false;
    }

    /**
     * 拦截网页
     *
     * @param url
     * @return
     */
    protected boolean interceptUrl(String url) {
        return false;
    }

    /**
     * webview加载资源文件监听回调
     *
     * @param view
     * @param url
     * @return ture or false
     */
    protected boolean onLoadResource(WebView view, String url) {
        return false;
    }

    /**
     * 页面加载完成回调
     */
    protected void onPageFinished(WebView view, String url) {
        setTitle(view.getTitle()); //goback 刷新标题
    }

    public WebView getWebView() {
        return web;
    }

    public void loadUrl(String url) {
        if (!AbStrUtil.isEmpty(url)) {
            web.loadUrl(url);
        } else {
            mEmptyView.setText("无效地址!");
            mEmptyView.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
        }
    }

    /**
     * 清空当前加载的网页
     */
    protected void clearWebView() {
        if (web != null) {
            web.loadUrl(URL_EMPTY);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (web != null) {
            web.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (web != null) {
            web.onPause();
        }
    }

    @Override
    public void onDestroy() {
        if (web != null) {
            web.loadUrl(URL_EMPTY);
            web.stopLoading();
            web = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    public void back() {
        if (web.canGoBack()) {
            web.goBack();
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!showToolbarMenu) {
            return true;
        }
        if (webSharefUtil.getAutoFitScreen()) {
            menu.add(Menu.NONE, Menu.FIRST + 1, 4, "原始网页").setIcon(android.R.drawable.ic_menu_view);
        } else {
            menu.add(Menu.NONE, Menu.FIRST + 1, 4, "自适应屏幕").setIcon(android.R.drawable.ic_menu_view);
        }
        menu.add(Menu.NONE, Menu.FIRST + 2, 5, "清除缓存").setIcon(android.R.drawable.ic_menu_recent_history);
        menu.add(Menu.NONE, Menu.FIRST + 3, 6, "退出浏览器").setIcon(android.R.drawable.ic_lock_power_off);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST + 1:
                if (webSharefUtil.getAutoFitScreen()) {
                    web.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
                    item.setTitle("自适应屏幕");
                    webSharefUtil.setAutoFitScreen(false);
                } else {
                    web.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
                    item.setTitle("原始网页");
                    webSharefUtil.setAutoFitScreen(true);
                }
                web.stopLoading();
                web.reload();
                break;
            case Menu.FIRST + 2:
                Observable.create(new Observable.OnSubscribe<Boolean>() {
                    @Override
                    public void call(Subscriber<? super Boolean> subscriber) {
                        deleteDatabase("frame_webview.db");
                        deleteDatabase("webviewCache.db");

                        subscriber.onNext(true);
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                web.clearCache(true);
                                //这个api仅仅清除自动完成填充的表单数据，并不会清除WebView存储到本地的数据。
                                web.clearFormData();
                                //清除当前webview访问的历史记录，只会webview访问历史记录里的所有记录除了当前访问记录.
                                web.clearHistory();

                                showSnacke("清理成功");
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        });
                break;
            case Menu.FIRST + 3:
                finish();
                break;
            default:
                break;
        }
        return super.onMenuItemClick(item);
    }

    /**
     * 添加toolbar后，此方法以及不执行
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST + 1:
                if (webSharefUtil.getAutoFitScreen()) {
                    web.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
                    item.setTitle("自适应屏幕");
                    webSharefUtil.setAutoFitScreen(false);
                } else {
                    web.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
                    item.setTitle("原始网页");
                    webSharefUtil.setAutoFitScreen(true);
                }
                web.stopLoading();
                web.reload();
                break;
            case Menu.FIRST + 2:
                Observable.create(new Observable.OnSubscribe<Boolean>() {
                    @Override
                    public void call(Subscriber<? super Boolean> subscriber) {
                        deleteDatabase("frame_webview.db");
                        deleteDatabase("webviewCache.db");
                        subscriber.onNext(true);
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                showSnacke("清理成功");
                                //T.show(FinalWebActivity.this, "清理成功");
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        });
                break;
            case Menu.FIRST + 3:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                long contentLength) {
        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        WebView.HitTestResult result = ((WebView) v).getHitTestResult();
        if (null == result)
            return false;
        return interuptHitTestResult(result);
    }

    /**
     * 拦截图片，并进行二维码失识别
     *
     * @param result
     * @return
     */
    protected abstract boolean interuptHitTestResult(WebView.HitTestResult result);
}
