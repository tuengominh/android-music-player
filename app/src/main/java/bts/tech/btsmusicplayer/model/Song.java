package bts.tech.btsmusicplayer.model;

public class Song {

    private int resId;
    private String resPath;
    private String flagResPath;
    private String title;
    private String country;
    private String duration;
    private String comment;

    public Song(int resId, String resPath, String flagResPath, String title, String country, String duration, String comment) {
        this.resId = resId;
        this.resPath = resPath;
        this.flagResPath = flagResPath;
        this.title = title;
        this.country = country;
        this.duration = duration;
        this.comment = comment;
    }

    public int getResId() { return resId; }

    public void setResId(int resId) { this.resId = resId; }

    public String getResPath() {
        return resPath;
    }

    public void setResPath(String resPath) {
        this.resPath = resPath;
    }

    public String getFlagResPath() { return flagResPath; }

    public void setFlagResPath(String flagResPath) { this.flagResPath = flagResPath; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
