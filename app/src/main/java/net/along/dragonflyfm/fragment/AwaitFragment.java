package net.along.dragonflyfm.fragment;


import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import net.along.dragonflyfm.R;
import net.lzzy.commutils.BaseFragment;

import java.util.Calendar;

/**
 * 创建者 by:陈泰龙
 * <p>
 * 2020/7/7
 **/

public class AwaitFragment extends BaseFragment {
    /**
     * 创建一个图片存放数组器
     */
    private int[] images = new int[]{R.drawable.await1, R.drawable.await2, R.drawable.await3};
    private OnCancelSplashListener listener;

    /**
     * 填充数据到界面
     */
    @Override
    public void populate() {
        int index = Calendar.getInstance().get(Calendar.SECOND) % 3;
        View wall = find(R.id.fragment_await_wall);
        wall.setBackgroundResource(images[index]);
        wall.setOnClickListener( v -> listener.cancelCountDown());
    }


    /**
     * 调用返回布局
     * @return
     */
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_await;
    }

    /**
     * 关联与父类的关系  既Fragment与Activity建立联系时必须使用
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnCancelSplashListener) {
            listener = (OnCancelSplashListener) context;
        } else {
            throw new ClassCastException(context.toString() + "必须实现OnCancelSplashListener");
        }

    }

    /**
     * fragment下必须重写的一个方法，解绑父类联系
     */
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnCancelSplashListener {
        /**
         * 取消倒计时
         */
        void cancelCountDown();

    }
}
