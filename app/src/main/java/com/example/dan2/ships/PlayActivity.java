package com.example.dan2.ships;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class PlayActivity extends Activity {

    //db
    private DBHelper mydb;
    ArrayAdapter<CharSequence> adapter;
    int id_To_Update = 0;

    //stats
    int games = 0;
    int wins = 0;
    int loses  = 0;
    int shots = 0;
    int AIShots = 0;
    int placedShips = 0;
    int placedBigShips = 0;
    int placedMiddleShips = 0;
    int placedSmallShips = 0;
    SharedPreferences mySharedPref;
    SharedPreferences.Editor mySharedEditor;

    //pozadi
    View vCred;
    int safeBackNextAct = 3;

    TextView textShipCount;
    TextView textBigShip;
    TextView textMiddleShip;
    TextView textSmallShip;
    TextView textSelectShip;
    TextView textEnemyPlayboard;
    TextView textAlive;
    TextView textAliveEnemy;
    TextView textActualShots;
    TextView textActualEShots;

    ImageView imageOKBigS;
    ImageView imageOKMiddleS;
    ImageView imageOKSmallS;
    ImageView imageBack;
    ImageView imageSipka;
    ImageView imageEndTitle;
    ImageView imageSave;

    Button playBut;
    Button resetBut;

    LinearLayout linearLayout;
    TableLayout tableLayout;
    TableLayout EnemyTableLayout;

    //shipCounts
    int count = 0; //count kolik lodi je vybranych - celkem
    int shipTotal = 9; //max. lodi
    int selectedShip = 0; //jaka lod je vybrana
    int countActualShots = 0;
    int countActualEShots = 0;

    //pocet vybranych lodi
    int countBigS = 0;
    int maxBigS = 1;
    int countMiddleS = 0;
    int maxMiddleS = 3;
    int countSmallS = 0;
    int maxSmallS = 5;

    //AI
    Random rnd = new Random();
    int[] shottedButtons = new int[100];
    int indexShotu = 0;
    int randomShot;
    boolean shot2 = true;

    //prohozeni - lod je/neni
    boolean sw = true;

    //enemyListener
    boolean shot = true;

    //end game
    boolean endGame = false;
    EditText inputName;

    //time
    long startTime;
    long difference;

    //sound
    MediaPlayer click1; //select ship, reset button
    MediaPlayer click2; //placed ship
    MediaPlayer click3; //unplaced ship
    MediaPlayer rollover1; //all menu
    Random rndSound = new Random();
    //int waterArr[] = {R.raw.voda, R.raw.voda1, R.raw.voda2, R.raw.voda3};
    //int explosionArr[] = {R.raw.explosion, R.raw.explosion1, R.raw.explosion2, R.raw.explosion3};
    MediaPlayer water;
    MediaPlayer explosion;

    //arr lists
    ArrayList<ImageButton> arrLiImage = new ArrayList<>(); //vybrane pozice lodi
    ArrayList<ImageButton> arrLiButtons = new ArrayList<>(); //naplneni buttonu do listu a pak pridarit listener

    ArrayList<ImageButton> arrLiEnemyImage = new ArrayList<>();
    ArrayList<ImageButton> arrLiEnemyButtons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        //db
        mydb = new DBHelper(this);

        //stats
        mySharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);

        //sound
        click1 = MediaPlayer.create(this,R.raw.click1);
        click2 = MediaPlayer.create(this, R.raw.click2);
        click3 = MediaPlayer.create(this, R.raw.click3);
        rollover1 = MediaPlayer.create(this, R.raw.rollover3);
        water = MediaPlayer.create(this, R.raw.voda);
        explosion = MediaPlayer.create(this, R.raw.explosion);

        //background
        vCred = findViewById(R.id.playAct);
        setBackground();

        //text
        textSelectShip = findViewById(R.id.textSelectShip);
        textShipCount = findViewById(R.id.textShipCount);
        textBigShip = findViewById(R.id.textBigShip);
        textMiddleShip = findViewById(R.id.textMiddleShip);
        textSmallShip = findViewById(R.id.textSmallShip);
        textEnemyPlayboard = findViewById(R.id.textEnemyPlayboard);
        textAlive = findViewById(R.id.textAlive);
        textAliveEnemy = findViewById(R.id.textAliveEnemy);
        textActualShots = findViewById(R.id.textActualShots);
        textActualEShots = findViewById(R.id.textActualEShots);

        //imageView
        imageOKBigS = findViewById(R.id.imageOKBigS);
        imageOKMiddleS = findViewById(R.id.imageOKMiddleS);
        imageOKSmallS = findViewById(R.id.imageOKSmallS);
        imageBack = findViewById(R.id.imageBack);
        imageSipka = findViewById(R.id.imageSipka);
        imageEndTitle = findViewById(R.id.imageEndTitle);
        imageSave = findViewById(R.id.imageSave);

        //button
        playBut = findViewById(R.id.butPlay);
        playBut.setEnabled(false); //TODO - odkomentovat!!!!!!!!!!! - just testing
        resetBut = findViewById(R.id.buttonReset);

        //ostatni - srartGame
        linearLayout = findViewById(R.id.linearLayout);
        tableLayout = findViewById(R.id.tableLayout);
        EnemyTableLayout = findViewById(R.id.EnemyTableLayout);
        EnemyTableLayout.setClickable(false);

        loadImgButtons();

        textBigShip.setBackgroundResource(R.drawable.obrys);

        //Toast.makeText(getApplicationContext(), "count:" + pocet, Toast.LENGTH_LONG).show();
    }

    //back "button"
    public void closeActivity(final View view) {
        rollover1.start();

        if(endGame){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Quit game without save");
            alert.setIcon(R.drawable.alert);
            alert.setMessage("Do you really want quit game without save score?");
            alert.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    endGame = false;
                    saveScoreWithName(view);
                }
            });
            alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    endGame = false;
                    finish();
                }
            });
            alert.create().show();
        }
        else{
            finish();
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
        click1.start();

        for (View v : arrLiImage){
            v.setBackgroundResource(R.drawable.wall);
        }
        for(int i = 0; i < arrLiImage.size(); i++){
            arrLiImage.remove(i);
        }
        for(int i = 0; i < arrLiImage.size(); i++){
            arrLiImage.remove(i);
        }

        playBut.setBackgroundResource(R.drawable.play);
        playBut.setEnabled(false);

        selectedShip = 0;
        resetBackground();
        textBigShip.setBackgroundResource(R.drawable.obrys);

        imageOKBigS.setVisibility(View.INVISIBLE);
        imageOKMiddleS.setVisibility(View.INVISIBLE);
        imageOKSmallS.setVisibility(View.INVISIBLE);

        count = 0;
        countBigS = 0;
        countMiddleS = 0;
        countSmallS = 0;
        textSelectShip.setText("Selected ship: Big Ship");
        textShipCount.setText("Placed ships: " + count + "/9");
        textBigShip.setText("Name: Big ship\nHP: 5\nPlaced: " + countBigS + "/1");
        textMiddleShip.setText("Name: Middle ship\nHP: 3\nPlaced: " + countMiddleS + "/3");
        textSmallShip.setText("Name: Small ship\nHP: 1\nPlaced: " + countSmallS + "/5");
        Toast.makeText(getApplicationContext(), "Yours field has been resetted.", Toast.LENGTH_LONG).show();
    }

    public void setAllInvisible(){
        textSelectShip.setEnabled(false);
        textSelectShip.setVisibility(View.INVISIBLE);

        textShipCount.setEnabled(false);
        textShipCount.setVisibility(View.INVISIBLE);

        resetBut.setEnabled(false);
        resetBut.setClickable(false);
        resetBut.setVisibility(View.INVISIBLE);

        playBut.setEnabled(false);
        playBut.setClickable(false);
        playBut.setVisibility(View.INVISIBLE);

        imageBack.setEnabled(false);
        imageBack.setClickable(false);
        imageBack.setVisibility(View.INVISIBLE);

        linearLayout.setEnabled(false);
        linearLayout.setClickable(false);
        linearLayout.setVisibility(View.INVISIBLE);

        textEnemyPlayboard.setVisibility(View.VISIBLE);
        EnemyTableLayout.setClickable(true);
        EnemyTableLayout.setVisibility(View.VISIBLE);
        imageSipka.setVisibility(View.VISIBLE);
        imageOKBigS.setVisibility(View.INVISIBLE);
        imageOKMiddleS.setVisibility(View.INVISIBLE);
        imageOKSmallS.setVisibility(View.INVISIBLE);
        textAlive.setVisibility(View.VISIBLE);
        textAliveEnemy.setVisibility(View.VISIBLE);
        textActualShots.setVisibility(View.VISIBLE);
        textActualEShots.setVisibility(View.VISIBLE);
    }

    public void setAllNoEnable(){
        //setNoClickable/enable
        for (ImageButton imgB : arrLiEnemyButtons){
            imgB.setEnabled(false);
        }
    }
    public void setAllEnable(){
        //setClickable/enable
        for (ImageButton imgB : arrLiEnemyButtons){
            imgB.setEnabled(true);
        }
    }

    public void loadImgButtons(){
        //playboard
        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            if (tableLayout.getChildAt(i) instanceof TableRow) {
                TableRow row = (TableRow) tableLayout.getChildAt(i);
                for(int j = 0; j < row.getChildCount(); j++) {
                    if (row.getChildAt(j) instanceof ImageButton) {
                        arrLiButtons.add((ImageButton) row.getChildAt(j));
                    }
                }
            }
        }
        for(ImageButton button : arrLiButtons){
            button.setOnTouchListener(insertShipListener);
        }

        //enemy playboard
        for (int i = 0; i < EnemyTableLayout.getChildCount(); i++) {
            if (EnemyTableLayout.getChildAt(i) instanceof TableRow) {
                TableRow row = (TableRow) EnemyTableLayout.getChildAt(i);
                for(int j = 0; j < row.getChildCount(); j++) {
                    if (row.getChildAt(j) instanceof ImageButton) {
                        arrLiEnemyButtons.add((ImageButton) row.getChildAt(j));
                    }
                }
            }
        }
        for(ImageButton button : arrLiEnemyButtons){
            button.setOnTouchListener(enemyListener);
        }
    }

    //startgame - invisible right side screen, visible enemy field
    public void startGame(View view) {
        rollover1.start();
        startTime = System.currentTimeMillis();

        //in/visible
        setAllInvisible();

        //save stats
        games++;
        mySharedEditor = mySharedPref.edit();
        mySharedEditor.putInt("games", games);
        mySharedEditor.apply();

        //setNoClickable/enable
        for (ImageButton imgB : arrLiButtons){
            imgB.setEnabled(false);
        }

        //set enemy ship position
        setEnemyShips();
    }

    public void AI(){
        randomShot = rnd.nextInt(arrLiButtons.size());

        if(indexShotu >= 100){
            indexShotu = 100;
        }

        if(indexShotu == 0){
            randomShot = 0;
        }
        else {
            for (int i = 0; i < shottedButtons.length; i++) {
                if (shottedButtons[i] == randomShot) {
                    randomShot = rnd.nextInt(arrLiButtons.size());
                    i = -1;
                }
            }
        }

        ImageButton a = arrLiButtons.get(randomShot);

        AIShots++; //stats
        countActualEShots++;
        textActualEShots.setText("E. Shots: " + countActualEShots);

        //int rndS = rndSound.nextInt(4);
        shot2 = true;
        for (View v : arrLiImage) {
            if (v.equals(a)){
                shot2 = false;
                v.setBackgroundResource(R.drawable.box2);
                shottedButtons[indexShotu] = randomShot;
                indexShotu++;
            }
        }
        if(shot2){
            //water = MediaPlayer.create(getApplicationContext(),waterArr[rndS]);
            water.start(); //TODO - odkomentovat!!!!!!!!!!! - just testing

            a.setBackgroundResource(R.drawable.wall2);
            shottedButtons[indexShotu] = randomShot;
            indexShotu++;
        }
        else{
            //explosion = MediaPlayer.create(getApplicationContext(),waterArr[rndS]);
            explosion.start(); //TODO - odkomentovat!!!!!!!!!!! - just testing
            arrLiImage.remove(a);
            textAlive.setText("Alive ships: " + arrLiImage.size() + "/9");
        }

        //setAllEnable(); //TODO - smazat - just testing
        //TODO - odkomentovat!!!!!!!!!!! - just testing
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageSipka.setImageResource(R.drawable.sipka);
                setAllEnable();
            }
        }, 2000);
    }

    public void setEnemyShips(){
        int[] placedShips = new int[9];
        int index = 0;

        Random randPosition = new Random();
        int rndEnemyShipPosition = randPosition.nextInt(arrLiEnemyButtons.size() - 1);
        placedShips[index] = rndEnemyShipPosition;
        index++;

        ImageButton a = arrLiEnemyButtons.get(rndEnemyShipPosition);
        a.setBackgroundResource(R.drawable.wall); //big ship
        arrLiEnemyImage.add(a);

        for(int i = 0; i < 3; i++){
            rndEnemyShipPosition = randPosition.nextInt(arrLiEnemyButtons.size() - 1);
            for(int j = 0; j < placedShips.length; j++){
                if(placedShips[j] == rndEnemyShipPosition){
                    rndEnemyShipPosition = randPosition.nextInt(arrLiEnemyButtons.size() - 1);
                    j = -1;
                }
            }
            placedShips[index] = rndEnemyShipPosition;
            index++;
            ImageButton b = arrLiEnemyButtons.get(rndEnemyShipPosition);
            b.setBackgroundResource(R.drawable.wall); //middle ship
            arrLiEnemyImage.add(b);
        }

        for(int i = 0; i < 5; i++){
            rndEnemyShipPosition = randPosition.nextInt(arrLiEnemyButtons.size() - 1);
            for(int j = 0; j < placedShips.length; j++){
                if(placedShips[j] == rndEnemyShipPosition){
                    rndEnemyShipPosition = randPosition.nextInt(arrLiEnemyButtons.size() - 1);
                    j = -1;
                }
            }
            placedShips[index] = rndEnemyShipPosition;
            index++;
            ImageButton c = arrLiEnemyButtons.get(rndEnemyShipPosition);
            c.setBackgroundResource(R.drawable.wall); //small ship
            arrLiEnemyImage.add(c);
        }
    }

    //make imageBack visible and clickable + invisible sipka + save stats
    public void endThings(int winner){
        difference = System.currentTimeMillis() - startTime;
        //Toast.makeText(getApplicationContext(), "time: " + difference / 1000 + "sec", Toast.LENGTH_LONG).show();
        endGame = true;

        imageBack.setEnabled(true);
        imageBack.setClickable(true);
        imageBack.setVisibility(View.VISIBLE);
        imageSipka.setVisibility(View.INVISIBLE);

        //setNoClickable/enable
        for (ImageButton imgB : arrLiEnemyButtons){
            imgB.setEnabled(false);
        }

        //save stats
        mySharedEditor = mySharedPref.edit();
        switch (winner){
            case 0:
                wins++;
                mySharedEditor.putInt("wins", wins);
                break;
            case 1:
                loses++;
                mySharedEditor.putInt("loses", loses);
                break;
        }
        mySharedEditor.putInt("shots", shots);
        mySharedEditor.putInt("AIShots", AIShots);
        mySharedEditor.putInt("placedShips", placedShips);
        mySharedEditor.putInt("placedBigShips", placedBigShips);
        mySharedEditor.putInt("placedMiddleShips", placedMiddleShips);
        mySharedEditor.putInt("placedSmallShips", placedSmallShips);
        mySharedEditor.apply();
    }

    public void saveScoreWithName(final View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Save Score");
        alert.setIcon(R.drawable.alert);
        alert.setMessage("Enter your name:");
        inputName = new EditText(this);
        alert.setView(inputName);
        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String nameToSave = inputName.getText().toString();
                Bundle extras = getIntent().getExtras();
                if(extras != null)
                {
                    if(mydb.insertContact(nameToSave, difference / 1000, countActualShots, arrLiImage.size())){
                        Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "You must enter name", Toast.LENGTH_SHORT).show();
                        saveScoreWithName(view);
                    }
                }
                //Toast.makeText(getApplicationContext(), "jmeno k ulozeni: " + nameToSave, Toast.LENGTH_LONG).show();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                endGame = true;
                dialogInterface.dismiss();
            }
        });
        alert.create().show();
    }

    ImageButton.OnTouchListener enemyListener = new View.OnTouchListener(){
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    //int rndS = rndSound.nextInt(4);
                    countActualShots++;
                    textActualShots.setText("Shots: " + countActualShots);
                    shots++;
                    shot = true;
                    for (View v : arrLiEnemyImage){
                        if (v.equals(((ImageButton) view))) {
                            shot = false;
                            //Toast.makeText(getApplicationContext(), "Zásah", Toast.LENGTH_LONG).show();
                            ((ImageButton) view).setBackgroundResource(R.drawable.box2);

                            if(arrLiEnemyImage.size() <= 0){ //TODO - <= 0 - predelat
                                imageEndTitle.setVisibility(View.VISIBLE);
                                imageSave.setVisibility(View.VISIBLE);
                                endThings(0);
                                //Toast.makeText(getApplicationContext(), "You Won!", Toast.LENGTH_LONG).show();
                            }
                            else if(arrLiImage.size() <= 0){
                                imageEndTitle.setImageResource(R.drawable.ai_won);
                                imageEndTitle.setVisibility(View.VISIBLE);
                                imageSave.setVisibility(View.VISIBLE);
                                endThings(1);
                                //Toast.makeText(getApplicationContext(), "AI - Won!", Toast.LENGTH_LONG).show();
                            }
                            else{
                                //AI();//TODO - smazat
                                //TODO - odkomentovat!!!!!!!!!!! - just testing
                                setAllNoEnable();
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        imageSipka.setImageResource(R.drawable.sipka2);
                                        AI();
                                    }
                                }, 2500);
                            }
                        }
                    }
                    if(shot){
                        //water = MediaPlayer.create(getApplicationContext(),waterArr[rndS]);
                        water.start(); //TODO - odkomentovat!!!!!!!!!!! - just testing
                        ((ImageButton) view).setBackgroundResource(R.drawable.wall2);

                        if(arrLiEnemyImage.size() <= 0){
                            imageEndTitle.setVisibility(View.VISIBLE);
                            imageSave.setVisibility(View.VISIBLE);
                            endThings(0);
                        }
                        else if(arrLiImage.size() <= 0){
                            imageEndTitle.setImageResource(R.drawable.ai_won);
                            imageEndTitle.setVisibility(View.VISIBLE);
                            imageSave.setVisibility(View.VISIBLE);
                            endThings(1);
                        }
                        else{
                            //AI();//TODO - smazat
                            //TODO - odkomentovat!!!!!!!!!!! - just testing
                            setAllNoEnable();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    imageSipka.setImageResource(R.drawable.sipka2);
                                    AI();
                                }
                            }, 2500);
                        }
                    }
                    else {
                        //explosion = MediaPlayer.create(getApplicationContext(), explosionArr[rndS]);
                        explosion.start(); //TODO - odkomentovat!!!!!!!!!!! - just testing
                        arrLiEnemyImage.remove(view);
                        textAliveEnemy.setText("Alive enemy ships: " + arrLiEnemyImage.size() + "/9");
                    }
                    break;
                }
            }
            return true;
        }
    };

    ImageButton.OnTouchListener insertShipListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction())
            {
                case MotionEvent.ACTION_DOWN: {
                    //Toast.makeText(getApplicationContext(), "switch", Toast.LENGTH_LONG).show();
                    sw = true;
                    for (View v : arrLiImage) {
                        if (v.equals(((ImageButton) view))) {
                            count--;
                            ((ImageButton) view).setBackgroundResource(R.drawable.wall);
                            sw = false;
                            textShipCount.setText("Placed ships: " + count + "/9");
                            switch (selectedShip){
                                case 0:
                                    click3.start();
                                    countBigS--;
                                    textBigShip.setText("Name: Big ship\nHP: 5\nPlaced: " + countBigS + "/1");
                                    break;
                                case 1:
                                    click3.start();
                                    countMiddleS--;
                                    textMiddleShip.setText("Name: Middle ship\nHP: 3\nPlaced: " + countMiddleS + "/3");
                                    break;
                                case 2:
                                    click3.start();
                                    countSmallS--;
                                    textSmallShip.setText("Name: Small ship\nHP: 1\nPlaced: " + countSmallS + "/5");
                                    break;
                            }
                        }
                        if(count < shipTotal){ //TODO - odkomentovat!!!!!!!!!!!! - just testing
                            playBut.setBackgroundResource(R.drawable.play);
                            playBut.setEnabled(false);
                        }
                        if(countBigS < maxBigS){imageOKBigS.setVisibility(View.INVISIBLE);}
                        if(countMiddleS < maxMiddleS){imageOKMiddleS.setVisibility(View.INVISIBLE);}
                        if(countSmallS < maxSmallS){imageOKSmallS.setVisibility(View.INVISIBLE);}
                    }
                    if(count < shipTotal) {
                        if (sw) {
                            placedShips++;
                            switch (selectedShip){
                                case 0:
                                    if(countBigS < maxBigS) {
                                        click2.start();
                                        placedBigShips++;
                                        countBigS++;
                                        count++;
                                        ((ImageButton) view).setBackgroundResource(R.drawable.bigshipv2);
                                        arrLiImage.add((ImageButton) view);
                                        textShipCount.setText("Placed ships: " + count + "/9");
                                        textBigShip.setText("Name: Big ship\nHP: 5\nPlaced: " + countBigS + "/1");
                                    }
                                    if(countBigS == maxBigS){
                                        imageOKBigS.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                case 1:
                                    if(countMiddleS < maxMiddleS) {
                                        click2.start();
                                        placedMiddleShips++;
                                        countMiddleS++;
                                        count++;
                                        ((ImageButton) view).setBackgroundResource(R.drawable.middleshipv2);
                                        arrLiImage.add((ImageButton) view);
                                        textShipCount.setText("Placed ships: " + count + "/9");
                                        textMiddleShip.setText("Name: Middle ship\nHP: 3\nPlaced: " + countMiddleS + "/3");
                                    }
                                    if(countMiddleS == maxMiddleS){
                                        imageOKMiddleS.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                case 2:
                                    if(countSmallS < maxSmallS) {
                                        click2.start();
                                        placedSmallShips++;
                                        countSmallS++;
                                        count++;
                                        ((ImageButton) view).setBackgroundResource(R.drawable.smallshipv2);
                                        arrLiImage.add((ImageButton) view);
                                        textShipCount.setText("Placed ships: " + count + "/9");
                                        textSmallShip.setText("Name: Small ship\nHP: 1\nPlaced: " + countSmallS + "/5");
                                    }
                                    if(countSmallS == maxSmallS){
                                        imageOKSmallS.setVisibility(View.VISIBLE);
                                    }
                                    break;
                            }
                            if(count == shipTotal) {
                                playBut.setBackgroundResource(R.drawable.play_yellow);
                                playBut.setEnabled(true);
                            }
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

    public void resetBackground(){
        textBigShip.setBackgroundColor(Color.TRANSPARENT);
        textMiddleShip.setBackgroundColor(Color.TRANSPARENT);
        textSmallShip.setBackgroundColor(Color.TRANSPARENT);
    }
    public void bigShipSelected(View view) {
        click1.start();
        selectedShip = 0;
        textSelectShip.setText("Selected ship: Big Ship");
        resetBackground();
        textBigShip.setBackgroundResource(R.drawable.obrys);
    }
    public void middleShipSelected(View view) {
        click1.start();
        selectedShip = 1;
        textSelectShip.setText("Selected ship: Middle Ship");
        resetBackground();
        textMiddleShip.setBackgroundResource(R.drawable.obrys);
    }
    public void smallShipSelected(View view) {
        click1.start();
        selectedShip = 2;
        textSelectShip.setText("Selected ship: Small Ship");
        resetBackground();
        textSmallShip.setBackgroundResource(R.drawable.obrys);
    }
}
