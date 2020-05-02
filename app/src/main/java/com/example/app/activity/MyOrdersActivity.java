package com.example.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.app.R;
import com.example.app.widget.EnjoyshopToolBar;

import butterknife.BindView;

/**
 * Created by 高勤
 * Time  2017/8/21
 * Describe: 我的訂單
 */

public class MyOrdersActivity extends BaseActivity {

    @BindView(R.id.toolbar) EnjoyshopToolBar mToolBar;

    @Override
    protected void init() {
        initToolBar();
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_my_orders;
    }

    private void initToolBar() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOrdersActivity.this.finish();
            }
        });
    }
}
