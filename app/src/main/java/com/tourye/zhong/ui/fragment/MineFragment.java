package com.tourye.zhong.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tourye.zhong.BuildConfig;
import com.tourye.zhong.Constants;
import com.tourye.zhong.R;
import com.tourye.zhong.base.BaseApplication;
import com.tourye.zhong.base.BaseFragment;
import com.tourye.zhong.beans.UserAccountBean;
import com.tourye.zhong.beans.UserBasicBean;
import com.tourye.zhong.net.HttpCallback;
import com.tourye.zhong.net.HttpUtils;
import com.tourye.zhong.ui.activity.CommonWebActivity;
import com.tourye.zhong.ui.activity.SettingActivity;
import com.tourye.zhong.ui.activity.UpdateHeadActivity;
import com.tourye.zhong.utils.GlideCircleTransform;
import com.tourye.zhong.utils.SaveUtil;
import com.tourye.zhong.views.dialogs.ModifyNameDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * MineFragment
 * author:along
 * 2018/9/19 下午2:33
 * <p>
 * 描述:我的页面
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {

    private ImageView mImgFragmentMineHead;
    private TextView mTvFragmentMineName;
    private TextView mTvFragmentMineId;
    private TextView mTvFragmentMineGoldCount;//金币
    private TextView mTvFragmentMineGold;
    private TextView mTvFragmentMineOverageCount;//余额
    private TextView mTvFragmentMineOverage;
    private TextView mTvFragmentMineVoucherCount;//代金劵
    private TextView mTvFragmentMineVoucher;
    private RelativeLayout mRlFragmentMineSupport;//我的支持
    private RelativeLayout mRlFragmentMineRecord;//完赛记录
    private LinearLayout mLlFragmentMinePoster;//海报生成
    private RelativeLayout mRlFragmentMineDynamic;//我的动态
    private LinearLayout mLlFragmentMineRebate;//返利
    private LinearLayout mLlFragmentMineSetting;//设置
    private LinearLayout mLlFragmentMineGoldCount;//我的金币
    private LinearLayout mLlFragmentMineOverageCount;//我的账户
    private LinearLayout mLlFragmentMineVoucherCount;//我的代金劵
    private LinearLayout mLlFragmentMineOrderCrowdfund;//众筹订单
    private LinearLayout mLlFragmentMineOrderGift;//礼品订单
    private LinearLayout mLlFragmentMineOrderOther;//其他订单


    @Override
    public void initView(View view) {

        mImgFragmentMineHead = (ImageView) view.findViewById(R.id.img_fragment_mine_head);
        mTvFragmentMineName = (TextView) view.findViewById(R.id.tv_fragment_mine_name);
        mTvFragmentMineId = (TextView) view.findViewById(R.id.tv_fragment_mine_id);
        mTvFragmentMineGoldCount = (TextView) view.findViewById(R.id.tv_fragment_mine_gold_count);
        mTvFragmentMineGold = (TextView) view.findViewById(R.id.tv_fragment_mine_gold);
        mTvFragmentMineOverageCount = (TextView) view.findViewById(R.id.tv_fragment_mine_overage_count);
        mTvFragmentMineOverage = (TextView) view.findViewById(R.id.tv_fragment_mine_overage);
        mTvFragmentMineVoucherCount = (TextView) view.findViewById(R.id.tv_fragment_mine_voucher_count);
        mTvFragmentMineVoucher = (TextView) view.findViewById(R.id.tv_fragment_mine_voucher);
        mRlFragmentMineSupport = (RelativeLayout) view.findViewById(R.id.rl_fragment_mine_support);
        mRlFragmentMineRecord = (RelativeLayout) view.findViewById(R.id.rl_fragment_mine_record);
        mLlFragmentMinePoster = (LinearLayout) view.findViewById(R.id.ll_fragment_mine_poster);
        mRlFragmentMineDynamic = (RelativeLayout) view.findViewById(R.id.rl_fragment_mine_dynamic);
        mLlFragmentMineRebate = (LinearLayout) view.findViewById(R.id.ll_fragment_mine_rebate);
        mLlFragmentMineSetting = (LinearLayout) view.findViewById(R.id.ll_fragment_mine_setting);
        mLlFragmentMineGoldCount = (LinearLayout) view.findViewById(R.id.ll_fragment_mine_gold_count);
        mLlFragmentMineOverageCount = (LinearLayout) view.findViewById(R.id.ll_fragment_mine_overage_count);
        mLlFragmentMineVoucherCount = (LinearLayout) view.findViewById(R.id.ll_fragment_mine_voucher_count);
        mLlFragmentMineOrderCrowdfund = (LinearLayout) view.findViewById(R.id.ll_fragment_mine_order_crowdfund);
        mLlFragmentMineOrderGift = (LinearLayout) view.findViewById(R.id.ll_fragment_mine_order_gift);
        mLlFragmentMineOrderOther = (LinearLayout) view.findViewById(R.id.ll_fragment_mine_order_other);

        mTvTitle.setText("个人中心");
        mImgCertain.setVisibility(View.VISIBLE);
        mImgCertain.setBackgroundResource(R.drawable.icon_find_comment);

        mImgFragmentMineHead.setOnClickListener(this);
        mLlFragmentMineSetting.setOnClickListener(this);
        mLlFragmentMineGoldCount.setOnClickListener(this);
        mLlFragmentMineOverageCount.setOnClickListener(this);
        mLlFragmentMineVoucherCount.setOnClickListener(this);
        mRlFragmentMineSupport.setOnClickListener(this);
        mLlFragmentMineRebate.setOnClickListener(this);
        mRlFragmentMineRecord.setOnClickListener(this);
        mTvFragmentMineName.setOnClickListener(this);
        mLlFragmentMineOrderCrowdfund.setOnClickListener(this);
        mLlFragmentMineOrderGift.setOnClickListener(this);
        mLlFragmentMineOrderOther.setOnClickListener(this);

    }

    @Override
    public void initData() {
        super.initData();
        getPersonInfo();
        getUserAccount();
    }

    /**
     * 获取用户账户信息
     */
    public void getUserAccount() {
        Map<String, String> map = new HashMap<>();
        HttpUtils.getInstance().get(Constants.USER_ACCOUNT, map, new HttpCallback<UserAccountBean>() {
            @Override
            public void onSuccessExecute(UserAccountBean userAccountBean) {
                if (userAccountBean.getStatus() == 0) {
                    UserAccountBean.DataBean data = userAccountBean.getData();
                    if (data != null) {
                        mTvFragmentMineGoldCount.setText(data.getCoin() + "");
                        mTvFragmentMineOverageCount.setText(data.getFund() + "");
                        mTvFragmentMineVoucherCount.setText(data.getCoupon() + "");
                    }
                }
            }
        });
    }

    /**
     * 获取用户基本信息
     */
    public void getPersonInfo() {
        Map<String, String> map = new HashMap<>();
        HttpUtils.getInstance().get(Constants.USER_BASIC_INFO, map, new HttpCallback<UserBasicBean>() {
            @Override
            public void onSuccessExecute(UserBasicBean userBasicBean) {
                if (userBasicBean.getStatus() == 0) {
                    UserBasicBean.DataBean data = userBasicBean.getData();
                    if (data != null) {
                        mTvFragmentMineName.setText(data.getNickname());
                        RequestOptions requestOptions = new RequestOptions().transform(new GlideCircleTransform(BaseApplication.mApplicationContext));
                        Glide.with(BaseApplication.mApplicationContext).load(data.getAvatar()).apply(requestOptions).into(mImgFragmentMineHead);
                        mTvFragmentMineId.setText("id:" + data.getId());
                    }
                }
            }
        });
    }

    @Override
    public int getRootView() {
        return R.layout.fragment_mine;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_fragment_mine_head:
                startActivity(new Intent(mActivity, UpdateHeadActivity.class));
                break;
            case R.id.tv_fragment_mine_name:
                ModifyNameDialog modifyNameDialog = new ModifyNameDialog(mActivity);
                modifyNameDialog.show();
                modifyNameDialog.setRenameCallback(new ModifyNameDialog.RenameCallback() {
                    @Override
                    public void rename(String name) {
                        mTvFragmentMineName.setText(name);
                    }
                });
                break;
            case R.id.ll_fragment_mine_setting:
                startActivity(new Intent(mActivity, SettingActivity.class));
                break;
            case R.id.ll_fragment_mine_gold_count:
                goToWeb("#/shop", "我的金币");
                break;
            case R.id.ll_fragment_mine_overage_count:
                goToWeb("#/user/account", "我的账户");
                break;
            case R.id.ll_fragment_mine_voucher_count:
                goToWeb("#/voucher", "流水明细");
                break;
            case R.id.rl_fragment_mine_support:
                goToWeb("#/supported", "我的支持");
                break;
            case R.id.ll_fragment_mine_rebate:
                goToWeb("#/rebate/account", "返利");
                break;
            case R.id.rl_fragment_mine_record:
                goToWeb("#/matchRecord", "完赛记录");
                break;
            case R.id.ll_fragment_mine_order_crowdfund:
                goToWeb("#/myCrowdfundingNew", "众筹订单");
                break;
            case R.id.ll_fragment_mine_order_gift:
                goToWeb("#/gift/order", "礼品订单");
                break;
            case R.id.ll_fragment_mine_order_other:
                goToWeb("#/order/myOrderNew", "其他订单");
                break;
        }

    }

    /**
     * 跳转web页面显示
     *
     * @param content   页面链接拼接参数
     * @param title   web页面标题
     */
    public void goToWeb(String content, String title) {
        String authorization = SaveUtil.getString("Authorization", "");
        if (TextUtils.isEmpty(authorization)) {
            return;
        }
        Intent intent = new Intent(mActivity, CommonWebActivity.class);
        String url = BuildConfig.WEB_URL + authorization + content;
        intent.putExtra("data", url);
        intent.putExtra("title", title);
        mActivity.startActivity(intent);
    }

    @Override
    public boolean isNeedTitle() {
        return true;
    }
}
