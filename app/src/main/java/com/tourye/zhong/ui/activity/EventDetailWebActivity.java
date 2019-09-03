package com.tourye.zhong.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tourye.zhong.BuildConfig;
import com.tourye.zhong.R;
import com.tourye.zhong.base.BaseActivity;
import com.tourye.zhong.beans.ShareDataBean;
import com.tourye.zhong.views.dialogs.WebShareDialog;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * EventDetailWebActivity
 * author:along
 * 2018/11/21 下午6:06
 * <p>
 * 描述:赛事详情web页面
 */

public class EventDetailWebActivity extends BaseActivity {

    private WebView mWebActivityEventDetailWeb;

    private String TAG = "EventDetailWebActivity";

    private String jsCode = "";//js交互代码

    public Map<String, ShareDataBean> mShareDataBeanMap = new HashMap<>();

    Gson mGson = new Gson();

    @Override
    public void initView() {

        mWebActivityEventDetailWeb = (WebView) findViewById(R.id.web_activity_event_detail_web);

        mImgReturn.setBackgroundResource(R.drawable.icon_return);
        mImgReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWebActivityEventDetailWeb.canGoBack()) {
                    mWebActivityEventDetailWeb.goBack();
                    return;
                }
                finish();
            }
        });
        mImgCertain.setBackgroundResource(R.drawable.icon_share);
        //设置点击分享回调监听
        mImgCertain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebShareDialog shareDialog = new WebShareDialog(mActivity);
                shareDialog.setShareDataBeanMap(mShareDataBeanMap);
                shareDialog.setShareCallback(new WebShareDialog.ShareCallback() {
                    @Override
                    public void onSuccess(String platform) {
                        mWebActivityEventDetailWeb.loadUrl("javascript:window.TouryeJSBridge.success('" + platform + "')");
                    }

                    @Override
                    public void onFailure(String platform) {
                        mWebActivityEventDetailWeb.loadUrl("javascript:window.TouryeJSBridge.fail('" + platform + "')");
                    }

                    @Override
                    public void onCancel(String platform) {
                        mWebActivityEventDetailWeb.loadUrl("javascript:window.TouryeJSBridge.cancel('" + platform + "')");
                    }
                });
                shareDialog.show();
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (mWebActivityEventDetailWeb.canGoBack()) {
            mWebActivityEventDetailWeb.goBack();
            WebBackForwardList webBackForwardList = mWebActivityEventDetailWeb.copyBackForwardList();
            mTvTitle.setText(webBackForwardList.getCurrentItem().getTitle());
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void initData() {

        //提取js代码
        AssetManager assets = getAssets();
        try {
            InputStream open = assets.open("android-internal.min.js");
            int available = open.available();
            byte[] bytes = new byte[available];
            open.read(bytes);
            jsCode = new String(bytes, "utf-8");
            open.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //获取上个页面传递的数据
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        String title = intent.getStringExtra("title");
        if (!TextUtils.isEmpty(data)) {

            mWebActivityEventDetailWeb.loadUrl(data);
            mWebActivityEventDetailWeb.getSettings().setJavaScriptEnabled(true);
            //添加代理方便h5作识别
            String userAgentString = mWebActivityEventDetailWeb.getSettings().getUserAgentString();
            userAgentString = userAgentString + " Tourye/v" + BuildConfig.VERSION_NAME;
            mWebActivityEventDetailWeb.getSettings().setUserAgentString(userAgentString);
            mWebActivityEventDetailWeb.addJavascriptInterface(new JSShareUtils(mActivity), "jsHook");
            mWebActivityEventDetailWeb.setWebViewClient(new WebViewClient() {


                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                    //7。0之上需要更改设置，要不webview不显示
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        view.loadUrl(request.getUrl().toString());
                    } else {
                        view.loadUrl(request.toString());
                    }
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);

                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                        mWebActivityEventDetailWeb.evaluateJavascript("javascript:" + jsCode, new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {

                            }
                        });
                    } else {
                        mWebActivityEventDetailWeb.loadUrl("javascript:" + jsCode);
                    }
                }
            });
            mWebActivityEventDetailWeb.setWebChromeClient(new WebChromeClient() {

                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                    mTvTitle.setText(title);
                }

            });
        }
    }

    @Override
    public int getRootView() {
        return R.layout.activity_event_detail_web;
    }

    /**
     * js交互
     */
    public class JSShareUtils {

        private Context mContext;

        public static final String PLATFORM_QQ = "onMenuShareQQ";
        public static final String PLATFORM_QZONE = "onMenuShareQzone";
        public static final String PLATFORM_WX = "onMenuShareWXMessage";
        public static final String PLATFORM_PYQ = "onMenuShareWXTimeline";
        public static final String PLATFORM_WEIBO = "onMenuShareWeibo";

        public JSShareUtils(Context context) {
            mContext = context;
        }

        @JavascriptInterface
        public void onMenuShareWXTimeline(String content) {
            ShareDataBean shareDataBean = mGson.fromJson(content, ShareDataBean.class);
            mShareDataBeanMap.put(JSShareUtils.PLATFORM_PYQ, shareDataBean);
        }

        @JavascriptInterface
        public void onMenuShareWXMessage(String content) {
            ShareDataBean shareDataBean = mGson.fromJson(content, ShareDataBean.class);
            mShareDataBeanMap.put(JSShareUtils.PLATFORM_WX, shareDataBean);
        }

        @JavascriptInterface
        public void onMenuShareQQ(String content) {
            ShareDataBean shareDataBean = mGson.fromJson(content, ShareDataBean.class);
            mShareDataBeanMap.put(JSShareUtils.PLATFORM_QQ, shareDataBean);
        }

        @JavascriptInterface
        public void onMenuShareQzone(String content) {
            ShareDataBean shareDataBean = mGson.fromJson(content, ShareDataBean.class);
            mShareDataBeanMap.put(JSShareUtils.PLATFORM_QZONE, shareDataBean);
        }

        @JavascriptInterface
        public void onMenuShareWeibo(String content) {
            ShareDataBean shareDataBean = mGson.fromJson(content, ShareDataBean.class);
            mShareDataBeanMap.put(JSShareUtils.PLATFORM_WEIBO, shareDataBean);
        }

    }

}
