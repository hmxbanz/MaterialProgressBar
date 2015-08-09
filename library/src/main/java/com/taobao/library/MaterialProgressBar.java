package com.taobao.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;



/**
 * Created by rowandjj on 15/8/6.
 *
 *
 * 一个material效果的progressbar
 * 支持indeterminate与determinate两种模式<br>
 *
 *通过setMode()指定模式<br/>
 *
 *
 * 注：<br>
 * 调整progressbar大小直接通过layout_width/layout_height指定即可
 * wrap_content代表默认大小<br>
 * 在indeterminate模式下设置progress无效，必须先更改模式
 *
 *
 */
public class MaterialProgressBar extends View {

    /**轮子宽度*/
    private int mBarWidth = (int) dp2px(3);
    /**轮子颜色*/
    private int mBarColor = 0xFF5588FF;
    /**轮子半径*/
    private int mCircleRadius = (int) dp2px(25);
    /**转速*/
    private int mSpinSpeed = 2;
    /**当前进度 0~1.0f*/
    private float mProgress = 0.0f;

    private Mode mCurMode = Mode.INDETERMINATE;

    public enum Mode{
        INDETERMINATE/*转动模式*/,DETERMINATE/*普通模式，可以设置progress*/
    }

    private RectF mBounds;

    private Paint mPaint;

    private float mStartAngle = 0;

    private float mRotateAngle;

    private float mSweepAngle = 0;

    private Runnable mAnimRunnable;

    private boolean isAnimStart = false;

    private boolean showRim = false;

    private Paint mRimPaint;

    private int mRimColor = 0xEEC0C0C0;

    private int mRimWidth = (int) dp2px(3);


    public MaterialProgressBar(Context context) {
        super(context);
        init();
    }

