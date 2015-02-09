package by.evgen.android.apiclient.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;

import by.evgen.android.apiclient.activity.StartActivity;
import by.evgen.android.apiclient.utils.Constant;
import by.evgen.android.apiclient.utils.Log;

/**
 * Created by User on 14.11.2014.
 */
public class ErrorDialog extends DialogFragment implements DialogInterface.OnClickListener {

    public String textButton = "Repeat";

    public static ErrorDialog newInstance(String title) {
        ErrorDialog frag = new ErrorDialog();
        Bundle args = new Bundle();
        args.putString("Error:", title);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("Error:");
        if (!TextUtils.isEmpty(title)) {
            if (title.equals(Constant.INLOGIN)) {
                textButton = "In login";
            }
        }
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle("Error: ")
                .setNegativeButton("Cancel", this)
                .setPositiveButton(textButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (textButton.equals("In login")) {
                            startActivity(new Intent(getActivity(), StartActivity.class));
                        }
                        dialog.cancel();
                    }
                })
                .setMessage(title);
        return adb.create();
    }

    public void onClick(DialogInterface dialog, int which) {
    }

}