package com.example.app.utils;


import android.annotation.SuppressLint;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author : 高勤
  * e-mail : 984992087@qq.com
  * time : 2017\9\22 0022
  * desc : 字符串操作工具包
 */
@SuppressLint("SimpleDateFormat")
public class StringUtils {

    /**
     * 大陸手機號碼十一位數，匹配格式：前三位固定格式+後8位任意數
     * 此方法中前三位格式有：
     * 13+任意數
     * 15+除4的任意數
     * 18+除1和4的任意數
     * 17+除9的任意數
     * 147
     *
     * 台灣手機號碼10位數，皆以09起頭
     */
    public static boolean isMobileNum(String mobiles) {
        //大陸手機號碼
        //String regExp = "13\\d{9}|14[579]\\d{8}|15[0123456789]\\d{8}|17[01235678]\\d{8}|18\\d{9}";
        //台灣手機號碼
        String regExp ="^(886)?09\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }


    /**
     * 設置密碼的長度
     * @param pwd
     * @return
     */
    public static boolean isPwdStrong(String pwd) {
        if (pwd == null || pwd.length() < 6) {
            return false;
        } else {
            return true;
        }
    }

}

