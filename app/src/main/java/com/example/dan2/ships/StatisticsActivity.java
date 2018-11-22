package com.example.dan2.ships;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class StatisticsActivity extends Activity {

    TextView textPlays;
    TextView textWins;
    TextView textLoses;

    View vCred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        textPlays = findViewById(R.id.textPlays);
        textWins = findViewById(R.id.textWins);
        textLoses = findViewById(R.id.textLoses);

        textPlays.setTextColor(Color.WHITE);
        textWins.setTextColor(Color.WHITE);
        textLoses.setTextColor(Color.WHITE);

        textPlays.setText("Plays: 0");
        textWins.setText("Wins: 0");
        textLoses.setText("Loses: 0");

        vCred = findViewById(R.id.seStatsAct);
        setBackground();
    }

    public void setBackground(){
        Intent intent = getIntent();
        switch (intent.getIntExtra("safeBack", 0)){
            case 1:
                vCred.setBackgroundResource(R.drawable.background);
                break;
            case 2:
                vCred.setBackgroundResource(R.drawable.background2);
                break;
            case 3:
                vCred.setBackgroundResource(R.drawable.background3);
                break;
            case 4:
                vCred.setBackgroundResource(R.drawable.background4);
                break;
        }
    }

    //back "button"
    public void closeActivity(View view) {
        finish();
    }
}
