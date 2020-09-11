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

import okhttp3.ResponseBody;


/**
 * 用于下载指定地区的FM数据
 */
public class FMItemJsonService extends IntentService {

    private static JSONArray LAST_GET_JSON;
    private static String TAG = "GetFMItemJsonService";


    public FMItemJsonService(String name) {
        super(name);
    }

    public FMItemJsonService() {
        super("GetFMItemJsonService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e("GetFMItemJsonService","数据下载服务开始");
        int provinceId=intent.getIntExtra("provinceId",239);
        int page=intent.getIntExtra("page",1);
        int pageSize=intent.getIntExtra("pageSize",18);
        ResponseBody responseBody=
        CommonHttpRequest.getHttp("https://rapi.qingting.fm/categories/"+provinceId+
                "/channels?with_total=true&page="+page+"&pagesize="+pageSize).body();
        try {
            String jsonData=responseBody.string();
            JSONObject dataJson=new JSONObject(jsonData);
            JSONArray item=dataJson.getJSONObject("Data").getJSONArray("items");
            LAST_GET_JSON=item;
        }
      catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: 数据加载完成");
    }

    public static JSONArray getLastGetJson() {
        if (LAST_GET_JSON!=null)
        return LAST_GET_JSON;
        return null;
    }
}
