package com.example.evgen.apiclient.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.evgen.apiclient.R;
import com.example.evgen.apiclient.bo.NoteGsonModel;

/**
 * Created by User on 22.10.2014.
 */
public class DetailsFragment extends Fragment {
    public DetailsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View content = inflater.inflate(R.layout.fragment_details, container, false);
        if(getArguments() != null) {
            NoteGsonModel obj = (NoteGsonModel) getArguments().getParcelable("key");
            ((TextView)content.findViewById(android.R.id.text1)).setText(obj.getTitle());
            ((TextView)content.findViewById(android.R.id.text2)).setText(obj.getContent());
        }


        return content;
    }

}