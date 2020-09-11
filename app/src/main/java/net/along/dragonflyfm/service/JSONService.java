package net.along.dragonflyfm.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import net.along.dragonflyfm.util.CommonHttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 创建者 by:陈泰龙
 * <p>  解析JSON数据的类，在Fragment中调用将更加简洁，使代码更加清爽
 * 2020/7/15
 **/

public class JSONService extends IntentService {

    private static final String TAG = "JSONUtils";
    private static JSONArray district = null;
    //所有地区JSON数据
    private static JSONArray categories = null;
    //电台类型JSON数据
    private static JSONObject userLocationInfo = null;
    //用户所在地JSON数据


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public JSONService(String name) {
        super(name);
    }

    public JSONService() {
        super("JSONUtils");
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "onStart: 开始加载数据 --- ");
    }

    /**
     * 闪屏时加载数据
     *
     * @param intent
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e("OnHandleIntent", "Running");
        try {
            Response response = CommonHttpRequest.getHttp("https://rapi.qingting.fm/regions");
            ResponseBody responseBody = response.body();

            Response response2 = CommonHttpRequest.getHttp("https://rapi.qingting.fm/categories?type=channel");
            ResponseBody responseBody2 = response2.body();

            Response response3 = CommonHttpRequest.getHttp("https://ip.qingting.fm/ip");
            ResponseBody responseBody3 = response3.body();

            JSONObject data = new JSONObject(responseBody.string());
            JSONArray array = data.getJSONArray("Data");
            district = array;

            JSONObject data2 = new JSONObject(responseBody2.string());
            JSONArray array2 = data2.getJSONArray("Data");
            categories = array2;


            JSONObject data3 = new JSONObject(responseBody3.string());
            userLocationInfo = data3.getJSONObject("data");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("InitDataService", "初始化数据服务结束");
    }

    public static JSONArray getDistrict() {
        if (district != null) {
            return district;
        }
        return null;
    }

    public static JSONArray getCategories() {
        if (categories != null)
            return categories;
        return null;
    }

    public static JSONObject getUserLocationInfo() {
        if (userLocationInfo != null)
            return userLocationInfo;
        return null;
    }
}
