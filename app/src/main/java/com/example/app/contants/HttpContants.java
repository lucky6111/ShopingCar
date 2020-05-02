package com.example.app.contants;

public interface HttpContants {
    //連接本機 MySQL
    //public static final String BASE_URL = "http://192.168.1.105/shoppingCar/";  //url的基類

    //連接 000webhostapp MySQL
    public static final String BASE_URL = "https://lucky6111.000webhostapp.com/shoppingCar/";  //url的基類

    public static final String HOME_BANNER_URL = BASE_URL + "php/queryBanner.php";  //首頁輪播圖url

    public static final String HOME_CAMPAIGN_URL = BASE_URL + "php/queryGoods.php"; //首頁商品信息url

    public static final String WARES_CAMPAIN_LIST = BASE_URL + "php/queryHotGoods.php"; //熱門商品下的商品列表

    public static final String HOT_WARES = BASE_URL + "php/queryHotWares.php"; //熱賣 fragment 數據

    public static final String WARES_DETAIL = BASE_URL + "html/detail.html"; //商品詳情圖文詳情

    public static final String CATEGORY_LIST = BASE_URL + "php/queryCategory.php"; //分類一級菜單

    public static final String WARES_LIST = BASE_URL + "php/queryWaresPic_1.php";  //分類二級菜單

    public static final String ORDER_CREATE = BASE_URL + "/order/create";        //提交訂單


}
