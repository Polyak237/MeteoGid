package com.example.meteogid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class History extends AppCompatActivity {

    String city, hour, cit0, h0, cit1, h1, cit2, h2, cit3, h3, cit4, h4, cit5, h5, cit6, h6;
    TextView H0;    TextView H1;
    TextView H2;    TextView H3;
    TextView H4;    TextView H5;
    TextView H6;
    TextView city0;    TextView city1;
    TextView city2;    TextView city3;
    TextView city4;    TextView city5;
    TextView city6;
    boolean f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        H0 = findViewById(R.id.H0); H1 = findViewById(R.id.H1);
        H2 = findViewById(R.id.H2); H3 = findViewById(R.id.H3);
        H4 = findViewById(R.id.H4); H5 = findViewById(R.id.H5);
        H6 = findViewById(R.id.H6);

        city0 = findViewById(R.id.hist0); city1 = findViewById(R.id.hist1);
        city2 = findViewById(R.id.hist2); city3 = findViewById(R.id.hist3);
        city4 = findViewById(R.id.hist4); city5 = findViewById(R.id.hist5);
        city6 = findViewById(R.id.hist6);


        SharedPreferences preferences = getSharedPreferences("PREFS",0);

        city=(preferences.getString("city", ""));
        hour=(preferences.getString("hour", ""));
        cit0=(preferences.getString("city0", ""));        h0=(preferences.getString("H0", ""));
        cit1=(preferences.getString("city1", ""));        h1=(preferences.getString("H1", ""));
        cit2=(preferences.getString("city2", ""));        h2=(preferences.getString("H2", ""));
        cit3=(preferences.getString("city3", ""));        h3=(preferences.getString("H3", ""));
        cit4=(preferences.getString("city4", ""));        h4=(preferences.getString("H4", ""));
        cit5=(preferences.getString("city5", ""));        h5=(preferences.getString("H5", ""));

        H6.setText(h5); city6.setText(cit5);
        H5.setText(h4); city5.setText(cit4);
        H4.setText(h3); city4.setText(cit3);
        H3.setText(h2); city3.setText(cit2);
        H2.setText(h1); city2.setText(cit1);
        H1.setText(h0); city1.setText(cit0);
        H0.setText(hour); city0.setText(city);

        f=preferences.getBoolean("key",false);

        //if (f==true){
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("city0", city);        editor.putString("H0", hour);
            editor.putString("city1", cit0);        editor.putString("H1", h0);
            editor.putString("city2", cit1);        editor.putString("H2", h1);
            editor.putString("city3", cit2);        editor.putString("H3", h2);
            editor.putString("city4", cit3);        editor.putString("H4", h3);
            editor.putString("city5", cit4);        editor.putString("H5", h4);
            editor.putString("city6", cit5);        editor.putString("H6", h5);

            f=false;
            editor.putBoolean("key", false);

            editor.apply();

        //}
    }

}
