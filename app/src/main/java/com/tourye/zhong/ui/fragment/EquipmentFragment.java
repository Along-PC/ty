package com.tourye.zhong.ui.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tourye.zhong.R;
import com.tourye.zhong.base.BaseFragment;

/**
 * EquipmentFragment
 * author:along
 * 2018/9/19 下午2:33
 * <p>
 * 描述:装备页面
 */

public class EquipmentFragment extends BaseFragment {
    private Button mBtFragmentEquipment;


    @Override
    public void initView(View view) {
        mBtFragmentEquipment = (Button) view.findViewById(R.id.bt_fragment_equipment);
        mBtFragmentEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invokeWXPay();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        registerWX();
    }

    @Override
    public int getRootView() {
        return R.layout.fragment_equipment;
    }


    private IWXAPI api;

    public void registerWX() {
        String app_id = "";

        api = WXAPIFactory.createWXAPI(this.getContext(), null);
        api.registerApp(app_id);
    }

    public void invokeWXPay() {
        if (!api.isWXAppInstalled()) {
            Toast.makeText(mActivity, "你没有安装微信", Toast.LENGTH_SHORT).show();
            return;
        }

        PayReq request = new PayReq();
        request.appId = "wxd930ea5d5a258f4f";
        request.partnerId = "1900000109";
        request.prepayId = "1101000000140415649af9fc314aa427";
        request.packageValue = "Sign=WXPay";
        request.nonceStr = "1101000000140429eb40476f8896f4c9";
        request.timeStamp = "1398746574";
        request.sign = "7FFECB600D7157C5AA49810D2D8F28BC2811827B";
        api.sendReq(request);

    }

}
