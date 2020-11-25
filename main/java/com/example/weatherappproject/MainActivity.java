package com.example.weatherappproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainActivity extends AppCompatActivity {

    Button btnSearch;
    EditText etCityName;
    TextView tvCity, tvWeather;
    ImageView ivWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = findViewById(R.id.btnSearch);
        etCityName = findViewById(R.id.etCityName);
        tvCity = findViewById(R.id.tvCity);
        tvWeather = findViewById(R.id.tvWeather);
        ivWeather = findViewById(R.id.ivWeather);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = etCityName.getText().toString();

                if (city.isEmpty())
                    Toast.makeText(MainActivity.this, "Please enter a city name", Toast.LENGTH_SHORT).show();
                else {
                    loadWeatherByCityName(city);
                }
            }
        });
    }

    private void loadWeatherByCityName(final String city) {
        Ion.getDefault(MainActivity.this).getConscryptMiddleware().enable(false);


        // TODO 1 : make http request to call openweatherapi webservices
        final String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=df07195ad38e7d3cbb2f448d3aec3285";

        Ion.with(MainActivity.this)
                .load(apiUrl)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // TODO 2 : get json response
                        // do stuff with the result or error
                        //JsonObject cod = result.get("cod").getAsJsonObject();
                        if (e != null) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "server error", Toast.LENGTH_SHORT).show();
                        } else {
                            // TODO 3 : convert json response to java
                            // get temp

                            JsonPrimitive cod = result.get("cod").getAsJsonPrimitive();
                            int codeTest;
                            codeTest = cod.getAsInt();
                            if (codeTest == 404){
                                Toast.makeText(MainActivity.this,"City not found", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                JsonObject main = result.get("main").getAsJsonObject();
                                double temp = main.get("temp").getAsDouble();
                                // get city

                                JsonObject sys = result.get("sys").getAsJsonObject();
                                String country = sys.get("country").getAsString();
                                tvWeather.setText(temp + "°C");
                                tvCity.setText(city + ", " + country);

                                // TODO 4 : display weather result

                                // TODO 5 : display weather icon
                                JsonObject weather = result.get("weather").getAsJsonArray().get(0).getAsJsonObject();
                                String icon = weather.get("icon").getAsString();
                                loadImage(icon);
                            }
                        }
                    }
                });


    }

    private void loadImage(String icon) {
        Ion.with(this)
                .load("http://openweathermap.org/img/w/" + icon + ".png")
                .intoImageView(ivWeather);
    }

}
