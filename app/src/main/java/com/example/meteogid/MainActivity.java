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

import java.util.Objects;

public class MainActivity extends AppCompatActivity{

    ImageView pogoda;
    TextView waiting;
    EditText city;
    Button btnSaveCity, more;
    SharedPreferences sPref;

    final String SavedCity = "Город был изменён";

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Открытие приложения
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



    public void addListenerOnSave () { // Действия при нажатии на кнопку смены города
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


    public void addListenerOnButtonMore () {  // Действия при нажатии на кнопку с подробностями
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



    private void savetext() { // Функция сохранения введённого города
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SavedCity, city.getText().toString());
        ed.apply();
        Toast.makeText(MainActivity.this, "Город сохранён", Toast.LENGTH_SHORT).show();
    }

    public void loadtext() { // Функция загрузки введённого города
        sPref = getPreferences(MODE_PRIVATE);
        String SavedText = sPref.getString(SavedCity, "");
        city.setText(SavedText);

    }

    public void showdata(){
        String City = city.getText().toString();
        new GetURLData().execute();
    }




    private class GetURLData extends AsyncTask<String, String, JSONObject> {

        protected void onPreExecute() {
            waiting.setText("Ожидайте...");
        }

        @Override
        protected JSONObject doInBackground(String... strings) {

            try {

                // API погоды только на сегодня
                // Отсюда нужно брать координаты введённого города и его название
                String href1 = "https://api.openweathermap.org/data/2.5/weather?q="+city.getText()+"&appid=a1f886dec466dde0dd09b3f75fa9455d&units=metric&lang=ru";
                JSONObject Json=JsonReader.readJsonFromUrl(href1);
                System.out.println(Json);

                //Парсинг координат
                double lat = Json.getJSONObject("coord").getDouble("lat");
                double lon = Json.getJSONObject("coord").getDouble("lon");
                String name = Json.getString("name") + Json.getJSONObject("sys").getString("country");

                // Основной API с ситуацией и прогнозами
                String href2 = "https://api.openweathermap.org/data/2.5/onecall?lat=" + lat + "&lon=" + lon + "&exclude=minutely,alerts&appid=a1f886dec466dde0dd09b3f75fa9455d&lang=ru&units=metric";
                JSONObject Json2 = JsonReader.readJsonFromUrl(href2);
                System.out.println(Json2);

                return Json2;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(JSONObject weather) {
            super.onPostExecute(weather);

            JSONObject jsonObject = weather;
            int currentTemp=0;
            int currentWet=0;
            int currentPreasure=0;
            String sit = null;

            try {
                currentTemp= (int) jsonObject.getJSONObject("current").getDouble("temp");
                currentPreasure = (int) jsonObject.getJSONObject("current").getDouble("pressure");
                currentWet = (int) jsonObject.getJSONObject("current").getDouble("humidity");
                sit= jsonObject.getJSONObject("current").getJSONArray("weather").getJSONObject(0).getString("description");
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

            //Toast.makeText(MainActivity.this, "Найденное место: " + name, Toast.LENGTH_LONG).show();


        }
    }
}