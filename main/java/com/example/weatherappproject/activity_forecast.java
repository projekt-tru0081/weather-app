package com.example.weatherappproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class activity_forecast extends AppCompatActivity {

    float x1,x2,y1,y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        setTitle("Weather forecast");
        loadWeatherByCityName("Cadca");
    }

    public void loadWeatherByCityName(final String city) {
        Ion.getDefault(activity_forecast.this).getConscryptMiddleware().enable(false);



        final String apiUrl = "http://api.openweathermap.org/data/2.5/forecast?q=" + city + "&units=metric&appid=df07195ad38e7d3cbb2f448d3aec3285";

        Ion.with(activity_forecast.this)
                .load(apiUrl)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            e.printStackTrace();
                            Toast.makeText(activity_forecast.this, "server error", Toast.LENGTH_SHORT).show();
                        } else {

                            JsonPrimitive cod = result.get("cod").getAsJsonPrimitive();
                            int codeTest;
                            codeTest = cod.getAsInt();
                            if (codeTest == 404){
                                Toast.makeText(activity_forecast.this,"City not found", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                JsonArray list = result.get("list").getAsJsonArray();
                                Log.d(list.toString(),"list");
                                //JsonObject main = result.get("main").getAsJsonObject();
                                //double temp = main.get("temp").getAsDouble();
                                //double humidity = main.get("humidity").getAsDouble();
                                //double pressure = main.get("pressure").getAsDouble();
                                // get city

                                //JsonObject sys = result.get("sys").getAsJsonObject();
                                //String country = sys.get("country").getAsString();
                               // tvWeather.setText(temp + "°C");
                               // tvCity.setText(city + ", " + country);
                               // tvHumidity.setText(humidity + " %");
                               // tvPressure.setText(pressure + " hPa");

                                //JsonObject weather = result.get("weather").getAsJsonArray().get(0).getAsJsonObject();
                                //String icon = weather.get("icon").getAsString();
                                //loadImage(icon);
                            }
                        }
                    }
                });


    }

    public boolean onTouchEvent(MotionEvent touchEvent){
        switch (touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1=touchEvent.getX();
                y1=touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2=touchEvent.getX();
                y2=touchEvent.getY();
                if (x1<x2){
                    Intent i = new Intent(activity_forecast.this,MainActivity.class);
                    startActivity(i);
                }

                break;
        }
        return false;
    }

}