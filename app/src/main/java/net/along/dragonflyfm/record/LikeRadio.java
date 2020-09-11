package net.along.dragonflyfm.record;

import com.orm.SugarRecord;

/**
 * 创建者 by:陈泰龙
 * <p>最受欢迎的电台
 * 2020/8/16
 **/

public class LikeRadio extends SugarRecord {
    private String channel;
    private int channelId;
    private int count;
    private long timeStamp;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
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
        return "LikeRadio{" +
                "channel='" + channel + '\'' +
                ", channelId=" + channelId +
                ", count=" + count +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
