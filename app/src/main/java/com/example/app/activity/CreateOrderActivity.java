package com.example.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.app.EnjoyshopApplication;
import com.example.app.R;
import com.example.app.adapter.GoodsOrderAdapter;
import com.example.app.bean.Charge;
import com.example.app.bean.ShoppingCart;
import com.example.app.contants.Contants;
import com.example.app.contants.HttpContants;
import com.example.app.msg.CreateOrderRespMsg;
import com.example.app.msg.LoginRespMsg;
import com.example.app.utils.CartShopProvider;
import com.example.app.utils.LogUtil;
import com.example.app.widget.FullyLinearLayoutManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jelly.thor.okhttputils.OkHttpUtils;
import com.jelly.thor.okhttputils.callback.Callback;
import com.jelly.thor.okhttputils.request.OkHttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by 高勤
  * Time 2017/8/9
  * Describe: 訂單確認
 */

public class CreateOrderActivity extends BaseActivity implements View.OnClickListener {

    private CartShopProvider  cartProvider;
    private GoodsOrderAdapter mAdapter;

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.rl_alipay) RelativeLayout mLayoutAlipay;
    @BindView(R.id.rl_wechat) RelativeLayout mLayoutWechat;
    @BindView(R.id.rl_bd) RelativeLayout mLayoutBd;
    @BindView(R.id.txt_total) TextView mTxtTotal;
    @BindView(R.id.btn_createOrder) Button mBtnCreateOrder;

    private HashMap<String, RelativeLayout> channels = new HashMap<>();

    private static final String CHANNEL_WECHAT = "wx";  //微信支付渠道
    private static final String CHANNEL_ALIPAY = "alipay";  //支付支付渠道
    private static final String CHANNEL_BFB    = "bfb";  //百度支付渠道
    private String payChannel = CHANNEL_ALIPAY;  //默認為支付寶支付

    private float amount;
    private String orderNum;

    @Override
    protected void init() {
        showData();
        initView();
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_create_order;
    }

    @Override
    public void onClick(View v) {
        selectPayChannle(v.getTag().toString());
    }

    public void showData() {
        cartProvider = new CartShopProvider(this);
        mAdapter = new GoodsOrderAdapter(cartProvider.getAll());

        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
        //recyclerView外面嵌套ScrollView.數據顯示不全
        layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initView() {
        channels.put(CHANNEL_ALIPAY, mLayoutAlipay);
        channels.put(CHANNEL_WECHAT, mLayoutWechat);
        channels.put(CHANNEL_BFB, mLayoutBd);

        mLayoutAlipay.setOnClickListener(this);
        mLayoutWechat.setOnClickListener(this);
        mLayoutBd.setOnClickListener(this);

        amount = mAdapter.getTotalPrice();
        mTxtTotal.setText("應付款： $" + amount);
    }

    /**
     * 當前的支付渠道 以及三個支付渠道互斥 的功能
     */
    public void selectPayChannle(String paychannel) {
        for (Map.Entry<String, RelativeLayout> entry : channels.entrySet()) {
            payChannel = paychannel;
            RelativeLayout rb = entry.getValue();
            if (entry.getKey().equals(payChannel)) {
                int childCount = rb.getChildCount();
                LogUtil.e("測試子控件", childCount + "", true);

                View viewCheckBox = rb.getChildAt(2);      //這個是類似checkBox的控件
                viewCheckBox.setBackgroundResource(R.drawable.icon_check_true);
            } else {
                View viewCheckBox = rb.getChildAt(2);      //這個是類似checkBox的控件
                viewCheckBox.setBackgroundResource(R.drawable.icon_check_false);
            }
        }
    }

    @OnClick(R.id.rl_addr)
    public void chooseAddress(View view) {
        Intent intent = new Intent(CreateOrderActivity.this, AddressListActivity.class);
        startActivityForResult(intent, Contants.REQUEST_CHOOSE_ADDRESS);
    }

    @OnClick(R.id.btn_createOrder)
    public void createNewOrder(View view) {
        postNewOrder();     //提交訂單
    }

    private void postNewOrder() {
        final List<ShoppingCart> carts = mAdapter.getData();

        List<WareItem> items = new ArrayList<>(carts.size());
        for (ShoppingCart c : carts) {
            // c.getPrice()  是double類型 而接口總價為int 類型,需要轉化
            WareItem item = new WareItem(Long.parseLong(String.valueOf(c.getId())), (int) Math
                    .floor(c.getPrice()));
            items.add(item);
        }
        String item_json = new Gson().toJson(items);

        Map<String, String> params = new HashMap<>();
        params.put("user_id", EnjoyshopApplication.getInstance().getUser().getId() + "");
        params.put("item_json", item_json);
        params.put("pay_channel", payChannel);
        params.put("amount", (int) amount + "");
        params.put("addr_id", 1 + "");

        mBtnCreateOrder.setEnabled(false);

        OkHttpUtils.post().url(HttpContants.ORDER_CREATE)
                .params(params).build()
                .execute(new Callback<CreateOrderRespMsg>() {
                    @Override
                    public void onResponse(int code, CreateOrderRespMsg response, int id) {
                        mBtnCreateOrder.setEnabled(true);
                        orderNum = response.getData().getOrderNum();
                        Charge charge = response.getData().getCharge();
                    }

                    @Override
                    public CreateOrderRespMsg parseNetworkResponse(Response response, int id, OkHttpRequest okHttpRequest) throws Exception {
                        LogUtil.e("支付", "AAAAAAAAAAA", true);
                        String string = response.body().string();
                        CreateOrderRespMsg msg = new Gson().fromJson(string, new
                                TypeToken<LoginRespMsg>() {
                                }.getType());
                        return msg;
                    }
                });
    }

    /**
     * 因為接口文檔要求,商品列表為json格式,所以這裡需要定義一個內部類
     */
    class WareItem {
        private Long ware_id;
        private int  amount;

        public WareItem(Long ware_id, int amount) {
            this.ware_id = ware_id;
            this.amount = amount;
        }

        public Long getWare_id() {
            return ware_id;
        }
        public void setWare_id(Long ware_id) {
            this.ware_id = ware_id;
        }
        public int getAmount() {
            return amount;
        }
        public void setAmount(int amount) {
            this.amount = amount;
        }
    }
}
