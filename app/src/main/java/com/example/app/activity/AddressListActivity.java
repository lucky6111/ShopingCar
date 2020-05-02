package com.example.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.app.EnjoyshopApplication;
import com.example.app.R;
import com.example.app.adapter.AddressListAdapter;
import com.example.app.data.DataManager;
import com.example.app.data.dao.Address;
import com.example.app.utils.PreferencesUtils;
import com.example.app.widget.EnjoyshopToolBar;

import java.util.List;

import butterknife.BindView;

/**
 * Created by 高勤
  * Time 2017/8/10
  * Describe: 收貨地址
 */
public class AddressListActivity extends BaseActivity {

    @BindView(R.id.toolbar) EnjoyshopToolBar mToolBar;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerview;

    private AddressListAdapter mAdapter;
    private List<Address> mAddressDataList;

    /**
     * 默認設置的地址
     */
    private int isDefaultPosition = 0;

    @Override
    protected void init() {
        initToolbar();
        initView();
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_address_list;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAddress();
        isDefaultPosition = PreferencesUtils.getInt(this, "isDefaultPosition", 0);
    }

    /**
     * 標題的初始化
     */
    private void initToolbar() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳轉到添加地址界面
                jumpAddressAdd(null);
            }
        });
    }

    private void jumpAddressAdd(Address address) {
        Intent intent = new Intent(AddressListActivity.this, AddressAddActivity.class);
        //這裡不能使用序列化
        if (address != null) {
            intent.putExtra("addressId", address.getAddressId());
            intent.putExtra("name", address.getName());
            intent.putExtra("phone", address.getPhone());
            intent.putExtra("BigAddress", address.getBigAddress());
            intent.putExtra("SmallAddress", address.getSmallAddress());
            intent.putExtra("isDefaultAddress", address.getIsDefaultAddress());
        }
        startActivity(intent);
    }

    private void initView() {
        if (mAdapter == null) {
            mAdapter = new AddressListAdapter(mAddressDataList);
            mRecyclerview.setAdapter(mAdapter);
            mRecyclerview.setLayoutManager(new LinearLayoutManager(AddressListActivity.this));
            mRecyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                    .HORIZONTAL));

            mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    Address address = (Address)adapter.getData().get(position);
                    switch (view.getId()) {
                        case R.id.txt_edit:
                            updateAddress(address);
                            break;
                        case R.id.txt_del:
                            delAddress(address);
                            break;
                        case R.id.cb_is_defualt:
                            chooseDefult(mAddressDataList, position);
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    private void updateAddress(Address address) {
        jumpAddressAdd(address);
    }

    private void delAddress(Address address) {
        Long addressId = address.getAddressId();
        DataManager.deleteAddress(addressId);
        initAddress();
        if(mAddressDataList != null && mAddressDataList.size() > 0){
            if (address.getIsDefaultAddress()){
                mAddressDataList.get(0).setIsDefaultAddress(true);
                PreferencesUtils.putInt(this, "isDefaultPosition", 0);
                isDefaultPosition = 0;
            }
        }else{
            PreferencesUtils.putInt(this, "isDefaultPosition", 0);
            isDefaultPosition = 0;
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 初始化地址信息
     * 商業項目這裡是請求接口
     */
    private void initAddress() {
        Long userId = EnjoyshopApplication.getInstance().getUser().getId();
        mAddressDataList = DataManager.queryAddress(userId);
        if (mAddressDataList != null && mAddressDataList.size() > 0) {
            for (int i = 0; i < mAddressDataList.size(); i++) {
                mAdapter.setNewData(mAddressDataList);
            }
        }else {
            mAddressDataList.clear();
            mAdapter.setNewData(mAddressDataList);
        }
    }

    /**
     * 需要改變2個對象的值.一個是 之前默認的.一個是當前設置默認的
     * @param mAddressDataList
     * @param position
     */
    private void chooseDefult(List<Address> mAddressDataList, int position) {

        Address preAddress = mAddressDataList.get(isDefaultPosition);
        Address nowAddress = mAddressDataList.get(position);

        isDefaultPosition = position;
        PreferencesUtils.putInt(this, "isDefaultPosition", isDefaultPosition);

        changeBean(preAddress);
        changeBean(nowAddress);

        initAddress();
        mAdapter.notifyDataSetChanged();
    }

    private void changeBean(Address address) {
        Long addressId = address.getAddressId();
        address.setAddressId(addressId);
        address.setIsDefaultAddress(!address.getIsDefaultAddress());
        DataManager.updateAddress(address);
    }
}
