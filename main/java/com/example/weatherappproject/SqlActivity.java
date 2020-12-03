package com.example.weatherappproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class SqlActivity extends AppCompatActivity {

    Button btn_Add;
    EditText addCity;
    ListView lv_customer;

    ArrayAdapter cityArrayAdapter;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql);
        setTitle("Database of cities");

        btn_Add= findViewById(R.id.btn_Add);
        addCity=findViewById(R.id.addCity);
        lv_customer=findViewById(R.id.lv_customer);
        final Switch switchVisibility = findViewById(R.id.switch1);

        dataBaseHelper = new DataBaseHelper(SqlActivity.this);

        ShowCityOnListView(dataBaseHelper);

        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nullString = addCity.getText().toString();
                CityModel cityModel;
                if (nullString == null || nullString.isEmpty()){
                    Toast.makeText(SqlActivity.this, "Nothing written", LENGTH_SHORT).show();
                    //cityModel = new CityModel(-1, "error");
                }else {
                    cityModel = new CityModel(-1, addCity.getText().toString());
                    Toast.makeText(SqlActivity.this, addCity.getText().toString(), LENGTH_SHORT).show();
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(SqlActivity.this);
                    boolean success = dataBaseHelper.addOne(cityModel);
                    Toast.makeText(SqlActivity.this, "Success "+ success, LENGTH_SHORT).show();
                    ShowCityOnListView(dataBaseHelper);
                }

            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);
        switchVisibility.setChecked(sharedPreferences.getBoolean("value",true));
        switchVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchVisibility.isChecked()){
                    SharedPreferences.Editor editor = getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",true);
                    editor.apply();
                    switchVisibility.setChecked(true);
                    lv_customer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            CityModel clickedCity = (CityModel) parent.getItemAtPosition(position);
                            //Log.d(clickedCity.getCity(),"clickedcity");
                            dataBaseHelper.deleteOne(clickedCity);
                            ShowCityOnListView(dataBaseHelper);
                            Toast.makeText(SqlActivity.this, "Deleted", LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    SharedPreferences.Editor editor = getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",false);
                    switchVisibility.setChecked(false);
                }
            }
        });
    }

    private void ShowCityOnListView(DataBaseHelper dataBaseHelper2) {
        cityArrayAdapter = new ArrayAdapter<CityModel>(SqlActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper2.getEveryone());
        lv_customer.setAdapter(cityArrayAdapter);
    }
}