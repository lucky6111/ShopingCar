package com.example.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.EnjoyshopApplication;
import com.example.app.R;
import com.example.app.bean.ShoppingCart;
import com.example.app.fragment.ShopCartFragment;
import com.example.app.utils.GlideUtils;
import com.example.app.widget.NumberAddSubView;

import java.util.List;

/**
 * Created by 高勤
  * Time 2017/8/9
  * Describe: 購物車的適配器
 */

public class ShopCartAdapter extends RecyclerView.Adapter<ShopCartAdapter.ViewHolder> {

    private Context            mContext;
    private List<ShoppingCart> mDatas;
    private CheckItemListener  mCheckListener;

    public ShopCartAdapter(Context mContext, List<ShoppingCart> mDatas, CheckItemListener
            mCheckListener) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.mCheckListener = mCheckListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.template_cart, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ShoppingCart cart = mDatas.get(position);

        holder.mTvTitle.setText(cart.getName());
        holder.mTvPrice.setText("￥" + cart.getPrice());
        holder.mCheckBox.setChecked(cart.isChecked());
        holder.mNumberAddSubView.setValue(cart.getCount());
        GlideUtils.load(EnjoyshopApplication.sContext, cart.getImgUrl(), holder.mIvLogo);

        //點擊實現選擇功能，當然可以把點擊事件放在item_cb對應的CheckBox上，只是焦點範圍較小
        holder.mLlContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.setIsChecked(!cart.isChecked());
                holder.mCheckBox.setChecked(cart.isChecked());
                if (null != mCheckListener) {
                    mCheckListener.itemChecked(cart, holder.mCheckBox.isChecked());
                }
                notifyDataSetChanged();
                ((ShopCartFragment)mCheckListener).showTotalPrice();
            }
        });

        holder.mNumberAddSubView.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {
                cart.setCount(value);
                //mCartShopProvider.updata(cart);
                ((ShopCartFragment)mCheckListener).showTotalPrice();
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                cart.setCount(value);
                //mCartShopProvider.updata(cart);
                ((ShopCartFragment)mCheckListener).showTotalPrice();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mLlContent;
        private TextView mTvTitle;
        private TextView         mTvPrice;
        private CheckBox mCheckBox;
        private ImageView mIvLogo;
        private NumberAddSubView mNumberAddSubView;

        public ViewHolder(View itemView) {
            super(itemView);
            mLlContent = itemView.findViewById(R.id.ll_item);
            mCheckBox = itemView.findViewById(R.id.checkbox);
            mIvLogo = itemView.findViewById(R.id.iv_view);
            mTvTitle = itemView.findViewById(R.id.text_title);
            mTvPrice = itemView.findViewById(R.id.text_price);
            mNumberAddSubView = itemView.findViewById(R.id.num_control);
        }
    }

    public interface CheckItemListener {
        void itemChecked(ShoppingCart checkBean, boolean isChecked);
    }
}
