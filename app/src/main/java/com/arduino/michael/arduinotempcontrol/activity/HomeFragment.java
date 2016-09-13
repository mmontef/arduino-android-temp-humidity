package com.arduino.michael.arduinotempcontrol.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arduino.michael.arduinotempcontrol.R;

public class HomeFragment extends Fragment {

    private TextView humidityHome, temperatureHome, networkHome;
    Double currentTempHome,currentHumidHome;
    String tempText, humidText, networkText;

    //TODO when this fragment is created or called, load data from MainActivity

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);





        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        humidityHome = (TextView) getActivity().findViewById(R.id.home_humid);
        temperatureHome = (TextView) getActivity().findViewById(R.id.home_temp);
        networkHome = (TextView) getActivity().findViewById(R.id.home_network);
        updateTextViewsHome();
    }

    public void updateTextViewsHome(){

        Bundle bundle = getArguments();
        tempText = bundle.getString("tempKey");
        humidText = bundle.getString("humidKey");
        networkText = bundle.getString("networkKey");

        setTemperatureHomeText(tempText);
        setHumidityHomeText(humidText);
        setNetworkHomeText(networkText);

        Double t = ((MainActivity)getActivity()).getLatestTemperature();
        Double h = ((MainActivity)getActivity()).getLatestHumidity();


        //setTemperatureHomeText(t.toString());
        //setHumidityHomeText(h.toString());
    }

    public TextView getHomeText(){
        return humidityHome;
    }

    public TextView getTemperatureHome(){
        return temperatureHome;
    }

    public TextView getNetworkHome(){
        return networkHome;
    }

    public void setHumidityHomeText(String text){
        this.humidityHome.setText("Humidity: " + text);
    }

    public void setTemperatureHomeText(String text){
        this.temperatureHome.setText("Temperature: " + text);
    }

    public void setNetworkHomeText(String text){
        this.networkHome.setText("Network: " + text);
    }


}
