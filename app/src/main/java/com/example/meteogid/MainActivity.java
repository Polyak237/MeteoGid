package com.example.meteogid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText etText;
    Button btnSaveCity;
    SharedPreferences sPref;

    final String SavedCity = "Город был изменён";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etText = (EditText)findViewById(R.id.etText);

        btnSaveCity = (Button)findViewById(R.id.btnSaveCity);
        btnSaveCity.setOnClickListener(this);

        loadtext();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSaveCity){
            savetext();
        }
    }

    private void savetext() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SavedCity, etText.getText().toString());
        ed.commit();
        Toast.makeText(MainActivity.this, "Город сохранён", Toast.LENGTH_SHORT).show();
    }

    public void loadtext() {
        sPref = getPreferences(MODE_PRIVATE);
        String SavedText = sPref.getString(SavedCity, "");
        etText.setText(SavedText);

    }
}