
package com.example.animation.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

import com.example.animation.R;

public class GifView extends View {
    //默认为2秒
    private static final int DEFAULT_MOVIE_DURATION = 2000;
    private int mMovieResourceId;
    private Movie mMovie;
    private long mMovieStart;
    private int mCurrentAnimationTime = 0;
    private float mLeft;
    private float mTop;
    private float mScale;
    private int mMeasuredMovieWidth;
    private int mMeasuredMovieHeight;
    private boolean mVisible = true;
    private volatile boolean mPaused = false;

    public GifView(Context context) {
        this(context, null);
    }

    public GifView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GifView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setViewAttributes(context);
    }

    private void setViewAttributes(Context context) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //从描述文件中读出gif的值，创建出Movie实例
        TypedArray array = context.obtainStyledAttributes(R.styleable.GifView);
        mMovieResourceId = array.getResourceId(R.styleable.GifView_gif, -1);
        mPaused = array.getBoolean(R.styleable.GifView_paused, false);
        array.recycle();
        if (mMovieResourceId != -1) {
            mMovie = Movie.decodeStream(getResources().openRawResource(mMovieResourceId));
        }
    }

    //设置gif图资源
    public void setMovieResource(int movieResId) {
        this.mMovieResourceId = movieResId;
        mMovie = Movie.decodeStream(getResources().openRawResource(
                mMovieResourceId));
        requestLayout();
    }

    public void setMovie(Movie movie) {
        this.mMovie = movie;
        requestLayout();
    }

    public Movie getMovie() {
        return mMovie;
    }

    public void setMovieTime(int time) {
        mCurrentAnimationTime = time;
        invalidate();
    }

    //设置暂停
    public void setPaused(boolean paused) {
        this.mPaused = paused;
        if (!paused) {
            mMovieStart = android.os.SystemClock.uptimeMillis()
                    - mCurrentAnimationTime;
        }
        invalidate();
    }

    //判断gif图是否停止了
    public boolean isPaused() {
        return this.mPaused;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMovie != null) {
            int movieWidth = mMovie.width();
            int movieHeight = mMovie.height();
            int maximumWidth = MeasureSpec.getSize(widthMeasureSpec);
            float scaleW = (float) movieWidth / (float) maximumWidth;
            mScale = 1f / scaleW;
            mMeasuredMovieWidth = maximumWidth;
            mMeasuredMovieHeight = (int) (movieHeight * mScale);
            setMeasuredDimension(mMeasuredMovieWidth, mMeasuredMovieHeight);
        } else {
            setMeasuredDimension(getSuggestedMinimumWidth(),
                    getSuggestedMinimumHeight());
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mLeft = (getWidth() - mMeasuredMovieWidth) / 2f;
        mTop = (getHeight() - mMeasuredMovieHeight) / 2f;
        mVisible = getVisibility() == View.VISIBLE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mMovie != null) {
            if (!mPaused) {
                updateAnimationTime();
                drawMovieFrame(canvas);
                invalidateView();
            } else {
                drawMovieFrame(canvas);
            }
        }
    }

    @SuppressLint("NewApi")
    private void invalidateView() {
        if (mVisible) {
            postInvalidateOnAnimation();
        }
    }

    private void updateAnimationTime() {
        long now = android.os.SystemClock.uptimeMillis();
        //如果第一帧，记录起始时间
        if (mMovieStart == 0) {
            mMovieStart = now;
        }
        //取出动画的时长
        int dur = mMovie.duration();
        if (dur == 0) {
            dur = DEFAULT_MOVIE_DURATION;
        }
        //算出需要显示第几帧
        mCurrentAnimationTime = (int) ((now - mMovieStart) % dur);
    }

    private void drawMovieFrame(Canvas canvas) {
        //设置要显示的帧，绘制即可
        mMovie.setTime(mCurrentAnimationTime);
        canvas.save();
        canvas.scale(mScale, mScale);
        mMovie.draw(canvas, mLeft / mScale, mTop / mScale);
        canvas.restore();
    }

    @SuppressLint("NewApi")
    @Override
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
        mVisible = screenState == SCREEN_STATE_ON;
        invalidateView();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        mVisible = (visibility == VISIBLE && getVisibility() == VISIBLE);
        invalidateView();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = (visibility == VISIBLE && getVisibility() == VISIBLE);
        invalidateView();
    }
}
