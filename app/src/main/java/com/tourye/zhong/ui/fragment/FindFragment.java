package com.tourye.zhong.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;

import com.tourye.zhong.R;
import com.tourye.zhong.base.BaseFragment;
import com.tourye.zhong.ui.activity.CreateDynamicActivity;
import com.tourye.zhong.ui.adapter.FindVpAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * FindFragment
 * author:along
 * 2018/9/19 下午2:32
 *
 * 描述:发现页面
 */

public class FindFragment extends BaseFragment{
    private ViewPager mVpFragmentFind;
    private MagicIndicator magicIndicatorFragmentFind;

    private Fragment mFindCommunity;
    private Fragment mFindArticle;
    private Fragment mFindVideo;
    private Fragment mFindPhotos;
    private List<Fragment> mFragments=new ArrayList<>();
    private FragmentManager mFragmentManager;
    private FindVpAdapter mFindVpAdapter;
    private List<String> mTitles=new ArrayList<>();

    @Override
    public void initView(View view) {
        mVpFragmentFind = (ViewPager) view.findViewById(R.id.vp_fragment_find);
        magicIndicatorFragmentFind = (MagicIndicator) view.findViewById(R.id.magic_indicator_fragment_find);

        mTvTitle.setText("发现");
        mImgCertain.setVisibility(View.VISIBLE);
        mImgCertain.setBackgroundResource(R.drawable.icon_find_create);

        mImgCertain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, CreateDynamicActivity.class));
            }
        });

    }

    @Override
    public void initData() {
        super.initData();


        initVp();

        initIndicator();
    }



    /**
     * 初始化viewpager
     */
    private void initVp() {
        mTitles.clear();
        mTitles.add("社区");
        mTitles.add("文章");
        mTitles.add("视频");
        mTitles.add("相册");
        mFindCommunity=new FindCommunityFragment();
        mFindArticle=new FindArticleFragment();
        mFindVideo=new FindVideoFragment();
        mFindPhotos=new FindPhotosFragment();
        mFragments.add(mFindCommunity);
        mFragments.add(mFindArticle);
        mFragments.add(mFindVideo);
        mFragments.add(mFindPhotos);
        mFragmentManager = this.getChildFragmentManager();
        mFindVpAdapter = new FindVpAdapter(mFragmentManager);
        mFindVpAdapter.setFragments(mFragments);
        mVpFragmentFind.setAdapter(mFindVpAdapter);

        mVpFragmentFind.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position!=0) {
                    mImgCertain.setVisibility(View.GONE);
//                    mImgCertain.setBackgroundResource(R.drawable.icon_find_create);
                }else{
                    mImgCertain.setVisibility(View.VISIBLE);
                    mImgCertain.setBackgroundResource(R.drawable.icon_find_create);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    /**
     * 初始化导航栏
     */
    private void initIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(mActivity);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mTitles == null ? 0 : mTitles.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(getResources().getColor(R.color.color_font_black));
                colorTransitionPagerTitleView.setSelectedColor(Color.parseColor("#CC1C24"));
                colorTransitionPagerTitleView.setText(mTitles.get(index));
                colorTransitionPagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mVpFragmentFind.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(Color.parseColor("#CC1C24"));
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                return indicator;
            }
        });
        magicIndicatorFragmentFind.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicatorFragmentFind, mVpFragmentFind);

    }

    @Override
    public int getRootView() {
        return R.layout.fragment_find;
    }

    @Override
    public boolean isNeedTitle() {
        return true;
    }
}
