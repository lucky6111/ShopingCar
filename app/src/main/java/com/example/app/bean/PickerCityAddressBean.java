package com.example.app.bean;


import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

/**
  * TODO<json數據源>
  * 三級聯動 數據源
  *
  * @author: 小嵩
  * @date: 2017/3/16 15:36
  */

public class PickerCityAddressBean implements IPickerViewData {

     /**
     * name : 縣市
     * city : [{"name":"台北市","area":["信義區","中正區"]}]
     * */

     private String         CityName;
    private List<CityBean> AreaList;

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String CityName) {
        this.CityName = CityName;
    }

    public List<CityBean> getAreaList() {
        return AreaList;
    }

    public void setAreaList(List<CityBean> AreaList) {
        this.AreaList = AreaList;
    }


    // 實現 IPickerViewData 接口，
    // 這個用來顯示在PickerView上面的字符串，
    // PickerView會通過IPickerViewData獲取getPickerViewText方法顯示出來。
    @Override
    public String getPickerViewText() {
        return this.CityName;
    }

    public static class CityBean {
        /**
         * name : 鄉鎮市區
         * area : ["中正路","中山街"]
         */

        private String       AreaName;
        private List<RoadBean> RoadList;

        public String getAreaName() {
            return AreaName;
        }

        public void setAreaName(String AreaName) {
            this.AreaName = AreaName;
        }

        public List<RoadBean> getRoadList() {
            return RoadList;
        }

        public void setRoadList(List<RoadBean> RoadList) {
            this.RoadList = RoadList;
        }
    }

    public static class RoadBean {
        /**
         * name : 街路名
         */

        private String RoadName;

        public String getRoadName() {
            return RoadName;
        }

        public void setRoadName(String RoadName) {
            this.RoadName = RoadName;
        }
    }
}
