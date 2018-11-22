package com.example.dan2.ships;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.Iterator;

public class PlayActivity extends Activity {

    /*class Data{
        int[] X;
        int[] Y;
        int[] shipType;
    }*/

    //nextAct
    String longText = "";

    //pozadi
    View vCred;
    int safeBackNextAct = 3;

    TextView textShipCount;
    TextView textBigShip;
    TextView textMiddleShip;
    TextView textSmallShip;
    TextView textSelectShip;

    ImageView imagePlay;
    ImageView imageBigShip;
    ImageView imageMiddleShip;
    ImageView imageSmallShip;

    int count = 0; //count kolik lodi je vybranych - celkem
    int buttonTotal = 100;
    int shipTotal = 9; //max. lodi
    int selectedShip = 0; //jake lod je vybrana

    //pocet vybranych lodi
    int countBigS = 0;
    int maxBigS = 1;
    int countMiddleS = 0;
    int maxMiddleS = 3;
    int countSmallS = 0;
    int maxSmallS = 5;

    //prohozeni
    boolean sw = true;

    ArrayList<ImageButton> arrLiImage = new ArrayList<>();
    ArrayList<ImageButton> arrLiButtons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        //background
        vCred = findViewById(R.id.playAct);
        setBackground();

        //text
        textSelectShip = findViewById(R.id.textSelectShip);
        textShipCount = findViewById(R.id.textShipCount);
        textBigShip = findViewById(R.id.textBigShip);
        textMiddleShip = findViewById(R.id.textMiddleShip);
        textSmallShip = findViewById(R.id.textSmallShip);

        //imageView
        imagePlay = findViewById(R.id.imagePlayPlayAct);
        imageBigShip = findViewById(R.id.imageBigShip);
        imageMiddleShip = findViewById(R.id.imageMidlleShip);
        imageSmallShip = findViewById(R.id.imageSmallShip);

