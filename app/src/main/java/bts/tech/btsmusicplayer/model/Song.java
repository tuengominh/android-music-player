package bts.tech.btsmusicplayer.model;

public class Song {

    private int songId;
    private String path;
    private String title;
    private String country;
    private String duration;
    private String comment;
    private boolean isClicked;
    private boolean isPlaying;

    public Song(int songId, String path, String title, String country, String duration, String comment) {
        this.songId = songId;
        this.path = path;
        this.title = title;
        this.country = country;
        this.duration = duration;
        this.comment = comment;
        this.isClicked = false;
        this.isPlaying = false;
    }

    public int getSongId() { return songId; }

    public void setSongId(int songId) { this.songId = songId; }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

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

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean isClicked() { return isClicked; }

    public void setClicked(boolean clicked) { isClicked = clicked; }
}
