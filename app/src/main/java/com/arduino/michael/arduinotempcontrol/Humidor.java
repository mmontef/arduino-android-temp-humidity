package com.arduino.michael.arduinotempcontrol;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Michael on 2016-08-18.
 */
public class Humidor {

    private int id;
    private double temperature;
    private double humidity;
    private String currentDate;

    public Humidor(){

    }

    public Humidor(double temperature, double humidity){

        this.temperature = temperature;
        this.humidity = humidity;

        //auto assign current date to private attribute currentDate
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = df.format(c.getTime());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate){
        this.currentDate = currentDate;
    }
}
