package com.example.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.app.R;
import com.example.app.adapter.HotGoodsAdapter;
import com.example.app.bean.HotGoods;
import com.example.app.contants.Contants;
import com.example.app.contants.HttpContants;
import com.example.app.utils.LogUtil;
import com.example.app.utils.ToastUtils;
import com.example.app.widget.EnjoyshopToolBar;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.jelly.thor.okhttputils.OkHttpUtils;
import com.jelly.thor.okhttputils.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

public class GoodsListActivity extends BaseActivity implements View.OnClickListener, TabLayout
        .OnTabSelectedListener {

    @BindView(R.id.toolbar) EnjoyshopToolBar mToolbar;
    @BindView(R.id.tab_layout) TabLayout mTablayout;
    @BindView(R.id.txt_summary) TextView mTxtSummary;
    @BindView(R.id.ll_summary) LinearLayout mLlSummary;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerview;


    public static final int ACTION_LIST = 3;   //列表形式
    public static final int ACTION_GIRD = 4;   //多列形式
    private int actionType  = ACTION_LIST;     //列表形式默認的值

    public static final int TAG_DEFAULT = 0;     //tabLayout 默認
    public static final int TAG_SALE    = 1;     //tabLayout 價格
    public static final int TAG_PRICE   = 2;     //tabLayout 銷量

    private long campaignId = 0; //從上一個界面傳遞過來的參數,也是本界面請求接口的參數,以判斷是點擊哪里傳過來的,進而請求接口

    private Gson mGson = new Gson();

    private int  currPage   = 1;     //當前是第幾頁
    private int  totalPage  = 1;    //一共有多少頁
    private int  pageSize   = 10;     //每頁數目

    private List<HotGoods.ListBean> datas;
    private HotGoodsAdapter mAdatper;

    @Override
    protected void init() {
        initToolBar();
        campaignId = getIntent().getLongExtra(Contants.COMPAINGAIN_ID, 0);
        initTab();
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_goods_list;
    }

    private void initToolBar() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsListActivity.this.finish();
            }
        });

        mToolbar.setRightButtonIcon(R.drawable.icon_grid_32);
        mToolbar.getRightButton().setTag(ACTION_LIST);
        mToolbar.setRightButtonOnClickListener(this);
    }

    private void initTab() {
        //這一句必須放在添加tab的前面,要不然第一次進入時,沒有默認的
        //   參考 http://www.jianshu.com/p/493d40a9d38e
        mTablayout.setOnTabSelectedListener(this);

        TabLayout.Tab tab = mTablayout.newTab();
        tab.setText("默認");
        tab.setTag(TAG_DEFAULT);
        mTablayout.addTab(tab);

        tab = mTablayout.newTab();
        tab.setText("價格");
        tab.setTag(TAG_SALE);
        mTablayout.addTab(tab);

        tab = mTablayout.newTab();
        tab.setText("銷量");
        tab.setTag(TAG_PRICE);
        mTablayout.addTab(tab);
    }

    @Override
    public void onClick(View v) {
        //點擊後,取反
        if (actionType == ACTION_LIST) {
            actionType = ACTION_GIRD;
            mToolbar.setRightButtonIcon(R.drawable.icon_list_32);
            mToolbar.getRightButton().setTag(ACTION_GIRD);
        } else {
            actionType = ACTION_LIST;
            mToolbar.setRightButtonIcon(R.drawable.icon_grid_32);
            mToolbar.getRightButton().setTag(ACTION_LIST);
        }

        showData(); //這裡必須在獲取一次數據,不調用的話,只會在 獲取後台數據後展示.點擊後不會再走showData()
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int orderBy = (int) tab.getTag();

        String url = HttpContants.WARES_CAMPAIN_LIST;
                //+ "?campaignId=" + campaignId
                //+ "&orderBy=" + orderBy
                //+ "&curPage=" + 1
                //+ "&pageSize=" + 10;
        Log.v("sam","=>"+url);

        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onResponse(int code, String response, int id) {
                LogUtil.e(TAG, "onResponse:成功 " + response, true);
                HotGoods goodsBean = mGson.fromJson(response, HotGoods.class);
                totalPage = goodsBean.getTotalPage();
                currPage = goodsBean.getCurrentPage();
                datas = goodsBean.getList();

                showData();
            }
        });
    }

    /**
     * 展示數據
     */
    private void showData() {
        if (datas != null && datas.size() > 0) {
            mTxtSummary.setText("共有" + datas.size() + "件商品");
        } else {
            mLlSummary.setVisibility(View.GONE);
            ToastUtils.showUiToast(GoodsListActivity.this,"暫無商品信息");
            return;
        }

        mAdatper = new HotGoodsAdapter(datas, this);
        mRecyclerview.setAdapter(mAdatper);
        if (actionType == ACTION_LIST) {
            mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        } else {
            mRecyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        }

        mRecyclerview.setItemAnimator(new DefaultItemAnimator());
        mRecyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                .HORIZONTAL));
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
