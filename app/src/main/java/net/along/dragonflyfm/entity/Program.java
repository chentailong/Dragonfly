package net.along.dragonflyfm.entity;

import java.util.List;

public class Program {
    private int id;
    private String start_time;
    private String end_time;
    private int duration;
    private int chatgroup_id;
    private int res_id;
    private int day;
    private int channel_id;
    private int program_id;
    private String title;

    private List<Broadcasters> broadcasters;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getChatgroup_id() {
        return chatgroup_id;
    }

    public void setChatgroup_id(int chatgroup_id) {
        this.chatgroup_id = chatgroup_id;
    }

    public int getRes_id() {
        return res_id;
    }

    public void setRes_id(int res_id) {
        this.res_id = res_id;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(int channel_id) {
        this.channel_id = channel_id;
    }

    public int getProgram_id() {
        return program_id;
    }

    public void setProgram_id(int program_id) {
        this.program_id = program_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Broadcasters> getBroadcasters() {
        return broadcasters;
    }

    public void setBroadcasters(List<Broadcasters> broadcasters) {
        this.broadcasters = broadcasters;
    }
}
