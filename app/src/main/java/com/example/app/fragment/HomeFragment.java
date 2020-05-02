package com.example.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.app.EnjoyshopApplication;
import com.example.app.R;
import com.example.app.activity.GoodsListActivity;
import com.example.app.activity.SearchActivity;
import com.example.app.adapter.HomeCatgoryAdapter;
import com.example.app.bean.BannerBean;
import com.example.app.bean.HomeCampaignBean;
import com.example.app.contants.Contants;
import com.example.app.contants.HttpContants;
import com.example.app.helper.DividerItemDecortion;
import com.example.app.widget.EnjoyshopToolBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jelly.thor.okhttputils.OkHttpUtils;
import com.jelly.thor.okhttputils.callback.StringCallback;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;


public class HomeFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.toolbar)
    EnjoyshopToolBar mToolBar;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    //@BindView(R.id.banner)
    //Banner mBanner;

    private Banner mBanner;
    View viewHeader;
    private Gson gson = new Gson();
    private List<String> images = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private List<HomeCampaignBean> datas  = new ArrayList<>();

    private HomeCatgoryAdapter mAdatper;

    @Override
    protected void init() {

        viewHeader = LayoutInflater.from(getActivity()).inflate(R.layout.header_fragment_home,
                (ViewGroup) mRecyclerView.getParent(), false);
        mBanner = viewHeader.findViewById(R.id.banner);

        initView();

        requestBannerData();     //請求輪播圖數據
        requestCampaignData();     //請求商品詳情數據
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.fragment_home;
    }

    //跳轉到搜索界面
    @Override
    public void onClick(View v) {
        startActivity(new Intent(getContext(), SearchActivity.class));
    }

    @Override
    public void onStart() {
        super.onStart();
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
    }

    private void initView() {
        mToolBar.setOnClickListener(this);
        //設置banner樣式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //設置圖片加載器
        mBanner.setImageLoader(new GlideImageLoader());
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(EnjoyshopApplication.sContext).load(path).into(imageView);
        }
    }

    /**
     * 請求輪播圖的數據
     */
    private void requestBannerData() {
        OkHttpUtils.get().url(HttpContants.HOME_BANNER_URL)
                .addParam("type", "1")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(int code, String response, int id) {
                Type collectionType = new TypeToken<Collection<BannerBean>>() {
                }.getType();
                Collection<BannerBean> enums = gson.fromJson(response, collectionType);
                Iterator<BannerBean> iterator = enums.iterator();
                titles.clear();
                images.clear();
                while (iterator.hasNext()) {
                    BannerBean bean = iterator.next();
                    titles.add(bean.getName());
                    images.add(bean.getImgUrl());
                }
                setBannerData();
            }
        });
    }

    /**
     * 輪播圖數據
     */
    private void setBannerData() {
        //設置圖片集合
        mBanner.setImages(images);
        //設置標題集合（當banner樣式有顯示title時）
        mBanner.setBannerTitles(titles);

        //設置指示器位置（當banner模式中有指示器時）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        mBanner.start();
    }

    /**
     * 商品分類數據
     */
    private void requestCampaignData() {
        OkHttpUtils.get().url(HttpContants.HOME_CAMPAIGN_URL)
                .addParam("type", "1")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(int code, String response, int id) {
                Type collectionType = new TypeToken<Collection<HomeCampaignBean>>() {
                }.getType();
                Collection<HomeCampaignBean> enums = gson.fromJson(response,
                        collectionType);
                Iterator<HomeCampaignBean> iterator = enums.iterator();
                datas.clear();
                while (iterator.hasNext()) {
                    HomeCampaignBean bean = iterator.next();
                    datas.add(bean);
                }
                setRecyclerViewData();
            }
        });
    }

    /**
     * 首頁商品數據
     */

    private Long defaultId = 0L;

    private void setRecyclerViewData() {

        for (int i = 0; i < datas.size(); i++) {
            if (i % 2 == 0) {
                //左邊樣式的item
                datas.get(i).setItemType(HomeCampaignBean.ITEM_TYPE_LEFT);
            } else {
                //右邊樣式的item
                datas.get(i).setItemType(HomeCampaignBean.ITEM_TYPE_RIGHT);
            }
        }

        mAdatper = new HomeCatgoryAdapter(datas);
        mRecyclerView.setAdapter(mAdatper);
        mRecyclerView.addItemDecoration(new DividerItemDecortion());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdatper.addHeaderView(viewHeader);

        mAdatper.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomeCampaignBean campaign = (HomeCampaignBean) adapter.getData().get(position);
                Intent intent = new Intent(getContext(), GoodsListActivity.class);
                intent.putExtra(Contants.COMPAINGAIN_ID, campaign.getId());
                startActivity(intent);
            }
        });

        mAdatper.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                HomeCampaignBean bean = (HomeCampaignBean) adapter.getData().get(position);
                Long oneId = bean.getCpOne().getId();
                Long twoId = bean.getCpTwo().getId();
                Long threeId = bean.getCpThree().getId();

                switch (view.getId()){
                    case R.id.imgview_big:
                        defaultId=oneId;
                        break;
                    case R.id.imgview_small_top:
                        defaultId=twoId;
                        break;
                    case R.id.imgview_small_bottom:
                        defaultId=threeId;
                        break;
                }

                Intent intent = new Intent(getContext(), GoodsListActivity.class);
                intent.putExtra(Contants.COMPAINGAIN_ID, defaultId);
                startActivity(intent);
            }
        });
    }
}
