package com.example.app.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.app.EnjoyshopApplication;
import com.example.app.R;
import com.example.app.bean.HotGoods;
import com.example.app.utils.GlideUtils;

import java.util.List;

/**
 * Created by 高勤
  * Time 2017/8/9
  * Dscribe: 分類 二級菜單 適配器
 */

public class SecondGoodsAdapter extends BaseQuickAdapter<HotGoods.ListBean, BaseViewHolder> {

    public SecondGoodsAdapter(List<HotGoods.ListBean> datas) {
        super(R.layout.template_category_wares, datas);
    }

    @Override
    protected void convert(BaseViewHolder holder, HotGoods.ListBean bean) {
        holder.setText(R.id.text_title, bean.getName())
                .setText(R.id.text_price, "$" + bean.getPrice());
        GlideUtils.load(EnjoyshopApplication.sContext, bean.getImgUrl(), (ImageView) holder
                .getView(R.id.iv_view));
    }
}

