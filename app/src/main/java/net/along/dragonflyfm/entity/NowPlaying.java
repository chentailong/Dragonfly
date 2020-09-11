package net.along.dragonflyfm.entity;

import java.util.List;

public class NowPlaying {
    private int id;
    private int duration;
    private String start_time;
    private String name;
    private String title;

    private List<Broadcasters> broadcasters;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBroadcasters(List<Broadcasters> broadcasters) {
        this.broadcasters = broadcasters;
    }

    public List<Broadcasters> getBroadcasters() {
        return broadcasters;
    }

    @Override
    public String toString() {
        return "NowPlaying{" +
                "id=" + id +
                ", duration=" + duration +
                ", start_time='" + start_time + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
