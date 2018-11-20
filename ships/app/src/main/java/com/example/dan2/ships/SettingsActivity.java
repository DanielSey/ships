package com.example.dan2.ships;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SettingsActivity extends Activity {

    //int back;
    //int setBack = 3;
    View vSett;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        vSett = findViewById(R.id.settAct);
        setBackground();

        //Toast.makeText(getApplicationContext(), "text: " + back, Toast.LENGTH_LONG).show();

    }

    public void setBackground(){
        Intent intent = getIntent();
        switch (intent.getIntExtra("safeBack", 0)){
            case 1:
                vSett.setBackgroundResource(R.drawable.background);
                break;
            case 2:
                vSett.setBackgroundResource(R.drawable.background2);
                break;
            case 3:
                vSett.setBackgroundResource(R.drawable.background3);
                break;
            case 4:
                vSett.setBackgroundResource(R.drawable.background4);
                break;
        }
    }

    //back "button"
    public void closeActivity(View view){
        Intent intent = new Intent();
        intent.putExtra("background", 0);
        setResult(100, intent);
        finish();
    }

    public void selectedBackground(View view) {
        Intent intent = new Intent();
        intent.putExtra("background", 1);
        setResult(100, intent);
        finish();
    }

    public void selectedBackground2(View view) {
        Intent intent = new Intent();
        intent.putExtra("background", 2);
        setResult(100, intent);
        finish();
    }

    public void selectedBackground3(View view) {
        Intent intent = new Intent();
        intent.putExtra("background", 3);
        setResult(100, intent);
        finish();
    }

    public void selectedBackground4(View view) {
        Intent intent = new Intent();
        intent.putExtra("background", 4);
        setResult(100, intent);
        finish();
    }
}
