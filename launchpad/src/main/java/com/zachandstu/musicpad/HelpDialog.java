package com.zachandstu.musicpad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Zach on 2017-05-25.
 */

public class HelpDialog extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View textEntryView = factory.inflate(R.layout.custom_dialog, null);
        LinearLayout childLayout = (LinearLayout) textEntryView.findViewById(R.id.llChild);
        LinearLayout layout = (LinearLayout) textEntryView.findViewById(R.id.ll);
        layout.removeAllViews();

        builder.setView(childLayout);
        return builder.create();
    }
}
