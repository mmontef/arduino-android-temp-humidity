package com.arduino.michael.arduinotempcontrol.activity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arduino.michael.arduinotempcontrol.R;
import com.arduino.michael.arduinotempcontrol.model.Humidor;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;


public class HumidorFragment extends Fragment {

    String temp, humid;

    private FloatingActionButton fab;

    private View fragView;

    private  LineChart chart;

    private TextView currentHumidity, currentTemperature;

    private String inputTemp, inputHumid;

    private ArrayList<Humidor> humidorsFrag;

    public OnAddHumidor sendHumidor;

    public HumidorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        humidorsFrag = ((MainActivity)getActivity()).getHumidors();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragView = inflater.inflate(R.layout.fragment_humidor, container, false);
        chart = (LineChart) fragView.findViewById(R.id.chart);

        populateChart(chart);

        fab = (FloatingActionButton) fragView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAddHumidorDialog();
            }
        });

        updateHumidorTextViews();

        return fragView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        populateChart(chart);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void callAddHumidorDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Humidor");

        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.addhumidor, (ViewGroup) getView(), false);


        final EditText tempInput = (EditText)viewInflated.findViewById(R.id.tempAdd);
        final EditText humidInput = (EditText)viewInflated.findViewById(R.id.humidAdd);

        builder.setView(viewInflated);


        // Set up the buttons
        builder.setPositiveButton(R.string.save_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                inputTemp = tempInput.getText().toString();
                inputHumid = humidInput.getText().toString();

                Double tempDouble = Double.parseDouble(inputTemp);
                Double humidDouble = Double.parseDouble(inputHumid);

                //toast that action is received
                Toast.makeText(getActivity(), "Temp: "+ inputTemp + " Humid: " + inputHumid + " saved!",
                        Toast.LENGTH_LONG).show();

                //save humidor to database and humidors arraylist in mainactivity
                Humidor h = new Humidor(tempDouble,humidDouble);

                //created new humidor, now add it to humidor arraylist
                sendHumidor = (OnAddHumidor)getContext();
                sendHumidor.sendHumidor(h);
                currentHumidity.setText("Humidity: " + inputHumid);
                currentTemperature.setText("Temperature: " + inputTemp);
                humidorsFrag = ((MainActivity)getActivity()).getHumidors();
                refreshChart(chart);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    public void updateHumidorTextViews(){

        currentHumidity = (TextView)fragView.findViewById(R.id.humidity_label);
        currentTemperature= (TextView)fragView.findViewById(R.id.temperature_label);

        Bundle bundle = getArguments();

        temp = bundle.getString("tempKey");;
        humid = bundle.getString("humidKey");

        currentHumidity.setText("Humidity: " + humid);
        currentTemperature.setText("Temperature: " + temp);

    }

    public void refreshChart(LineChart chart){
        chart.getData().notifyDataChanged();
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    public void populateChart(LineChart chart){

        List<Entry> entries = new ArrayList<Entry>();
        for(Humidor h:humidorsFrag){
            entries.add(new Entry(h.getId(),(int)h.getTemperature()));//TODO figure out date thing
        }

        LineDataSet tempDataSet = new LineDataSet(entries, "Temperature"); // add entries to dataset
        tempDataSet.setColor(R.color.colorAccent);
        //dataSet.setValueTextColor(); // styling, ...

        LineData tempLineData = new LineData(tempDataSet);
        chart.setData(tempLineData);


        List<Entry> entries1 = new ArrayList<Entry>();
        for(Humidor h:humidorsFrag){
            entries1.add(new Entry(h.getId(),(int)h.getHumidity()));
        }

        LineDataSet humidityDataSet = new LineDataSet(entries1, "Humidity"); // add entries to dataset
        humidityDataSet.setColor(R.color.colorAccent);
        //dataSet.setValueTextColor(); // styling, ...

        LineData humidityLineData = new LineData(humidityDataSet);
        chart.setData(humidityLineData);


        chart.setDescription("temperature & humidity over time");
        chart.invalidate();


    }

    public interface OnAddHumidor{
        public void sendHumidor(Humidor humidor);
    }


}
