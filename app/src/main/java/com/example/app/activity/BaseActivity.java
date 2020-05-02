package com.example.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.app.EnjoyshopApplication;
import com.example.app.R;
import com.example.app.bean.User;
import com.example.app.utils.PreferencesUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity{

    public SystemBarTintManager tintManager;
    protected static final String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentResourseId());

        setStatusBar();
        ButterKnife.bind(this);
        init();
    }

    protected abstract void init();

    protected abstract int getContentResourseId();

    /**
     * android 5.0 及以下沉浸式狀態欄
     */
    protected void setStatusBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//設置透明狀態欄
        }
        initSystemBar(this);
    }

    /**
     * 沉浸式狀態欄
     */
    public void initSystemBar(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true);
        }

        tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        // 使用顏色資源
        tintManager.setStatusBarTintResource(R.color.colorPrimary);
    }

    private void setTranslucentStatus(Activity activity, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void startActivity(Intent intent, boolean isNeedLogin) {
        if (isNeedLogin) {
            User user = EnjoyshopApplication.getInstance().getUser();
            if (user != null) {
                super.startActivity(intent);
            } else {
                EnjoyshopApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(this, LoginActivity.class);
                super.startActivity(intent);
            }

        } else {
            super.startActivity(intent);
        }
    }
}
