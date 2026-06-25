package com.example.customview.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Choreographer;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

/**
 * 声音光效 View。
 * <p>
 * 通过二次贝塞尔波形绘制底部声音光效。音量和发话状态控制水位线、波幅与扰动幅度；
 * 发话状态还控制透明度、模糊半径和白色蒙版。颜色渐变由独立动画进行水平往返移动。
 *
 * @author Daisw
 */
public class GradientVoiceLightView extends View {
    private static final String TAG = "GradientVoiceLightView";

    // 唤醒态透明度进场动画时长，单位为 ms。
    private static final long WAKE_UP_ANIMATION_DURATION = 133;
    // 唤醒态与发话态之间的透明度、蒙版切换时长，单位为 ms。
    private static final long SPEAKING_ANIMATION_DURATION = 330;

    // Android 12 及以上唤醒态 RenderEffect 的水平、垂直模糊半径，单位为 px。
    private static final int WAKE_UP_BLUR_RADIUS = 50;
    // Android 12 及以上发话态 RenderEffect 的模糊半径，单位为 px。
    private static final int SPEAKING_BLUR_RADIUS = 60;
    // 唤醒态扰动相位每毫秒增加的弧度近似值，数值越大波动越快。
    private static final float FLAME_FREQUENCY_WAKEUP = 0.008f;
    // 发话态扰动相位每毫秒增加的弧度近似值，发话时略快于唤醒态。
    private static final float FLAME_FREQUENCY_SPEAKING = 0.010f;
    // 渐变 Shader 水平往返移动的最大偏移量，单位为 px；实际范围为 -60px～60px。
    private static final int GRADIENT_OFFSET = 60;

    // 发话态 RenderEffect 的 Y 轴偏移，单位为 px；负值表示向上移动。
    private static final int SPEAKING_BLUR_OFFSET_Y = -20;

    // 唤醒态叠加到控制点 Y 坐标上的最大扰动幅度，单位为 px。
    private static final float FLAME_AMPLITUDE_WAKEUP = 6f;
    // 发话态叠加到控制点 Y 坐标上的最大扰动幅度，单位为 px。
    private static final float FLAME_AMPLITUDE_SPEAKING = 8f;
    // 唤醒态单个波长占 View 宽度的比例，0.39 表示 39%。
    private static final float WAVE_WIDTH_RATIO_WAKEUP = 0.39f;
    // 发话态单个波长占 View 宽度的比例，0.42 表示 42%。
    private static final float WAVE_WIDTH_RATIO_SPEAKING = 0.42f;
    // 唤醒态波幅占 View 高度的比例，0.05 表示中心线到波峰/波谷的距离为高度的 5%。
    private static final float WAVE_HEIGHT_RATIO_WAKEUP = 0.05f;
    // 发话态波幅占 View 高度的比例，0.08 表示中心线到波峰/波谷的距离为高度的 8%。
    private static final float WAVE_HEIGHT_RATIO_SPEAKING = 0.08f;

    // 唤醒态波形画笔 Alpha：178/255，约 70%。
    private static final int ALPHA_DEFAULT = 178;
    // 发话态波形画笔 Alpha：217/255，约 85%。
    private static final int ALPHA_SPEAKING_DAY = 217;
    // 发话态白色椭圆蒙版的最大整体 Alpha：89/255，约 35%。
    private static final int ALPHA_END_MASK = 89;

    // 唤醒态水位线距 View 底部的高度比例；0.3 在 200px 高度下对应距底部 60px。
    private static final float LEVEL_LINE_WAKE_UP = 0.3f;
    // 实时音量达到上限时，水位线距 View 底部的高度比例。
    private static final float LEVEL_LINE_SPEAKING_HIGH = 0.5f;
    // 发话态低位或实时音量下限时，水位线距 View 底部的高度比例。
    private static final float LEVEL_LINE_SPEAKING_LOW = 0.4f;
    // 当前值每帧向目标值靠近 12%，避免音量回调间隔不均造成参数阶跃。
    private static final float SOUND_PARAM_SMOOTH = 0.12f;
    // 当前值与目标值之差小于等于该阈值时，直接吸附到目标值。
    private static final float SOUND_PARAM_EPSILON = 0.01f;

    // 渐变流动和模拟音量动画单程的时长，单位为 ms；REVERSE 往返一轮约为 4000ms。
    private static final int ANIM_DURATION = 2000;

