package com.example.app.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.app.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GuideActivity extends BaseActivity {

    @BindView(R.id.viewpager) ViewPager mViewPager;
    @BindView(R.id.btn_start) Button mBtnStart;

    //获取图片资源
    int[] imgRes = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3, R
            .drawable.guide_4};
    private List<View> mViewList = new ArrayList<>();

    @Override
    protected void init() {
        initData();

        MyPagerAdapter adapter = new MyPagerAdapter();
        mViewPager.setAdapter(adapter);

        mViewPager.setOnPageChangeListener(new PagePositionLister());
    }

    /**
     * 初始化數據
     */
    private void initData() {

        for (int i = 0; i < imgRes.length; i++) {
            View inflate = getLayoutInflater().inflate(R.layout.guide_item, null);
            ImageView ivGuide = (ImageView) inflate.findViewById(R.id.iv_guide);
            ivGuide.setBackgroundResource(imgRes[i]);
            mViewList.add(inflate);
        }
    }

    @OnClick(R.id.btn_start)
    public void jumpActivity(View view) {
        startActivity(new Intent(GuideActivity.this, MainActivity.class));
        finish();
    }

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mViewList == null ? 0 : mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mViewList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));
        }
    }

    private class PagePositionLister implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //如果滑動到最後一張,顯示按鈕
            if (position == mViewList.size() - 1) {
                mBtnStart.setVisibility(View.VISIBLE);
            } else {
                mBtnStart.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_guide;
    }
}
