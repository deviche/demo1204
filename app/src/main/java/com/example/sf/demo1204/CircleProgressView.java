package com.example.sf.demo1204;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by sf on 2017/12/4 0004.
 */

public class CircleProgressView extends View {
    private int mCurrent;//当前进度
    private Paint mPaintOut;
    private Paint mPaintCurrent;
    private Paint mPaintText;
    private float mPaintWidth;//画笔宽度
    private int mPaintColor = Color.RED;//画笔颜色
    private int mTextColor = Color.BLACK;//字体颜色
    private float mTextSize;//字体大小
    private int location;//从哪个位置开始
    private float startAngle;// 开始角度
    private OnLoadingCompleteListener onLoadingCompleteListener;

    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleViewProgressView);
        location = array.getInt(R.styleable.CircleViewProgressView_location, 1);
        mPaintWidth = array.getDimension(R.styleable.CircleViewProgressView_progress_paint_width, dip2px(context, 4));//默认4dp
        mPaintColor = array.getInt(R.styleable.CircleViewProgressView_progress_paint_color, mPaintColor);
        mTextSize = array.getDimension(R.styleable.CircleViewProgressView_progress_text_size, dip2px(context, 18));//默认18dp
        mTextColor = array.getColor(R.styleable.CircleViewProgressView_progress_text_color, mTextColor);
        array.recycle();
        initPaint();
        if (location == 1) {//默认从左侧开始画进度
            startAngle = -180;
        } else if (location == 2) {
            startAngle = -90;
        } else if (location == 3) {
            startAngle = 0;
        } else if (location == 4) {
            startAngle = 90;
        }

    }

    private void initPaint() {
        //画笔---》 背景圆弧
        mPaintOut = new Paint();
        mPaintOut.setAntiAlias(true);
        mPaintOut.setStrokeWidth(mPaintWidth);
        //设置空心
        mPaintOut.setStyle(Paint.Style.STROKE);
        mPaintOut.setColor(Color.GRAY);
        //什么意思？？
        mPaintOut.setStrokeCap(Paint.Cap.ROUND);
        // 画笔----》 进度圆弧
        mPaintCurrent = new Paint();
        mPaintCurrent.setAntiAlias(true);
        mPaintCurrent.setStrokeWidth(mPaintWidth);
        mPaintCurrent.setStyle(Paint.Style.STROKE);
        mPaintCurrent.setColor(mPaintColor);
        mPaintCurrent.setStrokeCap(Paint.Cap.ROUND);

        //画笔-----》绘制字体
        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setStyle(Paint.Style.FILL);
        mPaintText.setColor(mTextColor);
        mPaintText.setTextSize(mTextSize);
    }

    /*public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }*/

    //根据手机实际分辨率 从dp单位转换成 px（像素）
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //这一段到底干了什么
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = width > height ? height : width;
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画外轮廓和外背景圆弧
        RectF rectF = new RectF(mPaintWidth / 2, mPaintWidth / 2, getWidth() - mPaintWidth / 2, getHeight() - mPaintWidth / 2);
        canvas.drawRect(rectF, mPaintOut);
        canvas.drawArc(rectF, 0, 360, false, mPaintOut);

        //绘制当前进度
        float sweepAngle = 360 * mCurrent / 100;
        canvas.drawArc(rectF, startAngle, sweepAngle, false, mPaintCurrent);

        //绘制进度数字
        String text = mCurrent + "%";
        float textWidth = mPaintText.measureText(text, 0, text.length());
        float dx = getWidth() / 2 - textWidth / 2;
        Paint.FontMetricsInt fontMetricsInt = mPaintText.getFontMetricsInt();


        //绘制自定义控件里面字的要点：
            // 1.确定字的内容和属性；
            // 2.确定字的长度，边界与基线（这些参数都是相对于控件）；
            // 3.写回调与canvas.drawText
        float dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        float baseLine = getHeight() / 2 + dy;
        canvas.drawText(text, dx, baseLine, mPaintText);
        if (onLoadingCompleteListener != null & mCurrent == 100) {
            onLoadingCompleteListener.onComplete();
        }


    }

    //获取当前进度
    public int getmCurrent() {
        return mCurrent;
    }

    //设置当前进度并刷新
    public void setmCurrent(int mCurrent) {
        this.mCurrent = mCurrent;
        invalidate();
    }

    public void setOnLoadingCompleteListener(OnLoadingCompleteListener onLoadingCompleteListener) {
        this.onLoadingCompleteListener = onLoadingCompleteListener;
    }
}
