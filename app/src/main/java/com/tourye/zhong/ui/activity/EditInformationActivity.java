package com.tourye.zhong.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tourye.zhong.R;
import com.tourye.zhong.base.BaseActivity;

/**
 * EditInformationActivity
 * author:along
 * 2018/12/19 1:36 PM
 *
 * 描述:设置默认头像、昵称页面
 */

public class EditInformationActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mImgActivityEditInforHead;
    private TextView mTvActivityEditInforHead;
    private EditText mEdtActivityEditInforName;
    private TextView mTvActivityEditInforName;
    private TextView mTvActivityEditInformationSkip;

    @Override
    public void initView() {
        mImgReturn.setBackground(getResources().getDrawable(R.drawable.icon_return));
        mImgActivityEditInforHead = (ImageView) findViewById(R.id.img_activity_edit_infor_head);
        mTvActivityEditInforHead = (TextView) findViewById(R.id.tv_activity_edit_infor_head);
        mEdtActivityEditInforName = (EditText) findViewById(R.id.edt_activity_edit_infor_name);
        mTvActivityEditInforName = (TextView) findViewById(R.id.tv_activity_edit_infor_name);
        mTvActivityEditInformationSkip = (TextView) findViewById(R.id.tv_activity_edit_information_skip);

        mTvTitle.setText("信息填写");
        mTvCertain.setVisibility(View.VISIBLE);
        mTvCertain.setTextColor(Color.parseColor("#FFCC1C24"));
        mTvCertain.setText("保存");

        mImgReturn.setOnClickListener(this);
        mTvCertain.setOnClickListener(this);
        mTvActivityEditInformationSkip.setOnClickListener(this);
        mTvActivityEditInforHead.setOnClickListener(this);

    }

    @Override
    public void initData() {

    }

    @Override
    public int getRootView() {
        return R.layout.activity_edit_information;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_return:
                finish();
                break;
            case R.id.tv_certain:
                startActivity(new Intent(mActivity,MainActivity.class));
                finish();
                break;
            case R.id.tv_activity_edit_infor_head:
                startActivity(new Intent(mActivity, UpdateHeadActivity.class));
                break;
            case R.id.tv_activity_edit_information_skip:
                startActivity(new Intent(mActivity,MainActivity.class));
                finish();
                break;
        }
    }
}
