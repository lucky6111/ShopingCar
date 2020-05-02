package com.example.app.utils;


import android.os.CountDownTimer;
import android.widget.TextView;

import com.example.app.R;


/**
 * Created by 高勤
  * Time 2017/8/12
  * Describe: 定時器
 */
public class CountTimerView extends CountDownTimer {

    public static final int TIME_COUNT = 61000; //時間防止從59s開始顯示（以倒計時60s為例子）
    private TextView btn;
    private int      endStrRid;


    /**
     * 參數 millisInFuture 倒計時總時間（如60S，120s等）
     * 參數 countDownInterval 漸變時間（每次倒計1s）
     * 參數 btn 點擊的按鈕(因為Button是TextView子類，為了通用我的參數設置為TextView）
     * 參數 endStrRid 倒計時結束後，按鈕對應顯示的文字
     */
    public CountTimerView(long millisInFuture, long countDownInterval, TextView btn, int
            endStrRid) {
        super(millisInFuture, countDownInterval);
        this.btn = btn;
        this.endStrRid = endStrRid;
    }


    /**
     * 參數上面有註釋
     */
    public CountTimerView(TextView btn, int endStrRid) {
        super(TIME_COUNT, 1000);
        this.btn = btn;
        this.endStrRid = endStrRid;
    }

    public CountTimerView(TextView btn) {
        super(TIME_COUNT, 1000);
        this.btn = btn;
        this.endStrRid = R.string.code_end;
    }


    // 計時完畢時觸發
    @Override
    public void onFinish() {
        btn.setText(endStrRid);
        btn.setEnabled(true);
    }

    // 計時過程顯示
    @Override
    public void onTick(long millisUntilFinished) {
        btn.setEnabled(false);
        btn.setText(millisUntilFinished / 1000 + " 秒後可重新發送");

    }
}

