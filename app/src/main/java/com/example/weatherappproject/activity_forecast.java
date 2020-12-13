package com.example.weatherappproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class activity_forecast extends AppCompatActivity {

    TextView temp6,temp12,temp18;
    ImageView imageTemp6,imageTemp12,imageTemp18;
    float x1,x2,y1,y2;
    JSONObject data,obj1 = null;
    String newFormattedDate,iconUrl,iconUrl2,iconUrl3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        Intent i = getIntent();
        String dataTransmited = i.getStringExtra("CITY_MAIN");
        //System.out.println(dataTransmited);
        setTitle("Forecast for tomorrow in " +dataTransmited);
       // System.out.println(dataTransmited.length());
        Integer cut = dataTransmited.length();
        cut = cut-4;
        dataTransmited=dataTransmited.substring(0,cut);
        System.out.println(dataTransmited);
        getJSON(dataTransmited);

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = myDateObj.format(myFormatObj);
        String formattedDate2 = formattedDate.substring(8);
        int integerDate = Integer.parseInt(formattedDate2.trim());
        integerDate = integerDate +1;
        String integerDate2 = Integer.toString(integerDate);
        newFormattedDate = formattedDate.substring(0,8) + integerDate2;
        //System.out.println("After formatting: " + newFormattedDate);

        temp6 = findViewById(R.id.temperature6);
        temp12 = findViewById(R.id.temperature12);
        temp18 = findViewById(R.id.temperature18);
        imageTemp6 = findViewById(R.id.imageTemp6);
        imageTemp12 = findViewById(R.id.imageTemp12);
        imageTemp18 = findViewById(R.id.imageTemp18);


    }

    public void getJSON(final String city) {

        new AsyncTask<Void, Void, Void>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?q="+city+"&units=metric&APPID=df07195ad38e7d3cbb2f448d3aec3285");

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer json = new StringBuffer(1024);
                    String tmp = "";

                    while((tmp = reader.readLine()) != null)
                        json.append(tmp).append("\n");
                    reader.close();

                    data = new JSONObject(json.toString());
                    JSONArray array = data.getJSONArray("list");
                    for (int i=0;i<array.length();i++)
                    {
                        JSONObject obj3 = array.getJSONObject(i);
                        JSONObject obj5 = null;
                        //System.out.println(obj3.getString("dt_txt"));
                        if (obj3.getString("dt_txt").equals(newFormattedDate+" 06:00:00")){

                            JSONObject obj4 = obj3.getJSONObject("main");
                            String temperature = obj4.getString("temp");
                            temp6.setText(temperature+ "°C");


                            JSONArray array2 = obj3.getJSONArray("weather");
                            for (int j=0; j<array2.length();j++){
                                obj5= array2.getJSONObject(j);
                                String icon = obj5.getString("icon");
                                iconUrl = "http://openweathermap.org/img/w/" + icon + ".png";
                            }

                        } else if (obj3.getString("dt_txt").equals(newFormattedDate+" 12:00:00")){

                            JSONObject obj4 = obj3.getJSONObject("main");
                            String temperature = obj4.getString("temp");
                            temp12.setText(temperature+ "°C");

                            JSONArray array2 = obj3.getJSONArray("weather");
                            for (int j=0; j<array2.length();j++){
                                obj5= array2.getJSONObject(j);
                                String icon = obj5.getString("icon");
                                iconUrl2 = "http://openweathermap.org/img/w/" + icon + ".png";

                            }
                        }else if (obj3.getString("dt_txt").equals(newFormattedDate+" 18:00:00")){

                            JSONObject obj4 = obj3.getJSONObject("main");
                            String temperature = obj4.getString("temp");
                            temp18.setText(temperature+ "°C");

                            JSONArray array2 = obj3.getJSONArray("weather");
                            for (int j=0; j<array2.length();j++){
                                obj5= array2.getJSONObject(j);
                                String icon = obj5.getString("icon");
                                iconUrl3 = "http://openweathermap.org/img/w/" + icon + ".png";
                            }
                        }


                    }



                    if(data.getInt("cod") != 200) {
                        System.out.println("Cancelled");
                        return null;
                    }


                } catch (Exception e) {

                    System.out.println("Exception "+ e.getMessage());
                    return null;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void Void) {
                if(data!=null){
                    //Log.d("my weather received",data.toString());
                    //System.out.println(iconUrl);
                    Picasso.with(activity_forecast.this).load(iconUrl).into(imageTemp6);
                    Picasso.with(activity_forecast.this).load(iconUrl2).into(imageTemp12);
                    Picasso.with(activity_forecast.this).load(iconUrl3).into(imageTemp18);
                }

            }
        }.execute();

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