    // ====== 状态参数 ======
    private boolean isMeasured;  // 是否已经执行过首次尺寸初始化；当前实现只初始化一次。
    private int mViewWidth;      // 首次测量得到的 View 宽度，单位为 px。
    private int mViewHeight;     // 首次测量得到的 View 高度，单位为 px。
    private final RectF mMaskBounds = new RectF(); // 白色椭圆蒙版的边界，可能向 View 底部以外延伸。

    // ====== 模糊参数 ======
    // RenderEffect 水平、垂直模糊半径，单位为 px；Android 12 以下不会应用。
    public int blurX = WAKE_UP_BLUR_RADIUS;
    public int blurY = WAKE_UP_BLUR_RADIUS;

    // ====== 火焰效果参数 ======
    private float mFlameAmplitude = FLAME_AMPLITUDE_WAKEUP; // 当前扰动幅度，单位为 px。
    private float mFlameFrequency = FLAME_FREQUENCY_WAKEUP; // 当前扰动相位增长速度，近似单位为 rad/ms。
    private long mStartTime; // 帧动画启动时间，单位为 ms，用于计算扰动相位。

    private float mLevelLine; // 当前水位线在 View 中的绝对 Y 坐标，单位为 px。

    private final List<Point> mPointsList = new ArrayList<>(); // 按水位线、波峰、水位线、波谷排列的控制点。

    private float mWaveHeight; // 当前波幅，表示中心线到波峰/波谷的距离，单位为 px。
    private float mWaveWidth; // 当前单个波长，单位为 px。

    private float mTargetFlameAmplitude = FLAME_AMPLITUDE_WAKEUP; // 音量或状态切换产生的目标扰动幅度。
    private float mTargetLevelLine; // 音量或状态切换产生的目标水位线 Y 坐标。
    private float mTargetWaveHeight; // 音量或状态切换产生的目标波幅。

    // ====== 绘制相关参数 ======
    private final Path mWavePath = new Path(); // 每帧根据控制点重新构建的闭合波浪填充路径。
    private Shader mShader; // 水平方向的多色 LinearGradient，在首次测量后创建。
    private final Paint mPaint = new Paint(); // 绘制波浪主体的填充画笔。
    private final Matrix mMatrix = new Matrix(); // 平移颜色 Shader 的复用矩阵。

    private final Paint mMaskPaint = new Paint(); // 绘制底部白色椭圆光斑的画笔。
    private int mMaskAlpha = 0; // 白色椭圆光斑的整体 Alpha。

    private ValueAnimator mGradientOffsetAnimator; // 控制 Shader 在 -GRADIENT_OFFSET～+GRADIENT_OFFSET 间往返。
    private int gradientOffset; // 当前 Shader 水平偏移量，单位为 px。

    // 唤醒态波形透明度进场动画及其加速插值器。
    private ValueAnimator wakeupInAnimator;
    private final AccelerateInterpolator wakeupInterpolator = new AccelerateInterpolator();

    // 唤醒态与发话态之间的透明度、蒙版切换动画。
    private ValueAnimator speakingInAnimator;
    private final Interpolator mAccelerateInterpolator = new AccelerateInterpolator();
    private final Interpolator mDecelerateInterpolator = new DecelerateInterpolator();

    // 波浪主体使用的五段不透明颜色渐变。
    private final int[] DAY_GRADIENT_COLORS = new int[]{
            Color.parseColor("#FF448DD4"),
            Color.parseColor("#FF0E59CE"),
            Color.parseColor("#FF466AE8"),
            Color.parseColor("#FFFF765C"),
            Color.parseColor("#FFC282B5")
    };

    // 与 DAY_GRADIENT_COLORS 一一对应的颜色停靠点。
    private final float[] GRADIENT_POSITIONS = new float[]{0f, 0.20f, 0.50f, 0.83f, 1f};

    // ====== 构造函数 ======
    /** 供代码直接创建 View 时使用。 */
    public GradientVoiceLightView(Context context) {
        this(context, null);
    }

    /** 供 XML 布局解析时使用。 */
    public GradientVoiceLightView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /** 供带默认样式属性的 XML/代码创建场景使用。 */
    public GradientVoiceLightView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    // ====== 初始化 ======
    /** 初始化波浪画笔的基础状态，并提前创建与尺寸无关的渐变偏移动画。 */
    private void init() {
        mPaint.setStyle(Paint.Style.FILL);
        setWaveAlpha(0);
        setupAnimator();
    }

