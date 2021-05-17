package com.example.meteogid;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

public class Podrobno extends AppCompatActivity {

    TextView waiting, name,
            H0, H1, H2, H3, H4, H5, H6, H7, H8, H9, H10, H11,
            sit0, sit1, sit2, sit3, sit4, sit5, sit6, sit7, sit8, sit9, sit10, sit11;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podrobno);

        waiting = (TextView)findViewById(R.id.Waiting1);
        name = (TextView)findViewById(R.id.CityName);

        H0 = (TextView)findViewById(R.id.H0); H1 = (TextView)findViewById(R.id.H1);
        H2 = (TextView)findViewById(R.id.H2); H3 = (TextView)findViewById(R.id.H3);
        H4 = (TextView)findViewById(R.id.H4); H5 = (TextView)findViewById(R.id.H5);
        H6 = (TextView)findViewById(R.id.H6); H7 = (TextView)findViewById(R.id.H7);
        H8 = (TextView)findViewById(R.id.H8); H9 = (TextView)findViewById(R.id.H9);
        H10 = (TextView)findViewById(R.id.H10); H11 = (TextView)findViewById(R.id.H11);

        sit0 = (TextView)findViewById(R.id.temp0); sit1 = (TextView)findViewById(R.id.temp1);
        sit2 = (TextView)findViewById(R.id.temp2); sit3 = (TextView)findViewById(R.id.temp3);
        sit4 = (TextView)findViewById(R.id.temp4); sit5 = (TextView)findViewById(R.id.temp5);
        sit6 = (TextView)findViewById(R.id.temp6); sit7 = (TextView)findViewById(R.id.temp7);
        sit8 = (TextView)findViewById(R.id.temp8); sit9 = (TextView)findViewById(R.id.temp9);
        sit10 = (TextView)findViewById(R.id.temp10); sit11 = (TextView)findViewById(R.id.temp11);


        Bundle arguments = getIntent().getExtras();

        String pressure = "Давление: " + String.valueOf(Math.round(arguments.getInt("currentPressure")*0.7500637554)) + " мм рт.ст.";
        String wet = "Влажность: " + String.valueOf(arguments.getInt("currentWet")) + "%";
        String windSpeed = "Скорость ветра: " + String.valueOf(arguments.getInt("windSpeed")) + " м/c";
        String UFind = "УФ-индекс: " + String.valueOf(arguments.getInt("UFind"));


        name.setText(arguments.getString("name") + "\n" + arguments.getString("H0"));
        waiting.setText(pressure + "\n" + wet + "\n" + windSpeed + "\n" + UFind);

        H0.setText(arguments.getString("H0")); sit0.setText("\n" + arguments.getString("sitH0"));
        H1.setText(arguments.getString("H1")); sit1.setText("\n" + arguments.getString("sitH1"));
        H2.setText(arguments.getString("H2")); sit2.setText("\n" + arguments.getString("sitH2"));
        H3.setText(arguments.getString("H3")); sit3.setText("\n" + arguments.getString("sitH3"));
        H4.setText(arguments.getString("H4")); sit4.setText("\n" + arguments.getString("sitH4"));
        H5.setText(arguments.getString("H5")); sit5.setText("\n" + arguments.getString("sitH5"));
        H6.setText(arguments.getString("H6")); sit6.setText("\n" + arguments.getString("sitH6"));
        H7.setText(arguments.getString("H7")); sit7.setText("\n" + arguments.getString("sitH7"));
        H8.setText(arguments.getString("H8")); sit8.setText("\n" + arguments.getString("sitH8"));
        H9.setText(arguments.getString("H9")); sit9.setText("\n" + arguments.getString("sitH9"));
        H10.setText(arguments.getString("H10")); sit10.setText("\n" + arguments.getString("sitH10"));
        H11.setText(arguments.getString("H11")); sit11.setText("\n" + arguments.getString("sitH11"));


    }





}