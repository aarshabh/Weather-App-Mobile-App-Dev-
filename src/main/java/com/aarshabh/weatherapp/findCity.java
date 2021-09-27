package com.aarshabh.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class findCity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_city);
        final EditText search = findViewById(R.id.searchCity);
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finish();
            }
        });

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String newCity = search.getText().toString();
                Intent intent = new Intent(findCity.this, MainActivity.class);
                intent.putExtra("City", newCity);
                startActivity(intent);
                return false;
            }
        });
    }
}