package net.along.dragonflyfm.entity;

import java.util.List;

public class SearchesData {
    private int content_id;
    private String content_type;
    private   String cover;
    private   String title;
    private String description;
    private NowPlaying nowplaying;

    private int audience_count;
    private String liveShow_id;
    private String update_time;

    private List<Categories> categories;
    private Region region;

    public int getContent_id() {
        return content_id;
    }

    public void setContent_id(int content_id) {
        this.content_id = content_id;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public  String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NowPlaying getNowplaying() {
        return nowplaying;
    }

    public void setNowplaying(NowPlaying nowplaying) {
        this.nowplaying = nowplaying;
    }

    public int getAudience_count() {
        return audience_count;
    }

    public void setAudience_count(int audience_count) {
        this.audience_count = audience_count;
    }

    public String getLiveShow_id() {
        return liveShow_id;
    }

    public void setLiveShow_id(String liveShow_id) {
        this.liveShow_id = liveShow_id;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public List<Categories> getCategories() {
        return categories;
    }

    public void setCategories(List<Categories> categories) {
        this.categories = categories;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }


}