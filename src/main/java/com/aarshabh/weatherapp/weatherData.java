package com.aarshabh.weatherapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.locks.Condition;

public class weatherData {
    private String mTemp, mIcon, mCity, mWeatherType;
    private int mCondition;

    public static weatherData fromJson(JSONObject jsonObject){
        try{
            weatherData data = new weatherData();
            data.mCity = jsonObject.getString("name");
            data.mCondition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            data.mWeatherType= jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            data.mIcon = updateWeatherIcon(data.mCondition);
            double temp = jsonObject.getJSONObject("main").getDouble("temp") - 273.15;
            int round = (int)Math.rint(temp);
            data.mTemp = Integer.toString(round);
            return data;
        }catch(JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    private static String updateWeatherIcon(int condition){
        if(condition >= 0 && condition< 300){
            return "thunderstorm";
        }
        else if(condition >= 300 && condition< 500){
            return "mildrain";
        }
        else if(condition >= 500 && condition< 600){
            return "heavyrain";
        }
        else if(condition >= 600 && condition< 700){
            return "heavysnow";
        }
        else if(condition >= 700 && condition< 771){
            return "mildsnow";
        }
        else if(condition >= 772 && condition< 800){
            return "partialcloud";
        }
        else if(condition == 800) {
            return "sunny";
        }
        else if(condition>=801 && condition< 805) {
            return "partialcloud";
        }
        else if(condition >= 900 && condition< 903){
            return "cloudy";
        }
        else if(condition ==904){
            return "sunny";
        }
        else if(condition >= 905 && condition< 1000){
            return "thunderstorm";
        }
        else
            return "idk";
    }

    public String getmTemp() {
        return mTemp + "°C";
    }

    public String getmIcon() {
        return mIcon;
    }

    public String getmCity() {
        return mCity;
    }

    public String getmWeatherType() {
        return mWeatherType;
    }
}
