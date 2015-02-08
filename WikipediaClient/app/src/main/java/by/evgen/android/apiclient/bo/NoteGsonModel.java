package by.evgen.android.apiclient.bo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 22.10.2014.
 */
public class NoteGsonModel implements Parcelable {

    private String title;
    private String content;

    public NoteGsonModel(Long id, String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{title, content});

    }

    public static final Parcelable.Creator<NoteGsonModel> CREATOR = new Parcelable.Creator<NoteGsonModel>() {
        public NoteGsonModel createFromParcel(Parcel in) {
            return new NoteGsonModel(in);
        }

        public NoteGsonModel[] newArray(int size) {
            return new NoteGsonModel[size];
        }
    };

    private NoteGsonModel(Parcel parcel) {
        String[] data = new String[2];
        parcel.readStringArray(data);
        title = data[0];
        content = data[1];
    }

}