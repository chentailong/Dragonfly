package net.along.dragonflyfm.entity;

public class Region {
    private int id;
    private String title;
    private int status;
    private int pid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "Region{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", pid=" + pid +
                '}';
    }
}
