package cn.yzl.countdown_library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * 逻辑:
 * 0,正常状态
 * 1,点击后,请求接口/其他操作-准备状态
 * 2,准备后,开始倒计时-计时状态
 * 3,计时结束后-结束状态
 * <p>
 * Created by  伊 on 2016/12/17.
 */
public class CountDownButton extends Button {


    /**
     * 是否有准备阶段,默认 有
     */
    private boolean hasPrepared;

    /**
     * 正常
     */
    private static final int STATE_NORMAL = 1;

    /**
     * 计时中
     */
    private static final int STATE_TIME = STATE_NORMAL << 1;

    /**
     * 准备开始
     */
    private static final int STATE_PREPARE = STATE_TIME << 1;

    /**
     * 准备开始
     */
    private static final int STATE_END = STATE_PREPARE << 1;


    @IntDef({STATE_NORMAL, STATE_PREPARE, STATE_TIME, STATE_END})
    private @interface State {
    }

    /**
     * 状态
     */
    private int state;

    private MyCountDownTimer timer;

    private int maxCount;

    private int spaceTime;

    /**
     * 正常的 文字
     */
    private String normalText = "发送验证码";

    /**
     * 结束的 文字
     */
    private String endText = "重新发送";

    /**
     * 倒计时文字 模板 如: 00秒后重新发送,自动替换00,00可以在任何位置
     */
    private String timerText = "00秒后重新发送";

    private String prepareText = "正在发送";

    private int normalTextColor = Color.parseColor("#000000");

    private int endTextColor = Color.parseColor("#000000");

    private int timerTextColor = Color.parseColor("#14A5E2");

    private int prepareTextColor = Color.parseColor("#000000");

    private int normalBg = R.drawable.bg_count_button_default;

    private int endBg = R.drawable.bg_count_button_default;

    private int timerBg = R.drawable.bg_count_button_default;

    private int prepareBg = R.drawable.bg_count_button_default;

    public CountDownButton(Context context) {
        super(context);
        init();
    }

    public CountDownButton(Context context, AttributeSet attrs) throws Exception {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CountDownButton);

        String tempString = typedArray.getString(R.styleable.CountDownButton_normal_text);
        if (!TextUtils.isEmpty(tempString)) {
            normalText = tempString;
        }
        tempString = typedArray.getString(R.styleable.CountDownButton_prepare_text);
        if (!TextUtils.isEmpty(tempString)) {
            prepareText = tempString;
        }
        tempString = typedArray.getString(R.styleable.CountDownButton_timer_text);
        if (!TextUtils.isEmpty(tempString)) {
            timerText = tempString;
        }
        tempString = typedArray.getString(R.styleable.CountDownButton_end_text);
        if (!TextUtils.isEmpty(tempString)) {
            endText = tempString;
        }

        normalTextColor = typedArray.getColor(R.styleable.CountDownButton_normal_text_color, Color.parseColor("#000000"));

        prepareTextColor = typedArray.getColor(R.styleable.CountDownButton_prepare_text_color, Color.parseColor("#000000"));

        timerTextColor = typedArray.getColor(R.styleable.CountDownButton_timer_text_color, Color.parseColor("#000000"));

        endTextColor = typedArray.getColor(R.styleable.CountDownButton_end_text_color, Color.parseColor("#000000"));

        normalBg = typedArray.getResourceId(R.styleable.CountDownButton_normal_bg, R.drawable.bg_count_button_default);

        prepareBg = typedArray.getResourceId(R.styleable.CountDownButton_prepare_bg, R.drawable.bg_count_button_default);
        timerBg = typedArray.getResourceId(R.styleable.CountDownButton_timer_bg, R.drawable.bg_count_button_default);
        endBg = typedArray.getResourceId(R.styleable.CountDownButton_end_bg, R.drawable.bg_count_button_default);

        hasPrepared = typedArray.getBoolean(R.styleable.CountDownButton_has_prepare, false);

        maxCount = typedArray.getInt(R.styleable.CountDownButton_max_count, 60);

        spaceTime = typedArray.getInt(R.styleable.CountDownButton_space_time, 1);

        if (spaceTime == 0) {
            throw new Exception("时间间距不能为 0");
        }

