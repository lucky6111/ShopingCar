package com.example.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.app.R;
import com.example.app.data.DataManager;
import com.example.app.data.dao.User;
import com.example.app.utils.CountTimerView;
import com.example.app.utils.ToastUtils;
import com.example.app.widget.ClearEditText;
import com.example.app.widget.EnjoyshopToolBar;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 高勤
  * Time 2017/8/12
  * Describe: 接收驗證碼的註冊界面,即註冊界面二
 */

public class RegSecondActivity extends BaseActivity {

    @BindView(R.id.toolbar) EnjoyshopToolBar mToolBar;
    @BindView(R.id.txtTip) TextView mTxtTip;
    @BindView(R.id.btn_reSend) Button mBtnResend;
    @BindView(R.id.edittxt_code) ClearEditText mEtCode;

    private String phone;
    private String pwd;


    private Gson mGson = new Gson();

    @Override
    protected void init() {
        initToolBar();

        phone = getIntent().getStringExtra("phone");
        pwd = getIntent().getStringExtra("pwd");
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_reg_second;
    }

    /**
     * 標題欄 完成
     */
    private void initToolBar() {
        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCode();
            }
        });
    }

    /**
     * 提交驗證碼
     */
    private void submitCode() {

        String vCode = mEtCode.getText().toString().trim();

        if (TextUtils.isEmpty(vCode)) {
            ToastUtils.showSafeToast(RegSecondActivity.this, "請填寫驗證碼");
        }

        if (!"1234".equals(vCode)) {
            ToastUtils.showSafeToast(RegSecondActivity.this, "驗證碼不准確,請重新獲取");
        } else {
            addUser();
        }
    }

    private void addUser() {
        User user = new User();
        user.setPhone(phone);
        user.setPwd(pwd);

        DataManager.insertUser(user);

        ToastUtils.showSafeToast(RegSecondActivity.this, "註冊成功");
        startActivity(new Intent(RegSecondActivity.this, LoginActivity.class));
        finish();
    }

    @OnClick(R.id.btn_reSend)
    public void getVcode(View view) {

        mTxtTip.setText("驗證碼為:  1234");

        //倒計時
        CountTimerView timerView = new CountTimerView(mBtnResend);
        timerView.start();

        ToastUtils.showSafeToast(RegSecondActivity.this, "驗證碼為: 1234");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
