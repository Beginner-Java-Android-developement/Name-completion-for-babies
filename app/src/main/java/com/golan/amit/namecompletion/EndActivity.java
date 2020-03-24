package com.golan.amit.namecompletion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EndActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnPlayAgain;
    SharedPreferences sp;
    TextView tvDisplay;
    ImageView ivStatus;
    Animation animation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        ivStatus = findViewById(R.id.ivStatus);
        tvDisplay = findViewById(R.id.tvInfoDisplay);

        animation = AnimationUtils.loadAnimation(this, R.anim.anim_slideup);

        btnPlayAgain = findViewById(R.id.btnPlayAgain);
        btnPlayAgain.setOnClickListener(this);
        sp = getSharedPreferences("namecompetion", MODE_PRIVATE);
        String name = sp.getString("name", null);
        Boolean won = sp.getBoolean("state", false);
        if (name != null) {
            String toDisplay = name + " " + (won ? " ניצח/ה" : "הפסיד/ה");
            tvDisplay.setText(toDisplay);

            if (won) {
                ivStatus.setImageResource(R.mipmap.thumbupgreen);
            } else {
                ivStatus.setImageResource(R.mipmap.thumbdownred);
            }
            ivStatus.startAnimation(animation);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == btnPlayAgain) {
            Intent i = new Intent(this, NamingActivity.class);
            startActivity(i);
        }
    }
}
