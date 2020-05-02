package com.example.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.app.R;
import com.example.app.data.DataManager;
import com.example.app.data.dao.User;
import com.example.app.utils.StringUtils;
import com.example.app.utils.ToastUtils;
import com.example.app.widget.ClearEditText;
import com.example.app.widget.EnjoyshopToolBar;

import java.util.List;

import butterknife.BindView;

/**
 * Created by 高勤
  * Time 2017/8/12
  * Describe: 註冊activity
 */

public class RegActivity extends BaseActivity {

    @BindView(R.id.toolbar) EnjoyshopToolBar mToolBar;
    @BindView(R.id.txtCountry) TextView mTxtCountry;
    @BindView(R.id.edittxt_phone) ClearEditText mEtxtPhone;
    @BindView(R.id.edittxt_pwd) ClearEditText mEtxtPwd;

    private String phone;
    private String pwd;

    @Override
    protected void init() {
        initToolBar();
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_reg;
    }

    private void initToolBar() {

        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCode();
            }
        });
    }

    /**
     * 獲取手機號 密碼等信息
     */
    private void getCode() {

        phone = mEtxtPhone.getText().toString().trim().replaceAll("\\s*", "");
        pwd = mEtxtPwd.getText().toString().trim();

        checkPhoneNum();
    }

    /**
     * 對手機號進行驗證
     * 是否合法 是否已經註冊
     */
    private void checkPhoneNum() {

        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showSafeToast(RegActivity.this, "請輸入手機號碼");
            return;
        }

        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showSafeToast(RegActivity.this, "請設置密碼");
            return;
        }

        if (!StringUtils.isMobileNum(phone)) {
            ToastUtils.showSafeToast(RegActivity.this, "請核對手機號碼");
            return;
        }

        if (!StringUtils.isPwdStrong(pwd)) {
            ToastUtils.showSafeToast(RegActivity.this, "密碼太短,請重新設置");
            return;
        }
        queryUserData();
    }

    /**
     * 查詢手機號是否已經註冊了
     * <p>
     * 注意注意: 在商業項目中,這裡只需要請求註冊接口即可.手機號是否存在由後台崗位同事判斷
     */
    private void queryUserData() {

        List<User> mUserDataList = DataManager.queryUser(phone);
        if (mUserDataList != null && mUserDataList.size() > 0) {
            ToastUtils.showSafeToast(RegActivity.this, "手機號已被註冊");
        } else {
            jumpRegSecondUi();
        }
    }

    /**
     * 跳轉到註冊界面二
     */

    private void jumpRegSecondUi() {
        Intent intent = new Intent(this, RegSecondActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("pwd", pwd);
        startActivity(intent);
        finish();
    }
}
