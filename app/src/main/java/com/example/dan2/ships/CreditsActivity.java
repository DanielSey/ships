package com.example.dan2.ships;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CreditsActivity extends Activity {

    View vCred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        vCred = findViewById(R.id.credAct);
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
