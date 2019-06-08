package bts.tech.btsmusicplayer.model;

public class Song {

    private String songId;
    private int iconId;
    private String title;
    private String country;
    private String duration;
    private String comment;

    public Song(String songId, int iconId, String title, String country, String duration, String comment) {
        this.songId = songId;
        this.iconId = iconId;
        this.title = title;
        this.country = country;
        this.duration = duration;
        this.comment = comment;
    }

    public String getSongId() {
        return songId;
    }

    public int getIconId() {
        return iconId;
    }

    public String getTitle() {
        return title;
    }

    public String getCountry() {
        return country;
    }

    public String getDuration() {
        return duration;
    }

    public String getComment() {
        return comment;
    }
}
