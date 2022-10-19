package com.example.meteogid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity{

    ImageView pogoda;
    TextView waiting,
            sit1, sit2, sit3, sit4, sit5, sit6, temp1, temp2, temp3, temp4, temp5, temp6;
    EditText city;
    Button btnSaveCity, more;
    SharedPreferences sPref;
    int currentTemp = 0,
            currentWet = 0,
            currentPressure = 0,
            windSpeed = 0;
    double UFind = 0;

    String currentsit = null;
    String name = "";
    String H0x, H1x, H2x, H3x, H4x, H5x, H6x, H7x, H8x, H9x, H10x, H11x,
            sitH0, sitH1, sitH2, sitH3, sitH4, sitH5, sitH6, sitH7, sitH8, sitH9, sitH10, sitH11;

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
        EditText editText = (EditText)findViewById(R.id.city);
//        btnSaveCity.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (city.getText().toString().equals("")) {
//                            Toast.makeText(MainActivity.this, "Введите название города", Toast.LENGTH_LONG).show();
//                        } else {
//                            savetext();
//                            showdata();
//                        }
//                    }
//                }
//        );
        TextView.OnEditorActionListener listener = new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event != null &&
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        if (city.getText().toString().equals("")) {
                            Toast.makeText(MainActivity.this, "Введите название города", Toast.LENGTH_LONG).show();
                        } else {
                            savetext();
                            showdata();
                            return false;
                        }
                    }
                }
                return false; // pass on to other listeners.
            }
        };
        editText.setOnEditorActionListener(listener);
    }


    public void addListenerOnButtonMore () {  // Действия при нажатии на кнопку с подробностями
        more = (Button)findViewById(R.id.More);
        more.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(".Podrobno");
                        //~~~~~~~~~~~~~~~~~~~~~~  Передача данных на другую страницу  ~~~~~~~~~~~~~~~~~~~~~~
                        intent.putExtra("name", name);
                        intent.putExtra("currentWet", currentWet);
                        intent.putExtra("currentPressure", currentPressure);
                        intent.putExtra("UFind", UFind);
                        intent.putExtra("windSpeed", windSpeed);


                        intent.putExtra("H0", H0x); intent.putExtra("sitH0", sitH0);
                        intent.putExtra("H1", H1x); intent.putExtra("sitH1", sitH1);
                        intent.putExtra("H2", H2x); intent.putExtra("sitH2", sitH2);
                        intent.putExtra("H3", H3x); intent.putExtra("sitH3", sitH3);
                        intent.putExtra("H4", H4x); intent.putExtra("sitH4", sitH4);
                        intent.putExtra("H5", H5x); intent.putExtra("sitH5", sitH5);
                        intent.putExtra("H6", H6x); intent.putExtra("sitH6", sitH6);
                        intent.putExtra("H7", H7x); intent.putExtra("sitH7", sitH7);
                        intent.putExtra("H8", H8x); intent.putExtra("sitH8", sitH8);
                        intent.putExtra("H9", H9x); intent.putExtra("sitH9", sitH9);
                        intent.putExtra("H10", H10x); intent.putExtra("sitH10", sitH10);
                        intent.putExtra("H11", H11x); intent.putExtra("sitH11", sitH11);

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
                name = Json.getString("name") + ", " + Json.getJSONObject("sys").getString("country");

                // Основной API с ситуацией и прогнозами
                String href2 = "https://api.openweathermap.org/data/2.5/onecall?lat=" + lat + "&lon=" + lon + "&exclude=minutely,alerts&appid=a1f886dec466dde0dd09b3f75fa9455d&lang=ru&units=metric";
                JSONObject Json2 = JsonReader.readJsonFromUrl(href2);


                return Json2;

            }
            catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            return null;//В случае исключения
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(JSONObject weather) {
            super.onPostExecute(weather);
            if (weather == null) {
                Toast.makeText(MainActivity.this, "Нет такого города", Toast.LENGTH_LONG).show();
                return;
            }
            JSONObject jsonObject = weather;


            try {

                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~           Получение current-данных            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                currentTemp = (int) jsonObject.getJSONObject("current").getDouble("temp");
                currentsit= jsonObject.getJSONObject("current").getJSONArray("weather").getJSONObject(0).getString("description");
                currentPressure = jsonObject.getJSONObject("current").getInt("pressure");
                currentWet = jsonObject.getJSONObject("current").getInt("humidity");
                UFind = jsonObject.getJSONObject("current").getInt("uvi");
                windSpeed = jsonObject.getJSONObject("current").getInt("wind_speed");

                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~      Получение Unix-даты и временной зоны     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                Long unix = jsonObject.getJSONObject("current").getLong("dt");
                String zone = jsonObject.getString("timezone");

                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~              Корректировка даты               ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                Date date = new Date(unix*1000L);
                SimpleDateFormat sdfcur = new SimpleDateFormat("dd MMM, HH:mm");
                SimpleDateFormat sdfcal = new SimpleDateFormat("dd MMM, E");
                sdfcur.setTimeZone(TimeZone.getTimeZone(zone));
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                   Завтра                      ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                Long unix1 = jsonObject.getJSONArray("daily").getJSONObject(1).getLong("dt");
                Date date1 = new Date(unix1*1000L);
                String date1X = sdfcal.format(date1);
                sit1 = (TextView) findViewById(R.id.sit1);
                temp1 = (TextView) findViewById(R.id.temp1);
                sit1.setText(date1X + " \n" + jsonObject.getJSONArray("daily").getJSONObject(1).getJSONArray("weather").getJSONObject(0).getString("description"));
                temp1.setText(String.valueOf( (int) jsonObject.getJSONArray("daily").getJSONObject(1).getJSONObject("temp").getDouble("day")) + "°C");

                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                 Послезавтра                   ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                Long unix2 = jsonObject.getJSONArray("daily").getJSONObject(2).getLong("dt");
                Date date2 = new Date(unix2*1000L);
                sdfcal.setTimeZone(TimeZone.getTimeZone(zone));
                String date2X = sdfcal.format(date2);
                sit2 = (TextView) findViewById(R.id.sit2);
                temp2 = (TextView) findViewById(R.id.temp2);
                sit2.setText(date2X + " \n"+ jsonObject.getJSONArray("daily").getJSONObject(2).getJSONArray("weather").getJSONObject(0).getString("description"));
                temp2.setText(String.valueOf( (int) jsonObject.getJSONArray("daily").getJSONObject(2).getJSONObject("temp").getDouble("day")) + "°C");

                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          Через 2 дня            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                Long unix3 = jsonObject.getJSONArray("daily").getJSONObject(3).getLong("dt");
                Date date3 = new Date(unix3*1000L);
                String date3X = sdfcal.format(date3);
                sit3 = (TextView) findViewById(R.id.sit3);
                temp3 = (TextView) findViewById(R.id.temp3);
                sit3.setText(date3X + " \n"+ jsonObject.getJSONArray("daily").getJSONObject(3).getJSONArray("weather").getJSONObject(0).getString("description"));
                temp3.setText(String.valueOf( (int) jsonObject.getJSONArray("daily").getJSONObject(3).getJSONObject("temp").getDouble("day")) + "°C");

                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          Через 3 дня            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                Long unix4 = jsonObject.getJSONArray("daily").getJSONObject(4).getLong("dt");
                Date date4 = new Date(unix4*1000L);
                String date4X = sdfcal.format(date4);
                sit4 = (TextView) findViewById(R.id.sit4);
                temp4 = (TextView) findViewById(R.id.temp4);
                sit4.setText(date4X + " \n"+ jsonObject.getJSONArray("daily").getJSONObject(4).getJSONArray("weather").getJSONObject(0).getString("description"));
                temp4.setText(String.valueOf( (int) jsonObject.getJSONArray("daily").getJSONObject(4).getJSONObject("temp").getDouble("day")) + "°C");

                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          Через 4 дня            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                Long unix5 = jsonObject.getJSONArray("daily").getJSONObject(5).getLong("dt");
                Date date5 = new Date(unix5*1000L);
                String date5X = sdfcal.format(date5);
                sit5 = (TextView) findViewById(R.id.sit5);
                temp5 = (TextView) findViewById(R.id.temp5);
                sit5.setText(date5X + " \n"+ jsonObject.getJSONArray("daily").getJSONObject(5).getJSONArray("weather").getJSONObject(0).getString("description"));
                temp5.setText(String.valueOf( (int) jsonObject.getJSONArray("daily").getJSONObject(5).getJSONObject("temp").getDouble("day")) + "°C");

                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          Через 5 дней            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                Long unix6 = jsonObject.getJSONArray("daily").getJSONObject(6).getLong("dt");
                Date date6 = new Date(unix6*1000L);
                String date6X = sdfcal.format(date6);
                sit6 = (TextView) findViewById(R.id.sit6);
                temp6 = (TextView) findViewById(R.id.temp6);
                sit6.setText(date6X + " \n"+ jsonObject.getJSONArray("daily").getJSONObject(6).getJSONArray("weather").getJSONObject(0).getString("description"));
                temp6.setText(String.valueOf( (int) jsonObject.getJSONArray("daily").getJSONObject(6).getJSONObject("temp").getDouble("day")) + "°C");




                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  Блок для передачи переменных в "Подробно" ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                SimpleDateFormat sdfH = new SimpleDateFormat("HH:mm");
                sdfH.setTimeZone((TimeZone.getTimeZone(zone)));

                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                   Сейчас                     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                Long unixH0 = jsonObject.getJSONObject("current").getLong("dt");
                Date H0 = new Date(unixH0*1000L);
                H0x = sdfH.format(H0);
                sitH0 = String.valueOf(jsonObject.getJSONArray("hourly").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("description")) + ", " +
                        String.valueOf( (int) jsonObject.getJSONArray("hourly").getJSONObject(0).getDouble("temp") + "°C");

                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                Через час                  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                Long unixH1 = jsonObject.getJSONArray("hourly").getJSONObject(1).getLong("dt");
                Date H1 = new Date(unixH1*1000L);
                H1x = sdfH.format(H1);
                sitH1 = String.valueOf(jsonObject.getJSONArray("hourly").getJSONObject(1).getJSONArray("weather").getJSONObject(0).getString("description")) + ", " +
                        String.valueOf( (int) jsonObject.getJSONArray("hourly").getJSONObject(1).getDouble("temp") + "°C");

                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                Через 2 часа                  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                Long unixH2 = jsonObject.getJSONArray("hourly").getJSONObject(2).getLong("dt");
                Date H2 = new Date(unixH2*1000L);
                H2x = sdfH.format(H2);
                sitH2 = String.valueOf(jsonObject.getJSONArray("hourly").getJSONObject(2).getJSONArray("weather").getJSONObject(0).getString("description")) + ", " +
                        String.valueOf( (int) jsonObject.getJSONArray("hourly").getJSONObject(2).getDouble("temp") + "°C");

                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                Через 3 часа                  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                Long unixH3 = jsonObject.getJSONArray("hourly").getJSONObject(3).getLong("dt");
                Date H3 = new Date(unixH3*1000L);
                H3x = sdfH.format(H3);
                sitH3 = String.valueOf(jsonObject.getJSONArray("hourly").getJSONObject(3).getJSONArray("weather").getJSONObject(0).getString("description")) + ", " +
                        String.valueOf( (int) jsonObject.getJSONArray("hourly").getJSONObject(3).getDouble("temp") + "°C");

                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                Через 4 часа                  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                Long unixH4 = jsonObject.getJSONArray("hourly").getJSONObject(4).getLong("dt");
                Date H4 = new Date(unixH4*1000L);
                H4x = sdfH.format(H4);
                sitH4 = String.valueOf(jsonObject.getJSONArray("hourly").getJSONObject(4).getJSONArray("weather").getJSONObject(0).getString("description")) + ", " +
                        String.valueOf( (int) jsonObject.getJSONArray("hourly").getJSONObject(4).getDouble("temp") + "°C");

                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                Через 5 часов                 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                Long unixH5 = jsonObject.getJSONArray("hourly").getJSONObject(5).getLong("dt");
                Date H5 = new Date(unixH5*1000L);
                H5x = sdfH.format(H5);
                sitH5 = String.valueOf(jsonObject.getJSONArray("hourly").getJSONObject(5).getJSONArray("weather").getJSONObject(0).getString("description")) + ", " +
                        String.valueOf( (int) jsonObject.getJSONArray("hourly").getJSONObject(5).getDouble("temp") + "°C");

                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                Через 6 часов                 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                Long unixH6 = jsonObject.getJSONArray("hourly").getJSONObject(6).getLong("dt");
                Date H6 = new Date(unixH6*1000L);
                H6x = sdfH.format(H6);
                sitH6 = String.valueOf(jsonObject.getJSONArray("hourly").getJSONObject(6).getJSONArray("weather").getJSONObject(0).getString("description")) + ", " +
                        String.valueOf( (int) jsonObject.getJSONArray("hourly").getJSONObject(6).getDouble("temp") + "°C");

                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                Через 7 часов                 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                Long unixH7 = jsonObject.getJSONArray("hourly").getJSONObject(7).getLong("dt");
                Date H7 = new Date(unixH7*1000L);
                H7x = sdfH.format(H7);
                sitH7 = String.valueOf(jsonObject.getJSONArray("hourly").getJSONObject(7).getJSONArray("weather").getJSONObject(0).getString("description")) + ", " +
                        String.valueOf( (int) jsonObject.getJSONArray("hourly").getJSONObject(7).getDouble("temp") + "°C");

                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                Через 8 часов                 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                Long unixH8 = jsonObject.getJSONArray("hourly").getJSONObject(8).getLong("dt");
                Date H8 = new Date(unixH8*1000L);
                H8x = sdfH.format(H8);
                sitH8 = String.valueOf(jsonObject.getJSONArray("hourly").getJSONObject(8).getJSONArray("weather").getJSONObject(0).getString("description")) + ", " +
                        String.valueOf( (int) jsonObject.getJSONArray("hourly").getJSONObject(8).getDouble("temp") + "°C");

                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                Через 9 часов                 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                Long unixH9 = jsonObject.getJSONArray("hourly").getJSONObject(9).getLong("dt");
                Date H9 = new Date(unixH9*1000L);
                H9x = sdfH.format(H9);
                sitH9 = String.valueOf(jsonObject.getJSONArray("hourly").getJSONObject(9).getJSONArray("weather").getJSONObject(0).getString("description")) + ", " +
                        String.valueOf( (int) jsonObject.getJSONArray("hourly").getJSONObject(9).getDouble("temp") + "°C");

                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                Через 10 часов                 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                Long unixH10 = jsonObject.getJSONArray("hourly").getJSONObject(10).getLong("dt");
                Date H10 = new Date(unixH10*1000L);
                H10x = sdfH.format(H10);
                sitH10 = String.valueOf(jsonObject.getJSONArray("hourly").getJSONObject(10).getJSONArray("weather").getJSONObject(0).getString("description")) + ", " +
                        String.valueOf( (int) jsonObject.getJSONArray("hourly").getJSONObject(10).getDouble("temp") + "°C");

                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                Через 11 часов                 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                Long unixH11 = jsonObject.getJSONArray("hourly").getJSONObject(11).getLong("dt");
                Date H11 = new Date(unixH11*1000L);
                H11x = sdfH.format(H11);
                sitH11 = String.valueOf(jsonObject.getJSONArray("hourly").getJSONObject(11).getJSONArray("weather").getJSONObject(0).getString("description")) + ", " +
                        String.valueOf( (int) jsonObject.getJSONArray("hourly").getJSONObject(11).getDouble("temp") + "°C");


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

            Toast.makeText(MainActivity.this, "Найденное место: " + name, Toast.LENGTH_LONG).show();

        }
    }
}