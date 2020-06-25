package com.example.XConditions;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherDataModel {


    //Type of days
    private static String stickyDay = "Expect strong hard edged thermals with bullet cores, strong sink and most likely blue sky.";
    private static String fizzyDay = "Expect softer thermals with cumulus cloud development. Should be pleasant to fly, head for base and follow the clouds!";
    private static String soupDay = "Not likely possible for big XC distance. Warm moist stable air is thick and soup like, thermals can’t move through it well.";
    private static String windySticky = "May be turbulent! Hard edged thermals with strong micro cores. Weak lifts gets blown away and expect descent turbulence.";
    private static String windyFizzy = "Windy unstable day. Possibility for cloud street formation.";
    //Searchmode for the days
    private static String searchModeSticky = "Search wide and slow covering as much ground as possible with the best sink rate. Thermal will trigger on the most heated sources and fly over the mountain peaks.";
    private static String searchModeFizzy = "Thermals will trigger on slightest change in terrain. Usually best and strongest thermals are away from the mountain.";
    private static String searchModeSoup = "Try to search for very good heated source on day like this and hope that there will be any lift.";
    private static String searchModeWindyS = "Search downwind of any good heated source on the ground. Towns and villages works good on a blue day.";
    private static String searchModeWindyF = "The best lift you will find on the upwind side of the cloud. If you are low, then search downwind from heated source on the ground.";
    //ClimbMode for the days
    private static String climbModeSticky = "When you hit micro cores crank hard and tight as possible. You might get half turn in lift, but for sure you can climb like this. Fight for every meter and never give up!";
    private static String climbModeFizzy = "Don't turn immediately, relax and feel the glider. Listen to your vario and work your 360 turns.";
    private static String climbModeSoupDay = "Try to listen your vario and hang for any climb rate you get.";
    private static String climbModeW_S = "Stronger cores punch trough and stay upwind. That is your goal, center your 360s on the upwind side of thermals.";
    private static String climModeW_F = "Find the strongest lift by working the upwind side of the thermal.";
    private static String morning = "Good morning! Later you will get your info. First thing is coffee!";
    private static String morning_search = "Search for perfect spot to drink your coffee";
    // TODO: Declare the member variables here

    private String thisDate;
    private String temperature;
    private String feels_like;
    private String name;
    private double mSpeed;
    private String degrees;
    private int mPressure;
    private double humidity;
    private int cloudCover;
    private String searchMode;
    private String climbMode;
    private String xcPotential;


    // TODO: Create a WeatherDataModel from a JSON:
    public static WeatherDataModel fromJson(JSONObject jsonObject) {

        try {
            WeatherDataModel weatherDataModel = new WeatherDataModel();
            weatherDataModel.mSpeed = jsonObject.getJSONObject("wind").getDouble("speed");
            weatherDataModel.mPressure = jsonObject.getJSONObject("main").getInt("pressure");
            weatherDataModel.humidity = jsonObject.getJSONObject("main").getDouble("humidity");
            weatherDataModel.cloudCover = jsonObject.getJSONObject("clouds").getInt("all");
            weatherDataModel.name = jsonObject.getString("name");
            weatherDataModel.degrees = windDirection(jsonObject);
            weatherDataModel.xcPotential = xcPotential(weatherDataModel.mSpeed, weatherDataModel.mPressure, weatherDataModel.humidity, weatherDataModel.cloudCover);
            weatherDataModel.searchMode = searchMode(weatherDataModel);
            weatherDataModel.climbMode = climbMode(weatherDataModel);

            double tempResult = jsonObject.getJSONObject("main").getDouble("temp") - 273.15;
            int roundedValue = (int) Math.rint(tempResult);
            weatherDataModel.temperature = Integer.toString(roundedValue);

            double tempFeels = jsonObject.getJSONObject("main").getDouble("feels_like") - 273.15;
            int roundedValue3 = (int) Math.rint(tempFeels);
            weatherDataModel.feels_like = Integer.toString(roundedValue3);

            final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyy");
            String time = dateFormat.format(new Date());
            weatherDataModel.thisDate = time;


            return weatherDataModel;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    //Set xcPotential here:
    private static String xcPotential(double speed, int mPressure, double humidity, int cloudCover) {

        final SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
        String time = dateFormat.format(new Date());
        double timeToDouble = Double.parseDouble(time);
        Log.d("CONDITIONS", "Current time is: " + timeToDouble);

        //Type of days
        if (timeToDouble >= 19.0) {
            return "It's getting late. Except you want to catch moonlight thermals!?";
        } else if (timeToDouble >= 1.0 && timeToDouble <= 5.00) {
            return "Night XC flying? Try on the ground for change.";
        } else if (timeToDouble > 5 && timeToDouble <= 8) {
            return morning;
        } else if (speed < 4 && mPressure > 1021 && mPressure <= 1024 && humidity <= 42.0 && cloudCover <= 70) {
            return stickyDay;
        } else if (speed < 4 && mPressure >= 1011 && mPressure <= 1021 && humidity >= 20 && humidity <= 67 && cloudCover <= 60) {
            return fizzyDay;
        } else if (speed < 4 && mPressure >= 1021 && humidity > 45 && cloudCover <= 60) {
            return soupDay;
        } else if (speed >= 4 && speed <= 6 && mPressure > 1021 && mPressure <= 1024 && humidity <= 42.0 && cloudCover <= 70) {
            return windySticky;
        } else if (speed >= 4 && speed <= 6 && mPressure <= 1021 && humidity >= 20 && humidity <= 67 && cloudCover <= 60) {
            return windyFizzy;
        } else {
            return "No potential for XC flying.";
        }
    }

    private static String searchMode(WeatherDataModel weatherDataModel) {

        if (weatherDataModel.getXcPotential().equals(stickyDay)) {
            return searchModeSticky;
        } else if (weatherDataModel.getXcPotential().equals(fizzyDay)) {
            return searchModeFizzy;
        } else if (weatherDataModel.getXcPotential().equals(soupDay)) {
            return searchModeSoup;
        } else if (weatherDataModel.getXcPotential().equals(windySticky)) {
            return searchModeWindyS;
        } else if (weatherDataModel.getXcPotential().equals(windyFizzy)) {
            return searchModeWindyF;
        } else if (weatherDataModel.getXcPotential().equals(morning)) {
            return morning_search;
        } else return "Search for cold beer. Why not?";

    }

    private static String climbMode(WeatherDataModel weatherDataModel) {

        if (weatherDataModel.getXcPotential().equals(stickyDay)) {
            return climbModeSticky;
        } else if (weatherDataModel.getXcPotential().equals(fizzyDay)) {
            return climbModeFizzy;
        } else if (weatherDataModel.getXcPotential().equals(soupDay)) {
            return climbModeSoupDay;
        } else if (weatherDataModel.getXcPotential().equals(windySticky)) {
            return climbModeW_S;
        } else if (weatherDataModel.getXcPotential().equals(windyFizzy)) {
            return climModeW_F;
        } else
            return "Climb mode is turned OFF.";


    }


    private static String windDirection(JSONObject jsonObject) {

        String cardinalDirection = null;

        try {
            double directionInDegrees = jsonObject.getJSONObject("wind").getDouble("deg");

            if ((directionInDegrees >= 348.75) && (directionInDegrees <= 360) || (directionInDegrees >= 0) && (directionInDegrees <= 11.25)) {
                cardinalDirection = "N";
            } else if ((directionInDegrees >= 11.25) && (directionInDegrees <= 33.75)) {
                cardinalDirection = "NNE";
            } else if ((directionInDegrees >= 33.75) && (directionInDegrees <= 56.25)) {
                cardinalDirection = "NE";
            } else if ((directionInDegrees >= 56.25) && (directionInDegrees <= 78.75)) {
                cardinalDirection = "ENE";
            } else if ((directionInDegrees >= 78.75) && (directionInDegrees <= 101.25)) {
                cardinalDirection = "E";
            } else if ((directionInDegrees >= 101.25) && (directionInDegrees <= 123.75)) {
                cardinalDirection = "ESE";
            } else if ((directionInDegrees >= 123.75) && (directionInDegrees <= 146.25)) {
                cardinalDirection = "SE";
            } else if ((directionInDegrees >= 146.25) && (directionInDegrees <= 168.75)) {
                cardinalDirection = "SSE";
            } else if ((directionInDegrees >= 168.75) && (directionInDegrees <= 191.25)) {
                cardinalDirection = "S";
            } else if ((directionInDegrees >= 191.25) && (directionInDegrees <= 213.75)) {
                cardinalDirection = "SSW";
            } else if ((directionInDegrees >= 213.75) && (directionInDegrees <= 236.25)) {
                cardinalDirection = "SW";
            } else if ((directionInDegrees >= 236.25) && (directionInDegrees <= 258.75)) {
                cardinalDirection = "WSW";
            } else if ((directionInDegrees >= 258.75) && (directionInDegrees <= 281.25)) {
                cardinalDirection = "W";
            } else if ((directionInDegrees >= 281.25) && (directionInDegrees <= 303.75)) {
                cardinalDirection = "WNW";
            } else if ((directionInDegrees >= 303.75) && (directionInDegrees <= 326.25)) {
                cardinalDirection = "NW";
            } else if ((directionInDegrees >= 326.25) && (directionInDegrees <= 348.75)) {
                cardinalDirection = "NNW";
            } else {
                cardinalDirection = "?*";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (cardinalDirection != null) return cardinalDirection;
        return "?";
    }

    // TODO: Create getter methods for temperature, city, and icon name:
    public String getTemperature() {
        return temperature + "°";
    }

    public int getmPressure() {
        return mPressure;
    }

    public double getmSpeed() {
        return mSpeed;
    }

    public double getHumidity() {
        return humidity;
    }

    public int getCloudCover() {
        return cloudCover;
    }


    public String getDegrees() {
        return degrees;
    }

    public String getXcPotential() {
        return xcPotential;
    }

    public String getFeels_like() {
        return feels_like;
    }

    public String getName() {
        return name;
    }

    public String getThisDate() {
        return thisDate;
    }

    public String getSearchMode() {
        return searchMode;
    }

    public String getClimbMode() {
        return climbMode;
    }
}







