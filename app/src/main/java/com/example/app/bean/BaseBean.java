package com.example.app.bean;

import java.io.Serializable;

/**
 * <pre>
 *     author : 高勤
  *     e-mail : 984992087@qq.com
  *     time   : 2017/08/06
  *     desc   :model的基類
  *     version: 1.0
 * </pre>
 */

public class BaseBean implements Serializable {

    protected int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}