package net.along.dragonflyfm.record;

import com.orm.SugarRecord;

/**
 * 创建者 by:胡大航
 * <p>实现收藏功能的保存
 * * 2020/9/8
 **/

public class CollectRadio extends SugarRecord {

    private String title;
    private String imgUrl;
    private String start_time;
    private int channel_id;
    private int duration;
    private int ides;
    private int audience_count;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(int channel_id) {
        this.channel_id = channel_id;
    }

    public int getAudience_count() {
        return audience_count;
    }

    public void setAudience_count(int audience_count) {
        this.audience_count = audience_count;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public int getIdes() {
        return ides;
    }

    public void setIdes(int ides) {
        this.ides = ides;
    }

    @Override
    public String toString() {
        return "CollectRadio{" +
                "title='" + title + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", channel_id=" + channel_id +
                ", audience_count=" +
                '}';
    }
}
