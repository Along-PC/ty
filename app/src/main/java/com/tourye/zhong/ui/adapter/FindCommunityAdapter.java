package com.tourye.zhong.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tourye.zhong.Constants;
import com.tourye.zhong.R;
import com.tourye.zhong.base.BaseApplication;
import com.tourye.zhong.beans.DynamicListBean;
import com.tourye.zhong.beans.ThumbUpBean;
import com.tourye.zhong.net.HttpCallback;
import com.tourye.zhong.net.HttpUtils;
import com.tourye.zhong.ui.activity.ImageDetailActivity;
import com.tourye.zhong.utils.DateFormatUtils;
import com.tourye.zhong.utils.GlideCircleTransform;
import com.tourye.zhong.views.NineGridlayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by longlongren on 2018/9/27.
 * <p>
 * introduce:发现--社区适配器
 */

public class FindCommunityAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<DynamicListBean.DataBean> mDatas = new ArrayList<>();

    private int max_lines=5;//文字最多显示行数

    public FindCommunityAdapter(Context context, List<DynamicListBean.DataBean> datas) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDatas = datas;
    }

    public void setDatas(List<DynamicListBean.DataBean> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    public void addDatas(List<DynamicListBean.DataBean> datas) {
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final FindCommunityHolder findCommunityHolder;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.item_fragment_find_community, viewGroup, false);
            findCommunityHolder = new FindCommunityHolder(view);
            view.setTag(findCommunityHolder);
        } else {
            findCommunityHolder = (FindCommunityHolder) view.getTag();
        }
        //获取数据，开始赋值
        final DynamicListBean.DataBean dataBean = mDatas.get(i);
        //头像
        RequestOptions requestOptions = new RequestOptions().transform(new GlideCircleTransform(BaseApplication.mApplicationContext));
        Glide.with(BaseApplication.mApplicationContext).load(dataBean.getAvatar()).apply(requestOptions).into(findCommunityHolder.mImgItemFindCommunityHead);
        findCommunityHolder.mTvItemFindCommunityName.setText(dataBean.getNickname());

        //文字内容
        setTextContent(dataBean,findCommunityHolder);

        //发表时间
        String create_time = dataBean.getCreate_time();
        if (TextUtils.isEmpty(create_time)) {
            findCommunityHolder.mTvItemFindCommunityTime.setText("");
        } else {
            findCommunityHolder.mTvItemFindCommunityTime.setText(DateFormatUtils.format(create_time));
        }

        //图片数据
        final ArrayList<String> urls = dataBean.getImages();
        if (urls==null || urls.size()==0) {
            findCommunityHolder.mNineItemFindCommunity.setVisibility(View.GONE);
        }else{
            findCommunityHolder.mNineItemFindCommunity.setVisibility(View.VISIBLE);
            findCommunityHolder.mNineItemFindCommunity.setImagesData(urls);
            findCommunityHolder.mNineItemFindCommunity.setOnItemClickListener(new NineGridlayout.OnItemClickListener() {
                @Override
                public void onItemClick(int i) {
                    Intent intent = new Intent(mContext, ImageDetailActivity.class);
                    intent.putStringArrayListExtra("data", urls);
                    intent.putExtra("pos", i);
                    mContext.startActivity(intent);
                }
            });
        }

        //点赞数
        findCommunityHolder.mTvItemFindCommunityThumb.setText(dataBean.getThumb_up_count() + "");
        //点赞状态
        findCommunityHolder.mImgItemFindCommunityThumb.setSelected(dataBean.isAlready_thumb_up());
        final FindCommunityHolder finalFindCommunityHolder = findCommunityHolder;
        findCommunityHolder.mRlItemFindCommunityThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //当发出网络请求，在没有拿到网络请求结果时，取消监听
                finalFindCommunityHolder.mRlItemFindCommunityThumb.setOnClickListener(null);
                if (finalFindCommunityHolder.mImgItemFindCommunityThumb.isSelected()) {
                    cancel_thumb_up(dataBean, finalFindCommunityHolder);
                } else {
                    thumb_up(dataBean, finalFindCommunityHolder);
                }
            }
        });

        return view;
    }



    //取消点赞
    public void cancel_thumb_up(final DynamicListBean.DataBean dataBean, final FindCommunityHolder holder){
        Map<String,String> map=new HashMap<>();
        map.put("id",dataBean.getId()+"");
        HttpUtils.getInstance().post(Constants.THUMB_UP_CANCEL, map, new HttpCallback<ThumbUpBean>() {
            @Override
            public void onSuccessExecute(ThumbUpBean thumbUpBean) {
                if (thumbUpBean.getStatus()==0) {
                    if (thumbUpBean.isData()) {
                        holder.mImgItemFindCommunityThumb.setSelected(false);
                        dataBean.setThumb_up_count(dataBean.getThumb_up_count()-1);
                        dataBean.setAlready_thumb_up(false);
                        //拿到网络请求结果，重新添加监听
                        holder.mTvItemFindCommunityThumb.setText(dataBean.getThumb_up_count()+"");
                        holder.mImgItemFindCommunityThumb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                holder.mImgItemFindCommunityThumb.setOnClickListener(null);
                                if (holder.mImgItemFindCommunityThumb.isSelected()) {
                                    cancel_thumb_up(dataBean,holder);
                                }else{
                                    thumb_up(dataBean,holder);
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    //点赞
    public void thumb_up(final DynamicListBean.DataBean dataBean, final FindCommunityHolder holder){
        Map<String,String> map=new HashMap<>();
        map.put("id",dataBean.getId()+"");
        HttpUtils.getInstance().post(Constants.THUMB_UP, map, new HttpCallback<ThumbUpBean>() {
            @Override
            public void onSuccessExecute(ThumbUpBean thumbUpBean) {
                if (thumbUpBean.getStatus()==0) {
                    if (thumbUpBean.isData()) {
                        holder.mImgItemFindCommunityThumb.setSelected(true);
                        dataBean.setAlready_thumb_up(true);
                        int thumb_up_count = dataBean.getThumb_up_count();
                        dataBean.setThumb_up_count(thumb_up_count+1);
                        //拿到网络请求结果，重新添加监听
                        holder.mTvItemFindCommunityThumb.setText(dataBean.getThumb_up_count()+"");
                        holder.mImgItemFindCommunityThumb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                holder.mImgItemFindCommunityThumb.setOnClickListener(null);
                                if (holder.mImgItemFindCommunityThumb.isSelected()) {
                                    cancel_thumb_up(dataBean,holder);
                                }else{
                                    thumb_up(dataBean,holder);
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * 设置文字内容
     * @param dataBean
     * @param findCommunityHolder
     */
    public void setTextContent(final DynamicListBean.DataBean dataBean, final FindCommunityHolder findCommunityHolder) {
        String content = dataBean.getContent();
        if (TextUtils.isEmpty(content)) {
            findCommunityHolder.mTvItemFindCommunityContent.setVisibility(View.GONE);
            findCommunityHolder.mTvFindCommunityContentState.setVisibility(View.GONE);
        } else {
            //首次初始化
            if (dataBean.getContent_state() == 0) {
                findCommunityHolder.mTvItemFindCommunityContent.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        //这个回掉会调用多次，获取玩行数后记得注销监听
                        findCommunityHolder.mTvItemFindCommunityContent.getViewTreeObserver().removeOnPreDrawListener(this);
                        //如果内容显示的行数大于限定显示行数
                        if (findCommunityHolder.mTvItemFindCommunityContent.getLineCount() > max_lines) {
                            findCommunityHolder.mTvItemFindCommunityContent.setMaxLines(max_lines);//设置最大显示行数
                            findCommunityHolder.mTvFindCommunityContentState.setVisibility(View.VISIBLE);//让其显示全文的文本框状态为显示
                            findCommunityHolder.mTvFindCommunityContentState.setText("[全文]");//设置其文字为全文
                            dataBean.setContent_state(1);
                        } else {
                            findCommunityHolder.mTvFindCommunityContentState.setVisibility(View.GONE);//显示全文隐藏
                            findCommunityHolder.mTvItemFindCommunityContent.setMaxLines(max_lines);
                            dataBean.setContent_state(3);
                        }
                        return true;
                    }
                });
            } else {
//            如果之前已经初始化过了，则使用保存的状态，无需在获取一次
                switch (dataBean.getContent_state()) {
                    case 3:
                        findCommunityHolder.mTvFindCommunityContentState.setVisibility(View.GONE);
                        findCommunityHolder.mTvItemFindCommunityContent.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        findCommunityHolder.mTvItemFindCommunityContent.setVisibility(View.VISIBLE);
                        findCommunityHolder.mTvItemFindCommunityContent.setMaxLines(max_lines);
                        findCommunityHolder.mTvFindCommunityContentState.setVisibility(View.VISIBLE);
                        findCommunityHolder.mTvFindCommunityContentState.setText("[全文]");
                        break;
                    case 2:
                        findCommunityHolder.mTvItemFindCommunityContent.setVisibility(View.VISIBLE);
                        findCommunityHolder.mTvItemFindCommunityContent.setMaxLines(Integer.MAX_VALUE);
                        findCommunityHolder.mTvFindCommunityContentState.setVisibility(View.VISIBLE);
                        findCommunityHolder.mTvFindCommunityContentState.setText("[收起]");
                        break;
                }
            }
            findCommunityHolder.mTvItemFindCommunityContent.setText(content);
            findCommunityHolder.mTvFindCommunityContentState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int content_state = dataBean.getContent_state();
                    if (content_state == 1) {
                        findCommunityHolder.mTvItemFindCommunityContent.setMaxLines(Integer.MAX_VALUE);
                        findCommunityHolder.mTvFindCommunityContentState.setText("[收起]");
                        dataBean.setContent_state(2);
                    } else if (content_state == 2) {
                        findCommunityHolder.mTvItemFindCommunityContent.setMaxLines(max_lines);
                        findCommunityHolder.mTvFindCommunityContentState.setText("[全文]");
                        dataBean.setContent_state(1);
                    }
                }
            });
        }
    }

    public class FindCommunityHolder {
        private ImageView mImgItemFindCommunityHead;
        private TextView mTvItemFindCommunityName;
        private TextView mTvItemFindCommunityTime;
        private RelativeLayout mRlItemFindCommunityShare;
        private TextView mTvItemFindCommunityShare;
        private RelativeLayout mRlItemFindCommunityComment;
        private TextView mTvItemFindCommunityComment;
        private RelativeLayout mRlItemFindCommunityThumb;
        public TextView mTvItemFindCommunityThumb;
        private TextView mTvItemFindCommunityContent;
        private ImageView mImgItemFindCommunityThumb;
        private NineGridlayout mNineItemFindCommunity;//九宫格图片
        private TextView mTvFindCommunityContentState;



        public FindCommunityHolder(View view) {
            mImgItemFindCommunityHead = (ImageView) view.findViewById(R.id.img_item_find_community_head);
            mTvItemFindCommunityName = (TextView) view.findViewById(R.id.tv_item_find_community_name);
            mTvItemFindCommunityTime = (TextView) view.findViewById(R.id.tv_item_find_community_time);
            mRlItemFindCommunityShare = (RelativeLayout) view.findViewById(R.id.rl_item_find_community_share);
            mTvItemFindCommunityShare = (TextView) view.findViewById(R.id.tv_item_find_community_share);
            mRlItemFindCommunityComment = (RelativeLayout) view.findViewById(R.id.rl_item_find_community_comment);
            mTvItemFindCommunityComment = (TextView) view.findViewById(R.id.tv_item_find_community_comment);
            mRlItemFindCommunityThumb = (RelativeLayout) view.findViewById(R.id.rl_item_find_community_thumb);
            mTvItemFindCommunityThumb = (TextView) view.findViewById(R.id.tv_item_find_community_thumb);
            mTvItemFindCommunityContent = (TextView) view.findViewById(R.id.tv_item_find_community_content);
            mImgItemFindCommunityThumb = (ImageView) view.findViewById(R.id.img_item_find_community_thumb);
            mNineItemFindCommunity = (NineGridlayout) view.findViewById(R.id.nine_item_find_community);
            mTvFindCommunityContentState = (TextView) view.findViewById(R.id.tv_find_community_content_state);

        }
    }
}
