package com.example.app.bean;

import java.io.Serializable;

/**
 * Created by 高勤
  * Time 2017/8/9
  * Describe: 購物車商品信息.數據來源於商品數據.但多了數量、是否選中兩個 屬性
 */

public class ShoppingCart extends HotGoods.ListBean implements Serializable {
    private int count;
    private boolean isChecked = true;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
