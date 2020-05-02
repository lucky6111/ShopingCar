package com.example.app.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.EnjoyshopApplication;

/**
 * Created by 高勤
  * Time 2018/2/10
  * Describe: Toast的工具類
 */

public class ToastUtils {

    private static Toast mToast;

    /**
     * 安全彈出Toast。處理線程的問題。
     */
    public static void showSafeToast(final Context context, final String text) {

        if (Looper.myLooper() != Looper.getMainLooper()) {//如果不是在主線程彈出吐司，那麼拋到主線程彈
            new Handler(Looper.getMainLooper()).post(
                    new Runnable() {
                        @Override
                        public void run() {
                            if (context == null) {
                                showUiToast(EnjoyshopApplication.sContext, text);
                            } else {
                                showUiToast(context, text);
                            }
                        }
                    }
            );
        } else {
            if (context == null) {
                showUiToast(EnjoyshopApplication.sContext, text);
            } else {
                showUiToast(context, text);
            }
        }
    }

    /**
     * 彈出Toast，處理單例的問題。 ----如果是在主線程,可以用這個,子線程就不要用這個,不安全
     */
    public static void showUiToast(Context context, String text) {
        if (context == null) {
            context = EnjoyshopApplication.sContext;
        }

        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }


        if (text.length() < 10) {
            mToast.setDuration(Toast.LENGTH_SHORT);
        } else {
            mToast.setDuration(Toast.LENGTH_LONG);
        }

        View view = mToast.getView();
        view.setPadding(28, 20, 28, 20);
        TextView tv = view.findViewById(android.R.id.message);
        tv.setTextSize(14);
        tv.setTextColor(Color.WHITE);
        mToast.setText(text);
        mToast.setGravity(Gravity.CENTER, 0, 40);
        mToast.setView(view);
        mToast.show();
    }

}
