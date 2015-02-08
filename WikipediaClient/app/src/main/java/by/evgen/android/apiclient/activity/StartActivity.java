package by.evgen.android.apiclient.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import by.evgen.android.apiclient.auth.Authorized;

/**
 * Created by User on 07.10.2014.
 */

public class StartActivity extends ActionBarActivity {

    public static final int REQUEST_L = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Authorized.isLogged()) {
            startMainActivity();
        } else {
            startActivityForResult(new Intent(this, VkLoginActivity.class), REQUEST_L);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_L && resultCode == RESULT_OK) {
            Authorized.setLogged(true);
            startMainActivity();
        } else {
            finish();
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, WikiActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
        finish();
    }

}