    public MaterialProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context, attrs);
        init();
    }

    private void parseAttributes(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MaterialProgressBar);
        mProgress = array.getFloat(R.styleable.MaterialProgressBar_bar_progress,0.0f);
        mBarColor = array.getColor(R.styleable.MaterialProgressBar_bar_color,mBarColor);
        mBarWidth = array.getDimensionPixelSize(R.styleable.MaterialProgressBar_bar_width,mBarWidth);
        int mode = array.getInt(R.styleable.MaterialProgressBar_bar_mode,0);
        switch (mode){
            case 0:
                mCurMode = Mode.INDETERMINATE;
                break;
            case 1:
                mCurMode = Mode.DETERMINATE;
                break;
        }
        mRimColor = array.getColor(R.styleable.MaterialProgressBar_rim_color,mRimColor);
        mRimWidth = array.getDimensionPixelSize(R.styleable.MaterialProgressBar_rim_width, mRimWidth);
        showRim = array.getBoolean(R.styleable.MaterialProgressBar_bar_rimshown,showRim);
        array.recycle();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBarWidth);
        mPaint.setColor(mBarColor);

        mRimPaint = new Paint();
        mRimPaint.setAntiAlias(true);
        mRimPaint.setStrokeWidth(mRimWidth);
        mRimPaint.setColor(mRimColor);
        mRimPaint.setDither(true);
        mRimPaint.setStyle(Paint.Style.STROKE);

        if(mCurMode == Mode.INDETERMINATE){
            startAnim();
            isAnimStart = true;
        }
    }

    private void startAnim() {
        if(mAnimRunnable == null)
            mAnimRunnable = new AnimRunnable();
        post(mAnimRunnable);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //一定要removecallback
        if(mAnimRunnable != null)
            removeCallbacks(mAnimRunnable);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mPaint.setStrokeWidth(mBarWidth);
        mPaint.setColor(mBarColor);
        mRimPaint.setStrokeWidth(mRimWidth);
        mRimPaint.setColor(mRimColor);

        if(mCurMode == Mode.INDETERMINATE){
            canvas.save();
            if(isInEditMode()){
                mStartAngle = 0;
                mSweepAngle = 270;
            }
            if(showRim){
                canvas.drawCircle(mBounds.centerX(),mBounds.centerY(),mBounds.width()/2,mRimPaint);
            }
            //控制自身旋转
            canvas.rotate(mRotateAngle+=4,mBounds.centerX(),mBounds.centerY());
            //根据bounds画弧
            canvas.drawArc(mBounds, mStartAngle, mSweepAngle, false, mPaint);
            canvas.restore();
            if(!isAnimStart){
                isAnimStart = true;
                startAnim();
            }

        }else{
            if(isInEditMode()){
                mProgress = 0.6f;
            }
            isAnimStart = false;
            //clear动画
            getHandler().removeCallbacks(mAnimRunnable);
            //draw progress
            canvas.rotate(-90, mBounds.centerX(), mBounds.centerY());
            if(showRim){
                canvas.drawCircle(mBounds.centerX(),mBounds.centerY(),mBounds.width()/2,mRimPaint);
            }
            canvas.drawArc(mBounds,0,mProgress*360,false,mPaint);
        }

    }

    public void setProgress(float progress){
        if(progress > 1.0f || progress < 0f){
            return;
        }
        if(this.mCurMode == Mode.INDETERMINATE){
            return;
        }

        this.mProgress = progress;
        postInvalidate();

    }

    public float getProgress(){
        return this.mProgress;
    }

    public void setMode(Mode mode){
        this.mCurMode = mode;
        postInvalidate();
    }

    public Mode getMode(){
        return this.mCurMode;
    }

    public int getBarWidth() {
        return mBarWidth;
    }

    public void setBarWidth(int mBarWidth) {
        this.mBarWidth = mBarWidth;
        postInvalidate();
    }

    public int getBarColor() {
        return mBarColor;
    }

    public void setBarColor(int mBarColor) {
        this.mBarColor = mBarColor;
        postInvalidate();
    }

    public int getRimColor() {
        return mRimColor;
    }

    public void setRimColor(int mRimColor) {
        this.mRimColor = mRimColor;
        postInvalidate();
    }

    public int getRimWidth() {
        return mRimWidth;
    }

    public void setRimWidth(int mRimWidth) {
        this.mRimWidth = mRimWidth;
        postInvalidate();
    }

    public boolean isShowRim() {
        return showRim;
    }

    public void setShowRim(boolean showRim) {
        this.showRim = showRim;
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //计算自己需要的宽度和高度
        int width = mCircleRadius*2;
        int height = mCircleRadius*2;

        //考虑父容器的测量规则
        setMeasuredDimension(getResolvedSize(width, widthMeasureSpec), getResolvedSize(height, heightMeasureSpec));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        //简化处理，以最大的padding作为padding
        int padding = Math.max(Math.max(paddingLeft, paddingRight), Math.max(paddingTop, paddingBottom));
        int diameter;
        //保证bounds是一个正方形
        if(w >= h){
            diameter = h;
            mBounds = new RectF(padding+mBarWidth+(w-h)/2,padding+mBarWidth,diameter-padding-mBarWidth+(w-h)/2,diameter-padding-mBarWidth);
        }else if(w < h){
            diameter = w;
            mBounds = new RectF(padding+mBarWidth,padding+mBarWidth+(h-w)/2,diameter-padding-mBarWidth,diameter-padding-mBarWidth+(h-w)/2);
        }

    }

    private int getResolvedSize(int desiredSize, int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int size;
        if(mode == MeasureSpec.EXACTLY){
            size = specSize;
        }else{
            size = desiredSize;
            if(mode == MeasureSpec.AT_MOST){
                size = Math.min(desiredSize,specSize);
            }
        }
        return size;
    }


    private float dp2px(int dip){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dip,getResources().getDisplayMetrics());
    }


    private static final float delta = 6f;
    private float temp = 0;
    class AnimRunnable implements Runnable{
        @Override
        public void run() {
            if (mStartAngle == temp) {
                mSweepAngle += delta;
            }
            if (mSweepAngle >= 280 || mStartAngle > temp) {
                mStartAngle += delta;
                if(mSweepAngle > 20) {
                    mSweepAngle -= delta;
                }
            }
            if (mStartAngle > temp + 280) {
                temp = mStartAngle;
                mStartAngle = temp;
                mSweepAngle = 20;
            }

            postInvalidate();
            postDelayed(this,mSpinSpeed);
        }
    }


    //---  状态保存与恢复 -----

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(! (state instanceof SavedState)){
            super.onRestoreInstanceState(state);
            return;
        }
        //先恢复父类的状态
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        //在恢复自己的状态
        this.mCurMode = savedState.mCurMode == 0 ? Mode.INDETERMINATE : Mode.DETERMINATE;
        this.mRimWidth = savedState.mRimWidth;
        this.mRimColor = savedState.mRimColor;
        this.mBarColor = savedState.mBarColor;
        this.mBarWidth = savedState.mBarWidth;
        this.showRim = savedState.showRim;
        this.isAnimStart = savedState.isAnimStart;
        this.mProgress = savedState.mProgress;
    }

    @Override
    protected Parcelable onSaveInstanceState() {

        //相当于是做了一层包装
        //先保存父类的状态，然后包装，再保存自己的状态
        Parcelable parcelable = super.onSaveInstanceState();
        SavedState savedState = new SavedState(parcelable);

        savedState.mCurMode = (this.mCurMode == Mode.INDETERMINATE) ? 0 : 1;
        savedState.mRimWidth = this.mRimWidth;
        savedState.mRimColor = this.mRimColor;
        savedState.mBarColor = this.mBarColor;
        savedState.mBarWidth = this.mBarWidth;
        savedState.showRim = this.showRim;
        savedState.isAnimStart = this.isAnimStart;
        savedState.mProgress = this.mProgress;

        return savedState;
    }


    static class SavedState extends BaseSavedState{

        float mProgress;
        int mBarWidth;
        int mBarColor;
        int mRimColor;
        int mRimWidth;
        boolean showRim;
        boolean isAnimStart;
        int mCurMode;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel source) {
            super(source);
            this.mProgress = source.readFloat();
            this.mBarWidth = source.readInt();
            this.mBarColor = source.readInt();
            this.mRimColor = source.readInt();
            this.mRimWidth = source.readInt();
            this.showRim = source.readByte() != 0;
            this.isAnimStart = source.readByte() != 0;
            this.mCurMode = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeFloat(this.mProgress);
            dest.writeInt(this.mBarWidth);
            dest.writeInt(this.mBarColor);
            dest.writeInt(this.mRimColor);
            dest.writeInt(this.mRimWidth);
            dest.writeByte((byte) (this.showRim ? 1 : 0));
            dest.writeByte((byte) (this.isAnimStart ? 1 : 0));
            dest.writeInt(this.mCurMode);
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };

    }
}

