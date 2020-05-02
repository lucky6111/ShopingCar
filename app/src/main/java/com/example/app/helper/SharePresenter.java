package com.example.app.helper;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.app.EnjoyshopApplication;
import com.example.app.R;
import com.example.app.utils.AppExistUtils;
import com.example.app.utils.ToastUtils;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


/**
 * <pre>
 * author : 高勤
  * e-mail : 984992087@qq.com
  * time : 2018/02/10
  * desc : 社會化分享的封裝
 * </pre>
 */


public class SharePresenter implements View.OnClickListener {

    private volatile static SharePresenter sGetShareInstance;

    private SharePresenter() {
    }

    public static SharePresenter getInstance() {

        if (sGetShareInstance == null) {
            synchronized (SharePresenter.class) {
                if (sGetShareInstance == null) {
                    sGetShareInstance = new SharePresenter();
                }
            }
        }
        return sGetShareInstance;
    }


    private static Dialog dialog;

    private String   platformType;   //分享到哪個平台
    private int      shareType;      //分享哪種類型(比如鍊接 視頻 音頻等等)
    private Activity mActivity;
    private String   title;
    private String   description;
    private String   id;    //分享鏈接的唯一id


    /**
     * 分享對話框，默認底部彈出
     */
    public void showShareDialogOnBottom(int shareType, Activity mActivity
            , String title, String description, String id) {

        this.shareType = shareType;
        this.mActivity = mActivity;
        this.title = title;
        this.description = description;
        this.id = id;


        dialog = new Dialog(mActivity, R.style.ActionSheetDialogStyle);
        View shareView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_main_share_bottom, null);
        ImageView close = (ImageView) shareView.findViewById(R.id.iv_dismiss);

        shareView.findViewById(R.id.weixin).setOnClickListener(this);
        shareView.findViewById(R.id.wxcircle).setOnClickListener(this);
        shareView.findViewById(R.id.sina).setOnClickListener(this);
        shareView.findViewById(R.id.qq).setOnClickListener(this);
        shareView.findViewById(R.id.qzone).setOnClickListener(this);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //將佈局設置給Dialog
        dialog.setContentView(shareView);
        //獲取當前Activity所在的窗體
        Window dialogWindow = dialog.getWindow();
        //設置Dialog從窗體底部彈出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //獲得窗體的屬性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //將屬性設置給窗體
        dialogWindow.setAttributes(lp);
        dialog.show();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.weixin:
                boolean wx = AppExistUtils.isWeixinAvilible(mActivity);
                if (wx) {
                    share(Wechat.NAME);
                } else {
                    ToastUtils.showSafeToast(EnjoyshopApplication.sContext,"未安裝微信,無法分享");
                    dialog.dismiss();
                }
                break;
            case R.id.wxcircle:
                boolean wx1 = AppExistUtils.isWeixinAvilible(mActivity);
                if (wx1) {
                    share(WechatMoments.NAME);
                } else {
                    ToastUtils.showSafeToast(EnjoyshopApplication.sContext,"未安裝微信,無法分享");
                    dialog.dismiss();
                }

                break;
            case R.id.sina:
                boolean wb = AppExistUtils.isWeiboAvailable(mActivity);
                if (wb) {
                    share(SinaWeibo.NAME);
                } else {
                    ToastUtils.showSafeToast(EnjoyshopApplication.sContext,"未安裝微博,無法分享");
                    dialog.dismiss();
                }
                break;
            case R.id.qq:
                boolean qq = AppExistUtils.isQQClientAvailable(mActivity);
                if (qq) {
                    share(QQ.NAME);
                } else {
                    ToastUtils.showSafeToast(EnjoyshopApplication.sContext,"未安裝QQ,無法分享");
                    dialog.dismiss();
                }
                break;
            case R.id.qzone:
                boolean qq1 = AppExistUtils.isQQClientAvailable(mActivity);
                if (qq1) {
                    share(QZone.NAME);
                } else {
                    ToastUtils.showSafeToast(EnjoyshopApplication.sContext,"未安裝QQ,無法分享");
                    dialog.dismiss();
                }
                break;
        }
    }

    /**
     * 分享
     */
    private void share(String platform) {

        dialog.dismiss();

        OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果為空，還是會調用九宮格的平台列表界面
        if (platform != null) {
            oks.setPlatform(platform);
        }

        //關閉sso授權
        oks.disableSSOWhenAuthorize();
        oks.setTitle(title);
        oks.setTitleUrl("http://www.baidu.com");
        oks.setText(description);
        oks.setUrl("http://sharesdk.cn");
        oks.setSite("來自輕鬆購的分享");
        oks.setSiteUrl("http://www.baidu.com");

        // 啟動分享GUI
        oks.show(mActivity);

    }
}

