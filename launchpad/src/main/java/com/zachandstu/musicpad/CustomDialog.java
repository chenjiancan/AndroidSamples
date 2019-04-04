package com.zachandstu.musicpad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CustomDialog extends DialogFragment {

    ExpanListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    LinearLayout linny ;
    String id;
    int song;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View textEntryView = factory.inflate(R.layout.fragment_blank, null);
       // textEntryView.setVisibility(View.INVISIBLE);
        // get the listview
        expListView = (ExpandableListView) textEntryView.findViewById(R.id.lvExp);
        linny  = (LinearLayout) textEntryView.findViewById(R.id.linny);
        // preparing list data
        prepareListData();


        listAdapter = new ExpanListAdapter(getContext(), listDataHeader, listDataChild);

        // setting list adapter
        linny.removeAllViews();
        expListView.setAdapter(listAdapter);
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {


                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                String item = listDataChild.get(
                        listDataHeader.get(groupPosition)).get(
                        childPosition);

                String btnID = CustomDialog.this.getTag();
                Resources res = getResources();
                int identifier = res.getIdentifier(btnID, "id", getContext().getPackageName());
                Button b = (Button) getActivity().findViewById(identifier);
                String sound = getSound(CustomDialog.this.getTag(),item);
                int soundId = res.getIdentifier(sound, "raw", getContext().getPackageName());

                setText(btnID, item, PadActivity.swap);
                int[] ids = PadActivity.soundsMap.get(b);
                int id1 = ids[0];
                int id2 = ids[1];
                if (!PadActivity.swap) {
                    SoundPool sp;
                    if(Integer.valueOf(b.getTag().toString().substring(1))>8){
                        sp = PadActivity.sp3;
                    }else{
                        sp = PadActivity.sp2;
                    }
                    sp.unload(id2);
                    int newID = sp.load(getContext(),soundId,1);
                    int[] arr = {id1,newID};
                    PadActivity.soundsMap.remove(b);
                    PadActivity.soundsMap.put(b,arr);


                } else {
                    SoundPool sp;
                    if(Integer.valueOf(b.getTag().toString().substring(1))>8){
                        sp = PadActivity.sp3;
                    }else{
                        sp = PadActivity.sp1;
                    }
                    sp.unload(id1);
                    sp.unload(id2);
                    int newID = sp.load(getContext(),soundId,1);
                    int[] arr = {newID,0};
                    PadActivity.soundsMap.remove(b);
                    PadActivity.soundsMap.put(b,arr);
                }

                CustomDialog.this.dismiss();
                return false;
            }
        });
        builder.setView(expListView);
        return builder.create();


    }

    public void CustomDialog(String id){
        this.id=id;
    }

    private String getSound(String tag, String item){
        String sound = "hello";
        if(item.equals("Snare 1")){
            sound = "snare1";
        }
        else if(item.equals("Snare 2")){
            sound = "snare2";
        }
        else if(item.equals("Snare 3")){
            sound = "snare3";
        }
        else if(item.equals("Snare 4")){
            sound = "snare4";
        }
        else if(item.equals("Kick 1")){
            sound = "kick1";
        }
        else if(item.equals("Kick 2")){
            sound = "kick2";
        }
        else if(item.equals("Kick 3")){
            sound = "kick3";
        }
        else if(item.equals("Kick 4")){
            sound = "kick4";
        }
        else if(item.equals("Lead 1")){
            sound = "lead1";
        }
        else if(item.equals("Lead 2")){
            sound = "lead2";
        }
        else if(item.equals("Lead 3")){
            sound = "lead3";
        }
        else if(item.equals("Lead 4")){
            sound = "lead4";
        }
        else if(item.equals("Hat 1")){
            sound = "hat1";
        }
        else if(item.equals("Hat 2")){
            sound = "hat2";
        }
        else if(item.equals("Hat 3")){
            sound = "hat3";
        }
        else if(item.equals("Hat 4")){
            sound = "hat4";
        }
        else if(item.equals("Bass 1")){
            sound = "bass1";
        }
        else if(item.equals("Bass 2")){
            sound = "bass2";
        }
        else if(item.equals("Bass 3")){
            sound = "bass3";
        }
        else if(item.equals("Bass 4")){
            sound = "bass4";
        }
        else if(item.equals("FX 1")){
            sound = "fx1crash";
        }
        else if(item.equals("FX 2")){
            sound = "fx2noise";
        }
        else if(item.equals("FX 3")){
            sound = "fx3bassdrop";
        }
        else if(item.equals("FX 4")){
            sound = "fx4riser";
        }
        else if(item.equals("FX 5")){
            sound = "fx5poonpooncannon";
        }
        else if(item.equals("FX 6")){
            sound = "fx6tom";
        }
        else if(item.equals("FX 7")){
            sound = "fx7tang";
        }


        return sound;
    }

    private void setText(String btnID, String sound, Boolean swap){
            TextView tv1;
            TextView tv2;
        System.out.println("this is btn id :" + btnID);
            if (btnID.equals("b1")) {
                tv1 = (TextView) getActivity().findViewById(R.id.b1tv1);
                tv2 = (TextView) getActivity().findViewById(R.id.b1tv2);
                if(swap) {
                    tv1.setText(sound);
                    tv2.setText("");
                }else{
                    tv2.setText(sound);
                }
            }
            else if (btnID.equals("b2")) {
                tv1 = (TextView) getActivity().findViewById(R.id.b2tv1);
                tv2 = (TextView) getActivity().findViewById(R.id.b2tv2);
                if(swap) {
                    tv1.setText(sound);
                    tv2.setText("");
                }else{
                    tv2.setText(sound);
                }
            }
            else if (btnID.equals("b3")) {
                tv1 = (TextView) getActivity().findViewById(R.id.b3tv1);
                tv2 = (TextView) getActivity().findViewById(R.id.b3tv2);
                if(swap) {
                    tv1.setText(sound);
                    tv2.setText("");
                }else{
                    tv2.setText(sound);
                }
            }
            else if (btnID.equals("b4")) {
                tv1 = (TextView) getActivity().findViewById(R.id.b4tv1);
                tv2 = (TextView) getActivity().findViewById(R.id.b4tv2);
                if(swap) {
                    tv1.setText(sound);
                    tv2.setText("");
                }else{
                    tv2.setText(sound);
                }
            }
            else if (btnID.equals("b5")) {
                tv1 = (TextView) getActivity().findViewById(R.id.b5tv1);
                tv2 = (TextView) getActivity().findViewById(R.id.b5tv2);
                System.out.println("helllo");
                if(swap) {
                    tv1.setText(sound);
                    tv2.setText("");
                }else{
                    tv2.setText(sound);
                }
            }
            else if (btnID.equals("b6")) {
                tv1 = (TextView) getActivity().findViewById(R.id.b6tv1);
                tv2 = (TextView) getActivity().findViewById(R.id.b6tv2);
                if(swap) {
                    tv1.setText(sound);
                    tv2.setText("");
                }else{
                    tv2.setText(sound);
                }
            }
            else if (btnID.equals("b7")) {
                tv1 = (TextView) getActivity().findViewById(R.id.b7tv1);
                tv2 = (TextView) getActivity().findViewById(R.id.b7tv2);
                if(swap) {
                    tv1.setText(sound);
                    tv2.setText("");
                }else{
                    tv2.setText(sound);
                }
            }
            else if (btnID.equals("b8")) {
                tv1 = (TextView) getActivity().findViewById(R.id.b8tv1);
                tv2 = (TextView) getActivity().findViewById(R.id.b8tv2);
                if(swap) {
                    tv1.setText(sound);
                    tv2.setText("");
                }else{
                    tv2.setText(sound);
                }
            }
            else if (btnID.equals("b9")) {
                tv1 = (TextView) getActivity().findViewById(R.id.b9tv1);
                tv2 = (TextView) getActivity().findViewById(R.id.b9tv2);
                if(swap) {
                    tv1.setText(sound);
                    tv2.setText("");
                }else{
                    tv2.setText(sound);
                }
            }
            else if (btnID.equals("b10")) {
                tv1 = (TextView) getActivity().findViewById(R.id.b10tv1);
                tv2 = (TextView) getActivity().findViewById(R.id.b10tv2);
                if(swap) {
                    tv1.setText(sound);
                    tv2.setText("");
                }else{
                    tv2.setText(sound);
                };
            }
            else if (btnID.equals("b11")) {
                tv1 = (TextView) getActivity().findViewById(R.id.b11tv1);
                tv2 = (TextView) getActivity().findViewById(R.id.b11tv2);
                if(swap) {
                    tv1.setText(sound);
                    tv2.setText("");
                }else{
                    tv2.setText(sound);
                }
            }
            else if (btnID.equals("b12")) {
                tv1 = (TextView) getActivity().findViewById(R.id.b12tv1);
                tv2 = (TextView) getActivity().findViewById(R.id.b12tv2);
                if(swap) {
                    tv1.setText(sound);
                    tv2.setText("");
                }else{
                    tv2.setText(sound);
                }
            }
            else if (btnID.equals("b13")) {
                tv1 = (TextView) getActivity().findViewById(R.id.b13tv1);
                tv2 = (TextView) getActivity().findViewById(R.id.b13tv2);
                if(swap) {
                    tv1.setText(sound);
                    tv2.setText("");
                }else{
                    tv2.setText(sound);
                }
            }
            else if (btnID.equals("b14")) {
                tv1 = (TextView) getActivity().findViewById(R.id.b14tv1);
                tv2 = (TextView) getActivity().findViewById(R.id.b14tv2);
                if(swap) {
                    tv1.setText(sound);
                    tv2.setText("");
                }else{
                    tv2.setText(sound);
                }
            }
            else if (btnID.equals("b15")) {
                tv1 = (TextView) getActivity().findViewById(R.id.b15tv1);
                tv2 = (TextView) getActivity().findViewById(R.id.b15tv2);
                if(swap) {
                    tv1.setText(sound);
                    tv2.setText("");
                }else{
                    tv2.setText(sound);
                }
            }
            else if (btnID.equals("b16")) {
                tv1 = (TextView) getActivity().findViewById(R.id.b16tv1);
                tv2 = (TextView) getActivity().findViewById(R.id.b16tv2);
                if(swap) {
                    tv1.setText(sound);
                    tv2.setText("");
                }else{
                    tv2.setText(sound);
                }
            }
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Swap/Add Sound Toggle");
        listDataHeader.add("Snare");
        listDataHeader.add("Kick");
        listDataHeader.add("Lead");
        listDataHeader.add("Bass");
        listDataHeader.add("Hat");
        listDataHeader.add("FX");

        // Adding child data
        List<String> arToggle = new ArrayList<String>();
        arToggle.add("Swap sound");
        arToggle.add("Add sound");

        List<String> snares = new ArrayList<String>();
        snares.add("Snare 1");
        snares.add("Snare 2");
        snares.add("Snare 3");
        snares.add("Snare 4");

        List<String> kicks = new ArrayList<String>();
        kicks.add("Kick 1");
        kicks.add("Kick 2");
        kicks.add("Kick 3");
        kicks.add("Kick 4");

        List<String> leads = new ArrayList<String>();
        leads.add("Lead 1");
        leads.add("Lead 2");
        leads.add("Lead 3");
        leads.add("Lead 4");

        List<String> basses = new ArrayList<String>();
        basses.add("Bass 1");
        basses.add("Bass 2");
        basses.add("Bass 3");
        basses.add("Bass 4");

        List<String> hiHats = new ArrayList<String>();
        hiHats.add("Hat 1");
        hiHats.add("Hat 2");
        hiHats.add("Hat 3");
        hiHats.add("Hat 4");

        List<String> fx = new ArrayList<String>();
        fx.add("FX 1");
        fx.add("FX 2");
        fx.add("FX 3");
        fx.add("FX 4");
        fx.add("FX 5");
        fx.add("FX 6");
        fx.add("FX 7");

        listDataChild.put(listDataHeader.get(0), arToggle);
        listDataChild.put(listDataHeader.get(1), snares); // Header, Child data
        listDataChild.put(listDataHeader.get(2), kicks);
        listDataChild.put(listDataHeader.get(3), leads);
        listDataChild.put(listDataHeader.get(4), basses);
        listDataChild.put(listDataHeader.get(5), hiHats);
        listDataChild.put(listDataHeader.get(6), fx);
    }
}
