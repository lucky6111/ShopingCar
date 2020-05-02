package com.example.app.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.app.EnjoyshopApplication;
import com.example.app.R;
import com.example.app.activity.AddressListActivity;
import com.example.app.activity.LoginActivity;
import com.example.app.activity.MyFavoriteActivity;
import com.example.app.activity.MyOrdersActivity;
import com.example.app.bean.User;
import com.example.app.contants.Contants;
import com.example.app.utils.GlideUtils;
import com.example.app.utils.ToastUtils;
import com.example.app.widget.CircleImageView;

import butterknife.BindView;
import butterknife.OnClick;

public class MineFragment extends BaseFragment {

    @BindView(R.id.img_head) CircleImageView mImageHead;
    @BindView(R.id.txt_username) TextView mTxtUserName;
    @BindView(R.id.btn_logout) Button mbtnLogout;

    @Override
    protected void init() {
        User user = EnjoyshopApplication.getInstance().getUser();
        showUser(user);
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.fragment_mine;
    }

    private void showUser(User user) {
        if (user != null) {
            mTxtUserName.setText(user.getUsername());
            GlideUtils.portrait(getContext(), user.getLogo_url(), mImageHead);
        } else {
            mTxtUserName.setText("請登入");
            GlideUtils.portrait(getContext(), null, mImageHead);
        }
    }

    @OnClick({R.id.txt_my_address, R.id.txt_my_favorite, R.id.txt_my_orders, R.id.txt_username, R
            .id.img_head, R.id.btn_logout})
    public void onclick(View view) {
        switch (view.getId()) {
            //收貨地址
            case R.id.txt_my_address:
                startActivity(new Intent(getActivity(), AddressListActivity.class), true);
                break;
            //我的收藏
            case R.id.txt_my_favorite:
                startActivity(new Intent(getActivity(), MyFavoriteActivity.class), true);
                break;
            //我的訂單
            case R.id.txt_my_orders:
                startActivity(new Intent(getActivity(), MyOrdersActivity.class), true);
                break;
            case R.id.txt_username:
            case R.id.img_head:
                User user = EnjoyshopApplication.getInstance().getUser();
                if (user == null) {
                    Intent intent2 = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent2, Contants.REQUEST_CODE);
                } else {
                    ToastUtils.showSafeToast(getContext(), "更換頭像或修改暱稱");
                }
                break;
            case R.id.btn_logout:
                EnjoyshopApplication.getInstance().clearUser();
                showUser(null);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        User user = EnjoyshopApplication.getInstance().getUser();
        showUser(user);
    }
}
