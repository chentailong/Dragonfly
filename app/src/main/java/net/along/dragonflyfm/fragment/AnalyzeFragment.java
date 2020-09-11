package net.along.dragonflyfm.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.orm.SugarRecord;

import net.along.dragonflyfm.R;
import net.along.dragonflyfm.record.AppVisitCount;
import net.along.dragonflyfm.record.LikeRadio;
import net.along.dragonflyfm.record.ProgramGreet;
import net.along.dragonflyfm.record.RadioTendency;
import net.along.dragonflyfm.record.RegionTable;
import net.along.dragonflyfm.util.DateBaseUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 创建者 by:陈泰龙
 * <p>
 * 2020/7/1
 *
 * @author 每天都有最爱的傻子陪着
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class AnalyzeFragment extends Fragment {
    private final String[] titles = {"APP访问次数", "各地区访问比例（%）", "最受欢迎电台", "最受欢迎节目", "电台类型倾向"};
    private final String[] RadioName = {"资讯台", "音乐台", "交通台", "经济台", "文艺台", "都市台", "体育台", "双语台", "综合台", "生活台", "旅游台", "曲艺台", "方言台"};
    private static final String TAG = "AnalyzeFragment";
    private String pattern = "MM-dd";
    private static final float MIN_TOUCH_DISTANCE = 10f;
    private TextView title_message;
    private int chartIndex = 0;
    private View mRootView;
    private float touchX1;
    private View[] dots;
    private Chart[] mChart;
    private PieChart mPieChart;
    private LineChart mLineChart1;
    private BarChart mBarChart1;
    private HorizontalBarChart mBarChart2;
    private RadarChart mRadarChart;
    private RadioGroup mRadioGroup;
    private long id;
    private Iterator visit;
    private String mNowTime;
    private Map<String, Integer> mMap;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_analyze, container, false);
        initView();
        return mRootView;
    }

    /**
     * 初始化视图数据，实现滑动变化 MPAndroid布局
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        mRadioGroup = mRootView.findViewById(R.id.radioGroup_time);
        mRadioGroup.setOnCheckedChangeListener(RadioGroup);
        title_message = mRootView.findViewById(R.id.analyze_titles);
        View dot1 = mRootView.findViewById(R.id.fragment_chart_dot1);
        View dot2 = mRootView.findViewById(R.id.fragment_chart_dot2);
        View dot3 = mRootView.findViewById(R.id.fragment_chart_dot3);
        View dot4 = mRootView.findViewById(R.id.fragment_chart_dot4);
        View dot5 = mRootView.findViewById(R.id.fragment_chart_dot5);
        dots = new View[]{dot1, dot2, dot3, dot4, dot5};
        mPieChart = mRootView.findViewById(R.id.fragment_chart_p);
        mLineChart1 = mRootView.findViewById(R.id.fragment_chart_l1);
        mBarChart1 = mRootView.findViewById(R.id.fragment_chart_b1);
        mBarChart2 = mRootView.findViewById(R.id.fragment_chart_b2);
        mRadarChart = mRootView.findViewById(R.id.fragment_chart_r);
        mChart = new Chart[]{mLineChart1, mPieChart, mBarChart1, mBarChart2, mRadarChart};
        for (Chart chart : mChart) {
            chart.setTouchEnabled(false);
            //可滑动
            chart.setVisibility(View.GONE);
            //隐藏其他视图
            chart.setNoDataText("数据获取中......");
            chart.setExtraOffsets(5, 10, 5, 25);
        }
        mRootView.findViewById(R.id.fragment_chart_container).setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                touchX1 = event.getX();
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                float touchX2 = event.getX();
                if (Math.abs(touchX1 - touchX2) > MIN_TOUCH_DISTANCE) {
                    if (touchX2 < touchX1) {
                        if (chartIndex < mChart.length - 1) {
                            chartIndex++;
                        } else {
                            chartIndex = 0;
                        }
                    } else {
                        if (chartIndex > 0) {
                            chartIndex--;
                        } else {
                            chartIndex = mChart.length - 1;
                        }
                    }
                    switchChart();
                }
            }
            return true;
        }); //滑动界面的变化
        mLineChart1.setVisibility(View.VISIBLE);
    }

    /**
     * App点击次数的折线图，实现次数的传递显示
     *
     * @param weekCount
     */
    private void LineChart(ArrayList<Integer> weekCount, ArrayList<Long> time) {
        //显示数据，传入访问次数
        ArrayList<Entry> entry = new ArrayList<>();
        List<String> times = new ArrayList<>();
        int j = 0;
        for (Integer i = 0; i < weekCount.size(); i++) {
            entry.add(new Entry(j, weekCount.get(i)));
            String Time = DateBaseUtil.getDateString(time.get(i), pattern);
            //将时间戳转化成字符串显示出来
            times.add(Time);
            j++;
        }
        LineDataSet lineDataSet = new LineDataSet(entry, "");
        lineDataSet.setColor(Color.parseColor("#F15A4A"));
        lineDataSet.setLineWidth(1.6f);
        //线宽度
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        //线条平滑
        lineDataSet.setValueTextSize(12f);
        //设置显示值的文字大小
        lineDataSet.setValueFormatter(new DefaultAxisValueFormatter(0));    //不要小数点
        LineData data = new LineData(lineDataSet);    //设置数据

        XAxis xAxis = mLineChart1.getXAxis();     //得到X轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //设置X轴的位置（默认在上方)
        xAxis.setDrawGridLines(false);   //不显示网格线
        xAxis.setGranularity(1f); //设置X轴坐标之间的最小间隔
        xAxis.setLabelRotationAngle(45);     //标签倾斜
        xAxis.setValueFormatter(new IndexAxisValueFormatter(times));
        YAxis yAxis = mLineChart1.getAxisLeft(); //得到Y轴
        YAxis rightYAxis = mLineChart1.getAxisRight();
        rightYAxis.setEnabled(false); //右侧Y轴不显示
        yAxis.setDrawGridLines(true); //不显示网格线
        yAxis.setGranularity(10f);   //设置Y轴坐标之间的最小间隔
        yAxis.setAxisMinimum(0f); //设置从Y轴值

        mLineChart1.getDescription().setEnabled(false); //隐藏提示信息
        mLineChart1.setData(data); //设置数据
        mLineChart1.invalidate();  //图标刷新

    }

    /**
     * 各地区访问次数的饼图
     */
    private void PieChart() {
        ArrayList<PieEntry> entry = new ArrayList<>();
        entry.clear();
        Iterator<String> iterator = mMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            entry.add(new PieEntry(mMap.get(key), key));
        }

        mPieChart.setUsePercentValues(true); //设置是否显示百分比
        mPieChart.getDescription().setEnabled(false);  //取消Description字体的显示
        mPieChart.setExtraOffsets(5, 5, 5, 5);
        mPieChart.setEntryLabelColor(Color.BLACK);//设置pieChart图表文本字体颜色
        mPieChart.setEntryLabelTextSize(15f);//设置pieChart图表文本字体大小
        mPieChart.setDrawHoleEnabled(false);  //取消中心圆
        mPieChart.getLegend().setOrientation(Legend.LegendOrientation.HORIZONTAL);
        Legend legend = mPieChart.getLegend(); //获取图例
        legend.setXEntrySpace(7f); //设置图例实体之间延X轴的间距（setOrientation = HORIZONTAL有效）
        legend.setYEntrySpace(0f); //设置图例实体之间延Y轴的间距（setOrientation = VERTICAL 有效）
        legend.setYOffset(25f);  //图例的y偏移量
        legend.setXOffset(10f);  //图例x的偏移量
        legend.setTextSize(13);  //图例文字的大小
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);  //设置图例水平显示
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM); //顶部
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT); //右对其

        PieDataSet dataSet = new PieDataSet(entry, "各地区访问比例%");
        ArrayList<Integer> integer = new ArrayList<>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            integer.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            integer.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            integer.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            integer.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            integer.add(c);
        integer.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(integer);  //设置颜色

        PieData data = new PieData(dataSet);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(15f);
        data.setValueFormatter(new PercentFormatter());
        mPieChart.setData(data);
        mPieChart.invalidate();
    }

    /**
     * 最受欢迎电台的柱形图
     *
     * @param count
     * @param name
     */
    private void BarChart(ArrayList<Integer> count, ArrayList<String> name) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < count.size(); i++) {
            entries.add(new BarEntry(i, count.get(i)));
            names.add(name.get(i));
        }
        BarDataSet barDataSet = new BarDataSet(entries, "最受欢迎电台");
        BarData data = new BarData(barDataSet);
        barDataSet.setValueFormatter(new DefaultAxisValueFormatter(0));
        XAxis xAxis = mBarChart1.getXAxis();
        xAxis.setGranularity(1);
        xAxis.setLabelCount(10);
        xAxis.setLabelRotationAngle(-90);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(names));
        YAxis rightAxis = mBarChart1.getAxisRight();
        rightAxis.setEnabled(false);
        mBarChart1.setData(data);
        mBarChart1.invalidate();
        mBarChart1.getDescription().setEnabled(false);
        mBarChart1.animateXY(3000, 3000);//为X轴Y轴设置动画
        mBarChart1.setDrawGridBackground(false);
        mBarChart1.setDrawBorders(false);
    }

    /**
     * 最受欢迎的节目的横向柱形图
     *
     * @param name
     * @param count
     */
    private void HBarChart(List<String> name, List<Integer> count) {
        List<BarEntry> entries = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < count.size(); i++) {
            names.add(name.get(i));
            entries.add(new BarEntry(i, count.get(i)));
        }
        BarDataSet barDataSet = new BarDataSet(entries, "最受欢迎节目");
        BarData data = new BarData(barDataSet);
        data.setValueTextSize(10);
        XAxis xAxis = mBarChart2.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1);
        xAxis.setLabelCount(10);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(names));
        YAxis yl = mBarChart2.getAxisLeft();
        yl.setEnabled(false);
        mBarChart2.setFitBars(true);
        mBarChart2.animateXY(3000, 3000);//为X轴Y轴设置动画
        mBarChart2.setData(data);
        mBarChart2.invalidate();
        mBarChart2.getDescription().setEnabled(false);
    }

    private void RadarChart(List<String> name, List<Integer> count) {
        List<String> RadioName = new ArrayList<>();
        List<RadarEntry> entries = new ArrayList<>();
        for (int i = 0; i < name.size(); i++) {
            entries.add(new RadarEntry(count.get(i)));
            RadioName.add(name.get(i));
        }
        RadarDataSet set = new RadarDataSet(entries, "");
        set.setColor(ColorTemplate.LIBERTY_COLORS[0]);
        set.setFillColor(ColorTemplate.LIBERTY_COLORS[0]);
        set.setDrawFilled(true);
        set.setLineWidth(2f);
        set.setValueTextSize(10);
        RadarData data = new RadarData(set);
        mRadarChart.setData(data);
        mRadarChart.invalidate();
        mRadarChart.getDescription().setEnabled(false);
        XAxis xAxis = mRadarChart.getXAxis();
        xAxis.setTextSize(13);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(RadioName));
        YAxis yAxis = mRadarChart.getYAxis();
        yAxis.setLabelCount(13, false);
        yAxis.setStartAtZero(true);
        yAxis.setDrawLabels(false);
    }

    /**
     * 切换时底部圆圈颜色变化 And 标题变化
     */
    private void switchChart() {
        for (int i = 0; i < mChart.length; i++) {
            if (chartIndex == i) {
                mChart[i].setVisibility(View.VISIBLE);
                dots[i].setBackgroundResource(R.drawable.btn_flat_fill_style);
                title_message.setText(titles[i]);
            } else {
                mChart[i].setVisibility(View.GONE);
                dots[i].setBackgroundResource(R.drawable.btn_flat_style);
            }
        }
    }


    /**
     * 点击选择时间，查询活动轨迹
     */
    private RadioGroup.OnCheckedChangeListener RadioGroup = (group, checkedId) -> {
        switch (group.getCheckedRadioButtonId()) {
            case R.id.button_day:
                //=========================APP访问次数的实现================================
                visit = AppVisitCount.findAll(AppVisitCount.class);  //访问sugar数据库
                while (visit.hasNext()) {
                    AppVisitCount tableObj = (AppVisitCount) visit.next();
                    SugarRecord sugarRecord = tableObj;
                    id = sugarRecord.getId(); //获取最新id
                }
                mNowTime = DateBaseUtil.getCurrentDate(pattern);   //获取当前时间
                AppVisitCount DayVisitCount = AppVisitCount.findById(AppVisitCount.class, id);  //根据ID查询数据
                int VisitCount = DayVisitCount.getCount();  //次数
                ArrayList<Integer> DayCount = new ArrayList<>();  //将次数装入
                ArrayList<Long> DayDate = new ArrayList<>();  //将时间装入
                DayCount.add(VisitCount);
                long VisitTimeStamp = DayVisitCount.getTimeStamp();  //时间戳
                DayDate.add(VisitTimeStamp);
                String time = DateBaseUtil.getDateString(VisitTimeStamp, pattern);   //将时间戳转化成字符串显示出来
                if (mNowTime != time) {
                    LineChart(DayCount, DayDate);  //线型图表
                }
                //=============================各地区访问比例===============================
                long nowTimeStamp = System.currentTimeMillis();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(nowTimeStamp));
                visit = RegionTable.findAll(RegionTable.class);  //访问sugar数据库
                mMap = new HashMap<>();
                while (visit.hasNext()) {
                    RegionTable tableObj = (RegionTable) visit.next();
                    long stamp = tableObj.getStamp();
                    Calendar thisCalendar = Calendar.getInstance();
                    thisCalendar.setTime(new Date(stamp));
                    if (DateBaseUtil.isToday(calendar, thisCalendar)) {
                        mMap.put(tableObj.getProvince(), tableObj.getCount());
                    }
                }
                PieChart();
                //==============================最受欢迎电台====================================
                ArrayList<Integer> LikeCount = new ArrayList<>();
                ArrayList<String> LikeName = new ArrayList<>();
                visit = LikeRadio.findAll(LikeRadio.class);  //访问sugar数据库
                while (visit.hasNext()) {
                    LikeRadio tableObj = (LikeRadio) visit.next();
                    long stamp = tableObj.getTimeStamp();
                    Calendar thisCalendar = Calendar.getInstance();
                    thisCalendar.setTime(new Date(stamp));
                    if (DateBaseUtil.isToday(calendar, thisCalendar)) {
                        LikeCount.add(tableObj.getCount());
                        LikeName.add(tableObj.getChannel());
                    }
                }
                BarChart(LikeCount, LikeName);
                //============================最受欢迎节目=====================================

                //============================电台类型倾向=====================================
                List<String> RadioName = new ArrayList<>();
                List<Integer> RadioCount = new ArrayList<>();
                Iterator<RadioTendency> iterator1 = SugarRecord.findAll(RadioTendency.class);
                visit = RadioTendency.findAll(RadioTendency.class);  //访问sugar数据库
                List<RadioTendency> tendencies = new ArrayList<>();
                while (iterator1.hasNext()) {
                    tendencies.add(iterator1.next());
                    RadioTendency tendency = (RadioTendency) visit.next();
                    long stamp = tendency.getTimeStamp();
                    Calendar thisCalendar = Calendar.getInstance();
                    thisCalendar.setTime(new Date(stamp));
                    RadioCount.add(tendency.getCount());
                    if (DateBaseUtil.isToday(calendar, thisCalendar)) {

                    }
                }
                for (RadioTendency table : tendencies) {
                    String Area = table.getCategoryTitle();
                    boolean judge = false;
                    for (String key : RadioName) {
                        if (key.equals(Area)) {
                            judge = true;
                            break;
                        }
                    }
                    if (!judge) {
                        RadioName.add(Area);
                    }
                }
                RadarChart(RadioName, RadioCount);
                Log.e(TAG, ": " + RadioName);
                break;
//===================================================================================================================================================
            case R.id.button_week:
                Log.d(TAG, "前周 ");
                ArrayList<Integer> WeekCount = new ArrayList<>();
                ArrayList<Long> WeekDate = new ArrayList<>();
                List<AppVisitCount> WeekVisitCount = AppVisitCount.listAll(AppVisitCount.class);
                for (int i = 0; i < WeekVisitCount.size(); i++) {
                    if (i < 7) {
                        WeekCount.add(WeekVisitCount.get(i).getCount());
                        WeekDate.add(WeekVisitCount.get(i).getTimeStamp());
                    }
                    continue;
                }
                LineChart(WeekCount, WeekDate);
                //=====================各地区访问比例===================================
                ArrayList<String> WeekRegion = new ArrayList<>(); //访问地区
                Iterator<RegionTable> iterator = SugarRecord.findAll(RegionTable.class);
                List<RegionTable> tables = new ArrayList<>();
                //获取数据
                while (iterator.hasNext()) {
                    tables.add(iterator.next());
                }
                for (RegionTable table : tables) {
                    String Area = table.getProvince();
                    boolean judge = false;
                    for (String key : WeekRegion) {
                        if (key.equals(Area)) {
                            judge = true;
                            break;
                        }
                    }
                    if (!judge) {
                        WeekRegion.add(Area);
                    }
                }
                mMap = new HashMap<>();
                for (String key : WeekRegion) {
                    int count = 0;
                    for (RegionTable table : tables) {
                        if (table.getProvince().equals(key)) {
                            count += table.getCount();
                        }
                    }
                    mMap.put(key, count);
                }
                PieChart();
                //==============================最受欢迎电台==========================
                ArrayList<Integer> WeekLikeCount = new ArrayList<>();
                ArrayList<String> WeekLikeName = new ArrayList<>();
                ArrayList<String> WeekLike = new ArrayList<>();
                Iterator<LikeRadio> ite = SugarRecord.findAll(LikeRadio.class);
                List<LikeRadio> radios = new ArrayList<>();
                //获取数据
                while (ite.hasNext()) {
                    radios.add(ite.next());
                }
                for (LikeRadio table : radios) {
                    String Area = table.getChannel();
                    boolean judge = false;
                    for (String key : WeekLike) {
                        if (key.equals(Area)) {
                            judge = true;
                            break;
                        }
                    }
                    if (!judge) {
                        WeekLike.add(Area);
                    }
                }
                for (String key : WeekLike) {
                    int count = 0;
                    for (LikeRadio table : radios) {
                        if (table.getChannel().equals(key)) {
                            count += table.getCount();
                        }
                    }
                    WeekLikeCount.add(count);
                    WeekLikeName.add(key);
                }
                BarChart(WeekLikeCount, WeekLikeName);
                //===============================最受欢迎节目=============================
                List<String> name = new ArrayList<>();
                List<Integer> WeekGreetCount = new ArrayList<>();
                List<ProgramGreet> greets = ProgramGreet.listAll(ProgramGreet.class);
                Log.e(TAG, ": " + greets);
                for (ProgramGreet table : greets) {
                    String Area = table.getProgramName();
                    boolean judge = false;
                    for (String key : name) {
                        if (key.equals(Area)) {
                            judge = true;
                            break;
                        }
                    }
                    if (!judge) {
                        name.add(Area);
                    }
                }
                for (String key : name) {
                    int count = 0;
                    for (ProgramGreet table : greets) {
                        if (table.getProgramName().equals(key)) {
                            count += table.getCount();
                        }
                    }
                    WeekGreetCount.add(count);
                }
                HBarChart(name, WeekGreetCount);
                Log.e(TAG, ":节目 ：" + name);
                Log.e(TAG, ":次数 ：" + WeekGreetCount);
                //=============================电台类型倾向====================================

                break;
//===================================================================================================================================================
            case R.id.button_month:
                Log.d(TAG, ": 前一月");
                ArrayList<Integer> MonthCount = new ArrayList<>();
                ArrayList<Long> MonthDate = new ArrayList<>();
                List<AppVisitCount> MonthVisitCount = AppVisitCount.listAll(AppVisitCount.class);
                for (int i = 0; i < MonthVisitCount.size(); i++) {
                    MonthCount.add(MonthVisitCount.get(i).getCount());
                    MonthDate.add(MonthVisitCount.get(i).getTimeStamp());
                }
                LineChart(MonthCount, MonthDate);
                //=====================各地区访问比例===================================
                ArrayList<String> MonthRegion = new ArrayList<>(); //访问地区
                Iterator<RegionTable> iterators = SugarRecord.findAll(RegionTable.class);
                List<RegionTable> tab = new ArrayList<>();
                //获取数据
                while (iterators.hasNext()) {
                    tab.add(iterators.next());
                }
                for (RegionTable table : tab) {
                    String Area = table.getProvince();
                    boolean judge = false;
                    for (String key : MonthRegion) {
                        if (key.equals(Area)) {
                            judge = true;
                            break;
                        }
                    }
                    if (!judge) {
                        MonthRegion.add(Area);
                    }
                }
                mMap = new HashMap<>();
                for (String key : MonthRegion) {
                    int count = 0;
                    for (RegionTable table : tab) {
                        if (table.getProvince().equals(key)) {
                            count += table.getCount();
                        }
                    }
                    mMap.put(key, count);
                }
                PieChart();
                //==============================最受欢迎电台==========================
                ArrayList<Integer> MonthLikeCount = new ArrayList<>();
                ArrayList<String> MonthLikeName = new ArrayList<>();
                ArrayList<String> MonthLike = new ArrayList<>();
                Iterator<LikeRadio> iteIs = SugarRecord.findAll(LikeRadio.class);
                List<LikeRadio> rad = new ArrayList<>();
                //获取数据
                while (iteIs.hasNext()) {
                    rad.add(iteIs.next());
                }
                for (LikeRadio table : rad) {
                    String Area = table.getChannel();
                    boolean judge = false;
                    for (String key : MonthLike) {
                        if (key.equals(Area)) {
                            judge = true;
                            break;
                        }
                    }
                    if (!judge) {
                        MonthLike.add(Area);
                    }
                }
                for (String key : MonthLike) {
                    int count = 0;
                    for (LikeRadio table : rad) {
                        if (table.getChannel().equals(key)) {
                            count += table.getCount();
                        }
                    }
                    MonthLikeCount.add(count);
                    MonthLikeName.add(key);
                }
                BarChart(MonthLikeCount, MonthLikeName);
                Log.e(TAG, ": " + rad);
                //=========================最受欢迎节目=============================
                List<String> ProgramName = new ArrayList<>();
                List<Integer> MonthGreetCount = new ArrayList<>();
                List<ProgramGreet> greet = ProgramGreet.listAll(ProgramGreet.class);
                Log.e(TAG, ": " + greet);
                for (ProgramGreet table : greet) {
                    String Area = table.getProgramName();
                    boolean judge = false;
                    for (String key : ProgramName) {
                        if (key.equals(Area)) {
                            judge = true;
                            break;
                        }
                    }
                    if (!judge) {
                        ProgramName.add(Area);
                    }
                }
                for (String key : ProgramName) {
                    int count = 0;
                    for (ProgramGreet table : greet) {
                        if (table.getProgramName().equals(key)) {
                            count += table.getCount();
                        }
                    }
                    MonthGreetCount.add(count);
                }
                HBarChart(ProgramName, MonthGreetCount);
                Log.e(TAG, ":节目 ：" + ProgramName);
                Log.e(TAG, ":次数 ：" + MonthGreetCount);
                break;

            default:
                break;
        }
    };
}