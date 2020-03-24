package com.golan.amit.namecompletion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class NamingActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    TextView[] tvLetters, tvBase;
    MediaPlayer mp;
    SeekBar sb;
    AudioManager am;
    NamingHelper nh;
    ImageButton ibSoundOff, ibRollbackId;
    SharedPreferences sp;

    RadioGroup radioGroupName;
    RadioButton radioButtonTheName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naming);

        init();

        PlayNameAsync();

        setListeners();

    }

    private void setListeners() {
        for(int i = 0; i < tvBase.length; i++) {
            tvBase[i].setOnClickListener(this);
        }
        radioGroupName.setOnCheckedChangeListener(this);
    }

    private void PlayNameAsync() {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    PlayName();
                } catch (Exception e) {
                }
                return null;
            }
        }.execute();

    }

    private void PlayName() {

        try {
            radioGroupName.setVisibility(View.INVISIBLE);
            if(MainActivity.DEBUG) {
                Log.d(MainActivity.DEBUGTAG, "set radio to invisible");
            }
        } catch (Exception e) {
            Log.e(MainActivity.DEBUGTAG, "setting invisible exception");
        }

        nh.generate_Name_rnd_index();
        for (int r = 0; r < nh.getName_rnd_index(); r++) {
            nh.generate_random_name();
            if (r % 3 != 0) {
                for (int i = 0; i < nh.getRnd_name().length; i++) {
                    setText(tvLetters[i], nh.getNameCharByIndex(i));
                    SystemClock.sleep(150);
                }
            } else {
                for (int i = (nh.getRnd_name().length - 1); i >= 0; i--) {
                    setText(tvLetters[i], nh.getNameCharByIndex(i));
                    SystemClock.sleep(150);
                }
            }
            SystemClock.sleep(100);
        }

        //  typing the base:
        if(MainActivity.DEBUG) {
            Log.d(MainActivity.DEBUGTAG, "tv base length: " + tvBase.length);
        }
        for (int i = 0; i < tvBase.length; i++) {
            try {
                setVisible(tvBase[i]);
            } catch (Exception e) {
                Log.e(MainActivity.DEBUGTAG, "visible exception " + e);
            }
        }

        for (int i = (nh.getRnd_name().length - 1); i >= 0; i--) {
            setText(tvBase[i], nh.getNameCharByIndex(i));
            SystemClock.sleep(10);
        }


        nh.generate_ordered_name();
        SystemClock.sleep(500);
        for (int i = (nh.getCurr_name().length() - 1); i >= 0; i--) {
            setText(tvLetters[i], nh.getNameCharByIndex((nh.getCurr_name().length() - 1) - i));
            SystemClock.sleep(100);
        }
        SystemClock.sleep(1000);
        for (int i = 0; i < tvLetters.length; i++) {
            setText(tvLetters[i], "");
            SystemClock.sleep(100);
        }

        try {
            setVisible(radioGroupName);
            if(MainActivity.DEBUG) {
                Log.d(MainActivity.DEBUGTAG, "set radio to visible");
            }
        } catch (Exception e) {
            Log.e(MainActivity.DEBUGTAG, "setting visible exception");
        }
    }

    private void setVisible(final RadioGroup rdg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rdg.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setVisible(final TextView textView) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setInVisible(final TextView textView) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setText(final TextView tvLetter, final String nameCharByIndex) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvLetter.setText(nameCharByIndex);
            }
        });
    }

    private void init() {
        tvLetters = new TextView[]{
                findViewById(R.id.tvLetter0), findViewById(R.id.tvLetter1),
                findViewById(R.id.tvLetter2), findViewById(R.id.tvLetter3),
                findViewById(R.id.tvLetter4)
        };

        tvBase = new TextView[]{
                findViewById(R.id.tvBase0), findViewById(R.id.tvBase1),
                findViewById(R.id.tvBase2), findViewById(R.id.tvBase3),
                findViewById(R.id.tvBase4)
        };
        nh = new NamingHelper();
        sb = findViewById(R.id.sb);
        mp = MediaPlayer.create(this, R.raw.pinkpanther);
        mp.start();

        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        sb.setMax(max);
        sb.setProgress(max / 4);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, max / 4, 0);
        sb.setOnSeekBarChangeListener(this);

        ibSoundOff = findViewById(R.id.ibSoundOff);
        ibSoundOff.setOnClickListener(this);
        ibRollbackId = findViewById(R.id.ibRollbackId);
        ibRollbackId.setOnClickListener(this);

        sp = getSharedPreferences("namecompetion", MODE_PRIVATE);

        radioGroupName = findViewById(R.id.radioBtnName);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mp != null) {
            try {
                mp.start();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        if(v == ibRollbackId) {
            if(MainActivity.DEBUG) {
                Log.d(MainActivity.DEBUGTAG, "rollback delete last button was clicked");
            }
            //  decrease counter
            // if counter == 0 invisible
            nh.decreaseName_counter();
            if(nh.getName_counter() == 0)
                ibRollbackId.setVisibility(View.INVISIBLE);
            int tmpCurrPosition = -1;
            if(nh.getSti().size() > 0) {
                tmpCurrPosition = nh.pop_stack();
                if(MainActivity.DEBUG) {
                    Log.d(MainActivity.DEBUGTAG, "fetched last current position from stack: " + tmpCurrPosition);
                }
            }
            String tmpChar = null;
            int hebCurr = (nh.getCurr_name().length() - 1) - nh.getName_counter();
            try {
//                tmpChar = tvLetters[nh.getName_counter()].getText().toString();
                tmpChar = tvLetters[hebCurr].getText().toString();
                if(MainActivity.DEBUG) {
                    Log.d(MainActivity.DEBUGTAG, "the current character is: " + tmpChar);
                }
            } catch (Exception e) {}
            if(tmpChar != null && tmpCurrPosition != -1) {
//                tvLetters[nh.getName_counter()].setText("");
                tvLetters[hebCurr].setText("");
                tvBase[tmpCurrPosition].setText(tmpChar);
            }
            return;
        }
        else if (v == ibSoundOff) {
            mp.stop();
            sb.setVisibility(View.INVISIBLE);
            ibSoundOff.setVisibility(View.INVISIBLE);
            return;
        }
        int tmpCurrBtn = -1;
        for(int i = 0; i < tvBase.length; i++) {
            if(v == tvBase[i]) {
                tmpCurrBtn = i;
                break;
            }
        }
        if(tmpCurrBtn == -1) {
            if(MainActivity.DEBUG) {
                Log.d(MainActivity.DEBUGTAG, "btn was not detected");
            }
            return;
        }
        if(MainActivity.DEBUG) {
            Log.d(MainActivity.DEBUGTAG, "button " + tmpCurrBtn + " was clicked");
        }

        //  Let the fun begin
        ibRollbackId.setVisibility(View.VISIBLE);
        String tmpChar = null;
        try {
            tmpChar = tvBase[tmpCurrBtn].getText().toString();
        } catch (Exception e) {
            Log.e(MainActivity.DEBUGTAG, "pick character exception");
            return;
        }
        if(tmpChar.equalsIgnoreCase("")) {
            if(MainActivity.DEBUG) {
                Log.d(MainActivity.DEBUGTAG, "empty - no character");
            }
            return;
        }
        tvBase[tmpCurrBtn].setText("");
        int hebCounter = (nh.getCurr_name().length() - 1) -  nh.getName_counter();
//        tvLetters[nh.getName_counter()].setText(tmpChar);
        tvLetters[hebCounter].setText(tmpChar);
        nh.increaseName_counter();
        if(nh.getName_counter() == nh.getCurr_name().length()) {
            //  finish !!
            if(MainActivity.DEBUG) {
                Log.d(MainActivity.DEBUGTAG, "finished ");
            }
            for (int i = 0; i < tvBase.length; i++) {
                try {
                    setInVisible(tvBase[i]);
                } catch (Exception e) {
                    Log.e(MainActivity.DEBUGTAG, "invisible exception " + e);
                }
            }
            ibRollbackId.setVisibility(View.INVISIBLE);
            boolean won = won_evaluated();
            String endgame_state = won ? "WON" : "LOST";
            if(MainActivity.DEBUG) {
                Log.d(MainActivity.DEBUGTAG, endgame_state);
            }

            SharedPreferences.Editor editor = sp.edit();
            editor.putString("name", nh.getCurr_name());
            editor.putBoolean("state", won);
            editor.commit();
            Intent i = new Intent(this, EndActivity.class);
            startActivity(i);

            return;
        }
        nh.push_stack(tmpCurrBtn);
        if(MainActivity.DEBUG) {
            Log.d(MainActivity.DEBUGTAG, "stack size is now: " + nh.getSti().size());
        }

    }

    private boolean won_evaluated() {
        StringBuilder sb = new StringBuilder();
        for(int i = tvLetters.length - 1; i >= 0 ; i--) {
            sb.append(tvLetters[i].getText().toString());
        }
        if(MainActivity.DEBUG) {
            Log.d(MainActivity.DEBUGTAG, "evaluated name: " + sb.toString() +
                    " and original name is: " + nh.getCurr_name());
        }
        return sb.toString().equalsIgnoreCase(nh.getCurr_name());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.ariel:
                Toast.makeText(this, "ariel ", Toast.LENGTH_SHORT).show();
                nh = new NamingHelper();
                nh.setName_ptr(NamingHelper.ARIEL);
                for (int i = 0; i < tvBase.length; i++) {
                    try {
                        setInVisible(tvBase[i]);
                    } catch (Exception e) {
                        Log.e(MainActivity.DEBUGTAG, "invisible exception " + e);
                    }
                }
                ibRollbackId.setVisibility(View.INVISIBLE);
                PlayNameAsync();
                break;
            case R.id.lior:
                Toast.makeText(this, "lior ", Toast.LENGTH_SHORT).show();
                nh = new NamingHelper();
                nh.setName_ptr(NamingHelper.LIOR);
                for (int i = 0; i < tvBase.length; i++) {
                    try {
                        setInVisible(tvBase[i]);
                    } catch (Exception e) {
                        Log.e(MainActivity.DEBUGTAG, "invisible exception " + e);
                    }
                }
                ibRollbackId.setVisibility(View.INVISIBLE);
                PlayNameAsync();
                break;
            default:
                Toast.makeText(this, "default ", Toast.LENGTH_SHORT).show();
                break;
        }
        ;
    }
}