        //playboard
        for(int i = 0; i < buttonTotal; i++){
            arrLiButtons.add((ImageButton)findViewById(R.id.button + i));
        }
        for (ImageButton button : arrLiButtons){
            button.setOnTouchListener(insertShipListener);
        }
    }

    public void setBackground(){
        Intent intent = getIntent();
        switch (intent.getIntExtra("safeBack", 0)){
            case 1:
                vCred.setBackgroundResource(R.drawable.background);
                safeBackNextAct = 1;
                break;
            case 2:
                vCred.setBackgroundResource(R.drawable.background2);
                safeBackNextAct = 2;
                break;
            case 3:
                vCred.setBackgroundResource(R.drawable.background3);
                safeBackNextAct = 3;
                break;
            case 4:
                vCred.setBackgroundResource(R.drawable.background4);
                safeBackNextAct = 4;
                break;
        }
    }

    //reset button
    public void resetSelectedShips(View view) {
        for (View v : arrLiImage){
            v.setBackgroundResource(R.drawable.wall);
        }
        for(int i = 0; i < arrLiImage.size(); i++){
            arrLiImage.remove(i);
        }
        for(int i = 0; i < arrLiImage.size(); i++){
            arrLiImage.remove(i);
        }

        imagePlay.setImageResource(R.drawable.play);

        selectedShip = 0;
        count = 0;
        countBigS = 0;
        countMiddleS = 0;
        countSmallS = 0;
        textSelectShip.setText("Selected ship: Big Ship");
        textShipCount.setText("Placed ships: " + count + "/9");
        textBigShip.setText("Name: Big ship\nHP: 5\nPlaced: " + countBigS + "/1");
        textMiddleShip.setText("Name: Middle ship\nHP: 3\nPlaced: " + countMiddleS + "/3");
        textSmallShip.setText("Name: Small ship\nHP: 1\nPlaced: " + countSmallS + "/5");
        //Toast.makeText(getApplicationContext(), "je list empty: " + arrLiImage.isEmpty(), Toast.LENGTH_LONG).show();
    }

    //back "button"
    public void closeActivity(View view) {
        finish();
    }

    public void startGameActivity(View view) {
        Intent intent = new Intent(this, StartGameActivity.class);
        intent.putExtra("safeBackNext", safeBackNextAct);
        intent.putExtra("coordinates", longText);
        startActivity(intent);
    }

    ImageButton.OnTouchListener insertShipListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            //int x = (int) motionEvent.getX();
            //int y = (int) motionEvent.getY();

            switch (motionEvent.getAction())
            {
                case MotionEvent.ACTION_DOWN: {
                    //Toast.makeText(getApplicationContext(), "x: " + x + ", y: " + y, Toast.LENGTH_LONG).show();
                    sw = true;
                    for (View v : arrLiImage) {
                        if (v.equals(((ImageButton) view))) {
                            count--;
                            ((ImageButton) view).setBackgroundResource(R.drawable.wall);
                            sw = false;
                            textShipCount.setText("Placed ships: " + count + "/9");
                            switch (selectedShip){
                                case 0:
                                    countBigS--;
                                    textBigShip.setText("Name: Big ship\nHP: 5\nPlaced: " + countBigS + "/1");
                                    break;
                                case 1:
                                    countMiddleS--;
                                    textMiddleShip.setText("Name: Middle ship\nHP: 3\nPlaced: " + countMiddleS + "/3");
                                    break;
                                case 2:
                                    countSmallS--;
                                    textSmallShip.setText("Name: Small ship\nHP: 1\nPlaced: " + countSmallS + "/5");
                                    break;
                            }
                            //Toast.makeText(getApplicationContext(), "je v listu: " + count, Toast.LENGTH_LONG).show();
                        }
                    }
                    if(count < shipTotal) {
                        if (sw) {
                            switch (selectedShip){
                                case 0:
                                    if(countBigS < maxBigS) {
                                        countBigS++;
                                        count++;
                                        ((ImageButton) view).setBackgroundResource(R.drawable.bigshipv2);
                                        arrLiImage.add((ImageButton) view);

                                        //longText += (((ImageButton) view).getId() + ",0;");
                                        longText += ((ImageButton) view).getId();

                                        Toast.makeText(getApplicationContext(), "image: " + longText, Toast.LENGTH_LONG).show();

                                        textShipCount.setText("Placed ships: " + count + "/9");
                                        textBigShip.setText("Name: Big ship\nHP: 5\nPlaced: " + countBigS + "/1");
                                    }
                                    break;
                                case 1:
                                    if(countMiddleS < maxMiddleS) {
                                        countMiddleS++;
                                        count++;
                                        ((ImageButton) view).setBackgroundResource(R.drawable.middleshipv2);
                                        arrLiImage.add((ImageButton) view);
                                        textShipCount.setText("Placed ships: " + count + "/9");
                                        textMiddleShip.setText("Name: Middle ship\nHP: 3\nPlaced: " + countMiddleS + "/3");
                                    }
                                    break;
                                case 2:
                                    if(countSmallS < maxSmallS) {
                                        countSmallS++;
                                        count++;
                                        ((ImageButton) view).setBackgroundResource(R.drawable.smallshipv2);
                                        arrLiImage.add((ImageButton) view);
                                        textShipCount.setText("Placed ships: " + count + "/9");
                                        textSmallShip.setText("Name: Small ship\nHP: 1\nPlaced: " + countSmallS + "/5");
                                    }
                                    break;
                            }
                            if(count == 9){imagePlay.setImageResource(R.drawable.play_yellow);}

                            //Toast.makeText(getApplicationContext(), "neni v listu: " + count, Toast.LENGTH_LONG).show();
                        } else {
                            arrLiImage.remove(view);
                        }
                    }
                    break;
                }
            }
            return true;
        }
    };

    public void bigShipSelected(View view) {
        selectedShip = 0;
        textSelectShip.setText("Selected ship: Big Ship");
    }

    public void middleShipSelected(View view) {
        selectedShip = 1;
        textSelectShip.setText("Selected ship: Middle Ship");
    }

    public void smallShipSelected(View view) {
        selectedShip = 2;
        textSelectShip.setText("Selected ship: Small Ship");
    }
}
