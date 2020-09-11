package net.along.dragonflyfm.entity;

/**
 * @author 每天都有最爱的傻子陪着
 */
public class Broadcasters {
    private int id;
    private String username;
    private String thumb;
    private String weibo_name;
    private String weibo_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getWeibo_name() {
        return weibo_name;
    }

    public void setWeibo_name(String weibo_name) {
        this.weibo_name = weibo_name;
    }

    public String getWeibo_id() {
        return weibo_id;
    }

    public void setWeibo_id(String weibo_id) {
        this.weibo_id = weibo_id;
    }

    @Override
    public String toString() {
        return "Broadcasters{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", thumb='" + thumb + '\'' +
                ", weibo_name='" + weibo_name + '\'' +
                ", weibo_id='" + weibo_id + '\'' +
                '}';
    }
}
