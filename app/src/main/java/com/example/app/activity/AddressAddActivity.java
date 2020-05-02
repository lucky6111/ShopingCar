package com.example.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.example.app.EnjoyshopApplication;
import com.example.app.R;
import com.example.app.bean.PickerCityAddressBean;
import com.example.app.data.DataManager;
import com.example.app.data.dao.Address;
import com.example.app.utils.GetJsonDataUtil;
import com.example.app.utils.KeyBoardUtils;
import com.example.app.utils.ToastUtils;
import com.example.app.widget.ClearEditText;
import com.example.app.widget.EnjoyshopToolBar;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 高勤
  * Time 2017/8/11
  * Describe: 添加收貨地址
 */

public class AddressAddActivity extends BaseActivity {

    //三級聯動 github地址   //https://github.com/saiwu-bigkoo/Android-PickerView    start:4953

    private Thread thread;
    private static final int MSG_LOAD_DATA    = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED  = 0x0003;
    private boolean isLoaded = false;
    private boolean isDefaultAddress;

    private ArrayList<PickerCityAddressBean>        options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>>            options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    /**
     * 數據庫的操作類型.
     * 如果是0 就是增加(默認)
     * 1 就是修改
     */
    private int addressDoType = 0;

    /**
     * 默認只有 1 條數據
     */
    private boolean isOnlyAddress = true;

