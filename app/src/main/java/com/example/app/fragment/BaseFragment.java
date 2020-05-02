package com.example.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app.EnjoyshopApplication;
import com.example.app.activity.LoginActivity;
import com.example.app.bean.User;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 高勤
  * Time 2017/8/9
  * Describe: fragment的基類
 */

public abstract class BaseFragment extends Fragment {

    private   View           mView;
    protected Bundle         savedInstanceState;
    public    Context        mContext;
    protected LayoutInflater mInflater;
    Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mInflater = inflater;
        this.savedInstanceState=savedInstanceState;
        mView=mInflater.inflate(getContentResourseId(), null);
        unbinder= ButterKnife.bind(this,mView);
        init();
        return mView;
    }

    protected abstract void init();

    protected abstract int getContentResourseId();

    public void startActivity(Intent intent, boolean isNeedLogin){
        if (isNeedLogin) {
            User user = EnjoyshopApplication.getInstance().getUser();
            if (user != null) {
                super.startActivity(intent);    //需要登錄,切已經登錄.直接跳到目標activity中
            } else {
                EnjoyshopApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                super.startActivity(loginIntent);
            }
        } else {
            super.startActivity(intent);
        }
    }
}

