package by.evgen.android.apiclient.bo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by User on 17.11.2014.
 */
public class Category extends JSONObjectWrapper {

    private static final String URL = "fullurl";
    private static final String URLIMAGE = "source";
    private static final String URLFOTO = "photo_200_orig";
    private static final String NS = "ns";
    private static final String TITLE = "title";
    private static final String ID = "id";
    private static final String DIST = "dist";
    private static final String FIRSTNAME = "first_name";
    private static final String LASTNAME = "last_name";
    private static final String LINE = "line";
    private static final String TEXT = "text";
    private static final String EXTRACT = "extract";
    private static final String PAGEID = "pageid";
    private static final String VALUE = "value";

    public static final Parcelable.Creator<Category> CREATOR
            = new Parcelable.Creator<Category>() {
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public Category(String jsonObject) {
        super(jsonObject);
    }

    public Category(JSONObject jsonObject) {
        super(jsonObject);
    }

    protected Category(Parcel in) {
        super(in);
    }

    public String getLine() {
        return getString(LINE);
    }

    public String getNs() {
        return getString(NS);
    }

    public String getPageId() {
        return getString(PAGEID);
    }

    public String getTitle() {
        return getString(TITLE);
    }

    public String getValue() {
        return getString(VALUE);
    }

    public String getText() {
        return getString(TEXT);
    }

    public String getUrl() {
        return getString(URL);
    }

    public String getUrlFoto() {
        return getString(URLFOTO);
    }

    public String getUrlImage() {
        return getString(URLIMAGE);
    }

    public String getFirstName() {
        return getString(FIRSTNAME);
    }

    public String getLastName() {
        return getString(LASTNAME);
    }

    public String getDist() {
        return getString(DIST);
    }

    public String getExtract() {
        return getString(EXTRACT);
    }

    public Long getId() {
        return getLong(ID);
    }

}