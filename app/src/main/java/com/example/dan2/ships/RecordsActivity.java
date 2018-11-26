package com.example.dan2.ships;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RecordsActivity extends Activity {

    View vCred;
    int selBack;

    //db
    private DBHelper mydb;
    TextView textName;
    TextView textTime;
    TextView textShots;
    TextView textAliveShips;

    ArrayAdapter<CharSequence> adapter;

    //int id_To_Update = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);



        //text
        textName = findViewById(R.id.textName);
        textTime = findViewById(R.id.textTime);
        textShots = findViewById(R.id.textShots);
        textAliveShips = findViewById(R.id.textAliveShips);

        //db
        mydb = new DBHelper(this);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            int value = extras.getInt("id");
            selBack = extras.getInt("safebackk");
            if (value > 0)
            {
                Cursor rs = mydb.getData(value);
                //id_To_Update = value; //TODO - update zatim nepotrebuju
                rs.moveToFirst();

                String nam = rs.getString(rs.getColumnIndex(DBHelper.RECORDS_COLUMN_NAME));
                String tim = rs.getString(rs.getColumnIndex(DBHelper.RECORDS_COLUMN_TIME));
                String sho = rs.getString(rs.getColumnIndex(DBHelper.RECORDS_COLUMN_SHOTS));
                String ali = rs.getString(rs.getColumnIndex(DBHelper.RECORDS_COLUMN_ALIVED));

                if (!rs.isClosed())
                {
                    rs.close();
                }
                textName.setText((CharSequence)nam);
                textTime.setText((CharSequence)tim);
                textShots.setText((CharSequence)sho);
                textAliveShips.setText((CharSequence)ali);
            }
        }
        vCred = findViewById(R.id.recordsAct);
        setBackground(selBack);
        Toast.makeText(getApplicationContext(), "back:" + selBack, Toast.LENGTH_LONG).show();
    }

    public void setBackground(int background){
        switch (background){
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

    public void closeActivity(View view) {
        finish();
    }
}
