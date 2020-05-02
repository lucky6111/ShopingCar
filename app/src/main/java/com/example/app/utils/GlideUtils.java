package com.example.app.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.app.R;


/**
 * <pre>
  *     author : 高勤
  *     e-mail : 984992087@qq.com
  *     time   : 2017/08/07
  *     desc   :對glide進行封裝的工具類
  *     version: 2.0
  * </pre>
 */

public class GlideUtils {

    /**
     * 加載普通的圖片
     *
     * @param context
     * @param url
     * @param iv
     */
    public static void load(Context context, String url, ImageView iv) {

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.company_logo)
                .error(R.drawable.default_head)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE);

        Glide.with(context).load(url).apply(options).into(iv);
    }

    /**
     * 展示用户头像的Glide
     *
     * @param url
     * @param iv  最好去掉動畫.不去動畫,有可能(每次)出現第一次只實現展位圖,第二次進入時是好圖片
     *      * 如果用戶沒有傳圖像,直接加載默認的用戶圖像
     */
    public static void portrait(Context context, String url, ImageView iv) {

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.company_logo)
                .error(R.drawable.default_head)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE);

        if (TextUtils.isEmpty(url)) {
            Glide.with(context).load(R.drawable.default_head).apply(options).into(iv);
        } else {
            Glide.with(context).load(url).apply(options).into(iv);
        }
    }

}

