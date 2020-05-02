package com.example.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.app.EnjoyshopApplication;
import com.example.app.R;
import com.example.app.activity.GoodsDetailsActivity;
import com.example.app.adapter.CategoryAdapter;
import com.example.app.adapter.SecondGoodsAdapter;
import com.example.app.bean.Category;
import com.example.app.bean.HotGoods;
import com.example.app.contants.HttpContants;
import com.example.app.service.LocationService;
import com.example.app.utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jelly.thor.okhttputils.OkHttpUtils;
import com.jelly.thor.okhttputils.callback.StringCallback;
import com.sunfusheng.marqueeview.MarqueeView;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

import static com.example.app.EnjoyshopApplication.getApplication;

/**
 * <pre>
 *     author : 高勤
  *     e-mail : 984992087@qq.com
  *     time   : 2017/08/08
  *     desc   : 分類fragment
  *     version: 1.0
 * </pre>
 */

public class CategoryFragment extends BaseFragment {

    private List<String> mVFMessagesList; //上下輪播的信息
    private List<Category> categoryFirst = new ArrayList<>();   //一級菜單
    private CategoryAdapter mCategoryAdapter;  //一級菜單
    @BindView(R.id.recyclerview_category) RecyclerView mRecyclerView;

    private SecondGoodsAdapter mSecondGoodsAdapter;   //二級菜單
    @BindView(R.id.recyclerview_wares) RecyclerView mRecyclerviewWares;

    private Gson mGson = new Gson();

    private boolean isclick = false;

    private int currPage  = 1;     //當前是第幾頁
    private int totalPage = 1;    //一共有多少頁
    private int pageSize  = 10;     //每頁數目

    private List<HotGoods.ListBean> datas;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE   = 2;
    private int state = STATE_NORMAL;       //正常情况

    @BindView(R.id.vf_hotmessage) MarqueeView mVfHotMessage;

    private LocationService locationService;
    private String          provinceName; //省份
    private String          cityName;     //城市名
    private String          dayWeather;
    private String          nightWeather;
    @BindView(R.id.tv_city) TextView mCityName;
    @BindView(R.id.tv_day_weather) TextView mDayWeather;
    @BindView(R.id.tv_night_weather) TextView mNightWeather;

    @Override
    protected void init() {
        mVFMessagesList = new ArrayList<>();

        requestCategoryData();      // 熱門商品數據
        requestMessageData();        //輪播信息數據
        getLocation();            //獲取當前城市的位置
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.fragment_category;
    }

    private void requestCategoryData() {
        OkHttpUtils.get().url(HttpContants.CATEGORY_LIST).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(int code, String response, int id) {
                        LogUtil.e("分類一級", response + "", true);

                        Type collectionType = new TypeToken<Collection<Category>>() {
                        }.getType();
                        Collection<Category> enums = mGson.fromJson(response, collectionType);
                        Iterator<Category> iterator = enums.iterator();
                        while (iterator.hasNext()) {
                            Category bean = iterator.next();
                            categoryFirst.add(bean);
                        }
                        showCategoryData();
                        defaultClick();
                    }
                });
    }

    /**
     * 展示一級菜單數據
     */
    private void showCategoryData() {
        mCategoryAdapter = new CategoryAdapter(categoryFirst);

        mCategoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Category category = (Category) adapter.getData().get(position);
                int id = category.getId();
                String name = category.getName();
                isclick = true;
                defaultClick();
                requestWares(id);
            }
        });

        mRecyclerView.setAdapter(mCategoryAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
    }

    private void defaultClick() {
        //默認選中第0個
        if (!isclick) {
            Category category = categoryFirst.get(0);
            int id = category.getId();
            requestWares(id);
        }
    }

    /**
     * 二級菜單數據
     *
     * @param firstCategorId 一級菜單的firstCategorId
     */
    private void requestWares(int firstCategorId) {
        //尚未新增商品, 目前只有熱門推薦, 所以 firstCategorId 先設為 1
        firstCategorId = 1;
        String url = HttpContants.WARES_LIST + "?categoryId=" + firstCategorId + "&curPage=" +
                currPage + "&pageSize=" + pageSize;

        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onResponse(int code, String response, int id) {
                LogUtil.e("二級菜單", response + "", true);

                HotGoods hotGoods = mGson.fromJson(response, HotGoods.class);
                totalPage = hotGoods.getTotalPage();
                currPage = hotGoods.getCurrentPage();
                datas = hotGoods.getList();

                showData();
            }
        });
    }

    /**
     * 展示二級菜單的數據
     */
    private void showData() {
        switch (state) {
            case STATE_NORMAL:
                mSecondGoodsAdapter = new SecondGoodsAdapter(datas);

                mSecondGoodsAdapter.setOnItemClickListener(new BaseQuickAdapter
                        .OnItemClickListener() {

                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        HotGoods.ListBean listBean = (HotGoods.ListBean) adapter.getData().get
                                (position);

                        Intent intent = new Intent(getContext(), GoodsDetailsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("itemClickGoods", (Serializable) listBean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

                mRecyclerviewWares.setAdapter(mSecondGoodsAdapter);
                mRecyclerviewWares.setLayoutManager(new GridLayoutManager(getContext(), 2));
                mRecyclerviewWares.setItemAnimator(new DefaultItemAnimator());
                mRecyclerviewWares.addItemDecoration(new DividerItemDecoration(getContext(),
                        DividerItemDecoration.HORIZONTAL));

                break;

            //case STATE_REFREH:
            //  mAdatper.clearData();
            //   mAdatper.addData(datas);
            //   mRecyclerView.scrollToPosition(0);
            //   mRefreshLaout.finishRefresh();
            //break;
            //
            //case STATE_MORE:
            //   mAdatper.addData(mAdatper.getDatas().size(), datas);
            //   mRecyclerView.scrollToPosition(mAdatper.getDatas().size());
            //   mRefreshLaout.finishRefreshLoadMore();
            //break;
        }
    }

    private void requestMessageData() {
        mVFMessagesList.add("開學季,憑錄取通知書購手機6折起");
        mVFMessagesList.add("都世麗人內衣今晚20點最低10元開搶");
        mVFMessagesList.add("購聯想手機達3000元以上即送贈電腦包");
        mVFMessagesList.add("輕鬆購為您準備了這些必備生活用品");
        mVFMessagesList.add("穿了幸福時光男裝,帥呆呆,妹子馬上來");

        if (!mVFMessagesList.isEmpty()) {
            mVfHotMessage.setVisibility(View.VISIBLE);
            mVfHotMessage.startWithList(mVFMessagesList);
        } else {
            mVfHotMessage.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mVfHotMessage.startFlipping();
    }

    @Override
    public void onPause() {
        super.onPause();
        mVfHotMessage.stopFlipping();
    }

    private void getLocation() {
        locationService = ((EnjoyshopApplication) getApplication()).locationService;
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getOption());
        locationService.start();// 定位SDK
    }

    /*****
     *
     * 定位結果回調，重寫onReceiveLocation方法，可以直接拷貝如下代碼到自己工程中修改
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                cityName = location.getCity();
                provinceName = location.getProvince();

                if (cityName != null) {
                    mCityName.setText(cityName.substring(0, cityName.length() - 1));
                } else {
                    mCityName.setText("上海");
                }
                getCityWeather();
            } else {
                getCityWeather();
            }
        }
    };

    /**
     * 查詢天氣數據
     */
    private void getCityWeather() {
        //測試沒有顯示資料 後續處理
    }
}
