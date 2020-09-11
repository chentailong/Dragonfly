package net.along.dragonflyfm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.orm.SugarContext;
import com.orm.SugarRecord;

import net.along.dragonflyfm.R;
import net.along.dragonflyfm.fragment.AwaitFragment;
import net.along.dragonflyfm.record.AppVisitCount;
import net.along.dragonflyfm.service.FMItemJsonService;
import net.along.dragonflyfm.service.JSONService;
import net.along.dragonflyfm.util.DateBaseUtil;
import net.lzzy.commutils.BaseActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 创建者 by:陈泰龙
 * <p>进入界面时的画面
 * 2020/7/7
 *
 * @author 每天都有最爱的傻子陪着
 */

public class AwaitActivity extends BaseActivity implements AwaitFragment.OnCancelSplashListener, View.OnClickListener {
    private static final String TAG = "AwaitActivity";
    private TextView mTextView;
    private Handler mHandler;
    private Runnable mRunnable;
    Timer mTimer = new Timer();
    public static int recLen = 5;
    public static int seconds = 5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(flag, flag);
        SugarContext.init(this);
        setContentView(R.layout.activity_await);
        record();   //记录访问次数
        startService();
        initVIew();
        startService(new Intent(this, JSONService.class));
        //启动服务
        mTimer.schedule(task, 1000, 1000);
        //等待时间一秒，停顿时间一秒
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable = () -> {
            //从闪屏界面跳转到首界面
            Intent intent = new Intent(AwaitActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 5000);
        //延迟5秒后发送handler信息
    }

    private void record() {
        Iterator visit = AppVisitCount.findAll(AppVisitCount.class);
        long timeStamp = System.currentTimeMillis();
        //获取时间戳
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timeStamp));

        while (visit.hasNext()) {
            AppVisitCount tableObj = (AppVisitCount) visit.next();
            long DataTimeStamp = tableObj.getTimeStamp();
            //获取数据库中的时间戳
            Calendar DataCalendar = Calendar.getInstance();
            DataCalendar.setTime(new Date(DataTimeStamp));
            if (DateBaseUtil.isToday(calendar, DataCalendar)) {
                //判断是否是同一天、
                int count = tableObj.getCount() + 1;
                SugarRecord sugarRecord = tableObj;
                long id = sugarRecord.getId();
                SugarRecord.executeQuery("update APP_VISIT_COUNT set count=? where id=?", count + "", id + "");
                Log.e(TAG, "record: 更新今天的访问次数");
                Log.e(TAG, "record: 新增一次访问APP次数" + count);
                return;
            }
        }
        //不是同一天的情况
        AppVisitCount newData = new AppVisitCount();
        newData.setCount(1);
        newData.setTimeStamp(timeStamp);
        newData.save();
        Log.e(TAG, "新增一次访问APP次数:" + 1);
    }

    /**
     * 初始化视图组件，点击跳过
     */
    private void initVIew() {
        mTextView = findViewById(R.id.await_tv_count_down);
        //跳过
        mTextView.setOnClickListener(this);
        //跳过的监听事件
    }

    /**
     * 启动服务 开始下载初始数据
     */
    private void startService() {
        Intent getFmItemJs = new Intent(this, FMItemJsonService.class);
        startService(getFmItemJs);
    }

    /**
     * 倒计时开始运转
     */
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(() -> {
                String display = recLen + "秒";
                mTextView.setText(display);
                recLen--;   //时间减一
                if (recLen < 0) {
                    mTimer.cancel();
                    mTextView.setVisibility(View.VISIBLE);  //倒计时到0时隐藏字体
                }
            });
        }
    };

    /**
     * 点击事件，点击跳转
     *
     * @param
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.await_tv_count_down:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                if (mRunnable != null) {
                    mHandler.removeCallbacks(mRunnable);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 时间计时等于0
     */
    @Override
    public void cancelCountDown() {
        seconds = 0;
    }

    @Override
    protected boolean isFeatureNoTitle() {
        return true;
    }

    /**
     * 获取网络信息时，应该出现的提示信息，无网络时，是否退出，有网络时应该如何操作
     */
    @Override
    protected void initViews() {

    }

    /**
     * 链接布局界面
     *
     * @return
     */
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_await;
    }

    /**
     * 绑定Fragment id
     *
     * @return
     */
    @Override
    protected int getContainerId() {
        return R.id.fragment_await_container;
    }

    /**
     * 返回Fragment
     *
     * @return
     */
    @Override
    protected Fragment createFragment() {
        return new AwaitFragment();
    }
}

