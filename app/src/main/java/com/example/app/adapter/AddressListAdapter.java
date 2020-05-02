package com.example.app.adapter;

import android.widget.CheckBox;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.app.R;
import com.example.app.data.dao.Address;

import java.util.List;

/**
  * Created by 高勤
  * Time 2017/8/11
  * Describe: 收貨地址 適配器
  */

public class AddressListAdapter extends BaseQuickAdapter<Address, BaseViewHolder> {

    public AddressListAdapter(List<Address> datas) {
        super(R.layout.template_address, datas);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Address item) {
        helper.setText(R.id.txt_name, item.getName())
                .setText(R.id.txt_phone, item.getPhone())
                .setText(R.id.txt_address, item.getAddress())
                .addOnClickListener(R.id.cb_is_defualt)
                .addOnClickListener(R.id.txt_edit)
                .addOnClickListener(R.id.txt_del);

        ((CheckBox)helper.getView(R.id.cb_is_defualt)).setChecked(item.getIsDefaultAddress());
    }
}
