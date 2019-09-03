package com.tourye.zhong.ui.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tourye.zhong.BuildConfig;
import com.tourye.zhong.R;
import com.tourye.zhong.base.BaseActivity;
/**
 * CommonWebActivity
 * author:along
 * 2018/10/30 上午10:40
 *
 * 描述:常规webview展示页面
 */

public class CommonWebActivity extends BaseActivity {

    private static final String TAG = "CommonWebActivity";

    private WebView mWebActivityCommonWeb;

    //webview访问相册需要的参数
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 10086;

    @Override
    public void initView() {

        mWebActivityCommonWeb = (WebView) findViewById(R.id.web_activity_common_web);

        mImgReturn.setBackgroundResource(R.drawable.icon_return);
        mImgReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWebActivityCommonWeb.canGoBack()) {
                    mWebActivityCommonWeb.goBack();
                    return;
                }
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (mWebActivityCommonWeb.canGoBack()) {
            mWebActivityCommonWeb.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        String title = intent.getStringExtra("title");
        mTvTitle.setText(title);
        if (!TextUtils.isEmpty(data)) {
            mWebActivityCommonWeb.loadUrl(data);
            mWebActivityCommonWeb.getSettings().setJavaScriptEnabled(true);
            mWebActivityCommonWeb.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            mWebActivityCommonWeb.getSettings().setDomStorageEnabled(true);
            //添加代理方便h5作识别
            String userAgentString = mWebActivityCommonWeb.getSettings().getUserAgentString();
            userAgentString = userAgentString + " Tourye/v" + BuildConfig.VERSION_NAME;
            mWebActivityCommonWeb.getSettings().setUserAgentString(userAgentString);

            //以下配置不适用8。0
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        mWvActivityTicketShop.getSettings().setSafeBrowsingEnabled(false);
//                    }
            mWebActivityCommonWeb.setWebViewClient(new WebViewClient(){
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
            });
            mWebActivityCommonWeb.setWebChromeClient(new WebChromeClient() {

                //WebChromeClient的几个方法：
                public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                    Log.e(TAG, "openFileChoose(ValueCallback<Uri> uploadMsg)");
                    mUploadMessage = uploadMsg;
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("*/*");
                    mActivity.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
                }

                public void openFileChooser(ValueCallback uploadMsg, String acceptType) {

                    this.openFileChooser(uploadMsg);
                }

                public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                    this.openFileChooser(uploadMsg);
                }

                // For Android 5.0+
                @Override
                public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                    Log.e(TAG, "openFileChoose(ValueCallback<Uri> uploadMsg)");
                    mUploadCallbackAboveL = filePathCallback;
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("*/*");
                    mActivity.startActivityForResult(
                            Intent.createChooser(i, "File Browser"),
                            10086);
                    return true;
                }

            });
        }
    }

    /**
     * 5。0weview访问相册回调专用
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE
                || mUploadCallbackAboveL == null) {
            return;
        }
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                        Log.e(TAG, "onActivityResultAboveL: " + results[i].getPath());
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
                Log.e(TAG, "onActivityResultAboveL: " + results.length);
            }
        }
        mUploadCallbackAboveL.onReceiveValue(results);
        mUploadCallbackAboveL = null;
        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
    }

    @Override
    public int getRootView() {
        return R.layout.activity_common_web;
    }

}
