package com.tourye.zhong.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.tourye.zhong.R;
import com.tourye.zhong.base.BaseActivity;
import com.tourye.zhong.utils.SaveUtil;

/**
 * SplashActivity
 * author:along
 * 2018/9/18 下午2:02
 *
 * 描述:启动页
 */

public class SplashActivity extends BaseActivity {

    private Activity mActivity;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10086:
                    String authorization = SaveUtil.getString("Authorization", "");
                    if (!TextUtils.isEmpty(authorization)) {
                        Intent intent = new Intent(mActivity, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        startActivity(new Intent(mActivity,LoginActivity.class));
                        finish();
                    }

                    break;
            }
        }
    };

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        mActivity=this;
        mHandler.sendEmptyMessageDelayed(10086,100);
    }


    @Override
    public boolean isNeedTitle() {
        return false;
    }

    @Override
    public int getRootView() {
        return R.layout.activity_splash;
    }

}
