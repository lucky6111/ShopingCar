package com.example.app.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by 高勤
  * Time 2017/8/7
  * Describe: 首頁 商品數據
 */

public class HomeCampaignBean implements MultiItemEntity {

    /**
     * item為大圖在左邊的
     */
    public static final int ITEM_TYPE_LEFT  = 0;

    /**
     * item為大圖在右邊的
     */
    public static final int ITEM_TYPE_RIGHT = 1;

    private Campaign cpOne;
    private Campaign cpTwo;
    private Campaign cpThree;
    private int      id;
    private String   title;
    private int      itemType;

    public HomeCampaignBean(int itemType) {
        this.itemType = itemType;
    }

    public Campaign getCpOne() {
        return cpOne;
    }

    public void setCpOne(Campaign cpOne) {
        this.cpOne = cpOne;
    }

    public Campaign getCpTwo() {
        return cpTwo;
    }

    public void setCpTwo(Campaign cpTwo) {
        this.cpTwo = cpTwo;
    }

    public Campaign getCpThree() {
        return cpThree;
    }

    public void setCpThree(Campaign cpThree) {
        this.cpThree = cpThree;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}

