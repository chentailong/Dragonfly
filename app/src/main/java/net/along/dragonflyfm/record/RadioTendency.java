package net.along.dragonflyfm.record;

import com.orm.SugarRecord;

/**
 * 创建者 by:陈泰龙
 * <p>电台类型倾向
 * 2020/8/16
 **/

public class RadioTendency extends SugarRecord {
    private int categoryId;
    private String categoryTitle;
    private int count;
    private long timeStamp;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "RadioTendency{" +
                "categoryId=" + categoryId +
                ", categoryTitle='" + categoryTitle + '\'' +
                ", count=" + count +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
