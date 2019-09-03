package com.tourye.zhong.ui.fragment;

import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tourye.zhong.BuildConfig;
import com.tourye.zhong.R;
import com.tourye.zhong.base.BaseFragment;
import com.tourye.zhong.utils.SaveUtil;

/**
 * CrowdfundingFragment
 * author:along
 * 2018/9/19 下午2:32
 *
 * 描述:众筹页面
 */

public class CrowdfundingFragment extends BaseFragment{

    private WebView mWebFragCrowdfunding;

    @Override
    public void initView(View view) {
        mWebFragCrowdfunding = (WebView) view.findViewById(R.id.web_frag_crowdfunding);

    }

    @Override
    public boolean onBackPressed() {
        if (mWebFragCrowdfunding.canGoBack()) {
            mWebFragCrowdfunding.goBack();
            return true;
        }
        return super.onBackPressed();
    }

    @Override
    public void initData() {
        super.initData();
        String authorization = SaveUtil.getString("Authorization", "");
        if (TextUtils.isEmpty(authorization)) {
            return;
        }

        String url= BuildConfig.WEB_URL+authorization + "#/myCrowdfundingNew";

        mWebFragCrowdfunding.loadUrl(url);
        mWebFragCrowdfunding.getSettings().setJavaScriptEnabled(true);
        mWebFragCrowdfunding.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebFragCrowdfunding.getSettings().setDomStorageEnabled(true);
        //添加代理方便h5作识别
        String userAgentString = mWebFragCrowdfunding.getSettings().getUserAgentString();
        userAgentString = userAgentString + " Tourye/v" + BuildConfig.VERSION_NAME;
        mWebFragCrowdfunding.getSettings().setUserAgentString(userAgentString);

        mWebFragCrowdfunding.setWebViewClient(new WebViewClient(){
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
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public int getRootView() {
        return R.layout.fragment_crowdfunding;
    }

    @Override
    public boolean isNeedTitle() {
        return true;
    }
}
