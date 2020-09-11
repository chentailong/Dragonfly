package net.along.dragonflyfm.record;

import com.orm.SugarRecord;

/**
 * 创建者 by:陈泰龙
 * <p>访问地区的次数
 * 2020/8/16
 **/

public class RegionTable extends SugarRecord {
    private String province;

    private long stamp;

    private int count;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public long getStamp() {
        return stamp;
    }

    public void setStamp(long stamp) {
        this.stamp = stamp;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "RegionTable{" +
                "province='" + province + '\'' +
                ", stamp=" + stamp +
                ", count=" + count +
                '}';
    }
}
