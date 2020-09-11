package net.along.dragonflyfm.record;

import com.orm.SugarRecord;

/**
 * 创建者 by:陈泰龙
 * <p>  APP访问记录
 * 2020/8/16
*/

public class AppVisitCount extends SugarRecord {
    public  long timeStamp;
    public  int count;

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "AppVisitTime{" +
                "time=" + timeStamp +
                ", count=" + count +
                '}';
    }
}
