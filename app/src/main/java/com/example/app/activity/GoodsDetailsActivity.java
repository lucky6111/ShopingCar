package com.example.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.example.app.R;
import com.example.app.bean.HotGoods;
import com.example.app.contants.HttpContants;
import com.example.app.helper.SharePresenter;
import com.example.app.utils.CartShopProvider;
import com.example.app.utils.LogUtil;
import com.example.app.utils.ToastUtils;
import com.example.app.widget.EnjoyshopToolBar;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.BindView;

/**
 * Created by 高勤
  * Time 2017/8/9
  * Describe: 商品詳情
 */

public class GoodsDetailsActivity extends BaseActivity implements View.OnClickListener {

    private HotGoods.ListBean goodsBean;

    @BindView(R.id.webView) WebView mWebView;
    @BindView(R.id.toolbar) EnjoyshopToolBar mToolBar;

    private WebAppInterface   mAppInterfce;
    private CartShopProvider cartProvider;

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_goods_details;
    }

    @Override
    protected void init() {
        //接收數據
        Bundle bundle = getIntent().getExtras();
        goodsBean = (HotGoods.ListBean) bundle.getSerializable("itemClickGoods");
        if (goodsBean == null) {
            finish();
        }
        //購物車
        cartProvider = new CartShopProvider(this);

        LogUtil.e("跳轉後數據", goodsBean.getName() + goodsBean.getPrice(), true);

        initToolBar();
        initData();
    }

    /**
     * 初始化標題欄(分享)
     */
    private void initToolBar() {
        mToolBar.setNavigationOnClickListener(this);
        mToolBar.setRightButtonText("分享");
        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePresenter.getInstance().showShareDialogOnBottom
                        (0, GoodsDetailsActivity.this, "計算機書籍",
                                "第二行代碼", "0");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                this.finish();
                break;
        }
    }

    /**
     * 初始化 webView 資料
     */
    private void initData() {
        final WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setAppCacheEnabled(true);
        mWebView.loadUrl(HttpContants.WARES_DETAIL);

        mAppInterfce = new WebAppInterface(this);
        mWebView.addJavascriptInterface(mAppInterfce, "appInterface");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                //整個頁面加載完後,才能調用這個方法
                mAppInterfce.showDetail();
            }
        });
    }

    class WebAppInterface {

        private Context context;

        public WebAppInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void showDetail() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //此功能是載入商品資料到 detail.html 中的 javascript showDetail(id)方法
                    mWebView.loadUrl("javascript:showDetail(" + goodsBean.getId() + ")");
                }
            });
        }

        @JavascriptInterface
        public void buy(long id) {
            //此功能是 detail.html 中的 javascript 按鍵 buy(id)方法
            cartProvider.put(goodsBean);
            ToastUtils.showSafeToast(GoodsDetailsActivity.this, "已添加到購物車");
        }

        /**
         * 這里和視頻 源代碼有出入.
         * 已經修改了.之前是 收藏 加入購物車.
         * 現在變成了 加入購物車 立即購物
         */
        @JavascriptInterface
        public void addToCart(long id) {
            //此功能是 detail.html 中的 javascript 按鍵 addToCart(id)方法
            //addToFavorite();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.destroy();
        }
    }
}
