package com.example.app.bean;

/**
 * <pre>
  *     author : 高勤
  *     e-mail : 984992087@qq.com
  *     time   : 2017/08/17
  *     desc   :首頁 分類數據 二級
  * 必須這麼寫,要不然adapter中的接口回調無法進行
  *     version: 1.0
  * </pre>
 */

import java.io.Serializable;


public class Campaign implements Serializable {

    private Long id;
    private String title;
    private String imgUrl;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}