package com.tourye.zhong.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tourye.zhong.R;
import com.tourye.zhong.beans.ShareDataBean;
import com.tourye.zhong.ui.activity.EventDetailWebActivity;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by longlongren on 2018/8/17.
 * <p>
 * introduce:分享dialog
 */

public class WebShareDialog extends Dialog implements View.OnClickListener {

    private final Context mContext;
    private LinearLayout mLlDialogShareWechat;
    private LinearLayout mLlDialogShareWechatMoment;
    private LinearLayout mLlDialogShareQq;
    private LinearLayout mLlDialogShareQqZone;
    private LinearLayout mLlDialogShareSina;
    private LinearLayout mLlDialogShare;
    private LinearLayout mLlDialogShareDownload;
    private TextView mTvDialogShareCancel;

    private String mTitle = "";
    private String mDesc = "";
    private String mImgUrl = "";
    private String mLink = "";
    private String mUrlDownload = "";//要下载的图片的地址

    private DownloadCallback mDownloadCallback;
    private ShareCallback mShareCallback;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private Map<String,ShareDataBean> mShareDataBeanMap;

    public WebShareDialog(@NonNull Context context) {
        super(context);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(Gravity.BOTTOM);
        setContentView(R.layout.dialog_share);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);

        getWindow().setWindowAnimations(R.style.HeadDialogStyle);

        mContext = context;

        mLlDialogShareWechat = (LinearLayout) findViewById(R.id.ll_dialog_share_wechat);
        mLlDialogShareWechatMoment = (LinearLayout) findViewById(R.id.ll_dialog_share_wechat_moment);
        mLlDialogShareQq = (LinearLayout) findViewById(R.id.ll_dialog_share_qq);
        mLlDialogShareQqZone = (LinearLayout) findViewById(R.id.ll_dialog_share_qq_zone);
        mLlDialogShareSina = (LinearLayout) findViewById(R.id.ll_dialog_share_sina);
        mLlDialogShare = (LinearLayout) findViewById(R.id.ll_dialog_share);
        mLlDialogShareDownload = (LinearLayout) findViewById(R.id.ll_dialog_share_download);
        mTvDialogShareCancel = (TextView) findViewById(R.id.tv_dialog_share_cancel);
        mTvDialogShareCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mLlDialogShareWechat.setOnClickListener(this);
        mLlDialogShareWechatMoment.setOnClickListener(this);
        mLlDialogShareQq.setOnClickListener(this);
        mLlDialogShareQqZone.setOnClickListener(this);
        mLlDialogShareSina.setOnClickListener(this);
        mLlDialogShareDownload.setOnClickListener(this);

    }

    public void setShareDataBeanMap(Map<String, ShareDataBean> shareDataBeanMap) {
        mShareDataBeanMap = shareDataBeanMap;
    }

    public void setShareCallback(ShareCallback shareCallback) {
        mShareCallback = shareCallback;
    }

    /**
     * https://zhong.tourye.cn/app/?referrer=1#mine
     * 报名二级列表： #/matchListNew/:roadId       roadId: 一级列表中路线id
     * 社区分享: #/discover/community/detail/:communityId   communityId: 社区这条详情的id
     */

    public void share(String platform) {

        String plat_form="";//分享平台

        ShareDataBean shareDataBean=null;
        if (platform.equals(Wechat.NAME)) {
            plat_form= EventDetailWebActivity.JSShareUtils.PLATFORM_WX;
            shareDataBean = mShareDataBeanMap.get(EventDetailWebActivity.JSShareUtils.PLATFORM_WX);
        } else if (platform.equals(WechatMoments.NAME)) {
            plat_form= EventDetailWebActivity.JSShareUtils.PLATFORM_PYQ;
            shareDataBean = mShareDataBeanMap.get(EventDetailWebActivity.JSShareUtils.PLATFORM_PYQ);
        } else if (platform.equals(QQ.NAME)) {
            plat_form= EventDetailWebActivity.JSShareUtils.PLATFORM_QQ;
            shareDataBean = mShareDataBeanMap.get(EventDetailWebActivity.JSShareUtils.PLATFORM_QQ);
        } else if (platform.equals(QZone.NAME)) {
            plat_form= EventDetailWebActivity.JSShareUtils.PLATFORM_QZONE;
            shareDataBean = mShareDataBeanMap.get(EventDetailWebActivity.JSShareUtils.PLATFORM_QZONE);
        } else if (platform.equals(SinaWeibo.NAME)) {
            plat_form= EventDetailWebActivity.JSShareUtils.PLATFORM_WEIBO;
            shareDataBean = mShareDataBeanMap.get(EventDetailWebActivity.JSShareUtils.PLATFORM_WEIBO);
        }
        if (shareDataBean==null || TextUtils.isEmpty(shareDataBean.getTitle())) {
            return;
        }

        mDesc = shareDataBean.getDesc();
        mImgUrl = shareDataBean.getImgUrl();
        mLink = shareDataBean.getLink();
        mTitle = shareDataBean.getTitle();

        if ("NULL".equalsIgnoreCase(mDesc)) {
            mDesc="";
        }

        final OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        oks.setPlatform(platform);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(mTitle);
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(mLink);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(mDesc);
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(mImgUrl);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(mLink);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(mDesc);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(mLink);
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(mLink);
        //分享成功失败的回调
        final String finalPlat_form = plat_form;
        oks.setCallback(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                // TODO Auto-generated method stub
                mHandler.post(new Runnable() {
                    public void run() {
                        Toast.makeText(mContext, "分享失败", Toast.LENGTH_SHORT).show();
                        mShareCallback.onFailure(finalPlat_form);
                    }
                });
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                // TODO Auto-generated method stub
                mHandler.post(new Runnable() {
                    public void run() {
                        Toast.makeText(mContext, "分享成功", Toast.LENGTH_SHORT).show();
                        mShareCallback.onSuccess(finalPlat_form);
                    }
                });

            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                // TODO Auto-generated method stub
                mHandler.post(new Runnable() {
                    public void run() {
                        Toast.makeText(mContext, "取消分享", Toast.LENGTH_SHORT).show();
                        mShareCallback.onCancel(finalPlat_form);
                    }
                });
            }
        });

        //启动分享
        oks.show(mContext);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_dialog_share_wechat:
                share(Wechat.NAME);
                break;
            case R.id.ll_dialog_share_wechat_moment:
                share(WechatMoments.NAME);
                break;
            case R.id.ll_dialog_share_qq:
                share(QQ.NAME);
                break;
            case R.id.ll_dialog_share_qq_zone:
                share(QZone.NAME);
                break;
            case R.id.ll_dialog_share_sina:
                share(SinaWeibo.NAME);
                break;
        }
        dismiss();
    }

    public interface DownloadCallback {
        public void download();
    }

    public interface ShareCallback {

        public void onSuccess(String platform);

        public void onFailure(String platform);

        public void onCancel(String platform);

    }

}
