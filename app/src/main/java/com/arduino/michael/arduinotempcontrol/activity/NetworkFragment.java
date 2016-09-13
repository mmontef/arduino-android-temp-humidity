package com.arduino.michael.arduinotempcontrol.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.arduino.michael.arduinotempcontrol.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NetworkFragment extends Fragment {

    public OnSendNetworkData dataPasser;

    private EditText networkField, passwordField, portField;
    private Button networkButton;
    private String network, password, port;

    public NetworkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_network, container, false);

    }

    public interface OnSendNetworkData{
        public void sendNetworkData(String networkData, String passwordData, String portData);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        dataPasser = (OnSendNetworkData) context;

    }

    public void passData(String networkData, String passwordData, String portData){
        dataPasser.sendNetworkData(networkData, passwordData, portData);
    }

    @Override
    public void onActivityCreated(Bundle savedInstances){
        super.onActivityCreated(savedInstances);
        networkField = (EditText) getActivity().findViewById(R.id.network_input);
        passwordField = (EditText)getActivity().findViewById(R.id.network_password);
        portField = (EditText)getActivity().findViewById(R.id.network_port);

        networkButton = (Button)getActivity().findViewById(R.id.network_button);

        networkButton.setOnClickListener(new View.OnClickListener(){//TODO MOVE THIS TO ON ACTIVITY CREATED
            @Override
            public void onClick(View v){
                //on click of button, save all info to variables in MainActivity to be used later
                if(networkField.getText()!=null){
                    network = networkField.getText().toString();
                }

                if(passwordField.getText()!=null){
                    password = passwordField.getText().toString();
                }

                if(portField.getText()!=null){
                    port = portField.getText().toString();
                }

                //got all the stuff, now send in bundle or something to mainactivity
                passData(network,password,port);

                //TODO make it so that it notifies user that it is saved
            }
        });


    }

}
