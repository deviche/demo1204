package com.example.sf.demo1204;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private  CircleProgressView   mCircleProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCircleProgressView = findViewById(R.id.circle_view);

        //绘制进度圆弧的回调
        ValueAnimator  animator = ValueAnimator.ofFloat(0,100);
        animator.setDuration(4000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float current = (float) animation.getAnimatedValue();
                mCircleProgressView.setmCurrent((int)current);
            }
        });
        animator.start();


        mCircleProgressView.setOnLoadingCompleteListener(new OnLoadingCompleteListener() {
            @Override
            public void onComplete() {
                Toast.makeText(MainActivity.this, "加载完成", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
