package com.example.app.helper;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * <pre>
 *     author : 高勤
 *     e-mail : 984992087@qq.com
 *     time   : 2017/08/06
 *     desc   :recyclerview  分割效果
 *     version: 1.0
 * </pre>
 */

public class DividerItemDecortion extends RecyclerView.ItemDecoration {

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
            state) {
        outRect.top=15;
        outRect.left=5;
        outRect.right=5;
    }
}