    /**
     * 根据首次测量得到的宽度创建水平多色渐变，并请求重绘。
     * 渐变使用 MIRROR 模式，Shader 平移到边界外时会镜像取色。
     */
    private void updateGradientShader() {
        if (mViewWidth <= 0) return;

        mShader = new LinearGradient(
                0, 0, mViewWidth, 0,
                DAY_GRADIENT_COLORS,
                GRADIENT_POSITIONS,
                Shader.TileMode.MIRROR
        );
        invalidate();
    }

    /**
     * 创建颜色 Shader 的水平往返动画。
     * 此 Animator 只更新 {@link #gradientOffset}，真正的逐帧重绘由 Choreographer 驱动。
     */
    private void setupAnimator() {
        // 仅负责渐变色的横向往返流动，波形刷新由 Choreographer 驱动。
        mGradientOffsetAnimator = ValueAnimator.ofInt(-GRADIENT_OFFSET, GRADIENT_OFFSET);
        mGradientOffsetAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mGradientOffsetAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mGradientOffsetAnimator.setDuration(ANIM_DURATION);
        mGradientOffsetAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mGradientOffsetAnimator.addUpdateListener(animation -> {
            gradientOffset = (int) animation.getAnimatedValue();
        });
    }

    private float currentSpeed = 1f; // 模拟音量当前倍率。
    private float targetSpeed = 1f; // 模拟音量随机选择的目标倍率。
    private static final float SMOOTHING = 0.9f; // 保留旧值的权重，越大越平滑、响应越慢。

    /** 随机调整模拟音量倍率，并通过插值避免倍率突然跳变。 */
    private void adjustSpeedDynamically() {
        double randomChoice = Math.random();

        if (randomChoice < 0.33) {
            // 不变
        } else if (randomChoice < 0.66) {
            // 加快
            targetSpeed = Math.min(targetSpeed + 0.1f, 2f); // 限制最大速度为 2
        } else {
            // 减慢
            targetSpeed = Math.max(targetSpeed - 0.1f, 0.5f); // 限制最小速度为 0.5
        }

        // 插值更新 currentSpeed，使其逐步接近 targetSpeed
        currentSpeed = currentSpeed * SMOOTHING + targetSpeed * (1f - SMOOTHING);
    }

