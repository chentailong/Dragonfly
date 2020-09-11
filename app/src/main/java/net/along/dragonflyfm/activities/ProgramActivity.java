package net.along.dragonflyfm.activities;

import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.along.dragonflyfm.R;
import net.along.dragonflyfm.adapter.ProgramAdapter;
import net.along.dragonflyfm.entity.Program;
import net.along.dragonflyfm.util.CommonHttpRequest;
import net.along.dragonflyfm.util.DateBaseUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 创建者 by:陈泰龙
 * <p>
 * 2020/7/15
 *
 * @author 每天都有最爱的傻子陪着*/

public class ProgramActivity extends AppCompatActivity {

    private final static String TAG = "ProgramActivity";
    private TextView TvPrevious;
    private TextView TvName;
    private RecyclerView mRecyclerView;
    private List<Program> mPrograms;

    public String cover;
    public String startTime;
    public static String channelName;
    public static int channelId;
    public int count;
    public int programId;
    public int duration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.program_list);
        initView();
        inData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        this.findViewById(R.id.play_list_return).setOnClickListener(onClickListener);
        //返回键
        this.findViewById(R.id.play_list_previous).setOnClickListener(onClickListener);
        TvPrevious = this.findViewById(R.id.play_list_previous);
        //返回
        TvName = this.findViewById(R.id.program_name);
        //电台节目名称
        mRecyclerView = this.findViewById(R.id.play_list_program);
        TvName.setText(getIntent().getStringExtra("channel"));
        TvPrevious.setText(getIntent().getStringExtra("previous"));
    }

    /**
     * 加载电台数据
     */
    private void inData() {
        startTime = getIntent().getStringExtra("startTime");
        cover = getIntent().getStringExtra("cover");
        count = getIntent().getIntExtra("audience_count", 0);
        programId = getIntent().getIntExtra("programId", 0);
        channelName = getIntent().getStringExtra("channelName");
        duration = getIntent().getIntExtra("duration", 0);
        channelId = getIntent().getIntExtra("channel_id", 1756);
        final int dayOFWeek = DateBaseUtil.dayOFWeek();
        final String baseUrl = "https://rapi.qingting.fm/v2/channels/" + channelId + "/playbills?day=" + dayOFWeek;
        final AlertDialog loadingDialog = new AlertDialog.Builder(ProgramActivity.this).create();
        View loadView = View.inflate(this, R.layout.loading_dialog_data, null);
        loadingDialog.setView(loadView);
        loadingDialog.show();
        CommonHttpRequest.getHttp(baseUrl, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(ProgramActivity.this, "获取电台数据失败", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                try {
                    JSONObject rootJson = new JSONObject(responseBody.string());
                    JSONObject dataObj = rootJson.getJSONObject("data");
                    JSONArray dayJson = dataObj.getJSONArray("" + dayOFWeek);
                    Gson gson = new Gson();
                    mPrograms = gson.fromJson(dayJson.toString(), new TypeToken<List<Program>>() {
                    }.getType());
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(() -> {
                    loadingDialog.dismiss();
                    showPlayList();
                });
            }
        });
    }

    private void showPlayList() {
        ProgramAdapter programAdapter = new ProgramAdapter(this, mPrograms);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(programAdapter);
    }


    private View.OnClickListener onClickListener = v -> {
        switch (v.getId()) {
            case R.id.play_list_previous:
            case R.id.play_list_return:
                finish();
                break;
        }
    };

}
