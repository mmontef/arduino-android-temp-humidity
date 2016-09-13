package com.arduino.michael.arduinotempcontrol.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.arduino.michael.arduinotempcontrol.R;
import com.arduino.michael.arduinotempcontrol.data.DBHandler;
import com.arduino.michael.arduinotempcontrol.model.Humidor;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener,
        NetworkFragment.OnSendNetworkData, HumidorFragment.OnAddHumidor {

    private static String TAG = MainActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    private ArrayList<Humidor> humidors;
    private DBHandler dbHandler;

    public Double latestTemp, latestHumid;

    public String network, password, port;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new DBHandler(this);

        //instantiate arrayList to be populated by data from db
        humidors = new ArrayList<Humidor>();

        //call method that pulls data from db and fills humidors arraylist
        getHumidorsFromDB(dbHandler);

        /*
        //add test humidors
        Humidor h1 = new Humidor(65.5,70.5);
        Humidor h2 = new Humidor(68.5, 71.5);
        Humidor h3 = new Humidor(75.7, 78.8);

        humidors.add(h1);
        humidors.add(h2);
        humidors.add(h3);
        */
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch, in this case the HomeFragment
        displayView(0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.action_search){
            Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {

        Bundle args = new Bundle();

        //get latest temp and humid

            latestTemp = getLatestTemperature();
            args.putString("tempKey", latestTemp.toString());



            latestHumid = getLatestHumidity();
            args.putString("humidKey", latestHumid.toString());



            args.putString("networkKey", network);


        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                fragment.setArguments(args);
                break;
            case 1:
                fragment = new HumidorFragment();
                title = getString(R.string.title_humidor);
                fragment.setArguments(args);
                break;
            case 2:
                fragment = new NetworkFragment();
                title = getString(R.string.title_network);
                break;
            default:
                break;
        }


        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);

        }
    }

    /**
     * this method will call all the humidor data from the db and populate it in the chart
     */
    public void getHumidorsFromDB(DBHandler dbHandler){

        humidors = dbHandler.getAllHumidors();

    }

    public ArrayList<Humidor> getHumidors(){
        return humidors;
    }

    public Double getLatestTemperature(){
        return humidors.get(humidors.size()-1).getTemperature();
    }

    public Double getLatestHumidity(){
        return humidors.get(humidors.size()-1).getHumidity();
    }

    @Override
    public void sendNetworkData(String networkData, String passwordData, String portData) {
        network = networkData;
        password = passwordData;
        port = portData;

    }

    /**
     * add humidor sent from HumidorFragment fab into humidor arraylist and database
     */
    @Override
    public void sendHumidor(Humidor humidor){
        humidors.add(humidor);
        //maybe have a try catch here to catch database insert errors
        dbHandler.insertHumidor(humidor);

    }
}