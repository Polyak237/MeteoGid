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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity{

    ImageView pogoda;
    TextView waiting, sit1, sit2, sit3, sit4, sit5, sit6, temp1, temp2, temp3, temp4, temp5, temp6;
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

                //Парсинг координат
                double lat = Json.getJSONObject("coord").getDouble("lat");
                double lon = Json.getJSONObject("coord").getDouble("lon");
                String name = Json.getString("name") + Json.getJSONObject("sys").getString("country");

                // Основной API с ситуацией и прогнозами
                String href2 = "https://api.openweathermap.org/data/2.5/onecall?lat=" + lat + "&lon=" + lon + "&exclude=minutely,alerts&appid=a1f886dec466dde0dd09b3f75fa9455d&lang=ru&units=metric";
                JSONObject Json2 = JsonReader.readJsonFromUrl(href2);

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
            String currentsit = null;




            try {

                currentTemp= (int) jsonObject.getJSONObject("current").getDouble("temp");
                currentPreasure = (int) jsonObject.getJSONObject("current").getDouble("pressure");
                currentWet = (int) jsonObject.getJSONObject("current").getDouble("humidity");
                currentsit= jsonObject.getJSONObject("current").getJSONArray("weather").getJSONObject(0).getString("description");

                Long unix = jsonObject.getJSONObject("current").getLong("dt");
                String zone = jsonObject.getString("timezone");
                int tz_off = jsonObject.getInt("timezone_offset");

                Date date = new Date(unix*1000L);
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm Z");
                sdf.setTimeZone(TimeZone.getTimeZone(zone));

                
                String formattedDate = sdf.format(date);
                System.out.println(formattedDate);



                sit1 = (TextView) findViewById(R.id.sit1);
                temp1 = (TextView) findViewById(R.id.temp1);
                sit1.setText("Завтра: " + jsonObject.getJSONArray("daily").getJSONObject(1).getJSONArray("weather").getJSONObject(0).getString("description"));
                temp1.setText(String.valueOf( (int) jsonObject.getJSONArray("daily").getJSONObject(1).getJSONObject("temp").getDouble("day")) + "°C");

                sit2 = (TextView) findViewById(R.id.sit2);
                temp2 = (TextView) findViewById(R.id.temp2);
                sit2.setText("Завтра: " + jsonObject.getJSONArray("daily").getJSONObject(2).getJSONArray("weather").getJSONObject(0).getString("description"));
                temp2.setText(String.valueOf( (int) jsonObject.getJSONArray("daily").getJSONObject(2).getJSONObject("temp").getDouble("day")) + "°C");

                sit3 = (TextView) findViewById(R.id.sit3);
                temp3 = (TextView) findViewById(R.id.temp3);
                sit3.setText("Завтра: " + jsonObject.getJSONArray("daily").getJSONObject(3).getJSONArray("weather").getJSONObject(0).getString("description"));
                temp3.setText(String.valueOf( (int) jsonObject.getJSONArray("daily").getJSONObject(3).getJSONObject("temp").getDouble("day")) + "°C");

                sit4 = (TextView) findViewById(R.id.sit4);
                temp4 = (TextView) findViewById(R.id.temp4);
                sit4.setText("Завтра: " + jsonObject.getJSONArray("daily").getJSONObject(4).getJSONArray("weather").getJSONObject(0).getString("description"));
                temp4.setText(String.valueOf( (int) jsonObject.getJSONArray("daily").getJSONObject(4).getJSONObject("temp").getDouble("day")) + "°C");

                sit5 = (TextView) findViewById(R.id.sit5);
                temp5 = (TextView) findViewById(R.id.temp5);
                sit5.setText("Завтра: " + jsonObject.getJSONArray("daily").getJSONObject(5).getJSONArray("weather").getJSONObject(0).getString("description"));
                temp5.setText(String.valueOf( (int) jsonObject.getJSONArray("daily").getJSONObject(5).getJSONObject("temp").getDouble("day")) + "°C");

                sit6 = (TextView) findViewById(R.id.sit6);
                temp6 = (TextView) findViewById(R.id.temp6);
                sit6.setText("Завтра: " + jsonObject.getJSONArray("daily").getJSONObject(6).getJSONArray("weather").getJSONObject(0).getString("description"));
                temp6.setText(String.valueOf( (int) jsonObject.getJSONArray("daily").getJSONObject(6).getJSONObject("temp").getDouble("day")) + "°C");




            } catch (JSONException e) {
                System.out.println("Ошибка парсинга");
            }

            switch (Objects.requireNonNull(currentsit)) {
                case "ясно":
                    pogoda.setImageResource(R.drawable.sun);
                    break;
                case "пасмурно":
                    pogoda.setImageResource(R.drawable.cloudy);
                    break;
                case ("малооблачно"):
                case ("облачно с прояснениями"):
                case ("переменная облачность"):
                    pogoda.setImageResource(R.drawable.smallcloudy);
                    break;
                case ("небольшой снег"):
                    pogoda.setImageResource(R.drawable.smallsnow);
                    break;
                case ("снег"):
                    pogoda.setImageResource(R.drawable.snow);
                    break;
                case ("небольшой дождь"):
                case ("снег с дождём"):
                    pogoda.setImageResource(R.drawable.smallrain);
                    break;
                case ("дождь"):
                    pogoda.setImageResource(R.drawable.rain);
                    break;
                default: {}
                break;
            }

          waiting.setText("Сегодня: \n" + String.valueOf(currentsit)+",\n"+String.valueOf(currentTemp)+"°С");

            //Toast.makeText(MainActivity.this, "Найденное место: " + name, Toast.LENGTH_LONG).show();


        }
    }
}