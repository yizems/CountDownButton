package cn.yzl.countdown_library;

/**
 * 计时监听
 * Created by  伊 on 2016/12/17.
 */
public interface CountDownListener {
    void start();

    void finish();

    /**
     * @param progress 值为0-max_count
     */
    void countDown(int progress);
}
