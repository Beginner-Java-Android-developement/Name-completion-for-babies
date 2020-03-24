package com.golan.amit.namecompletion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    public static final String DEBUGTAG = "AMGO";
    public static final boolean DEBUG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        redirect();
    }

    private void redirect() {
        Log.d(MainActivity.DEBUGTAG, "redirecting");
        Intent i = new Intent(this, NamingActivity.class);
        startActivity(i);
    }
}
