package com.example.app.fragment;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.activity.CreateOrderActivity;
import com.example.app.adapter.ShopCartAdapter;
import com.example.app.bean.MessageEvent;
import com.example.app.bean.ShoppingCart;
import com.example.app.utils.CartShopProvider;
import com.example.app.widget.EnjoyshopToolBar;
import com.example.app.widget.WrapContentLinearLayoutManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <pre>
 *     author : 高勤
  *     e-mail : 984992087@qq.com
  *     time   : 2017/08/09
  *     desc   : 購物車fragment
  *     version: 1.0
 * </pre>
 */

public class ShopCartFragment extends BaseFragment implements ShopCartAdapter.CheckItemListener {

    private CartShopProvider   mCartShopProvider;
    private List<ShoppingCart> checkedList;  //選中後的數據
    private List<ShoppingCart> dataArray;  //列表數據

    @BindView(R.id.toolbar) EnjoyshopToolBar mToolbar;
    @BindView(R.id.rv_bottom) RelativeLayout mRvBottom;
    @BindView(R.id.ll_empty) LinearLayout mLlEmpty;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.txt_total) TextView mTextTotal;
    @BindView(R.id.checkbox_all) CheckBox mCheckBox;

    private ShopCartAdapter mAdapter;

    private boolean isSelectAll;

    @Override
    protected void init() {
        mCartShopProvider = new CartShopProvider(getContext());
        checkedList = new ArrayList<>();
        changeToolbar();
        showData();
        showTotalPrice();
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.fragment_shopcart;
    }

    @Override
    public void itemChecked(ShoppingCart checkBean, boolean isChecked) {
        //處理Item點擊選中回調事件
        if (isChecked) {
            //選中處理
            if (!checkedList.contains(checkBean)) {
                checkedList.add(checkBean);
            }
        } else {
            //未選中處理
            if (checkedList.contains(checkBean)) {
                checkedList.remove(checkBean);
            }
        }
        //判斷列表數據是否全部選中
        if (checkedList.size() == dataArray.size()) {
            mCheckBox.setChecked(true);
        } else {
            mCheckBox.setChecked(false);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            refData();
        }
    }

    /**
     * 刷新數據
     * <p>
     * fragment是隱藏與顯示.生命週期很多沒走.
     * 先將數據全部清除,再重新添加(有可能和以前一樣,有可能有新數據)
     * 清除的目的,就是為了防止添加了新數據而界面上沒展示
     */
    private void refData() {
        List<ShoppingCart> carts = mCartShopProvider.getAll();
        if (carts != null && carts.size() > 0) {
            mLlEmpty.setVisibility(View.GONE);
            mRvBottom.setVisibility(View.VISIBLE);
            showData();
            showTotalPrice();
        } else {
            initEmptyView();
        }
    }

    /**
     * 改變標題欄
     */
    private void changeToolbar() {
        mToolbar.hideSearchView();
        mToolbar.showTitleView();
        mToolbar.setTitle(R.string.cart);
    }

    /**
     * 獲取數據
     */
    private void showData() {
        dataArray = mCartShopProvider.getAll();
        if (dataArray == null) {
            initEmptyView();           //如果數據為空,顯示空的試圖
            return;
        }

        /**
         * 購物車數據不為空
         */
        mAdapter = new ShopCartAdapter(getContext(), dataArray, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
    }

    private void initEmptyView() {
        mRvBottom.setVisibility(View.GONE);
        mLlEmpty.setVisibility(View.VISIBLE);
    }

    public void showTotalPrice() {
        float total = getTotalPrice();
        mTextTotal.setText(Html.fromHtml("合計 $<span style='color:#eb4f38'>" + total + "</span>"),
                TextView.BufferType.SPANNABLE);
    }

    /**
     * 計算總和
     */
    public boolean isNull() {
        return (dataArray != null && dataArray.size() > 0);
    }

    private float getTotalPrice() {
        float sum = 0;
        if (!isNull())
            return sum;

        for (ShoppingCart cart : dataArray) {
            if (cart.isChecked()) {   //是否勾上去了
                sum += cart.getCount() * cart.getPrice();
            }
        }
        return sum;
    }

    @OnClick({R.id.btn_del, R.id.btn_order, R.id.tv_goshop, R.id.checkbox_all})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_del:
                //                mAdapter.delCart();
                break;
            case R.id.btn_order:
                Intent intent = new Intent(getContext(), CreateOrderActivity.class);
                startActivity(intent, true);
                break;
            case R.id.tv_goshop:      //如果沒有商品時
                mLlEmpty.setVisibility(View.GONE);
                //跳轉到homeFragment中
                //不能這樣使用.replace也不行.會出現界面的重疊
                //                getActivity().getSupportFragmentManager()
                //                        .beginTransaction()
                //                        .add(R.id.realtabcontent, new HomeFragment())
                //                        .commit();

                EventBus.getDefault().post(new MessageEvent(0));

                break;
            case R.id.checkbox_all:
                isSelectAll = !isSelectAll;
                checkedList.clear();
                if (isSelectAll) {//全選處理
                    mCheckBox.setChecked(true);
                    checkedList.addAll(dataArray);
                }else {
                    mCheckBox.setChecked(false);
                }
                for (ShoppingCart checkBean : dataArray) {
                    checkBean.setIsChecked(isSelectAll);
                }
                mAdapter.notifyDataSetChanged();
                showTotalPrice();
                break;
        }
    }
}
