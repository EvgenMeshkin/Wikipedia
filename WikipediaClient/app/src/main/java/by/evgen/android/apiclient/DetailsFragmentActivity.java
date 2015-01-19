package by.evgen.android.apiclient;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import by.evgen.android.apiclient.bo.NoteGsonModel;
import by.evgen.android.apiclient.fragments.AbstractFragment;
import by.evgen.android.apiclient.fragments.DetailsFragment;
import by.evgen.android.apiclient.listener.OnBackPressedListener;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by User on 13.11.2014.
 */
public class DetailsFragmentActivity extends FragmentActivity implements AbstractFragment.Callbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
//        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            DetailsFragment details = new DetailsFragment();
            details.setArguments(getIntent().<Bundle>getParcelableExtra("key"));
            getSupportFragmentManager().beginTransaction().add(
                    android.R.id.content, details).commit();
        }
    }

    @Override
    public void onShowDetails(NoteGsonModel note) {
        NoteGsonModel noteGsonModel = (NoteGsonModel) note;
        Bundle bundle = new Bundle();
        bundle.putParcelable("key", noteGsonModel);
        Intent intent = new Intent();
        intent.setClass(this, DetailsFragmentActivity.class);
        intent.putExtra("key", bundle);
        startActivity(intent);
    }

    @Override
    public void onErrorDialog(Exception e) {

    }

//    @Override
//    public void onBackPressed() {
//        Log.text(getClass(), "PressOnBack");
//        FragmentManager fm = getSupportFragmentManager();
//        OnBackPressedListener backPressedListener = null;
//        for (Fragment fragment: fm.getFragments()) {
//            if (fragment instanceof  OnBackPressedListener) {
//                backPressedListener = (OnBackPressedListener) fragment;
//                break;
//            }
//        }
//        if (backPressedListener != null) {
//            backPressedListener.onBackPressed();
//        } else {
//            super.onBackPressed();
//        }
//    }

}