    @BindView(R.id.toolbar) EnjoyshopToolBar mToolBar;
    @BindView(R.id.edittxt_consignee) ClearEditText mEditConsignee;
    @BindView(R.id.edittxt_phone) ClearEditText mEditPhone;
    @BindView(R.id.txt_address) TextView mTxtAddress;
    @BindView(R.id.edittxt_add) ClearEditText mEditAddr;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已創建就不再重新創建子線程了
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 寫子線程中的操作,解析省市區數據
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;
                case MSG_LOAD_SUCCESS:
                    isLoaded = true;
                    break;
                case MSG_LOAD_FAILED:
                    ToastUtils.showSafeToast(AddressAddActivity.this, "數據獲取失敗,請重試");
                    break;

            }
        }
    };

    @Override
    protected void init() {
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);

        String name = (String) getIntent().getSerializableExtra("name");
        if (name != null) {
            addressDoType = 1;
            editAddress();
        } else {
            addressDoType = 0;
        }

        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddress();
            }
        });
    }

    @Override
    protected int getContentResourseId() {
        return R.layout.activity_address_add;
    }

    private void initJsonData() {  //解析數據
        /**
         * 注意：assets 目錄下的 Json文件僅供參考，實際使用可自行替換文件
         * 關鍵邏輯在於循環體
         *
         * */
        String JsonData = GetJsonDataUtil.getJson(this, "AllData.json");
        //獲取assets目錄下的 json文件數據

        ArrayList<PickerCityAddressBean> jsonBean = parseData(JsonData);     //用 Gson 轉成實體

        /**
         * 添加縣市數據
         * 注意：如果是添加的JavaBean實體，則實體類需要實現 IPickerViewData 接口，
         * PickerView會通過getPickerViewText方法獲取字符串顯示出來。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//縣市
            ArrayList<String> CityList = new ArrayList<>();//該縣市的鄉鎮市區列表（第二級）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//該縣市的所有街路列表（第三級）

            for (int c = 0; c < jsonBean.get(i).getAreaList().size(); c++) {//該縣市的鄉鎮市區
                String CityName = jsonBean.get(i).getAreaList().get(c).getAreaName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//該鄉鎮市區的所有街路列表

                //如果無街路數據，建議添加空字符串，防止數據為null 導致三個選項長度不匹配造成崩潰
                if (jsonBean.get(i).getAreaList().get(c).getRoadList() == null
                        || jsonBean.get(i).getAreaList().get(c).getRoadList().size() == 0) {
                    City_AreaList.add("");
                } else {
                    for (int d = 0; d < jsonBean.get(i).getAreaList().get(c).getRoadList().size();
                         d++) {//該鄉鎮市區對應街路所有數據
                        String AreaName = jsonBean.get(i).getAreaList().get(c).getRoadList().get(d).getRoadName();

                        City_AreaList.add(AreaName);//添加該鄉鎮市區對應街路所有數據
                    }
                }
                Province_AreaList.add(City_AreaList);//添加該縣市所有鄉鎮市區數據
            }

            //添加鄉鎮市區數據
            options2Items.add(CityList);
            //添加街路數據
            options3Items.add(Province_AreaList);
        }
        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
    }

    public ArrayList<PickerCityAddressBean> parseData(String result) {    //Gson 解析
        ArrayList<PickerCityAddressBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                PickerCityAddressBean entity = gson.fromJson(data.optJSONObject(i).toString(),
                        PickerCityAddressBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    /**
     * 修改新的地址
     */
    private void editAddress() {

        String name = (String) getIntent().getSerializableExtra("name");
        String phone = (String) getIntent().getSerializableExtra("phone");
        String bigAddress = (String) getIntent().getSerializableExtra("BigAddress");
        String smallAddress = (String) getIntent().getSerializableExtra("SmallAddress");
        isDefaultAddress = (boolean) getIntent().getSerializableExtra("isDefaultAddress");

        mEditConsignee.setText(name);
        mEditConsignee.setSelection(name.length());
        mEditPhone.setText(phone);
        mTxtAddress.setText(bigAddress);
        mEditAddr.setText(smallAddress);
    }

    /**
     * 創建新的地址
     */
    public void createAddress() {
        String consignee = mEditConsignee.getText().toString();    //收件人
        String phone = mEditPhone.getText().toString();

        String smallAddress = mEditAddr.getText().toString();
        String bigAddress = mTxtAddress.getText().toString();
        String address = bigAddress + smallAddress;

        //進行非空判斷
        if (TextUtils.isEmpty(consignee)) {
            ToastUtils.showSafeToast(AddressAddActivity.this, "收件人為空,請檢查");
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showSafeToast(AddressAddActivity.this, "聯繫電話為空,請檢查");
            return;
        }

        if (TextUtils.isEmpty(smallAddress) || TextUtils.isEmpty(bigAddress)) {
            ToastUtils.showSafeToast(AddressAddActivity.this, "地址不完整,請檢查");
            return;
        }

        Long userId = EnjoyshopApplication.getApplication().getUser().getId();

        List<Address> mAddressDataList = DataManager.queryAddress(userId);
        if (mAddressDataList != null && mAddressDataList.size() == 0) {
            isOnlyAddress = true;
        } else {
            isOnlyAddress = false;
        }

        Address addBean = new Address();
        addBean.setUserId(userId);
        addBean.setName(consignee);
        addBean.setPhone(phone);
        addBean.setIsDefaultAddress(isDefaultAddress);

        addBean.setBigAddress(bigAddress);
        addBean.setSmallAddress(smallAddress);
        addBean.setAddress(address);
        if (addressDoType == 0) {
            if (isOnlyAddress){
                addBean.setIsDefaultAddress(true);
            }else {
                addBean.setIsDefaultAddress(false);
            }
            DataManager.insertAddress(addBean);
            ToastUtils.showSafeToast(AddressAddActivity.this, "地址添加成功");
        } else if (addressDoType == 1) {
            Long addressId = (Long) getIntent().getSerializableExtra("addressId");
            addBean.setAddressId(addressId);
            DataManager.updateAddress(addBean);
            ToastUtils.showSafeToast(AddressAddActivity.this, "地址修改成功");
        }

        finish();
    }

    @OnClick({R.id.txt_address})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.txt_address:
                if (isLoaded) {
                    KeyBoardUtils.closeKeyboard(mEditConsignee,AddressAddActivity.this);
                    KeyBoardUtils.closeKeyboard(mEditPhone,AddressAddActivity.this);
                    KeyBoardUtils.closeKeyboard(mEditAddr,AddressAddActivity.this);
                    ShowPickerView();
                } else {
                    ToastUtils.showSafeToast(AddressAddActivity.this, "請稍等,數據獲取中");
                }
                break;
        }
    }

    private void ShowPickerView() {// 彈出選擇器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView
                .OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分別是三個級別的選中位置
                String tx = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);

                mTxtAddress.setText(tx);
            }
        })

                .setTitleText("城市選擇")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //設置選中項文字顏色
                .setContentTextSize(20)
                .build();

        pvOptions.setPicker(options1Items, options2Items, options3Items);       //三級選擇器
        pvOptions.show();
    }
}
