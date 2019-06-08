package bts.tech.btsmusicplayer.model;

public class Song {

    private String imageId;
    private String title;
    private String body;

    public Song(String imageId, String title, String body) {
        this.imageId = imageId;
        this.title = title;
        this.body = body;
    }

    public String getImageId() {
        return imageId;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
