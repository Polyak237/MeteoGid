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

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.util.Objects;

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
                            Toast.makeText(MainActivity.this, "Введите название города", Toast.LENGTH_LONG).show();
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




    private class GetURLData extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            waiting.setText("Ожидайте...");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;


            try {
         /*       String cityText = city.getText().toString();
                Document document = Jsoup.connect("https://yandex.ru/pogoda/search?request=" + cityText).get();
                Element hlink= document.selectFirst("li[class=place-list__item]").selectFirst("a");
                String link=hlink.attr("href");
                document = Jsoup.connect("https://yandex.ru"+link).get();

                Element currentTemp= document.selectFirst("div[class=temp fact__temp fact__temp_size_s]").selectFirst("span[class=temp__value temp__value_with-unit]");
                Element situation = document.selectFirst("div[class=link__condition day-anchor i-bem]");
                Element city = document.selectFirst("span[class=breadcrumbs__title]");
                String wind = document.selectFirst("span[class=wind-speed]").text(); */

                String href = "https://api.openweathermap.org/data/2.5/weather?q="+city.getText()+"&appid=a1f886dec466dde0dd09b3f75fa9455d&units=metric&lang=ru";
                System.out.println(href);
                JSONObject Json=JsonReader.readJsonFromUrl(href);

                return Json.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String weather) {
            super.onPostExecute(weather);
            JSONObject jsonObject = null;
            int currentTemp=0;
            int currentWet=0;
            int currentPreasure=0;
            String name="";
            String sit = null;

            try {
                 jsonObject = new JSONObject(weather);
                 currentTemp= (int) jsonObject.getJSONObject("main").getDouble("temp");
                 currentPreasure = (int) jsonObject.getJSONObject("main").getDouble("pressure");
                 currentWet = (int) jsonObject.getJSONObject("main").getDouble("humidity");
                 sit= jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
                 name=jsonObject.getString("name");
                 city.setText(name);
                System.out.println(currentTemp);
            } catch (JSONException e) {
                System.out.println("Ошибка парсинга");
            }


            switch (Objects.requireNonNull(sit)) {
                case "ясно":
                    pogoda.setImageResource(R.drawable.sun);
                    break;
                case "пасмурно":
                    pogoda.setImageResource(R.drawable.cloudy);
                    break;
                case ("малооблачно"):
                case ("облачно с прояснениями"):
                    pogoda.setImageResource(R.drawable.smallcloudy);
                    break;
                case ("небольшой снег"):
                    pogoda.setImageResource(R.drawable.smallsnow);
                    break;
                case ("снег"):
                    pogoda.setImageResource(R.drawable.snow);
                    break;
                case ("небольшой дождь"):
                    pogoda.setImageResource(R.drawable.smallrain);
                    break;
                case ("дождь"):
                    pogoda.setImageResource(R.drawable.rain);
                    break;
                default: {}
                break;
            }

          waiting.setText(String.valueOf(sit)+"\n"+String.valueOf(currentTemp)+"°С");

            Toast.makeText(MainActivity.this, "Найденное место: " + name, Toast.LENGTH_LONG).show();

        }
    }
}