package com.example.meteogid;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.net.HttpURLConnection;

public class MainActivity extends AppCompatActivity{

    ImageView pogoda;
    TextView waiting;
    EditText city;
    Button btnSaveCity, more;
    SharedPreferences sPref;

    final String SavedCity = "Город был изменён";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        waiting = (TextView)findViewById(R.id.Waiting);
        city = (EditText)findViewById(R.id.city);
        pogoda = (ImageView)findViewById(R.id.Pogoda);

        addListenerOnButtonMore ();
        addListenerOnSave ();
        loadtext();

        switch (city.getText().toString()) {
            case "":
                break;
            default:
                showdata();
                break;
        }
    }



    public void addListenerOnSave () {
        btnSaveCity = (Button)findViewById(R.id.btnSaveCity);
        btnSaveCity.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (city.getText().toString().equals("")) {
                            Toast.makeText(MainActivity.this, "Введите название города", Toast.LENGTH_SHORT).show();
                        } else {
                            savetext();
                            showdata();
                        }
                    }
                }
        );
    }


    public void addListenerOnButtonMore () {
        more = (Button)findViewById(R.id.More);
        more.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            Intent intent = new Intent(".Podrobno");
                            startActivity(intent);
                    }
                }
        );
    }



    private void savetext() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SavedCity, city.getText().toString());
        ed.apply();
        Toast.makeText(MainActivity.this, "Город сохранён", Toast.LENGTH_SHORT).show();
    }

    public void loadtext() {
        sPref = getPreferences(MODE_PRIVATE);
        String SavedText = sPref.getString(SavedCity, "");
        city.setText(SavedText);

    }

    public void showdata(){
        String City = city.getText().toString();
        new GetURLData().execute();
    }




    private class GetURLData extends AsyncTask<String, String, String[]> {

        protected void onPreExecute() {
            waiting.setText("Ожидайте...");
        }

        @Override
        protected String[] doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;


            try {
                String cityText = city.getText().toString();
                Document document = Jsoup.connect("https://yandex.ru/pogoda/search?request=" + cityText).get();
                Element hlink= document.selectFirst("li[class=place-list__item]").selectFirst("a");
                String link=hlink.attr("href");
                document = Jsoup.connect("https://yandex.ru"+link).get();

                Element currentTemp= document.selectFirst("div[class=temp fact__temp fact__temp_size_s]").selectFirst("span[class=temp__value temp__value_with-unit]");
                Element situation = document.selectFirst("div[class=link__condition day-anchor i-bem]");
                String wind = document.selectFirst("span[class=wind-speed]").text();

                System.out.println(document.selectFirst("span[class=term term_orient_v fact__pressure]"));
                System.out.println(document.selectFirst("span[class=term term_orient_v fact__humidity]"));


                String[] arr = new String[2];
                arr[0] = situation.text();
                arr[1] = situation.text() + "\n" + currentTemp.text()+"°С ";
                return arr;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] arr ) {
            super.onPostExecute(arr);

            switch (arr[0]) {
                case "Ясно":
                    pogoda.setImageResource(R.drawable.sun);
                    break;
                case "Пасмурно":
                    pogoda.setImageResource(R.drawable.cloudy);
                    break;
                case ("Малооблачно"):
                    pogoda.setImageResource(R.drawable.smallcloudy);
                    break;
                case ("Облачно с прояснениями"):
                    pogoda.setImageResource(R.drawable.smallcloudy);
                    break;
                case ("Небольшой снег"):
                    pogoda.setImageResource(R.drawable.smallsnow);
                    break;
                case ("Снег"):
                    pogoda.setImageResource(R.drawable.snow);
                    break;
                case ("Небольшой дождь"):
                    pogoda.setImageResource(R.drawable.smallrain);
                    break;
                case ("Дождь"):
                    pogoda.setImageResource(R.drawable.rain);
                    break;
                default: {}
                break;
            }

                waiting.setText(arr[1]);

        }
    }
}