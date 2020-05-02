package com.example.app.utils;

import android.content.Context;
import android.util.SparseArray;

import com.example.app.bean.HotGoods;
import com.example.app.bean.ShoppingCart;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 高勤
  * Time 2017/8/9
  * Describe: 購物車 數據的存儲
  * 這裡使用的是內存數據 本地數據 的方法
  * 在真實項目中,一般本地數據 換成 網絡數據的獲取(sp中get) 與提交(sp中 put)即可
  * <p>
  * 此處沒有把數據上傳至服務器.通過內存存儲 本地存儲
  * 內存存儲就是普通的集合,本地存儲通過sp進行
  * 為了確保數據的統一性,內存操作後,必須要對sp進行操作
 */

public class CartShopProvider {
    public static final String CART_JSON = "cart_json";

    private Context mContext;

    public SparseArray<ShoppingCart> datas = null;  //類似於hanshMap

    private Gson mGson = new Gson();

    public CartShopProvider(Context context) {
        datas = new SparseArray<>(10);
        this.mContext = context;
        listToSparse();
    }

    /**
     * 必須有這個操作.
     * 因為本地數據和 存儲數據是分開(獨立的)
     * 一進入時,必須將本地讀取到的數據 賦值給內存數據(list集合中) ---->在構造方法那裡調用
     */
    private void listToSparse() {

        List<ShoppingCart> carts = getDataFormLoad();
        if (carts != null && carts.size() > 0) {
            for (ShoppingCart cart : carts) {
                datas.put(cart.getId(), cart);
            }
        }
    }

    /**
     * 從本地讀取數據
     */
    public List<ShoppingCart> getDataFormLoad() {
        String dataStr = PreferencesUtils.getString(mContext, CART_JSON);
        List<ShoppingCart> carts = null;
        if (dataStr != null) {
            carts = mGson.fromJson(dataStr, new TypeToken<List<ShoppingCart>>() {
            }.getType());
        }
        return carts;
    }

    /**
     * 存入
     */
    public void put(HotGoods.ListBean goods) {
        ShoppingCart cart = convertData(goods);
        put(cart);
    }

    public ShoppingCart convertData(HotGoods.ListBean item) {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(item.getId());
        cart.setImgUrl(item.getImgUrl());
        cart.setName(item.getName());
        cart.setPrice(item.getPrice());

        return cart;
    }

    /**
     * 存入
     */
    public void put(ShoppingCart cart) {
        ShoppingCart temp = datas.get(cart.getId());
        if (temp != null) {
            //說明購物車中已經存在這個數據
            temp.setCount(cart.getCount() + 1);
        } else {
            //沒有這個數據.直接賦值
            temp = cart;
            temp.setCount(1);
        }
        datas.put(cart.getId(), temp);     //內存上進行操作
        savaDataToSp();                    //本地存儲操作(更新 刪除也是類似)
    }

    /**
     * 將數據保存在sp中
     */
    public void savaDataToSp() {
        List<ShoppingCart> carts = sparseToList();
        PreferencesUtils.putString(mContext, CART_JSON, mGson.toJson(carts));
    }

    public List<ShoppingCart> getAll() {
        return getDataFormLoad();
    }

    /**
     * 將SparseArray類型數據轉化為List數據
     * 一開始是通過SparseArray類型進行操作的.但讀取時 是list類型
     * 所以需要轉化
     */
    private List<ShoppingCart> sparseToList() {
        int size = datas.size();
        List<ShoppingCart> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(datas.valueAt(i));
        }
        return list;
    }

    /**
     * 更新
     */
    public void updata(ShoppingCart cart) {
        datas.put(cart.getId(), cart);
        savaDataToSp();
    }

    /**
     * 删除
     */
    public void delete(ShoppingCart cart) {
        datas.delete(cart.getId());
        savaDataToSp();
    }


}