    // ====== 动画更新逻辑 ======
    /**
     * 每次有效帧回调执行一次：平滑追踪目标参数，再更新全部控制点的 Y 坐标。
     * X 坐标保持不变；视觉变化来自水位、波幅以及随时间变化的正弦扰动。
     */
    private void updateWave() {
        // 水位线逐帧接近目标像素坐标。
        mLevelLine = smoothToTarget(mLevelLine, mTargetLevelLine, SOUND_PARAM_SMOOTH, SOUND_PARAM_EPSILON);
        // 火焰扰动幅度逐帧接近实时音量或状态对应的目标值。
        mFlameAmplitude = smoothToTarget(mFlameAmplitude, mTargetFlameAmplitude, SOUND_PARAM_SMOOTH, SOUND_PARAM_EPSILON);
        // 波高逐帧接近目标像素高度，避免音量回调造成阶跃。
        mWaveHeight = smoothToTarget(mWaveHeight, mTargetWaveHeight, SOUND_PARAM_SMOOTH, SOUND_PARAM_EPSILON);

        long now = System.currentTimeMillis();
        float timeOffset = (now - mStartTime) * mFlameFrequency;

        // 更新已有点坐标 + 扰动。控制点按“水位线/波峰/水位线/波谷”循环排列。
        for (int i = 0; i < mPointsList.size(); i++) {
            Point point = mPointsList.get(i);
            float fluctuation = (float) (Math.sin(-timeOffset + i) * mFlameAmplitude);

            switch (i % 4) {
                case 0:
                case 2:
                    point.y = mLevelLine + fluctuation;
                    break;
                case 1:
                    point.y = mLevelLine + mWaveHeight + fluctuation;
                    break;
                case 3:
                    point.y = mLevelLine - mWaveHeight + fluctuation;
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 将当前值按固定比例逐帧逼近目标值，并在足够接近时直接吸附到目标值。
     *
     * @param current 当前值
     * @param target  目标值
     * @param smooth  单帧逼近比例，取值越大响应越快
     * @param epsilon 吸附阈值，差值不超过该值时直接返回目标值
     * @return 本帧更新后的值
     */
    private float smoothToTarget(float current, float target, float smooth, float epsilon) {
        float diff = target - current;
        return Math.abs(diff) <= epsilon ? target : current + diff * smooth;
    }

    /**
     * 在起点和终点之间按指定进度进行线性插值。
     *
     * @param start    区间起点
     * @param end      区间终点
     * @param fraction 插值进度；0 返回起点，1 返回终点
     * @return 对应进度的插值结果
     */
    private float lerp(float start, float end, float fraction) {
        return start + (end - start) * fraction;
    }

    // ====== 生命周期控制 ======
    /** View 挂载到窗口时启动帧循环、颜色流动动画和唤醒态透明度进场动画。 */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    /** View 离开窗口时停止动画，并恢复为下次挂载前的未唤醒状态。 */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
        restoreInitialState();
    }

    private boolean isAnimating = false; // 是否已经注册并持续投递 Choreographer 帧回调。

    /**
     * 启动颜色流动和 Choreographer 帧循环，并在非发话态播放唤醒透明度进场动画。
     * 重复调用时不会重复启动正在运行的动画或重复注册帧回调。
     */
    private void startAnimation() {
        // View 可能先 attach 后获得尺寸，因此动画可以先启动，绘制会等待控制点初始化。
        if (mGradientOffsetAnimator != null && !mGradientOffsetAnimator.isRunning()) {
            mGradientOffsetAnimator.start();
        }
        if (!isAnimating) {
            isAnimating = true;
            mStartTime = System.currentTimeMillis();
            Choreographer.getInstance().postFrameCallback(frameCallback);
        }

        Log.d(TAG, "startAnimation: isSpeaking = " + isSpeaking);
        if (this.isSpeaking || (wakeupInAnimator != null && wakeupInAnimator.isRunning())) {
            return;
        }

        if (wakeupInAnimator == null) {
            wakeupInAnimator = ValueAnimator.ofInt(0, ALPHA_DEFAULT);
            wakeupInAnimator.addUpdateListener(animation -> {
                int alpha = (int)animation.getAnimatedValue();
                setWaveAlpha(alpha);
            });
        }

        wakeupInAnimator.setDuration(WAKE_UP_ANIMATION_DURATION);
        wakeupInAnimator.setInterpolator(wakeupInterpolator);
        wakeupInAnimator.start();

    }

    /**
     * 取消渐变流动、唤醒进场和发话切换三个动画，并移除帧回调。
     * 模拟音量 mSoundAnimator 由 soundRandom(false) 单独控制，不在这里取消。
     */
    private void stopAnimation() {
        if (mGradientOffsetAnimator != null && mGradientOffsetAnimator.isRunning()) {
            mGradientOffsetAnimator.cancel();
        }
        if (wakeupInAnimator != null && wakeupInAnimator.isRunning()) {
            wakeupInAnimator.cancel();
        }
        if (speakingInAnimator != null && speakingInAnimator.isRunning()) {
            speakingInAnimator.cancel();
        }
        if (isAnimating) {
            isAnimating = false;
            Choreographer.getInstance().removeFrameCallback(frameCallback);
        }
    }

    private long lastFrameTimeNs = 0; // 上一次实际更新波浪的帧时间，单位为 ns。
    private static long FRAME_INTERVAL_NS = -1; // 限帧间隔；小于等于0表示不主动跳帧。

    /**
     * 与系统 VSYNC 对齐的帧回调。
     * 未达到限帧间隔时跳过更新并直接预约下一帧；达到间隔时更新波浪并请求重绘，
     * 随后仅在 isAnimating 为 true 时继续预约下一帧。
     */
    private final Choreographer.FrameCallback frameCallback = new Choreographer.FrameCallback() {
        @Override
        public void doFrame(long frameTimeNanos) {
            if (FRAME_INTERVAL_NS > 0 && (frameTimeNanos - lastFrameTimeNs) < FRAME_INTERVAL_NS) {
                Choreographer.getInstance().postFrameCallback(this);
                return;
            }
            lastFrameTimeNs = frameTimeNanos;

            updateWave();
            invalidate();

            if (isAnimating) {
                Choreographer.getInstance().postFrameCallback(this);
            }
        }
    };

    // ====== 测量逻辑 ======
    /**
     * 首次测量后记录固定宽高，初始化波浪控制点、颜色 Shader 和底部光斑蒙版。
     * 受 isMeasured 控制，后续再次测量不会重新计算这些尺寸相关数据。
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isMeasured) {
            Log.d(TAG, "onMeasure");
            isMeasured = true;
            mViewHeight = getMeasuredHeight();
            mViewWidth = getMeasuredWidth();

            restoreInitialState();
            updateGradientShader();
            updateMaskConfig();
        }
    }

    /**
     * 根据当前波长生成覆盖 View 宽度的贝塞尔控制点。
     * 前四个负 X 坐标点会被跳过；额外数量用于保证从 x=0 开始并覆盖到右侧边界。
     */
    private void regeneratePoints() {
        mPointsList.clear();
        // 按“水位线、波峰、水位线、波谷”的顺序生成点，后续用 quadTo 连成波浪。
        int n = (int) Math.round(mViewWidth / mWaveWidth + 0.5);
        Log.d(TAG, "regeneratePoints: " + n + ", " + (4 * n + 5));
        for (int i = 0; i < (4 * n + 5); i++) { // 多出的数量用于抵消左侧被跳过的负坐标点并补足终点。
            float x = i * mWaveWidth / 4 - mWaveWidth;
            if (x < 0) {
                continue;
            }
            float y = 0;

            switch (i % 4) {
                case 0:
                case 2:
                    y = mLevelLine;
                    break;
                case 1:
                    y = mLevelLine + mWaveHeight;
                    break;
                case 3:
                    y = mLevelLine - mWaveHeight;
                    break;
                default:
                    break;
            }

            mPointsList.add(new Point(x, y));
        }
    }

    // ====== 绘制逻辑 ======
    /**
     * 根据当前控制点构建闭合波浪 Path，填充移动的多色渐变，再绘制底部白色光斑。
     * RenderEffect 是 View 级效果，设置后由系统在合成该 View 输出时应用。
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (mPointsList.isEmpty()) return;

        mWavePath.reset();

        // 构建波浪路径
        mWavePath.moveTo(mPointsList.get(0).x, mPointsList.get(0).y);
        for (int i = 0; i < mPointsList.size() - 2; i += 2) {
            mWavePath.quadTo(
                    mPointsList.get(i + 1).x,
                    mPointsList.get(i + 1).y,
                    mPointsList.get(i + 2).x,
                    mPointsList.get(i + 2).y
            );
        }

        // 连接到底部并闭合路径
        mWavePath.lineTo(mPointsList.get(mPointsList.size() - 1).x, mViewHeight);
        mWavePath.lineTo(-mWaveWidth, mViewHeight);
        mWavePath.close();

        // 开启颜色流动时，将当前动画偏移应用到 Shader；关闭后保留上一次矩阵位置。
        if (isLightFlow) {
            mMatrix.setTranslate(gradientOffset, 0);
            mShader.setLocalMatrix(mMatrix);
        }
        mPaint.setShader(mShader);

        canvas.drawPath(mWavePath, mPaint);

        // Android 12 及以上更新 View 级模糊效果。
        drawBlurEffect();

        // 蒙版 Alpha 大于0时绘制底部白色椭圆光斑。
        drawMask(canvas);
    }

    private int curBlurX = 0;
    private int curBlurY = 0;

    /**
     * Android 12 及以上根据当前半径更新 View 的 RenderEffect。
     * 半径未变化时直接复用已有 RenderEffect；发话态额外串联一个向上的 OffsetEffect。
     */
    private void drawBlurEffect() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // RenderEffect 设置成本较高，只有半径发生变化时才重新创建。
            if (blurX == curBlurX && blurY == curBlurY) return;

            curBlurX = blurX;
            curBlurY = blurY;
            RenderEffect blurEffect = RenderEffect.createBlurEffect(
                    curBlurX,
                    curBlurY,
                    Shader.TileMode.CLAMP
            );
            if (isSpeaking) {
                // 发话态将模糊结果上移，让扩散区域更靠近波形主体。
                RenderEffect offsetEffect = RenderEffect.createOffsetEffect(0, SPEAKING_BLUR_OFFSET_Y);
                setRenderEffect(RenderEffect.createChainEffect(blurEffect, offsetEffect));
            } else {
                setRenderEffect(blurEffect);
            }
        }
    }

