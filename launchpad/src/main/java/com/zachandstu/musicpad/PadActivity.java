package com.zachandstu.musicpad;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.util.HashMap;

public class PadActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {


    static SoundPool sp1, sp2, sp3;
    int spId1, spId2, spId3, spId4, spId5, spId6, spId7, spId8, spB1, spB2, spB3, spB4, spB5, spB6, spB7, spB8;
    Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16;
    TextView bpm;

    LinearLayout pad;
    LinearLayout topLvlpad;
    Integer focusIndex;
    static HashMap<Button, int[]> soundsMap;
    static HashMap<Button, Integer> clickedMap;
    int waitVar = 132;

    static Thread poll;
    SeekBar speedBar;
    Drawable unpressed;
    Drawable pressed;
    CustomDialog newFragment;
    HelpDialog helpFragment;
    public static boolean swap = true;
    private boolean lock = true;

//    AdView mAdView;



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.altlayout);

        try {
            varSetUp();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);


        seqSetUp();
        interSetUp();


    }


    private void startPolling(){
        poll = new Thread(new Runnable() {
            @Override
            public void run() {
                while (lock) {
                    try {
                        if (focusIndex == 8) {
                            focusIndex = 0;
                        }
                        playCell2((Activity) PadActivity.this);
                        focusIndex++;
                        if((600 - waitVar)<0){
                            waitVar = 600;
                        }
                        Thread.sleep((600 - waitVar)/4);

                    } catch (InterruptedException i) {

                    }
                }
            }
        });
        poll.start();
    }



    // swap / add song toggle method
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void toggle(View view){
        Button b = (Button) newFragment.getDialog().findViewById(R.id.lblListHeader1);
        Button b1 = (Button) newFragment.getDialog().findViewById(R.id.lblListHeader2);
        if(swap && b1==view){
            b1.setTextSize(16);
            b.setTextSize(12);
            swap=false;
        }else if(!swap & b==view){
            b.setTextSize(16);
            b1.setTextSize(12);
            swap=true;
        }

    }

    private void playCell2(Activity act) {
        int row;
        int col;
        int prevRow;
        int prevCol;

        if (focusIndex < 4) {
            row = 0;
            col = focusIndex;
        } else {
            row = 1;
            col = focusIndex - 4;
        }

        if (col == 0) {
            if (row == 0) {
                prevRow = 1;
            } else {
                prevRow = 0;
            }
            prevCol = 3;
        } else {
            prevRow = row;
            prevCol = col - 1;
        }

        LinearLayout v = (LinearLayout) topLvlpad.getChildAt(row);
        LinearLayout prevR = (LinearLayout) topLvlpad.getChildAt(prevRow);
        final RelativeLayout vChild = (RelativeLayout) v.getChildAt(col);
        Button child = (Button) vChild.getChildAt(0);
        int i = 1;
        if( child==null){
            return;
        }
        if (clickedMap.get(child) == 1) {

            int[] arr = soundsMap.get(child);
            sp1.play(arr[0], 1, 1, 0, 0, 1);
            if(arr[1] != 0) {
                sp2.play(arr[1], 1, 1, 0, 0, 1);
            }

        }

        final View p = prevR.getChildAt(prevCol);
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // This code will always run on the UI thread, therefore is safe to modify UI elements.
                vChild.setAlpha(0.1f);
                p.setAlpha(1);
            }
        });

    }

    public void openDialog(String tag) throws InterruptedException {
        newFragment = new CustomDialog();
        newFragment.show(getSupportFragmentManager(), tag);
    }

    public boolean openHelpDialog(MenuItem mItem) throws InterruptedException {
        helpFragment = new HelpDialog();
        helpFragment.show(getSupportFragmentManager(), null);
        return false;
    }


    /********* Setup Methods **************/
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void varSetUp() throws IOException {
//        mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
        bpm = (TextView) findViewById(R.id.bpm);
        unpressed = (Drawable) getResources().getDrawable(R.drawable.roundedbutton);
        pressed = (Drawable) getResources().getDrawable(R.drawable.buttonpressed);

        speedBar = (SeekBar) findViewById(R.id.seekBar);
        speedBar.setOnSeekBarChangeListener(this);
        speedBar.setProgress(132);

        b1 = (Button) findViewById(R.id.b1);
        b2 = (Button) findViewById(R.id.b2);
        b3 = (Button) findViewById(R.id.b3);
        b4 = (Button) findViewById(R.id.b4);
        b5 = (Button) findViewById(R.id.b5);
        b6 = (Button) findViewById(R.id.b6);
        b7 = (Button) findViewById(R.id.b7);
        b8 = (Button) findViewById(R.id.b8);

        b9 = (Button) findViewById(R.id.b9);
        b10 = (Button) findViewById(R.id.b10);
        b11 = (Button) findViewById(R.id.b11);
        b12 = (Button) findViewById(R.id.b12);
        b13 = (Button) findViewById(R.id.b13);
        b14 = (Button) findViewById(R.id.b14);
        b15 = (Button) findViewById(R.id.b15);
        b16 = (Button) findViewById(R.id.b16);

        //pad = (GridLayout) findViewById(R.id.pad);
        pad = (LinearLayout) findViewById(R.id.altpad);
        topLvlpad = (LinearLayout) pad.getChildAt(0);
        sp1 = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        spId1 = sp1.load(this,R.raw.kick3 ,1);
        spId2= sp1.load(this, R.raw.hat1, 1);
        spId3 = sp1.load(this, R.raw.kick2, 1);
        spId4 = sp1.load(this,R.raw.hat1 ,1);
        spId5= sp1.load(this, R.raw.kick3, 1);
        spId6 = sp1.load(this, R.raw.hat1, 1);
        spId7 = sp1.load(this,R.raw.hat2 ,1);
        spId8 = sp1.load(this, R.raw.hat1, 1);

        sp2 = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        sp3 = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        spB1 = sp3.load(this,R.raw.fx3bassdrop ,1);
        spB2= sp3.load(this, R.raw.fx4riser, 1);
        spB3 = sp3.load(this, R.raw.fx5poonpooncannon, 1);
        spB4 = sp3.load(this,R.raw.fx7tang ,1);
        spB5= sp3.load(this, R.raw.lead2, 1);
        spB6 = sp3.load(this, R.raw.lead3, 1);
        spB7 = sp3.load(this,R.raw.bass3 ,1);
        spB8 = sp3.load(this, R.raw.lead4, 1);

        soundsMap = new HashMap<Button, int[]>();

        int[] b1array = {spId1,0};
        int[] b2array = {spId2,0};
        int[] b3array = {spId3,0};
        int[] b4array = {spId4,0};
        int[] b5array = {spId5,0};
        int[] b6array = {spId6,0};
        int[] b7array = {spId7,0};
        int[] b8array = {spId8,0};

        int[] b9array = {spB1,0};
        int[] b10array = {spB2,0};
        int[] b11array = {spB3,0};
        int[] b12array = {spB4,0};
        int[] b13array = {spB5,0};
        int[] b14array = {spB6,0};
        int[] b15array = {spB7,0};
        int[] b16array = {spB8,0};


        soundsMap.put(b1, b1array);
        soundsMap.put(b2, b2array);
        soundsMap.put(b3, b3array);
        soundsMap.put(b4, b4array);
        soundsMap.put(b5, b5array);
        soundsMap.put(b6, b6array);
        soundsMap.put(b7, b7array);
        soundsMap.put(b8, b8array);
        soundsMap.put(b9, b9array);
        soundsMap.put(b10, b10array);
        soundsMap.put(b11, b11array);
        soundsMap.put(b12, b12array);
        soundsMap.put(b13, b13array);
        soundsMap.put(b14, b14array);
        soundsMap.put(b15, b15array);
        soundsMap.put(b16, b16array);

        clickedMap = new HashMap<Button, Integer>();
        clickedMap.put(b1, 1);
        clickedMap.put(b2, 0);
        clickedMap.put(b3, 0);
        clickedMap.put(b4, 0);
        clickedMap.put(b5, 0);
        clickedMap.put(b6, 0);
        clickedMap.put(b7, 0);
        clickedMap.put(b8, 0);

        b1.setBackground(pressed);


        focusIndex = 0;
    }

    private void seqSetUp() {
        b1.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    seqHelper(b1);
                }
                return false;
            }
        });
        b1.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onLongClick(View v) {
                seqHelper2(b1);
                return false;
            }
        });
        b2.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    seqHelper(b2);
                }
                return false;
            }
        });
        b2.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onLongClick(View v) {
                seqHelper2(b2);
                return false;
            }
        });
        b3.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    seqHelper(b3);
                }
                return false;
            }
        });
        b3.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onLongClick(View v) {
                seqHelper2(b3);
                return false;
            }
        });
        b4.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    seqHelper(b4);
                }
                return false;
            }
        });
        b4.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onLongClick(View v) {
                seqHelper2(b4);
                return false;
            }
        });
        b5.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    seqHelper(b5);
                }
                return false;
            }
        });
        b5.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onLongClick(View v) {
                seqHelper2(b5);
                return false;
            }
        });
        b6.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    seqHelper(b6);
                }
                return false;
            }
        });
        b6.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onLongClick(View v) {
                seqHelper2(b6);
                return false;
            }
        });
        b7.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    seqHelper(b7);
                }
                return false;
            }
        });
        b7.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onLongClick(View v) {
                seqHelper2(b7);
                return false;
            }
        });
        b8.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    seqHelper(b8);
                }
                return false;
            }
        });
        b8.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onLongClick(View v) {
                seqHelper2(b8);
                return false;
            }
        });


    }

    private void interSetUp() {
        b9.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                interHelper(v);
                return false;
            }
        });
        b9.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    bottomHelper(b9);
                }
                return false;
            }
        });

        b10.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                interHelper(v);
                return false;
            }
        });
        b10.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    bottomHelper(b10);
                }
                return false;
            }
        });
        b11.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                interHelper(v);
                return false;
            }
        });
        b11.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    bottomHelper(b11);
                }
                return false;
            }
        });
        b12.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                interHelper(v);
                return false;
            }
        });
        b12.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    bottomHelper(b12);
                }
                return false;
            }
        });
        b13.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                interHelper(v);
                return false;
            }
        });
        b13.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    bottomHelper(b13);
                }
                return false;
            }
        });
        b14.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                interHelper(v);
                return false;
            }
        });
        b14.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    bottomHelper(b14);
                }
                return false;
            }
        });
        b15.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                interHelper(v);
                return false;
            }
        });
        b15.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    bottomHelper(b15);
                }
                return false;
            }
        });
        b16.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                interHelper(v);
                return false;
            }
        });
        b16.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    bottomHelper(b16);
                }
                return false;
            }
        });


    }

     /*############    END   ################*/


    /********* Overriding Methods **************/
    @Override
    public void onPause(){

        lock=false;
        super.onPause();

    }
    @Override
    protected  void onStop(){

        lock=false;
        super.onStop();
    }
    @Override
    protected  void onDestroy(){

        sp1.release();
        sp1 = null;
        sp2.release();
        sp2 = null;
        sp3.release();
        sp3 = null;
        lock=false;
        super.onDestroy();
    }
    @Override
    protected  void onResume(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started_num), false);
        if(!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(getString(R.string.pref_previously_started_num), Boolean.TRUE);
            edit.commit();
            for (int i=0; i < 3; i++)
            {
                Toast.makeText(getApplicationContext(),"Click any square to play a sound!", Toast.LENGTH_LONG).show();
            }
            for (int i=0; i < 3; i++)
            {
                Toast.makeText(getApplicationContext(),"Hold down a square to set it's sound!", Toast.LENGTH_LONG).show();
            }
        }
        lock=true;
        super.onResume();
        startPolling();
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onRestart(){

        super.onRestart();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

     /*############    END   ################*/


    /********* Helper Methods **************/

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void seqHelper(Button b){
        Integer i = clickedMap.get(b);
        if (i == 0) {
            i = 1;
            b.setBackground(pressed);
        } else {
            i = 0;
            b.setBackground(unpressed);
        }
        clickedMap.remove(b);
        clickedMap.put(b, i);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void seqHelper2(Button b){
        clickedMap.remove(b);
        clickedMap.put(b, 0);
        b.setBackground(unpressed);
        try {
            openDialog(b.getTag().toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void interHelper(View v){
        try {
            openDialog(v.getTag().toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void interHelper2(MediaPlayer mp){
        if (mp.isPlaying()) {
            mp.seekTo(0);
        }
        mp.start();
    }

    private void bottomHelper(Button b){
        int[] arr = soundsMap.get(b);
        for(int i : arr){
            if(i!=0) {
                sp3.play(i, 1, 1, 0, 0, 1);
            }
        }
    }

    /*############    END   ################*/



    /********** SeekBar Methods ************/

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            waitVar = progress;   // min: 0  --  max: 300
            bpm.setText(60000/(600-waitVar) + "bpm");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //Toast.makeText(getApplicationContext(),"seekbar touch started!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //Toast.makeText(getApplicationContext(),"seekbar touch stopped!", Toast.LENGTH_SHORT).show();
    }

    /*############    END   ################*/


}

