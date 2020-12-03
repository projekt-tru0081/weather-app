package com.example.weatherappproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class SqlActivity extends AppCompatActivity {

    Button btn_Add, btn_Edit;
    EditText addCity;
    ListView lv_customer;

    ArrayAdapter cityArrayAdapter;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql);

        btn_Add= findViewById(R.id.btn_Add);
        btn_Edit=findViewById(R.id.btn_Edit);
        addCity=findViewById(R.id.addCity);
        lv_customer=findViewById(R.id.lv_customer);

        dataBaseHelper = new DataBaseHelper(SqlActivity.this);

        ShowCityOnListView(dataBaseHelper);

        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nullString = addCity.getText().toString();
                CityModel cityModel;
                if (nullString == null || nullString.isEmpty()){
                    Toast.makeText(SqlActivity.this, "Nothing written", LENGTH_SHORT).show();
                    cityModel = new CityModel(-1, "error");
                }else {
                    cityModel = new CityModel(-1, addCity.getText().toString());
                    Toast.makeText(SqlActivity.this, addCity.getText().toString(), LENGTH_SHORT).show();

                }
                DataBaseHelper dataBaseHelper = new DataBaseHelper(SqlActivity.this);
                boolean success = dataBaseHelper.addOne(cityModel);
                Toast.makeText(SqlActivity.this, "Success "+ success, LENGTH_SHORT).show();
                ShowCityOnListView(dataBaseHelper);
            }
        });

        btn_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(SqlActivity.this);

                ShowCityOnListView(dataBaseHelper);

                //Toast.makeText(SqlActivity.this, everyone.toString(), LENGTH_SHORT).show();
            }
        });
        lv_customer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CityModel clickedCity = (CityModel) parent.getItemAtPosition(position);
                dataBaseHelper.deleteOne(clickedCity);
                ShowCityOnListView(dataBaseHelper);
                Toast.makeText(SqlActivity.this, "Deleted", LENGTH_SHORT).show();
            }
        });

    }

    private void ShowCityOnListView(DataBaseHelper dataBaseHelper2) {
        cityArrayAdapter = new ArrayAdapter<CityModel>(SqlActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper2.getEveryone());
        lv_customer.setAdapter(cityArrayAdapter);
    }
}