    /** 当蒙版整体 Alpha 大于0时，绘制底部中心最亮、向四周衰减的白色椭圆光斑。 */
    private void drawMask(Canvas canvas) {
        if (mMaskAlpha > 0) {
            mMaskPaint.setAlpha(mMaskAlpha);
            canvas.drawOval(mMaskBounds, mMaskPaint);
        }
    }

    /** 单个贝塞尔控制点，坐标单位均为 px。 */
    static class Point {
        private float x;
        private float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }

    /**
     * 将音量值映射为目标扰动幅度、波幅和水位线，实际参数由逐帧更新平滑追踪。
     * <p>
     * 收到音量后会取消尚未结束的唤醒透明度进场动画，避免它继续修改波形画笔 Alpha。
     *
     * @param level 输入音量；方法内部限制到 0～100 后参与波形参数计算
     */
    public void setSoundLevel(int level) {
        Log.d(TAG, "configInAnimation level = " + level);
        if (wakeupInAnimator != null && wakeupInAnimator.isRunning()) {
            wakeupInAnimator.cancel();
        }
        // 确保 level 在合法范围内
        level = Math.min(Math.max(level, 0), 100);
        mLevel = level;

        float fraction = level / 100f;

        // 音量回调只更新目标值，实际值由 updateWave() 逐帧平滑追踪。
        setWaveTargetParams(fraction, true);
    }

