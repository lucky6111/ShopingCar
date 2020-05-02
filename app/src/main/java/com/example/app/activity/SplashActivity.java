package com.example.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.app.R;
import com.example.app.utils.PreferencesUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class SplashActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_time) TextView mTvTime;
    private int duration = 3;      //倒計時3秒
    Timer timer = new Timer();

    @Override
    protected void init() {
        setTranslucentStatus(this,true);
        timer.schedule(task, 1000, 1000);
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_splash;
    }

    /**
     * 如果點擊了,停止倒計時,直接跳轉
     */
    @OnClick(R.id.ll_time)
    public void onClick(View v) {
        timer.cancel();
        jumpActivity();
    }

    /**
     * 界面的跳轉
     */
    private void jumpActivity() {

        boolean isFirst = PreferencesUtils.getBoolean(SplashActivity.this, "isFirst", true);
        //默認為第一次

        if (isFirst) {
            PreferencesUtils.putBoolean(SplashActivity.this, "isFirst", false);
            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }

        finish();
    }

    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    duration--;
                    mTvTime.setText(duration + "");
                    if (duration < 2) {
                        timer.cancel();
                        jumpActivity();
                    }
                }
            });

        }
    };
    private void setTranslucentStatus(Activity activity, boolean on) {

        tintManager.setStatusBarTintEnabled(false);

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_FULLSCREEN;

        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);

    }
}
