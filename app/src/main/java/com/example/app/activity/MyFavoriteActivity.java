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
 * Describe: 我的收藏
 */

public class MyFavoriteActivity extends BaseActivity {

    @BindView(R.id.toolbar) EnjoyshopToolBar mToolBar;

    @Override
    protected void init() {
        initToolBar();
        //TODO 獲取後台的數據
        //getDataFromNet();
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_my_favorite;
    }

    /**
     * 關於標題欄的操作
     */
    private void initToolBar() {

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFavoriteActivity.this.finish();
            }
        });
    }
}
