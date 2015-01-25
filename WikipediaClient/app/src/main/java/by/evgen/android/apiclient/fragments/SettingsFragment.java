package by.evgen.android.apiclient.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.helper.ClearVkStorage;

/**
 * Created by evgen on 25.01.2015.
 */
public class SettingsFragment extends Fragment {

    private CheckBox mClearHistory;
    private CheckBox mClearStorage;
    private Context mContext;
    private final Uri WIKI_URI = Uri
            .parse("content://by.evgen.android.apiclient.GeoData/geodata");


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.fragment_settings, null);
        mClearHistory = (CheckBox) content.findViewById(R.id.clearHist);
        mClearStorage = (CheckBox) content.findViewById(R.id.clearFavor);
        mContext = getActivity();
        return content;
    }

    @Override
    public void onStop() {
        if (mClearHistory.isChecked()){
            getActivity().getContentResolver().delete(WIKI_URI, null, null);
        }
        if (mClearStorage.isChecked()){
            new ClearVkStorage(mContext);
        }
        super.onStop();
    }


}
