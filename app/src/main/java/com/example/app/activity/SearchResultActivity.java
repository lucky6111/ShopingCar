package com.example.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.app.R;

import butterknife.BindView;

public class SearchResultActivity extends BaseActivity {

    @BindView(R.id.tv_result) TextView mTextView;

    @Override
    protected void init() {
        String search = getIntent().getStringExtra("search");
        if (!TextUtils.isEmpty(search)) {
            mTextView.setText(search);
        }
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_search_result;
    }
}
