package com.example.dan2.ships;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

    //public static Context context;

    int safeBack = 3;

    ImageView imagePlay;
    ImageView imageSettings;
    ImageView imageCredits;
    ImageView imageExit;
    ImageView imageStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //context = getApplicationContext();

        imagePlay = findViewById(R.id.imagePlay);
        imageStats = findViewById(R.id.imageStats);
        imageSettings = findViewById(R.id.imageSettings);
        imageCredits = findViewById(R.id.imageCredits);
        imageExit = findViewById(R.id.imageExit);
    }

    //play "button"
    public void startPlayActivity(View view) {
        Intent intent = new Intent(this, PlayActivity.class);
        intent.putExtra("safeBack", safeBack);
        startActivity(intent);
    }

    //statistics "button"
    public void startStatisticsActivity(View view) {
        Intent intent = new Intent(this, StatisticsActivity.class);
        intent.putExtra("safeBack", safeBack);
        startActivity(intent);
    }

    //settings "button"
    public void startSettingsActivity(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("safeBack", safeBack);
        startActivityForResult(intent, 100);
    }

    //credits "button"
    public void startCreditActivity(View view) {
        Intent intent = new Intent(this, CreditsActivity.class);
        intent.putExtra("safeBack", safeBack);
        startActivity(intent);
    }

    //exit "button"
    public void exitApp(View view) {
        finish();
        System.exit(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        View vMain = findViewById(R.id.mainAct);
        if(requestCode == 100){
            //Toast.makeText(getApplicationContext(), "safeback:" + data.getIntExtra("background", 0), Toast.LENGTH_LONG).show();

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
