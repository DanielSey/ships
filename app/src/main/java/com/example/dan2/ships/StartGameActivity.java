package com.example.dan2.ships;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class StartGameActivity extends Activity {

    View vCred; //pozadi

    int buttonTotal = 100;
    ArrayList<ImageButton> arrLiButtons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        vCred = findViewById(R.id.startGameAct);
        setBackground();

        Intent intent = getIntent();
        String m = intent.getStringExtra("coordinates");
        Toast.makeText(getApplicationContext(), "image: " + m, Toast.LENGTH_LONG).show();

        for(int i = 0; i < buttonTotal; i++){
            arrLiButtons.add((ImageButton)findViewById(R.id.button + i));
        }

        for (View v : arrLiButtons){
            if(arrLiButtons.equals(m)){
                Toast.makeText(getApplicationContext(), "true", Toast.LENGTH_LONG).show();
                v.setBackgroundResource(R.drawable.bigshipv2);
            }

        }
    }

    public void setBackground(){
        Intent intent = getIntent();
        switch (intent.getIntExtra("safeBackNext", 0)){
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
}
