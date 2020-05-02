package com.example.app.service;

import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

//區域定位

public class LocationService {
    private LocationClient client = null;
    private LocationClientOption mOption,DIYoption;
    private Object  objLock = new Object();

    public LocationService(Context locationContext){
        synchronized (objLock) {
            if(client == null){
                client = new LocationClient(locationContext);
                client.setLocOption(getDefaultLocationClientOption());
            }
        }
    }


    public boolean registerListener(BDAbstractLocationListener listener){
        boolean isSuccess = false;
        if(listener != null){
            client.registerLocationListener(listener);
            isSuccess = true;
        }
        return  isSuccess;
    }

    public void unregisterListener(BDAbstractLocationListener listener){
        if(listener != null){
            client.unRegisterLocationListener(listener);
        }
    }


    public boolean setLocationOption(LocationClientOption option){
        boolean isSuccess = false;
        if(option != null){
            if(client.isStarted())
                client.stop();
            DIYoption = option;
            client.setLocOption(option);
        }
        return isSuccess;
    }

    public LocationClientOption getOption(){
        return DIYoption;
    }


    public LocationClientOption getDefaultLocationClientOption(){
        if(mOption == null){
            mOption = new LocationClientOption();
            mOption.setLocationMode(LocationMode.Hight_Accuracy);//可選，默認高精度，設置定位模式，高精度，低功耗，僅設備
            mOption.setCoorType("bd09ll");//可選，默認gcj02，設置返回的定位結果坐標系，如果配合百度地圖使用，建議設置為bd09ll;
            mOption.setScanSpan(3000);//可選，默認0，即僅定位一次，設置發起定位請求的間隔需要大於等於1000ms才是有效的
            mOption.setIsNeedAddress(true);//可選，設置是否需要地址信息，默認不需要
            mOption.setIsNeedLocationDescribe(true);//可選，設置是否需要地址描述
            mOption.setNeedDeviceDirect(false);//可選，設置是否需要設備方向結果
            mOption.setLocationNotify(false);//可選，默認false，設置是否當gps有效時按照1S1次頻率輸出GPS結果
            mOption.setIgnoreKillProcess(true);//可選，默認true，定位SDK內部是一個SERVICE，並放到了獨立進程，設置是否在stop的時候殺死這個進程，默認不殺死
            mOption.setIsNeedLocationDescribe(true);//可選，默認false，設置是否需要位置語義化結果，可以在BDLocation.getLocationDescribe裡得到，結果類似於“在北京天安門附近”
            mOption.setIsNeedLocationPoiList(true);//可選，默認false，設置是否需要POI結果，可以在BDLocation.getPoiList裡得到
            mOption.SetIgnoreCacheException(false);//可選，默認false，設置是否收集CRASH信息，默認收集

            mOption.setIsNeedAltitude(false);//可選，默認false，設置定位時是否需要海拔信息，默認不需要，除基礎定位版本都可用
        }
        return mOption;
    }

    public void start(){
        synchronized (objLock) {
            if(client != null && !client.isStarted()){
                client.start();
            }
        }
    }
    public void stop(){
        synchronized (objLock) {
            if(client != null && client.isStarted()){
                client.stop();
            }
        }
    }

    public boolean requestHotSpotState(){
        return client.requestHotSpotState();
    }

}