    /** 设置波浪主体画笔的 Alpha，取值语义为 0～255。 */
    private void setWaveAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    /** 保存底部白色椭圆光斑的整体 Alpha，实际应用发生在 drawMask()。 */
    private void setMaskAlpha(int maskAlpha) {
        mMaskAlpha = maskAlpha;
    }

    /**
     * 根据首次测量尺寸创建底部椭圆区域及横向、纵向组合渐变。
     * ComposeShader 使用 DST_IN，以纵向 Alpha 裁剪横向 Alpha，形成二维衰减光斑。
     */
    private void updateMaskConfig() {
        if (mViewWidth <= 0 || mViewHeight <= 0) return;

        // 蒙版圆心位于底边中点，只显示椭圆上半部分，贴合底部波形区域。
        float maskRadiusY = mViewHeight * 0.25f;
        mMaskBounds.set(0f, mViewHeight - maskRadiusY, mViewWidth, mViewHeight + maskRadiusY);

        // 横向以中心为最亮点，向左右两侧依次衰减并在边缘变为完全透明。
        Shader horizontalShader = new LinearGradient(
                mMaskBounds.left, 0f,
                mMaskBounds.right, 0f,
                new int[]{
                        Color.TRANSPARENT,
                        Color.argb(25, 255, 255, 255),
                        Color.argb(130, 255, 255, 255),
                        Color.WHITE,
                        Color.argb(130, 255, 255, 255),
                        Color.argb(25, 255, 255, 255),
                        Color.TRANSPARENT
                },
                new float[]{
                        0f,
                        0.12f,
                        0.32f,
                        0.5f,
                        0.68f,
                        0.88f,
                        1f
                },
                Shader.TileMode.CLAMP
        );

        // 纵向从椭圆中心向上衰减，在椭圆顶部变为完全透明。
        Shader verticalShader = new LinearGradient(
                0f, mMaskBounds.top,
                0f, mMaskBounds.centerY(),
                new int[]{
                        Color.TRANSPARENT,
                        Color.argb(25, 255, 255, 255),
                        Color.argb(130, 255, 255, 255),
                        Color.WHITE
                },
                new float[]{
                        0f,
                        0.25f,
                        0.65f,
                        1f
                },
                Shader.TileMode.CLAMP
        );

        // 用纵向透明度裁剪横向渐变，形成以底部中心为亮点、向所有方向渐弱的二维蒙版。
        Shader maskShader = new ComposeShader(horizontalShader, verticalShader, PorterDuff.Mode.DST_IN);
        mMaskPaint.setShader(maskShader);
    }

    private boolean isSpeaking; // true 表示当前目标状态为发话态。

