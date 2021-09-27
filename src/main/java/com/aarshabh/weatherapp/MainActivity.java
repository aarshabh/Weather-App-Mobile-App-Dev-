package com.aarshabh.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {


    final String api = "1a917b067c60b67e6b516877fa339151"; //1a917b067c60b67e6b516877fa339151
    final String weatherUrl = "https://api.openweathermap.org/data/2.5/weather";
    final long min_time = 50000;
    final float min_dis = 1000;
    final int req_code = 101;


    String location_provider = LocationManager.GPS_PROVIDER;

    TextView nameOfCity, weatherState, temperature;
    ImageView m_weather_icon;


    RelativeLayout m_city_finder;

    LocationManager m_location_manager;
    LocationListener m_location_listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherState = findViewById(R.id.weatherCondition);
        temperature = findViewById(R.id.temperature);
        m_weather_icon = findViewById(R.id.weatherIcon);
        m_city_finder = findViewById(R.id.city);
        nameOfCity = findViewById(R.id.cityName);

        m_city_finder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, findCity.class);
                startActivity(intent);
            }
        });
    }


//    protected void onResume() {
//        super.onResume();
//        getWeatherForCurrentLocation();
//    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent in = getIntent();
        String city = in.getStringExtra("City");
        if(city != null){
            getWeatherForNewCity(city);
        }
        else {
            getWeatherForCurrentLocation();
        }
    }

    private void getWeatherForNewCity(String city){
        RequestParams params = new RequestParams();
        params.put("q", city);
        params.put("appid", api);
        letsDoSomeNetworking(params);
    }

    private void getWeatherForCurrentLocation() {
        m_location_manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        m_location_listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                String latitude = String.valueOf(location.getLatitude());
                String longitude = String.valueOf(location.getLongitude());

                RequestParams params = new RequestParams();
                params.put("lat", latitude);
                params.put("lon", longitude);
                params.put("appid", api);
                letsDoSomeNetworking(params);
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
//
            @Override
            public void onProviderDisabled(@NonNull String provider) {
            }

        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, req_code);
            return;
        }
        m_location_manager.requestLocationUpdates(location_provider, min_time, min_dis, m_location_listener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if(requestCode==req_code){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this, "Location Found", Toast.LENGTH_SHORT).show();  //2 seconds. LENGTH_LONG -> 5 seconds
                getWeatherForCurrentLocation();
            }
            else{
                Toast.makeText(MainActivity.this, "Location Not Available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void letsDoSomeNetworking(RequestParams params){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(weatherUrl, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(MainActivity.this, "Data Fetched", Toast.LENGTH_SHORT).show();

                weatherData data = weatherData.fromJson(response);
                assert data != null;
                updateUI(data);

               // super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void updateUI(weatherData data){
        temperature.setText(data.getmTemp());
        nameOfCity.setText(data.getmCity());
        weatherState.setText(data.getmWeatherType());
        int resourceId = getResources().getIdentifier(data.getmIcon(), "drawable", getPackageName());
        m_weather_icon.setImageResource(resourceId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(m_location_manager != null){
            m_location_manager.removeUpdates(m_location_listener);
        }
    }
}

