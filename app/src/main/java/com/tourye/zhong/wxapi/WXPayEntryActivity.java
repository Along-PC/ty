package com.tourye.zhong.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tourye.zhong.R;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wx_entry);

//    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);

    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);

        setIntent(intent);

        api.handleIntent(intent, this);

    }

    @Override
    public void onReq(BaseReq req) {
        Log.e("along","进来了");
    }

    @Override
    public void onResp(BaseResp resp) {

        int result = 0;

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
//			result = R.string.errcode_success;
                Log.e("along","ok");
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
//			result = R.string.errcode_cancel;
                Log.e("along","ERR_USER_CANCEL");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
//			result = R.string.errcode_deny;
                Log.e("along","ERR_AUTH_DENIED");
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
//			result = R.string.errcode_unsupported;
                Log.e("along","ERR_UNSUPPORT");
                break;
            default:
//			result = R.string.errcode_unknown;
                Log.e("along","default");
                break;
        }

    }

}