    /** @return 当前是否处于发话态。 */
    public boolean isSpeaking() {
        return isSpeaking;
    }

    private int mLevel; // 最近一次经过 0～100 限制后的音量值。

    /** @return 最近一次设置并限制到 0～100 的音量值。 */
    public int getLevel() {
        return mLevel;
    }


    /**
     * 恢复未唤醒的初始状态，为首次绘制或下一次重新挂载做准备。
     * 该方法重置状态参数、目标参数、控制点、波形 Alpha 和蒙版 Alpha，但不启动动画。
     */
    private void restoreInitialState() {
        // 恢复到未唤醒基础状态，等待下一次 onAttachedToWindow() 或 speaking() 驱动显示。
        isSpeaking = false;
        blurX = WAKE_UP_BLUR_RADIUS;
        blurY = WAKE_UP_BLUR_RADIUS;
        mFlameFrequency = FLAME_FREQUENCY_WAKEUP;
        mFlameAmplitude = FLAME_AMPLITUDE_WAKEUP;
        mLevelLine = mViewHeight;
        mWaveWidth = mViewWidth * WAVE_WIDTH_RATIO_WAKEUP;
        mWaveHeight = mViewHeight * WAVE_HEIGHT_RATIO_WAKEUP;
        setWaveTargetParams(0f, false);

        // 已有有效尺寸时，重新生成下一次展示需要的控制点。
        if (mViewWidth > 0 && mViewHeight > 0) {
            regeneratePoints();
        } else {
            mPointsList.clear();
        }

        setWaveAlpha(0);
        setMaskAlpha(0);
    }

    /**
     * 切换发话状态。
     * <p>
     * 发话态会切换波长、模糊半径和扰动频率，并将波幅、扰动幅度、水位线、
     * 波形 Alpha 与白色蒙版过渡到发话配置；false 时向唤醒配置反向过渡。
     *
     * @param speaking true 进入发话态，false 返回唤醒态
     */
    public void speaking(boolean speaking) {
        Log.d(TAG, "current speaking state = " + this.isSpeaking + ", new speaking state = " + speaking);
        if (speaking == this.isSpeaking) {
            return;
        }
        isSpeaking = speaking;
        blurX = speaking ? SPEAKING_BLUR_RADIUS : WAKE_UP_BLUR_RADIUS;
        blurY = speaking ? SPEAKING_BLUR_RADIUS : WAKE_UP_BLUR_RADIUS;
        mFlameFrequency = speaking ? FLAME_FREQUENCY_SPEAKING : FLAME_FREQUENCY_WAKEUP;
        mWaveWidth = mViewWidth * (speaking ? WAVE_WIDTH_RATIO_SPEAKING : WAVE_WIDTH_RATIO_WAKEUP);
        setWaveTargetParams(speaking ? 1f : 0f, false);
        regeneratePoints();
        animateExchange();
    }

    /**
     * 设置火焰幅度、波高和水位线的目标值，实际值由逐帧动画平滑追踪。
     *
     * @param fraction    目标状态进度，当前调用方传入 0～1；0 表示区间下限，1 表示区间上限
     * @param soundDriven 是否由实时音量驱动；音量驱动使用发话态水位线区间，
     *                    状态切换使用唤醒态到发话态的水位线区间
     */
    private void setWaveTargetParams(float fraction, boolean soundDriven) {
        // 火焰扰动幅度始终在唤醒态与发话态最大值之间变化。
        mTargetFlameAmplitude = lerp(FLAME_AMPLITUDE_WAKEUP, FLAME_AMPLITUDE_SPEAKING, fraction);

        // 波高使用相同进度映射，并将相对 View 高度的比例转换为实际像素值。
        mTargetWaveHeight = mViewHeight * lerp(WAVE_HEIGHT_RATIO_WAKEUP, WAVE_HEIGHT_RATIO_SPEAKING, fraction);

        // 状态切换使用“唤醒→发话低位”区间；实时音量使用“发话低位→发话高位”区间。
        float levelLineStart = soundDriven ? LEVEL_LINE_SPEAKING_LOW : LEVEL_LINE_WAKE_UP;
        float levelLineEnd = soundDriven ? LEVEL_LINE_SPEAKING_HIGH : LEVEL_LINE_SPEAKING_LOW;
        float levelLineRatio = lerp(levelLineStart, levelLineEnd, fraction);

        // 比例表示距底部的高度，需转换为 Android 坐标系中从顶部向下递增的 Y 坐标。
        mTargetLevelLine = mViewHeight * (1f - levelLineRatio);
    }

