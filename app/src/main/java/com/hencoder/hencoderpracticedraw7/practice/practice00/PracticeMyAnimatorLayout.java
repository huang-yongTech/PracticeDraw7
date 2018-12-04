package com.hencoder.hencoderpracticedraw7.practice.practice00;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.hencoder.hencoderpracticedraw7.R;

/**
 * Created by huangyong on 2018/12/4
 * 自定义谷歌三维立体动画布局
 */
public class PracticeMyAnimatorLayout extends RelativeLayout {
    private PracticeMyAnimatorView animatorView;

    public PracticeMyAnimatorLayout(Context context) {
        super(context);
    }

    public PracticeMyAnimatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PracticeMyAnimatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        animatorView = findViewById(R.id.my_animator_view);
        AppCompatButton button = findViewById(R.id.my_animator_button);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                animatorView.start();
            }
        });
    }
}
