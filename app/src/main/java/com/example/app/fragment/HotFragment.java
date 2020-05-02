package com.example.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.app.R;
import com.example.app.activity.GoodsDetailsActivity;
import com.example.app.activity.SearchActivity;
import com.example.app.adapter.HotGoodsAdapter;
import com.example.app.bean.HotGoods;
import com.example.app.contants.HttpContants;
import com.example.app.utils.ToastUtils;
import com.google.gson.Gson;
import com.jelly.thor.okhttputils.OkHttpUtils;
import com.jelly.thor.okhttputils.callback.StringCallback;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <pre>
 *     author : 高勤
  *     e-mail : 984992087@qq.com
  *     time   : 2017/08/02
  *     desc   : 熱賣商品fragment
  *     version: 2.0
 * </pre>
 */

public class HotFragment extends BaseFragment {

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE   = 2;
    private int state = STATE_NORMAL;       //正常情况

    private int  currPage  = 1;     //當前是第幾頁
    private int  totalPage = 1;    //一共有多少頁
    private int  pageSize  = 10;     //每頁數目

    private Gson mGson = new Gson();

    private List<HotGoods.ListBean> datas;

    private HotGoodsAdapter mAdatper;

    @BindView(R.id.recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.refresh_view) MaterialRefreshLayout mRefreshLaout;

    @Override
    protected void init() {
        initRefreshLayout();     //控件初始化
        getData();              //獲取後台數據
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.fragment_hot;
    }

    private void initRefreshLayout() {
        mRefreshLaout.setLoadMore(true);
        mRefreshLaout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                if (currPage <= totalPage) {
                    loadMoreData();
                } else {
                    ToastUtils.showSafeToast(getContext(),"沒有更多商品");
                    mRefreshLaout.finishRefreshLoadMore();
                }
            }
        });
    }

    /**
     * 刷新
     */
    private void refreshData() {
        state = STATE_REFREH;
        currPage = 1;
        getData();
    }

    /**
     * 加载更多
     */
    private void loadMoreData() {
        state = STATE_MORE;
        currPage = ++currPage;
        getData();
    }

    private void getData() {
        String url = HttpContants.HOT_WARES + "?curPage=" + currPage + "&pageSize=" + pageSize;
        Log.v("sam","=>"+url);
        OkHttpUtils.get().url(url).addParam("type", "1")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(int code, String response, int id) {
                HotGoods hotGoods = mGson.fromJson(response, HotGoods.class);
                totalPage = hotGoods.getTotalPage();
                currPage = hotGoods.getCurrentPage();
                datas = hotGoods.getList();

                showData();
            }
        });
    }

    /**
     * 展示數據
     */
    private void showData() {
        switch (state) {
            case STATE_NORMAL:
                mAdatper = new HotGoodsAdapter(datas, getContext());
                mRecyclerView.setAdapter(mAdatper);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                        DividerItemDecoration.HORIZONTAL));
                break;
            case STATE_REFREH:
                mAdatper.clearData();
                mAdatper.addData(datas);
                mRecyclerView.scrollToPosition(0);
                mRefreshLaout.finishRefresh();
                break;
            case STATE_MORE:
                mAdatper.addData(mAdatper.getDatas().size(), datas);
                mRecyclerView.scrollToPosition(mAdatper.getDatas().size());
                mRefreshLaout.finishRefreshLoadMore();
                break;
        }

        mAdatper.setOnItemClickListener(new HotGoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //借助currPage 和pageSize 可以實現默認情況和刷新時,都可以使用
                HotGoods.ListBean listBean = mAdatper.getDatas().get(position);
                Intent intent = new Intent(getContext(), GoodsDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                Bundle bundle = new Bundle();
                bundle.putSerializable("itemClickGoods", (Serializable) listBean);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    //跳轉到搜索界面
    @OnClick({R.id.toolbar})
    public void searchView(View view) {
        startActivity(new Intent(getContext(), SearchActivity.class));
    }
}