        if (!timerText.contains("00")) {
            throw new Exception("计时模板中必须含有 00");
        }
        typedArray.recycle();
        init();

    }

    /**
     * 初始化
     */
    private void init() {
        state = STATE_NORMAL;
        changeState(STATE_NORMAL);
    }


    @Override
    public boolean performClick() {
        if (state == STATE_NORMAL || state == STATE_END) {
            changeState(STATE_PREPARE);
            if (!hasPrepared) {
                start();
            }
            return super.performClick();
        } else {
            return false;
        }
    }

    /**
     * 更换状态
     *
     * @param state
     */
    public void changeState(@State int state) {
        switch (state) {
            case STATE_NORMAL:
                setText(normalText);
                setTextColor(normalTextColor);
                setBackgroundResource(normalBg);
                break;
            case STATE_PREPARE:
                if (hasPrepared) {
                    setText(prepareText);
                    setTextColor(prepareTextColor);
                    setBackgroundResource(prepareBg);
                }
                break;
            case STATE_TIME:
                timer = new MyCountDownTimer(maxCount * 1000, spaceTime * 1000);
                timer.start();
                setTextColor(timerTextColor);
                setBackgroundResource(timerBg);
                break;
            case STATE_END:
                setText(endText);
                setTextColor(endTextColor);
                setBackgroundResource(endBg);
                break;
        }
    }

    public void start() {
        changeState(STATE_TIME);
    }

    public void restart() {
        timer = new MyCountDownTimer(maxCount * 1000, spaceTime * 1000);
        timer.start();
    }

    private class MyCountDownTimer extends CountDownTimer {
        /**
         * @param millisInFuture    表示以毫秒为单位 倒计时的总数
         *                          <p>
         *                          例如 millisInFuture=1000 表示1秒
         * @param countDownInterval 表示 间隔 多少微秒 调用一次 onTick 方法
         *                          <p>
         *                          例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            changeState(STATE_END);
        }

        /**
         * @param millisUntilFinished 剩余时间
         */
        @Override
        public void onTick(long millisUntilFinished) {
            setText(timerText.replace("00", String.valueOf(millisUntilFinished / 1000)));
        }

    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(@IntRange(from = 1, to = Integer.MAX_VALUE) int maxCount) {
        this.maxCount = maxCount;
    }

    public int getSpaceTime() {
        return spaceTime;
    }

    public void setSpaceTime(@IntRange(from = 1, to = Integer.MAX_VALUE) int spaceTime) {
        this.spaceTime = spaceTime;
    }

    public void setNormalText(String normalText) {
        this.normalText = normalText;
    }

    public void setEndText(String endText) {
        this.endText = endText;
    }

    /**
     * 准备时文字 模板 如: 00秒后重新发送,自动替换00,00可以在任何位置
     */
    public void setTimerText(String timerText) {
        this.timerText = timerText;
    }

    public void setPrepareText(String prepareText) {
        this.prepareText = prepareText;
    }

    public void setNormalTextColor(int normalTextColor) {
        this.normalTextColor = normalTextColor;
    }

    public void setEndTextColor(int endTextColor) {
        this.endTextColor = endTextColor;
    }

    public void setTimerTextColor(int timerTextColor) {
        this.timerTextColor = timerTextColor;
    }

    public void setPrepareTextColor(int prepareTextColor) {
        this.prepareTextColor = prepareTextColor;
    }

    public void setNormalBg(@DrawableRes int normalBg) {
        this.normalBg = normalBg;
    }

    public void setEndBg(@DrawableRes int endBg) {
        this.endBg = endBg;
    }

    public void setTimerBg(@DrawableRes int timerBg) {
        this.timerBg = timerBg;
    }

    public void setPrepareBg(@DrawableRes int prepareBg) {
        this.prepareBg = prepareBg;
    }


    /**
     * 是否有准备阶段
     *
     * @param hasPrepared
     */
    public void setHasPrepared(boolean hasPrepared) {
        this.hasPrepared = hasPrepared;
    }

}

//结尾附上彩蛋

//┏┓　　　┏┓
//┏┛┻━━━┛┻┓
//┃　　　　　　　┃ 　
//┃　　　━　　　┃
//┃　┳┛　┗┳　┃
//┃　　　　　　　┃
//┃　　　┻　　　┃
//┃　　　　　　　┃
//┗━┓　　　┏━┛
//┃　　　┃  神兽保佑　　　　　　　　
//┃　　　┃  代码无BUG！
//┃　　　┗━━━┓
//┃　　　　　　　┣┓
//┃　　　　　　　┏┛
//┗┓┓┏━┳┓┏┛
// ┃┫┫　┃┫┫
// ┗┻┛　┗┻┛
