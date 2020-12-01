package com.example.weatherappproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MainActivity extends AppCompatActivity {

    Button btnSearch;
    EditText etCityName;
    TextView tvCity, tvWeather, tvPressure, tvHumidity;
    ImageView ivWeather;

    private Button btn;
    private TextView textView;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 15 * 1000;  /* 15 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 sec */

    public static String cityString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = findViewById(R.id.btnSearch);
        etCityName = findViewById(R.id.etCityName);
        tvCity = findViewById(R.id.tvCity);
        tvWeather = findViewById(R.id.tvWeather);
        ivWeather = findViewById(R.id.ivWeather);
        tvPressure = findViewById(R.id.pressure);
        tvHumidity = findViewById(R.id.humidity);

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
        startLocationUpdates();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item2:
                loadWeatherByCityName(cityString);
                Toast.makeText(this,"Item 1 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item3:
                Intent i = new Intent(this, SqlActivity.class);
                this.startActivity(i);
                Toast.makeText(this,"Item 2 selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    // Trigger new location updates at interval
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        try {
                            onLocationChanged(locationResult.getLastLocation());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                Looper.myLooper());
    }

    public void onLocationChanged(Location location) throws IOException {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Log.d("msg", msg);
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        if (addresses.size() > 0) {
            //System.out.println(addresses.get(0).getLocality());
            cityString = addresses.get(0).getLocality();
            System.out.println(cityString);
        }
        else {
            // do your stuff
        }
    }


    private void loadWeatherByCityName(final String city) {
        Ion.getDefault(MainActivity.this).getConscryptMiddleware().enable(false);



        final String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=df07195ad38e7d3cbb2f448d3aec3285";

        Ion.with(MainActivity.this)
                .load(apiUrl)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "server error", Toast.LENGTH_SHORT).show();
                        } else {

                            JsonPrimitive cod = result.get("cod").getAsJsonPrimitive();
                            int codeTest;
                            codeTest = cod.getAsInt();
                            if (codeTest == 404){
                                Toast.makeText(MainActivity.this,"City not found", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                JsonObject main = result.get("main").getAsJsonObject();
                                double temp = main.get("temp").getAsDouble();
                                double humidity = main.get("humidity").getAsDouble();
                                double pressure = main.get("pressure").getAsDouble();
                                // get city

                                JsonObject sys = result.get("sys").getAsJsonObject();
                                String country = sys.get("country").getAsString();
                                tvWeather.setText(temp + "°C");
                                tvCity.setText(city + ", " + country);
                                tvHumidity.setText(humidity + " %");
                                tvPressure.setText(pressure + " hPa");

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
