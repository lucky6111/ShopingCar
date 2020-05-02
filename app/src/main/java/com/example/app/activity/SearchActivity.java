package com.example.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.app.R;
import com.example.app.adapter.HistorySearchAdapter;
import com.example.app.adapter.HotSearchAdapter;
import com.example.app.utils.PreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
  * Created by 高勤
  * Time 2017/8/22
  * Describe: 搜索界面
  *
  * 一開始使用的是greendao數據庫.對於之前沒有沒有搜索的數據,添加是沒有問題的
  * 對於之前存在的數據,不會出現重複添加,重複的數據點擊後可以放在最前面,但後面的順序有些錯亂
  * 所以想到使用集合.集合和字符串之間的相互轉化,然後用sp存起來.方法是有效的
  * 設計這個界面的目的是為了練習數據庫.但還是有問題.
  * 這段時間太忙了,等抽空再次使用greendao嘗試一些
  * 目前尚未使用 greendao 而是使用 SharedPreferences
  */

public class SearchActivity extends BaseActivity {

    private List<String> hotSearchData;
    private List<String> historySearchData;

    private HotSearchAdapter     mHotSearchAdapter;
    private HistorySearchAdapter mHistorySearchAdapter;

    @BindView(R.id.hot_search_ry)
    RecyclerView mHotSearchView;
    @BindView(R.id.history_search_ry)
    RecyclerView  mHistorySearchView;

    @Override
    protected void init() {
        hotSearchData = new ArrayList<>();
        historySearchData = new ArrayList<>();

        setHotSearchData();
        getHotSearchData();
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_search;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        getHistorySearchData();
        setHistorySearchData();
    }

    /**
     * 初始化熱門搜索相關的適配器及其信息
     */
    private void setHotSearchData() {
        mHotSearchAdapter = new HotSearchAdapter(hotSearchData);
        mHotSearchView.setAdapter(mHotSearchAdapter);
        mHotSearchView.setLayoutManager(new GridLayoutManager(this, 3));

        mHotSearchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String content = (String) adapter.getData().get(position);
                doData(content);
            }
        });
    }

    /**
     * 操作數據庫數據
     */
    private int position = -1;

    private void doData(String content) {
        //有歷史數據
        if (historySearchData != null && historySearchData.size() > 0) {
            for (int i = 0; i < historySearchData.size(); i++) {
                if (content.equals(historySearchData.get(i))) {
                    //有重複的
                    position = i;
                }
            }

            if (position != -1) {
                historySearchData.remove(position);
                historySearchData.add(0, content);
            } else {
                historySearchData.add(0, content);
            }

        } else {
            //沒有歷史數據
            historySearchData.add(content);
        }

        mHistorySearchAdapter.notifyDataSetChanged();
        String histortStr = new Gson().toJson(historySearchData);
        PreferencesUtils.putString(SearchActivity.this, "histortStr", histortStr);

        Bundle bundle = new Bundle();
        bundle.putString("search", content);
        Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);     //跳轉到搜索結果界面
    }

    /**
     * 热门搜索
     */
    private void getHotSearchData() {

        //TODO 真正開發這裡的數據從後台獲取

        hotSearchData.add("華為手機");
        hotSearchData.add("玫瑰花");
        hotSearchData.add("移動硬盤");
        hotSearchData.add("android高級進階");
        hotSearchData.add("蠶絲被");
        mHotSearchAdapter.notifyDataSetChanged();
    }

    /**
     * 歷史搜索
     * 必須進行判斷是不是本來就停留在這個界面進行操作,還是初始化進入
     * 要不然,會重複添加數據
     */
    private void getHistorySearchData() {

        String histortStr = PreferencesUtils.getString(SearchActivity.this, "histortStr");

        if (histortStr != null) {
            historySearchData = new Gson().fromJson(histortStr, new TypeToken<List<String>>() {
            }.getType());
        }

    }

    /**
     * 初始化歷史搜索相關的適配器及其信息
     */
    private void setHistorySearchData() {

        mHistorySearchAdapter = new HistorySearchAdapter(historySearchData);
        mHistorySearchView.setAdapter(mHistorySearchAdapter);
        mHistorySearchView.setLayoutManager(new GridLayoutManager(this, 3));

        mHistorySearchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String content = (String) adapter.getData().get(position);
                doData(content);
            }
        });
    }
}
