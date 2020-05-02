package com.example.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.app.EnjoyshopApplication;
import com.example.app.R;
import com.example.app.data.DataManager;
import com.example.app.data.dao.User;
import com.example.app.fragment.MineFragment;
import com.example.app.utils.StringUtils;
import com.example.app.utils.ToastUtils;
import com.example.app.widget.ClearEditText;
import com.example.app.widget.EnjoyshopToolBar;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    private String imageUrl = "http://192.168.1.105/shoppingCar/timg.jpg" ;

    @BindView(R.id.toolbar) EnjoyshopToolBar mToolBar;
    @BindView(R.id.etxt_phone) ClearEditText mEtxtPhone;
    @BindView(R.id.etxt_pwd) ClearEditText    mEtxtPwd;
    @BindView(R.id.txt_toReg) TextView mTxtToReg;

    @Override
    protected void init() {
        initToolBar();
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_login;
    }

    private void initToolBar() {

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
    }

    @OnClick({R.id.btn_login, R.id.txt_toReg})
    public void viewclick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();   //登錄
                break;
            case R.id.txt_toReg:
                Intent intent = new Intent(this, RegActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 登錄
          * 注意注意.商業項目是直接請求登錄接口.這次是對數據庫進行操作
     */
    private void login() {

        String phone = mEtxtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showSafeToast(LoginActivity.this, "請輸入手機號碼");
            return;
        }

        if (!StringUtils.isMobileNum(phone)) {
            ToastUtils.showSafeToast(LoginActivity.this, "請核對手機號碼");
            return;
        }

        String pwd = mEtxtPwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showSafeToast(LoginActivity.this, "請輸入密碼");
            return;
        }

        loginlogic(phone, pwd);


        /**
         * 真實的項目這裡很有可能需要對密碼進行加密.基本完成代碼如下
         */
        /********開始********************/
        //        Map<String, String> params = new HashMap<>();
        //        params.put("phone", phone);
        //        params.put("password", pwd);
        //
        //        OkHttpUtils.post().url(HttpContants.LOGIN).params(params).build().execute(new
        // Callback<LoginRespMsg<User>>() {
        //            @Override
        //            public LoginRespMsg<User> parseNetworkResponse(Response response, int id)
        // throws
        //                    Exception {
        //
        //                String string = response.body().string();
        //                LoginRespMsg loginRespMsg = new Gson().fromJson(string, LoginRespMsg
        // .class);
        //                return loginRespMsg;
        //
        //            }
        //
        //            @Override
        //            public void onError(Call call, Exception e, int id) {
        //            }
        //
        //            @Override
        //            public void onResponse(LoginRespMsg<User> response, int id) {
        //
        //                EnjoyshopApplication application = EnjoyshopApplication.getInstance();
        //                application.putUser(response.getData(), response.getToken());
        //                if (application.getIntent()==null) {
        //                    setResult(RESULT_OK);
        //                    finish();
        //                }else {
        //                    application.jumpToTargetActivity(LoginActivity.this);
        //                    finish();
        //                }
        //
        //            }
        //        });

        /********结束********************/
    }

    private void loginlogic(String phone, String pwd) {
        List<User> mUserDataList = DataManager.queryUser(phone);
        if (mUserDataList != null && mUserDataList.size() > 0) {
            String netPwd = mUserDataList.get(0).getPwd();
            Long netUserId = mUserDataList.get(0).getUserId();

            if (pwd.equals(netPwd)) {
                ToastUtils.showSafeToast(LoginActivity.this, "登錄成功");
                EnjoyshopApplication application = EnjoyshopApplication.getInstance();

                com.example.app.bean.User user = new com.example.app.bean.User();
                user.setMobi(phone);
                user.setUsername("年輕人");
                user.setId(netUserId);
                user.setLogo_url(imageUrl);

                application.putUser(user, "12345678asfghdssa");
                finish();

               /* if (application.getIntent() == null) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    startActivity(new Intent(LoginActivity.this, MineFragment.class));
                    finish();
                }
                */
            } else {
                ToastUtils.showSafeToast(LoginActivity.this, "密碼不正確");
            }
        } else {
            ToastUtils.showSafeToast(LoginActivity.this, "用户不存在");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
