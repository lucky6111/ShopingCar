package com.example.app.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.example.app.R;
import com.example.app.bean.Category;

import java.util.List;

/**
 * <pre>
 *     author : 高勤
  *     e-mail : 984992087@qq.com
  *     time   : 2017/08/08
  *     desc   : 分類一級菜單.
  *     version: 1.0
 * </pre>
 */


public class CategoryAdapter extends BaseQuickAdapter<Category, BaseViewHolder> {

    public CategoryAdapter(List<Category> datas) {
        super(R.layout.template_single_text, datas);
    }

    @Override
    protected void convert(BaseViewHolder holder, Category item) {
        holder.setText(R.id.textView, item.getName());
    }
}

