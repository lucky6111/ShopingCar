package com.example.app.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * <pre>
 *     author : 高勤
  *     e-mail : 984992087@qq.com
  *     time   : 2018/08/23
  *     desc   : 關閉軟鍵盤
  *     version: 1.0
 * </pre>
 */

public class KeyBoardUtils {
    /**
     * 打開軟鍵盤
     *
     * @param mEditText 輸入框
     * @param mContext  上下文
     */
    public static void openKeyboard(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    public static void openKeyboardForce(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
    }

    /**
     * 關閉軟鍵盤
     *
     * @param mEditText 輸入框
     * @param mContext  上下文
     */
    public static void closeKeyboard(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

}
