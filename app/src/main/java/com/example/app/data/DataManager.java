package com.example.app.data;

import android.util.Log;

import com.example.app.EnjoyshopApplication;

import com.example.app.data.dao.Address;
import com.example.app.data.dao.AddressDao;
import com.example.app.data.dao.User;
import com.example.app.data.dao.UserDao;

import java.util.List;

/**
 * <pre>
 *     author : 高勤
  *     e-mail : 984992087@qq.com
  *     time   : 2018/09/03
  *     desc   : 數據庫管理類
  *     version: 1.0
 * </pre>
 */

public class DataManager {

    /**
     * 添加數據
     *
     * @param user
     */
    public static void insertUser(User user) {
        EnjoyshopApplication.getDaoSession().getUserDao().insert(user);
    }

    /**
     * 删除数据
     *
     * @param id
     */
    public static void deleteUser(Long id) {
        EnjoyshopApplication.getDaoSession().getUserDao().deleteByKey(id);
    }

    /**
     * 更新数据
     *
     * @param user
     */
    public static void updateUser(User user) {
        EnjoyshopApplication.getDaoSession().getUserDao().update(user);
    }

    /**
     * 查询条件为Type=Phone的数据
     *
     * @return
     */
    public static List<User> queryUser(String phone) {
        return EnjoyshopApplication.getDaoSession().getUserDao().queryBuilder().where
                (UserDao.Properties.Phone.eq(phone)).list();
    }

    /**
     * 添加数据
     *
     * @param address
     */
    public static void insertAddress(Address address) {
        EnjoyshopApplication.getDaoSession().getAddressDao().insert(address);
    }

    /**
     * 删除数据
     *
     * @param id
     */
    public static void deleteAddress(Long id) {
        EnjoyshopApplication.getDaoSession().getAddressDao().deleteByKey(id);
    }

    /**
     * 更新数据
     *
     * @param address
     */
    public static void updateAddress(Address address) {
        EnjoyshopApplication.getDaoSession().getAddressDao().update(address);
    }

    /**
     * 查询条件为Type=UserId的数据
     *
     * @return
     */
    public static List<Address> queryAddress(Long userId) {
        return EnjoyshopApplication.getDaoSession().getAddressDao().queryBuilder().where
                (AddressDao.Properties.UserId.eq(userId)).list();
    }
}

