package com.tourye.zhong.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tourye.zhong.BuildConfig;
import com.tourye.zhong.R;
import com.tourye.zhong.base.BaseActivity;
import com.tourye.zhong.utils.SaveUtil;
import com.tourye.zhong.views.dialogs.ClearCacheDialog;

/**
 * SettingActivity
 * author:along
 * 2018/10/10 上午10:56
 *
 * 描述:设置页面
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mRlActivitySettingMessage;
    private RelativeLayout mRlActivitySettingBind;
    private RelativeLayout mRlActivitySettingBusiness;
    private RelativeLayout mRlActivitySettingCache;
    private RelativeLayout mRlActivitySettingHelp;
    private TextView mTvActivitySettingExit;



    @Override
    public void initView() {

        mRlActivitySettingMessage = (RelativeLayout) findViewById(R.id.rl_activity_setting_message);
        mRlActivitySettingBind = (RelativeLayout) findViewById(R.id.rl_activity_setting_bind);
        mRlActivitySettingBusiness = (RelativeLayout) findViewById(R.id.rl_activity_setting_business);
        mRlActivitySettingCache = (RelativeLayout) findViewById(R.id.rl_activity_setting_cache);
        mRlActivitySettingHelp = (RelativeLayout) findViewById(R.id.rl_activity_setting_help);
        mTvActivitySettingExit = (TextView) findViewById(R.id.tv_activity_setting_exit);

        mRlActivitySettingMessage.setOnClickListener(this);
        mTvActivitySettingExit.setOnClickListener(this);
        mRlActivitySettingHelp.setOnClickListener(this);
        mRlActivitySettingCache.setOnClickListener(this);

        mTvTitle.setText("设置");
        mImgReturn.setBackgroundResource(R.drawable.icon_return);
        mImgReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void initData() {

    }

    @Override
    public int getRootView() {
        return R.layout.activity_setting;
    }

    /**
     * 跳转web页面显示
     * @param content
     * @param title  web页面标题
     */
    public void goToWeb(String content,String title){
        String authorization = SaveUtil.getString("Authorization", "");
        if (TextUtils.isEmpty(authorization)) {
            return;
        }
        Intent intent = new Intent(mActivity, CommonWebActivity.class);
        String url= BuildConfig.WEB_URL+authorization+content;
        intent.putExtra("data",url);
        intent.putExtra("title",title);
        mActivity.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_activity_setting_message:
                startActivity(new Intent(mActivity,MessageNotifyActivity.class));
                break;
            case R.id.rl_activity_setting_bind:
                break;
            case R.id.rl_activity_setting_business:
                break;
            case R.id.rl_activity_setting_cache:
                ClearCacheDialog clearCacheDialog = new ClearCacheDialog(mActivity);
                clearCacheDialog.show();
                break;
            case R.id.rl_activity_setting_help:
                goToWeb("#/feedback","帮助与吐槽");
                break;
            case R.id.tv_activity_setting_exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("退出")
                        .setMessage("确定退出吗？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SaveUtil.putString("Authorization","");
                                Intent intent = new Intent(mActivity, LoginActivity.class);
                                //跳转之前清空activity栈中的activity
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                dialogInterface.dismiss();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
        }
    }
}
