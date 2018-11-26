package com.example.dan2.ships;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

    int safeBack = 3;

    ImageView imagePlay;
    ImageView imageSettings;
    ImageView imageCredits;
    ImageView imageExit;
    ImageView imageStats;

    //sound
    MediaPlayer rollover1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imagePlay = findViewById(R.id.imagePlay);
        imageStats = findViewById(R.id.imageStats);
        imageSettings = findViewById(R.id.imageSettings);
        imageCredits = findViewById(R.id.imageCredits);
        imageExit = findViewById(R.id.imageExit);

        //sound
        rollover1 = MediaPlayer.create(this, R.raw.rollover3);
    }

    //play "button"
    public void startPlayActivity(View view) {
        rollover1.start();
        Intent intent = new Intent(this, PlayActivity.class);
        intent.putExtra("safeBack", safeBack);
        startActivity(intent);
    }

    //statistics "button"
    public void startStatisticsActivity(View view) {
        rollover1.start();
        Intent intent = new Intent(this, StatisticsActivity.class);
        intent.putExtra("safeBack", safeBack);
        startActivity(intent);
    }

    //settings "button"
    public void startSettingsActivity(View view) {
        rollover1.start();
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("safeBack", safeBack);
        startActivityForResult(intent, 100);
    }

    //credits "button"
    public void startCreditActivity(View view) {
        rollover1.start();
        Intent intent = new Intent(this, CreditsActivity.class);
        intent.putExtra("safeBack", safeBack);
        startActivity(intent);
    }

    //exit "button"
    public void exitApp(View view) {
        rollover1.start();
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Exit");
        alert.setIcon(R.drawable.alert);
        alert.setMessage("Do you really want close app?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //finish();
                System.exit(0);
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        View vMain = findViewById(R.id.mainAct);
        if(requestCode == 100){
            switch(data.getIntExtra("background", 0)){
                case 1:
                    vMain.setBackgroundResource(R.drawable.background);
                    safeBack = 1;
                    break;
                case 2:
                    vMain.setBackgroundResource(R.drawable.background2);
                    safeBack = 2;
                    break;
                case 3:
                    vMain.setBackgroundResource(R.drawable.background3);
                    safeBack = 3;
                    break;
                case 4:
                    vMain.setBackgroundResource(R.drawable.background4);
                    safeBack = 4;
                    break;
            }
        }
    }
}
