package com.example.dan2.ships;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class StatisticsActivity extends Activity {

    int selectedBack;

    //stats
    int games = 0;
    int wins = 0;
    int loses = 0;
    int shots = 0;
    int AIShots = 0;
    int placedShips = 0;
    int placedBigShips = 0;
    int placedMiddleShips = 0;
    int placedSmallShips = 0;
    SharedPreferences mySharedPref;
    SharedPreferences.Editor mySharedEditor;

    ImageView imageResetStats;

    TextView textPlays;
    TextView textWins;
    TextView textLoses;
    TextView textShots;
    TextView textAIShots;
    TextView textPlacedShips;
    TextView textPlacedBigShip;
    TextView textPlacedMiddleShip;
    TextView textPlacedSmallShip;

    View vCred;

    //sound
    MediaPlayer rollover1;

    //db
    DBHelper mydb;
    private ListView obj;
    public static ArrayList<Long> arrayListId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        //stats
        loadStats();

        //sound
        rollover1 = MediaPlayer.create(this, R.raw.rollover3);

        imageResetStats = findViewById(R.id.imageResetStats);

        //text
        textPlays = findViewById(R.id.textPlays);
        textWins = findViewById(R.id.textWins);
        textLoses = findViewById(R.id.textLoses);
        textShots = findViewById(R.id.textShots);
        textAIShots = findViewById(R.id.textAIShots);
        textPlacedShips = findViewById(R.id.textPlacedShips);
        textPlacedBigShip = findViewById(R.id.textPlacedBigShips);
        textPlacedMiddleShip = findViewById(R.id.textPlacedMiddleShips);
        textPlacedSmallShip = findViewById(R.id.textPlacedSmallShips);

        textPlays.setText("Games: " + games);
        textWins.setText("Wins: " + wins);
        textLoses.setText("Loses: " + loses);
        textShots.setText("Shots: " + shots);
        textAIShots.setText("AI shots: " + AIShots);
        textPlacedShips.setText("Placed ships: " + placedShips);
        textPlacedBigShip.setText("Placed big ship: " + placedBigShips);
        textPlacedMiddleShip.setText("Placed middle ships: " + placedMiddleShips);
        textPlacedSmallShip.setText("Placed small ships: " + placedSmallShips);

        vCred = findViewById(R.id.seStatsAct);
        setBackground();

        //db
        mydb = new DBHelper(this);
        mydb.setAllContacs();
        ArrayList arrayList = mydb.getAllContacsName();

        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, arrayList);
        obj = (ListView)findViewById(R.id.listView1);
        obj.setAdapter(arrayAdapter);
        obj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                int id_To_Search = arg2 + 1;
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", id_To_Search);
                dataBundle.putInt("safebackk", selectedBack);

                Intent intent = new Intent(getApplicationContext(), RecordsActivity.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
        Toast.makeText(getApplicationContext(), "back:" + selectedBack, Toast.LENGTH_LONG).show();
    }

    public void loadStats(){
        mySharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        games = mySharedPref.getInt("games", 0);
        wins = mySharedPref.getInt("wins", 0);
        loses = mySharedPref.getInt("loses", 0);
        shots = mySharedPref.getInt("shots", 0);
        AIShots = mySharedPref.getInt("AIShots", 0);
        placedShips = mySharedPref.getInt("placedShips", 0);
        placedBigShips = mySharedPref.getInt("placedBigShips", 0);
        placedMiddleShips = mySharedPref.getInt("placedMiddleShips", 0);
        placedSmallShips = mySharedPref.getInt("placedSmallShips", 0);
    }

    public void setBackground(){
        Intent intent = getIntent();
        switch (intent.getIntExtra("safeBack", 0)){
            case 1:
                selectedBack = 1;
                vCred.setBackgroundResource(R.drawable.background);
                break;
            case 2:
                selectedBack = 2;
                vCred.setBackgroundResource(R.drawable.background2);
                break;
            case 3:
                selectedBack = 3;
                vCred.setBackgroundResource(R.drawable.background3);
                break;
            case 4:
                selectedBack = 4;
                vCred.setBackgroundResource(R.drawable.background4);
                break;
        }
    }
    public void resetStats(View view) {
        rollover1.start();

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Reset statistics");
        alert.setIcon(R.drawable.alert);
        alert.setMessage("Do you really want reset statistics?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mydb.removeAll();
                games = 0;
                wins = 0;
                loses = 0;
                shots = 0;
                AIShots = 0;
                placedShips = 0;
                placedBigShips = 0;
                placedMiddleShips = 0;
                placedSmallShips = 0;
                mySharedEditor = mySharedPref.edit();
                mySharedEditor.putInt("games", games);
                mySharedEditor.putInt("wins", wins);
                mySharedEditor.putInt("loses", loses);
                mySharedEditor.putInt("shots", shots);
                mySharedEditor.putInt("AIShots", AIShots);
                mySharedEditor.putInt("placedShips", placedShips);
                mySharedEditor.putInt("placedBigShips", placedBigShips);
                mySharedEditor.putInt("placedMiddleShips", placedMiddleShips);
                mySharedEditor.putInt("placedSmallShips", placedSmallShips);
                mySharedEditor.apply();
                Toast.makeText(getApplicationContext(), "Statistics has been resetted.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.create().show();
    }
    //back "button"
    public void closeActivity(View view) {
        rollover1.start();
        finish();
    }
}
