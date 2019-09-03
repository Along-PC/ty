package com.tourye.zhong.base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tourye.zhong.R;
import com.tourye.zhong.utils.NetStateUtils;
import com.tourye.zhong.utils.NoneNetUtils;


public abstract class BaseActivity extends AppCompatActivity {

    protected Activity mActivity;


    protected LayoutInflater mLayoutInflater;

    //标题栏控件
    protected ImageView mImgReturn;
    protected TextView mTvTitle;
    protected TextView mTvCertain;
    protected ImageView mImgCertain;
    private RelativeLayout mRlRootTop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        processCutout();

        //沉浸式---状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (isNeedDarkState()) {
            //android6.0以后可以对状态栏文字颜色和图标进行修改
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

        //强制竖屏
        if (isNeedPortrait()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        mLayoutInflater = this.getLayoutInflater();

        //模板模式完成初始化
        if (isNeedTitle()) {
            LinearLayout linearLayout=new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setLayoutParams(layoutParams);
            View inflateTitle = mLayoutInflater.inflate(R.layout.title_top, linearLayout, false);
            mImgReturn = (ImageView) inflateTitle.findViewById(R.id.img_return);
            mTvTitle = (TextView) inflateTitle.findViewById(R.id.tv_title);
            mTvCertain = (TextView) inflateTitle.findViewById(R.id.tv_certain);
            mImgCertain = (ImageView) inflateTitle.findViewById(R.id.img_certain);
            mRlRootTop = (RelativeLayout)inflateTitle.findViewById(R.id.rl_root_top);
            //获取状态栏高度设置padding
            int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                int result = this.getResources().getDimensionPixelSize(resourceId);
                mRlRootTop.setPadding(0,result,0,0);
            }

            View inflateContent = mLayoutInflater.inflate(getRootView(), linearLayout, false);

            linearLayout.addView(inflateTitle);
            linearLayout.addView(inflateContent);
            setContentView(linearLayout);
        }else{
            setContentView(getRootView());
        }

        mActivity=this;

        initView();
        initData();

    }

    /**
     * 是否需要暗系状态栏图标
     */
    private boolean isNeedDarkState() {
        return true;
    }

    /**
     * 刘海屏适配
     */
    private void processCutout() {
        if (Build.VERSION.SDK_INT>=28) {
            //全屏显示
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

            WindowManager.LayoutParams lp = getWindow().getAttributes();

            //隐藏刘海
//            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;
            //显示刘海
        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            //不处理
//        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT;
            getWindow().setAttributes(lp);
        }
    }

    /**
     * 是否强制竖屏
     * @return
     */
    public boolean isNeedPortrait(){
        return true;
    }

    /**
     * 禁止修改字体大小
     * @return
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }

    @Override
    protected void onStart() {
        super.onStart();
//        checkNet();
    }

    /**
     *  检查网络状态
     */
    public void checkNet(){
        int netState = NetStateUtils.getNetState(mActivity);
        if (netState==NetStateUtils.NETWORK_STATE_NONE) {
            NoneNetUtils.showDialog(mActivity);
        }
    }

    //初始化控件
    public abstract void initView();

    //初始化数据
    public abstract void initData();

    //获取页面布局
    public abstract int getRootView();

    //是否需要头部标题
    public boolean isNeedTitle(){
        return true;
    }

}
