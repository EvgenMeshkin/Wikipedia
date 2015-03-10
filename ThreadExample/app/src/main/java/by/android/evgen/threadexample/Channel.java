package by.android.evgen.threadexample;

/**
 * Created by evgen on 08.03.2015.
 */
public class Channel {

    int id;
    String title;
    String desc;
    String imageUrl;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Channel(int id, String title, String desc, String imageUrl) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.imageUrl = imageUrl;
    }
}
