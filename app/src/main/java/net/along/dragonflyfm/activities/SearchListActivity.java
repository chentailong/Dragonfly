package net.along.dragonflyfm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import net.along.dragonflyfm.R;

import fm.qingting.qtsdk.QTSDK;
import fm.qingting.qtsdk.entity.SimpleChannel;

import static android.widget.Toast.LENGTH_SHORT;


/**
 * @author 陈泰龙
 *  搜索类
 */

public class SearchListActivity extends AppCompatActivity {
    EditText mEditText;
    Button mButton;
    RecyclerView mRecyclerView;
    SimpleAdapter listAdapter;

    /**
     * 在主程序中传输数据，显示数据
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchactivity_list);
        mEditText = findViewById(R.id.et_search);
        mEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mEditText.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        mRecyclerView = findViewById(R.id.list);
        mButton = findViewById(R.id.bt_search);
        listAdapter = new SimpleAdapter<SimpleChannel>() {
            @Override
            public void bindData(SimpleHolder holder, SimpleChannel object) {
                holder.mTextView.setText(object.getTitle());
                Glide.with(holder.itemView.getContext()).load(object.getThumb()).into(holder.mImageView);
                holder.mLinearLayout.setOnClickListener(v -> {
                    switch (object.getType()) {
                        case "channel_live":
                            Intent intent = new Intent(v.getContext(), ProgramActivity.class);
                            intent.putExtra("channel_id", object.getId());
                            intent.putExtra("channel", object.getTitle());
                            intent.putExtra("previous", "搜索");
                            intent.putExtra("channelName", object.getTitle());
                            intent.putExtra("cover", object.getThumb());
                            intent.putExtra("startTime", object.getUpdateTime());
                            v.getContext().startActivity(intent);
                            break;
                        default:
                            break;
                    }
                });
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mRecyclerView.setAdapter(listAdapter);

        /**
         * 点击搜索
         */
        mButton.setOnClickListener(v -> {
            if (TextUtils.isEmpty(mEditText.getText().toString())) {
                Toast.makeText(this, "关键字不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            requestList(mEditText.getText().toString(), "channel_live");
        });
    }

    /**
     * 搜索信息
     *
     * @param keyword
     * @param type
     */
    private void requestList(String keyword, String type) {
        QTSDK.search(keyword, type, null, 1, (result, e) -> {
            if (e == null) {
                if (result != null) {
                    listAdapter.items = result.getData();
                    listAdapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(getBaseContext(), e.getMessage(), LENGTH_SHORT).show();
            }
        });
    }
}
