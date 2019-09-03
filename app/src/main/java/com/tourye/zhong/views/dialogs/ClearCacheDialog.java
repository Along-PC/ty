package com.tourye.zhong.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tourye.zhong.R;

/**
 * Created by longlongren on 2018/11/22.
 * <p>
 * introduce:是否清除缓存弹框
 */

public class ClearCacheDialog extends Dialog {
    private final Context mContext;
    private TextView mTvDialogClearCacheCancel;
    private TextView mTvDialogClearCacheCertain;


    public ClearCacheDialog(@NonNull Context context) {
        super(context);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.dialog_clear_cache);

        mContext = context;

        mTvDialogClearCacheCancel = (TextView) findViewById(R.id.tv_dialog_clear_cache_cancel);
        mTvDialogClearCacheCertain = (TextView) findViewById(R.id.tv_dialog_clear_cache_certain);
        mTvDialogClearCacheCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mTvDialogClearCacheCertain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Looper.myLooper() == Looper.getMainLooper()) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.get(mContext).clearDiskCache();
//                        BusUtil.getBus().post(new GlideCacheClearSuccessEvent());
                            }
                        }).start();
                    } else {
                        Glide.get(mContext).clearDiskCache();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dismiss();
            }
        });
    }


}
