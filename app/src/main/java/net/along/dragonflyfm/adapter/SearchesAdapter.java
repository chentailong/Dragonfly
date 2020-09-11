package net.along.dragonflyfm.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orm.SugarRecord;
import net.along.dragonflyfm.R;
import net.along.dragonflyfm.activities.ProgramActivity;
import net.along.dragonflyfm.entity.Categories;
import net.along.dragonflyfm.entity.SearchesData;
import net.along.dragonflyfm.record.CollectRadio;
import net.along.dragonflyfm.record.LikeRadio;
import net.along.dragonflyfm.record.RadioTendency;
import net.along.dragonflyfm.service.FMItemJsonService;
import net.along.dragonflyfm.util.DateBaseUtil;

import org.json.JSONArray;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 创建者 by:陈泰龙
 * <p>
 * 2020/7/13
 * @author 每天都有最爱的傻子陪着*/

public class
SearchesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "SearchesAdapter";
    protected List<SearchesData> data;
    private Context context;

    public SearchesAdapter(Context context, List<SearchesData> data) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_searches_view, null);
            CardViewHolder holder = new CardViewHolder(view);
            return holder;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, null);
            FooterHolder holder = new FooterHolder(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CardViewHolder) {
            final SearchesData fmCardView = data.get(position);
            ((CardViewHolder) holder).listeners.setText(fmCardView.getAudience_count() + "");
            ((CardViewHolder) holder).titleTextView.setText(fmCardView.getTitle());
            Glide.with(context).load(fmCardView.getCover()).into(((CardViewHolder) holder).coverImg);
            if (isCollect(fmCardView.getContent_id(), fmCardView.getTitle())) {
                ((CardViewHolder) holder).favorImg.setImageResource(R.drawable.ic_collect);
            } else {
                ((CardViewHolder) holder).favorImg.setImageResource(R.drawable.ic_not_collect);
            }

            ((CardViewHolder) holder).card_view.setOnClickListener(view -> {
                updateChannelRecord(fmCardView.getTitle(), fmCardView.getContent_id());
                handleCategories(fmCardView.getCategories());

                Intent intent = new Intent(context, ProgramActivity.class);
                intent.putExtra("cover", fmCardView.getCover());
                intent.putExtra("channelName", fmCardView.getTitle());
                intent.putExtra("previous", fmCardView.getRegion().getTitle());
                intent.putExtra("channel", fmCardView.getTitle());
                intent.putExtra("channel_id", fmCardView.getContent_id());
                intent.putExtra("audience_count", fmCardView.getAudience_count());
                if (fmCardView.getNowplaying() != null) {
                    intent.putExtra("startTime", fmCardView.getNowplaying().getStart_time());
                    intent.putExtra("programId", fmCardView.getNowplaying().getId());
                    intent.putExtra("duration", fmCardView.getNowplaying().getDuration());
                } else {
                    Toast.makeText(context, "暂无节目播放，请选择其他电台节目", Toast.LENGTH_SHORT).show();
                    return;
                }
                context.startActivity(intent);
            });

            ((CardViewHolder) holder).favorImg.setOnClickListener(view -> {
                if (isCollect(fmCardView.getContent_id(), fmCardView.getTitle())) {
                    Toast.makeText(context, "你取消收藏了这个电台", Toast.LENGTH_SHORT).show();
                    ((CardViewHolder) holder).favorImg.setImageResource(R.drawable.ic_not_collect);
                    updateFavourite(fmCardView.getContent_id(), fmCardView.getTitle(), fmCardView.getCover(),
                            fmCardView.getAudience_count(), fmCardView.getNowplaying().getStart_time(),
                            fmCardView.getNowplaying().getId(), fmCardView.getNowplaying().getDuration(), false);
                } else {
                    updateFavourite(fmCardView.getContent_id(), fmCardView.getTitle(), fmCardView.getCover(),
                            fmCardView.getAudience_count(), fmCardView.getNowplaying().getStart_time(),
                            fmCardView.getNowplaying().getId(), fmCardView.getNowplaying().getDuration(), true);
                    Toast.makeText(context, "你收藏了这个电台", Toast.LENGTH_SHORT).show();
                    ((CardViewHolder) holder).favorImg.setImageResource(R.drawable.ic_collect);
                }
            });
        }
    }


    /**
     * 判断电台是否被收藏
     *
     * @param channelId
     * @param title
     * @return
     */
    private boolean isCollect(int channelId, String title) {
        List<CollectRadio> radios = SugarRecord.findWithQuery(CollectRadio.class,
                "select * from COLLECT_RADIO where channelId=? "
                        + "and title=?", channelId + "", title);
        if (radios.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 添加收藏数据
     *
     * @param channelId
     * @param title
     * @param imgUrl
     * @param flag      当flog为true时，为添加收藏数据
     */
    private void updateFavourite(int channelId, String title, String imgUrl, int audience_count, String startTime,
                                 int programId, int duration, boolean flag) {
        if (flag) {
            CollectRadio radio = new CollectRadio();
            radio.setChannel_id(channelId);
            radio.setImgUrl(imgUrl);
            radio.setTitle(title);
            radio.setAudience_count(audience_count);
            radio.setStart_time(startTime);
            radio.setIdes(programId);
            radio.setDuration(duration);
            radio.save();
            Log.e("updateFavourite", "添加了一条数据——" + radio.toString());
        } else {
            SugarRecord.executeQuery("delete from COLLECT_RADIO where channelId=? " +
                    "and title=?", channelId + "", title);
            Log.e(TAG, "删除了一条数据——" + title);
        }
    }

    /**
     * 遍历Categories
     *
     * @param categories
     */
    private void handleCategories(List<Categories> categories) {
        for (Categories category : categories) {
            RadioTendencyRecord(category.getId(), category.getTitle());
        }
    }

    /**
     * 更新电台类型倾向数据
     */
    private void RadioTendencyRecord(int categoryId, String categoryTitle) {
        Iterator tables = RadioTendency.findAll(RadioTendency.class);
        long nowTimeStamp = System.currentTimeMillis();
        //获取当前的时间戳
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(nowTimeStamp));
        while (tables.hasNext()) {
            RadioTendency tableObj = (RadioTendency) tables.next();
            long thisTimeStamp = tableObj.getTimeStamp();
            //获取数据库的时间戳
            Calendar thisCalendar = Calendar.getInstance();
            thisCalendar.setTime(new Date(thisTimeStamp));
            if (DateBaseUtil.isToday(calendar, thisCalendar)) {
                //如果是同一天
                if (categoryId == tableObj.getCategoryId() && categoryTitle.equals(tableObj.getCategoryTitle())) {
                    int count = tableObj.getCount() + 1;
                    long id = tableObj.getId();
                    SugarRecord.executeQuery("update RADIO_TENDENCY set count=? where id=?",
                            count + "", id + "");
                    Log.e(TAG, "更新今天访问" + categoryTitle + "次数   " + "   新增了一条电台类型倾向记录");
                    Log.e(TAG, "访问次数为: " + count);
                    return;
                }
            }
        }

        RadioTendency table = new RadioTendency();
        table.setCategoryId(categoryId);
        table.setCategoryTitle(categoryTitle);
        table.setCount(1);
        table.setTimeStamp(nowTimeStamp);
        table.save();
        Log.e(TAG, "新增了一条电台类型倾向记录" + categoryTitle);
    }


    /**
     * 更新最受欢迎的电台数据
     *
     * @param channelTitle
     * @param channelId
     */
    private void updateChannelRecord(String channelTitle, int channelId) {
        Iterator tables = LikeRadio.findAll(LikeRadio.class);
        long nowTimeStamp = System.currentTimeMillis();
        //获取当前的时间戳
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(nowTimeStamp));

        while (tables.hasNext()) {
            LikeRadio tableObj = (LikeRadio) tables.next();
            long thisTimeStamp = tableObj.getTimeStamp();
            //获取数据库的时间戳
            Calendar thisCalendar = Calendar.getInstance();
            thisCalendar.setTime(new Date(thisTimeStamp));
            if (DateBaseUtil.isToday(calendar, thisCalendar)) {
                //如果是同一天
                if (channelId == tableObj.getChannelId() && channelTitle.equals(tableObj.getChannel())) {
                    //判断是否是同一个电台
                    int count = tableObj.getCount() + 1;
                    long id = tableObj.getId();
                    SugarRecord.executeQuery("update LIKE_RADIO set count=? where id=?", count + "", id + "");
                    Log.e(TAG, "最受欢迎电台: " + tableObj.getChannel() + "次数： " + count);
                    return;
                }
            }
        }

        //如果没有找到想要的数据，那就增加一条
        LikeRadio table = new LikeRadio();
        table.setChannel(channelTitle);
        table.setCount(1);
        table.setChannelId(channelId);
        table.setTimeStamp(nowTimeStamp);
        table.save();
        Log.e(TAG, "新增了一条最受欢迎电台记录   " + channelTitle);
    }


    /**
     * 更新适配器中的数据
     */
    public void upData() {
        JSONArray array = FMItemJsonService.getLastGetJson();
        Gson gson = new Gson();
        List<SearchesData> list =
                gson.fromJson(array.toString(), new TypeToken<List<SearchesData>>() {
                }.getType());
        this.data = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == data.size()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class CardViewHolder extends RecyclerView.ViewHolder {

        ImageView coverImg;
        TextView titleTextView;
        ImageView favorImg;
        TextView listeners;
        CardView card_view;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            coverImg = itemView.findViewById(R.id.fragment_searches_image);
            titleTextView = itemView.findViewById(R.id.fragment_searches_station_name);
            favorImg = itemView.findViewById(R.id.fragment_searches_collection);
            listeners = itemView.findViewById(R.id.fragment_searches_number_of_listeners);
            card_view = itemView.findViewById(R.id.card_view);
        }
    }

    /**
     * 加载中的id绑定
     */
    public class FooterHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;
        TextView loadingText;

        public FooterHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.item_loading_bar);
            loadingText = itemView.findViewById(R.id.item_loading_tv);
        }
    }
}
