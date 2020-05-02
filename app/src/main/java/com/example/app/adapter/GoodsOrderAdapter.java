package com.example.app.adapter;


import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.app.EnjoyshopApplication;
import com.example.app.R;
import com.example.app.bean.ShoppingCart;
import com.example.app.utils.GlideUtils;

import java.util.List;

/**
 * <pre>
 *     author : 高勤
  *     e-mail : 984992087@qq.com
  *     time   : 2017/08/13
  *     desc   : 商品訂單適配器
  *     version: 1.0
 * </pre>
 */

public class GoodsOrderAdapter extends BaseQuickAdapter<ShoppingCart, BaseViewHolder> {


    private List<ShoppingCart> mDatas;

    public GoodsOrderAdapter(List<ShoppingCart> datas) {
        super(R.layout.template_order_goods, datas);
        this.mDatas = datas;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ShoppingCart item) {
        GlideUtils.load(EnjoyshopApplication.sContext, item.getImgUrl(), (ImageView) helper
                .getView(R.id.iv_view));
    }

    public float getTotalPrice() {
        float sum = 0;
        if (!isNull())
            return sum;

        for (ShoppingCart cart : mDatas) {
            sum += cart.getCount() * cart.getPrice();
        }
        return sum;
    }

    private boolean isNull() {
        return (mDatas != null && mDatas.size() > 0);
    }
}
