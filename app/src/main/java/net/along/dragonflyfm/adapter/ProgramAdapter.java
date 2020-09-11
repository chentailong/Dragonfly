package net.along.dragonflyfm.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.along.dragonflyfm.R;
import net.along.dragonflyfm.activities.ProgramActivity;
import net.along.dragonflyfm.entity.Broadcasters;
import net.along.dragonflyfm.entity.Program;
import net.along.dragonflyfm.record.ProgramGreet;
import net.along.dragonflyfm.util.DateBaseUtil;
import net.along.fragonflyfm.util.PlayerActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author创建者 by:陈泰龙
 * <p>
 * 2020/7/16
 */

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ProgramItem> {
    private static final String TAG = "ProgramAdapter";
    private List<Program> mProgram;
    private Context context;
    private int StartProgram;

    public ProgramAdapter(Context context, List<Program> mProgram) {
        this.mProgram = mProgram;
        this.context = context;
    }


    @NonNull
    @Override
    public ProgramItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.program_list_view, null);
        ProgramItem item = new ProgramItem(view);
        return item;
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramItem holder, int position) {
        final Program entity = mProgram.get(position);
        List<Broadcasters> broadcasters = entity.getBroadcasters();
        String host = "";
        String title = "";
        title = entity.getTitle();
        for (Broadcasters hostObj : broadcasters) {
            host = host + "  " + hostObj.getUsername();
        }
        holder.hostView.setText(host);
        holder.stateImg.setImageResource(R.drawable.ic_trumpet);
        holder.titleView.setText(entity.getTitle());
        holder.durationView.setText("[" + entity.getStart_time() + "-" + entity.getEnd_time() + "]");

        int temp = ((ProgramActivity) context).programId;
        String startTime = ((ProgramActivity) context).startTime;
        if (entity.getProgram_id() == temp && entity.getStart_time().equals(startTime)) {
            holder.audience_count.setText(((ProgramActivity) context).count + "");
            holder.countImg.setVisibility(View.VISIBLE);
            holder.audience_count.setVisibility(View.VISIBLE);
            holder.mRelativeLayout.setBackground(context.getDrawable(R.drawable.program_be_show_style));
            StartProgram = position;
        }
        String finalHost = host;
        String finalTitle = title;
        holder.mRelativeLayout.setOnClickListener(view -> {
            if (position > StartProgram) {
                Toast.makeText(context, "节目尚未开始", Toast.LENGTH_SHORT).show();
                return;
            }
            //点击列表传输数据过去，实现播放功能
            PlayerActivity.Companion.start(context, ((ProgramActivity) context).channelId, null,
                    ((ProgramActivity) context).channelName, finalHost, finalTitle,
                    ((ProgramActivity) context).cover);
            updateProgramTable(entity.getProgram_id(), entity.getTitle());
        });
    }

    /**
     * 最受欢迎的节目
     * @param programId
     * @param programName
     */
    private void updateProgramTable(int programId, String programName) {
        long nowTimeStamp = System.currentTimeMillis();
        //获取时间戳
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(nowTimeStamp));
        Iterator iterator = ProgramGreet.findAll(ProgramGreet.class);
        while (iterator.hasNext()) {
            ProgramGreet programIterator = (ProgramGreet) iterator.next();
            long DataTimeStamp = programIterator.getTimeStamp();
            //数据库时间戳
            Calendar DataCalendar = Calendar.getInstance();
            DataCalendar.setTime(new Date(DataTimeStamp));
            if (DateBaseUtil.isToday(calendar, DataCalendar)) {
                //判断这一记录是否是今天产生的
                int thisProgramId = programIterator.getProgramId();
                String thisProgramName = programIterator.getProgramName();
                if (programId == thisProgramId && programName == thisProgramName) {
                    //判断这个节目的id和节目名是否和点击的节目一致
                    long thisId = programIterator.getId();
                    int updateCount = programIterator.getCount();
                    ProgramGreet.executeQuery("update PROGRAM_GREET set count=? where id=?",
                            updateCount + 1 + "", thisId + "");
                    Log.e(TAG, "u新增一条最受欢迎节目数据 "+ updateCount);
                    return;
                }
            }
        }
        //如果当天没有记录，就新增一条
        ProgramGreet newVisit = new ProgramGreet();
        newVisit.setProgramId(programId);
        newVisit.setProgramName(programName);
        newVisit.setCount(1);
        newVisit.setTimeStamp(nowTimeStamp);
        newVisit.save();
        Log.e(TAG, ": 新增一条最受欢迎节目数据  " + programName);
        Log.e(TAG, "访问次数： " + 1 );
    }


    @Override
    public int getItemCount() {
        return mProgram.size();
    }

    /**
     * 设置组件的名称
     */
    class ProgramItem extends RecyclerView.ViewHolder {
        ImageView stateImg;
        ImageView countImg;
        TextView titleView;
        TextView hostView;
        TextView audience_count;
        TextView durationView;
        RelativeLayout mRelativeLayout;


        /**
         * 初始化
         *
         * @param itemView
         */
        public ProgramItem(View itemView) {
            super(itemView);
            stateImg = itemView.findViewById(R.id.program_trumpet_imageView);
            countImg = itemView.findViewById(R.id.program_people_imageView);
            titleView = itemView.findViewById(R.id.program_title);
            hostView = itemView.findViewById(R.id.program_username);
            audience_count = itemView.findViewById(R.id.audience_count);
            durationView = itemView.findViewById(R.id.program_duration);
            mRelativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