    /**
     * 开关模拟音量数据源。
     * 开启后通过 {@link #soundRandomLiveData} 持续发布模拟值；本方法不会自动调用
     * {@link #setSoundLevel(int)}，观察者需要自行决定如何消费这些数值。
     *
     * @param isSoundRandom true 启动或继续模拟动画，false 取消正在运行的模拟动画
     */
    public void soundRandom(boolean isSoundRandom) {
        if (isSoundRandom) {
            if (mSoundAnimator == null) {
                mockSound();
            }
            if (!mSoundAnimator.isRunning()) {
                mSoundAnimator.start();
            }
        } else {
            if (mSoundAnimator != null && mSoundAnimator.isRunning()) {
                mSoundAnimator.cancel();
            }
        }
    }

    public MutableLiveData<Integer> soundRandomLiveData = new MutableLiveData<>(); // 对外发布模拟音量值。
    private ValueAnimator mSoundAnimator; // 40～50 往返变化的模拟音量基础动画。

    /** 创建模拟音量 Animator，并在每次更新时叠加随机平滑倍率后发布到 LiveData。 */
    private void mockSound() {
        // 基础值在40～50间往返；倍率限制为0.5～2，因此最终发布值理论范围约为20～100。
        mSoundAnimator = ValueAnimator.ofInt(40, 50); // 20-》100
        mSoundAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mSoundAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mSoundAnimator.setDuration(ANIM_DURATION);
        mSoundAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mSoundAnimator.addUpdateListener(animation -> {
            int baseValue = (int) animation.getAnimatedValue();
            // 随机调整
            adjustSpeedDynamically();
            soundRandomLiveData.setValue((int) (baseValue * currentSpeed));
        });
    }

    private boolean isLightFlow = true; // 是否在 onDraw() 中应用当前 Shader 平移矩阵。

    /**
     * 控制绘制时是否更新渐变 Shader 的水平位置。
     * 关闭后 Shader 停留在上一次已应用的位置，偏移动画本身仍继续运行。
     *
     * @param isLightFlow true 更新 Shader 位置，false 冻结当前显示位置
     */
    public void lightFlow(boolean isLightFlow) {
        this.isLightFlow = isLightFlow;
    }

    /**
     * 设置 Choreographer 帧回调的最小处理间隔。
     * 实际公式为 1_000_000_000 / (fps + 1) ns，并受设备 VSYNC 离散帧率影响，
     * 因此结果是近似限帧；fps 小于等于0时保持之前的设置不变。
     *
     * @param fps 大于0时更新帧间隔
     */
    public void setFps(int fps) {
        if (fps > 0) {
            FRAME_INTERVAL_NS = 1000000000 / (fps + 1);
        }
    }

    /**
     * 根据当前 isSpeaking 状态播放唤醒态与发话态之间的 Alpha、蒙版切换动画。
     * 进入发话态时正向 start，退出发话态时 reverse。
     */
    private void animateExchange() {
        Log.d(TAG, "animateExchange: mLevelLine = " + mLevelLine + ", mViewHeight = " + mViewHeight);
        if (speakingInAnimator == null) {
            speakingInAnimator = ValueAnimator.ofInt(ALPHA_DEFAULT, ALPHA_SPEAKING_DAY);
            speakingInAnimator.addUpdateListener(animation -> {
                float progress = animation.getAnimatedFraction();
                int alpha = (int)animation.getAnimatedValue();
                setWaveAlpha(alpha);
                setMaskAlpha((int)(ALPHA_END_MASK * progress));
            });
        }
        speakingInAnimator.setIntValues(ALPHA_DEFAULT, ALPHA_SPEAKING_DAY);
        if (isSpeaking) {
            speakingInAnimator.setInterpolator(mDecelerateInterpolator);
            speakingInAnimator.setDuration(SPEAKING_ANIMATION_DURATION);
            speakingInAnimator.start();
        } else {
            speakingInAnimator.setInterpolator(mAccelerateInterpolator);
            speakingInAnimator.setDuration(SPEAKING_ANIMATION_DURATION);
            speakingInAnimator.reverse();
        }
    }
}
