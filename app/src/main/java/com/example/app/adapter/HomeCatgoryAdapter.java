package com.example.app.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.app.EnjoyshopApplication;
import com.example.app.R;
import com.example.app.bean.HomeCampaignBean;
import com.example.app.utils.GlideUtils;

import java.util.List;

/**
 * <pre>
  *     author : 高勤
  *     e-mail : 984992087@qq.com
  *     time   : 2017/08/06
  *     desc   : 首頁商品分類的適配器
  * 涉及到多樣式的條目的話,注意繼承的是BaseMultiItemQuickAdapter
  *     version: 1.0
  * </pr>
 */

public class HomeCatgoryAdapter extends BaseMultiItemQuickAdapter<HomeCampaignBean,
        BaseViewHolder> {

    public HomeCatgoryAdapter(List<HomeCampaignBean> datas) {
        super(datas);
        addItemType(HomeCampaignBean.ITEM_TYPE_LEFT, R.layout.template_home_cardview);
        addItemType(HomeCampaignBean.ITEM_TYPE_RIGHT, R.layout.template_home_cardview2);
    }

    @Override
    protected void convert(BaseViewHolder holder, HomeCampaignBean bean) {
        switch (bean.getItemType()) {
            //左边的
            case HomeCampaignBean.ITEM_TYPE_LEFT:
                holder.setText(R.id.text_title, bean.getTitle());
                GlideUtils.load(EnjoyshopApplication.sContext, bean.getCpOne().getImgUrl(),
                        (ImageView) holder.getView(R.id.imgview_big));
                GlideUtils.load(EnjoyshopApplication.sContext, bean.getCpTwo().getImgUrl(),
                        (ImageView) holder.getView(R.id.imgview_small_top));
                GlideUtils.load(EnjoyshopApplication.sContext, bean.getCpThree().getImgUrl(),
                        (ImageView) holder.getView(R.id.imgview_small_bottom));

                holder.addOnClickListener(R.id.imgview_big)
                        .addOnClickListener(R.id.imgview_small_top)
                        .addOnClickListener(R.id.imgview_small_bottom);


                break;

            //右边的
            case HomeCampaignBean.ITEM_TYPE_RIGHT:

                holder.setText(R.id.text_title, bean.getTitle());
                GlideUtils.load(EnjoyshopApplication.sContext, bean.getCpOne().getImgUrl(),
                        (ImageView) holder.getView(R.id.imgview_big));
                GlideUtils.load(EnjoyshopApplication.sContext, bean.getCpTwo().getImgUrl(),
                        (ImageView) holder.getView(R.id.imgview_small_top));
                GlideUtils.load(EnjoyshopApplication.sContext, bean.getCpThree().getImgUrl(),
                        (ImageView) holder.getView(R.id.imgview_small_bottom));


                holder.addOnClickListener(R.id.imgview_big)
                        .addOnClickListener(R.id.imgview_small_top)
                        .addOnClickListener(R.id.imgview_small_bottom);
                break;
            default:
                break;
        }
    }
}

