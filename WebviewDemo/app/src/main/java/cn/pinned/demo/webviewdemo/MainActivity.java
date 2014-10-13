package cn.pinned.demo.webviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


public class MainActivity extends Activity {

    private final String TAG = MainActivity.class.getSimpleName();

    private WebView mWebView = null;

    private String mLoadUrl = null;

    private PopupWindow mGeoPopupWindow = null;

    private TextView mGeoTitleTv = null;
    private CheckBox mGeoSaveCb = null;
    private Button mShareLocationBtn = null;
    private Button mRefusedBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.initData();
        this.initView();
    }

    private void initData() {
        this.mLoadUrl = "http://map.baidu.com/";
    }

    private void initView() {
        this.mWebView = (WebView) this.findViewById(R.id.webview);
        WebSettings setting = mWebView.getSettings();
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        setting.setJavaScriptEnabled(true);// 支持js
        setting.setDomStorageEnabled(true);
        setting.setLoadWithOverviewMode(true);
        setting.setUseWideViewPort(true);
        setting.setDatabaseEnabled(true);
        setting.setAllowFileAccess(true);
        setting.setGeolocationEnabled(true);
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
//        setting.setPluginState(WebSettings.PluginState.ON);
        // 如果是仅在Wifi下下载图片,并且不是使用的Wifi网络
        this.mWebView.setWebChromeClient(new ServiceInfoWebViewClient());
        this.mWebView.setWebViewClient(new UrlWebViewClient());
        // 点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) { // 表示按返回键时的操作
                        if (mWebView.canGoBack()) {
                            mWebView.goBack(); // 后退
                            if (mGeoPopupWindow != null && mGeoPopupWindow.isShowing()) {
                                mGeoPopupWindow.dismiss();
                            }
                        } else {
                        }
                        return true; // 已处理
                    }
                }
                return false;
            }
        });
        this.loadUrl();
    }

    private void loadUrl() {
        if (TextUtils.isEmpty(mLoadUrl)){
            this.mWebView.loadUrl("file:///android_asset/error_page.html");
        } else {
            this.mWebView.loadUrl(mLoadUrl);
        }
    }
    private void showLocalRemidPopupWindow(final String title, final GeoListener callback) {
        String saved = PreferencesUtil.getString(this, title, "");
        if (!TextUtils.isEmpty(saved)) {
            if (saved.equals("true")){
                if (callback != null) {
                    callback.onSharedLocation();
                }
            } else if (saved.equals("false")){
                if (callback != null){
                    callback.onRefused();
                }
            }
            return;
        }
        if (mGeoPopupWindow == null){
            View view = LayoutInflater.from(this).inflate(R.layout.geo_popup_window, null);
            this.mGeoTitleTv = (TextView) view.findViewById(R.id.geo_title);
            this.mGeoSaveCb = (CheckBox) view.findViewById(R.id.geo_save_share_preference);
            this.mRefusedBtn = (Button) view.findViewById(R.id.refused_btn);
            this.mShareLocationBtn = (Button) view.findViewById(R.id.share_btn);
            this.mGeoPopupWindow = new PopupWindow(view,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, false);
        }
        this.mGeoTitleTv.setText(String.format(getString(R.string.geo_title), title));
        this.mGeoSaveCb.setChecked(true);
        this.mRefusedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSharePrefrence(title, false);
                if (callback != null) {
                    callback.onRefused();
                }
                mGeoPopupWindow.dismiss();
            }
        });
        this.mShareLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSharePrefrence(title, true);
                if (callback != null) {
                    callback.onSharedLocation();
                }
                mGeoPopupWindow.dismiss();
            }
        });
        this.mGeoPopupWindow.showAtLocation(mWebView, Gravity.BOTTOM, 0, 0);
    }

    private void saveSharePrefrence(String title, boolean saved) {
        if (mGeoSaveCb.isChecked()){
            PreferencesUtil.setString(this, title, String.valueOf(saved));
        } // end if
    }



    interface GeoListener {
        void onRefused();
        void onSharedLocation();
    }

    class ServiceInfoWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }
        @Override
        public void onGeolocationPermissionsShowPrompt(
                final String origin,
                final GeolocationPermissions.Callback callback) {
            DebugLog.d(TAG, "[onGeolocationPermissionsShowPrompt]");
            showLocalRemidPopupWindow(origin, new GeoListener() {
                @Override
                public void onRefused() {
                    callback.invoke(origin, false, false);
                }

                @Override
                public void onSharedLocation() {
                    callback.invoke(origin, true, false);
                }
            });
        }
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
        }
        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
        }

    }

    class UrlWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return  super.shouldOverrideUrlLoading(view, url);
        }

        public void onReceivedError(WebView view, int errorCode, String description,
                                    String failingUrl) {
            // 处理加载错误的情况
            DebugLog.d(TAG, "errorCode:" + errorCode);
            view.stopLoading(); // may not be needed
            view.loadUrl("file:///android_asset/error_page.html");
        }

    }

}
