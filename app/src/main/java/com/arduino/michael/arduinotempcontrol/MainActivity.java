package com.arduino.michael.arduinotempcontrol;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button networkButton, tempButton, humidButton, addTempButton, addHumidButton;
    private EditText ipaddressEdit, portEdit;
    private TextView temperature, humidity;
    private String ipaddress, port,temp, humid;
    private ArrayList<Humidor> humidors;
    private LineChart tChart, hChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instantiate arrayList to be populated by data from db
        humidors = new ArrayList<Humidor>();

        //add button listeners
        networkButtonListener();
        tempButtonListener();
        humidButtonListener();
        addTempButtonListener();
        addHumidButtonListener();

        //call method that pulls data from db and fills humidors arraylist
        getHumidorsFromDB();

        //using the now filled arraylist, populate chart

        tChart = (LineChart) findViewById(R.id.tempChart);
        hChart = (LineChart) findViewById(R.id.humidChart);
        //populateChart();
        populateChart2(tChart, hChart);


    }

    public void networkButtonListener() {
        networkButton = (Button) findViewById(R.id.networkButton);
        ipaddressEdit = (EditText) findViewById(R.id.ipaddressText);
        portEdit = (EditText)findViewById(R.id.portText);

        networkButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //send message to screen that this button was pressed
                ipaddress = ipaddressEdit.getText().toString();
                port = portEdit.getText().toString();


                Context context = getApplicationContext();
                CharSequence text = "ip address: " + ipaddress + " Password: " + port;
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

    }

    public void tempButtonListener()
    {
        tempButton = (Button) findViewById(R.id.getTemperatureButton);
        //doesnt exit anymore, need to review how to do this
        //maybe get the latest data from humidor
        //temperature = (TextView) findViewById(R.id.tempArduinoTextView);

        tempButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view){
                //temp = temperature.getText().toString();
                temp = "75";
                Context context = getApplicationContext();
                CharSequence text = "Current Temperature: " + temp;
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }
        });

    }

    public void humidButtonListener()
    {
        humidButton = (Button) findViewById(R.id.getHumidityButton);
        //same problem as in tempButtonListener
        //humidity = (TextView) findViewById(R.id.humidArduinoTextView);

        humidButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //humid = humidity.getText().toString();

                humid = "65";
                Context context = getApplicationContext();
                CharSequence text = "Current Humidity: " + humid;
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }
        });

    }

    public void addTempButtonListener()
    {
        addTempButton = (Button)findViewById(R.id.addTempButton);

        addTempButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //listening for adding Temperature Manually
                //need to have private arrayList of some sort to add data
                //that way, can also dynamically add data
                callAddHumidorDialog();

            }
        });
    }

    public void addHumidButtonListener()
    {
        addHumidButton = (Button)findViewById(R.id.addHumidButton);

        addHumidButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //listening for adding Humidity Manually
                //need to have private arrayList of some sort to add data
                //that way, can also dynamically add data
                callAddHumidorDialog();

            }
        });
    }



    /**
     * this method will call all the humidor data from the db and populate it in the chart
     */
    public void getHumidorsFromDB(){

        DBHandler dbHandler = new DBHandler(this);
        humidors = dbHandler.getAllHumidors();

    }

    private void callAddHumidorDialog(){

        final Dialog myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.addhumidor);
        myDialog.setCancelable(false);
        Button save = (Button) myDialog.findViewById(R.id.saveButton);
        Button cancel = (Button) myDialog.findViewById(R.id.cancelButton);

        final EditText tempAdd = (EditText) myDialog.findViewById(R.id.tempAdd);
        final EditText humidAdd = (EditText) myDialog.findViewById(R.id.humidAdd);
        myDialog.show();

        save.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                double newTemp;
                double newHumid;
                boolean isDouble = true;
                while(!isDouble){

                    String tempString = tempAdd.getText().toString();
                    String humidString = humidAdd.getText().toString();
                    if(tempString.length()>0 && humidString.length()>0)
                        try
                        {
                            newTemp= Double.parseDouble(tempString);
                            newHumid = Double.parseDouble(humidString);
                            // it means it is double

                            //create new humidor
                            Humidor h = new Humidor(newTemp,newHumid);
                            //if get here, have good values for temp and humidity
                            //now save to database

                            //dont know if getting right context
                            DBHandler db = new DBHandler(getApplicationContext());

                            //insert into database
                            db.insertHumidor(h);
                            //add to arraylist
                            humidors.add(h);

                            //update chart

                            refreshChart(tChart,hChart);

                            isDouble = true;
                            myDialog.dismiss();

                        } catch (Exception e1) {
                            // this means it is not double
                            e1.printStackTrace();
                            isDouble = false;
                        }
                }



            }
        });

        cancel.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                myDialog.dismiss();
            }
        });


    }

    public void refreshChart(LineChart tChart, LineChart hChart){
        tChart.getData().notifyDataChanged();
        tChart.notifyDataSetChanged();
        tChart.invalidate();
        hChart.getData().notifyDataChanged();
        hChart.notifyDataSetChanged();
        hChart.invalidate();
    }

    public void populateChart2(LineChart tChart, LineChart hChart){

        List<Entry> entries = new ArrayList<Entry>();
        for(Humidor h:humidors){
            entries.add(new Entry(h.getId(),(int)h.getTemperature()));
        }

        LineDataSet tempDataSet = new LineDataSet(entries, "Label"); // add entries to dataset
        tempDataSet.setColor(R.color.colorAccent);
        //dataSet.setValueTextColor(); // styling, ...

        LineData tempLineData = new LineData(tempDataSet);
        tChart.setData(tempLineData);
        tChart.setDescription("Temperature over time");
        tChart.invalidate();

        List<Entry> entries1 = new ArrayList<Entry>();
        for(Humidor h:humidors){
            entries1.add(new Entry(h.getId(),(int)h.getHumidity()));
        }

        LineDataSet humidityDataSet = new LineDataSet(entries1, "Label"); // add entries to dataset
        humidityDataSet.setColor(R.color.colorAccent);
        //dataSet.setValueTextColor(); // styling, ...

        LineData humidityLineData = new LineData(humidityDataSet);
        hChart.setData(humidityLineData);
        hChart.setDescription("Humidity over time");
        hChart.invalidate();


    }



}
