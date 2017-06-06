package com.example.challenge.weather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.challenge.weather.api.consumer.background.WeatherAPIConsumer;
import com.example.challenge.weather.providers.WeatherReportAdapter;
import com.example.challenge.weather.raw.structures.DailyWeather;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final int PERMISSIONS_REQUEST_LOCATION = 5000;
    public LocationManager mLocationManager;
    WeatherAPIConsumer mWeatherTask;
    ArrayList<DailyWeather> mDailyReport;
    WeatherReportAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWeatherTask = null;
        mLocationManager = null;
        mDailyReport = new ArrayList<>();
        RecyclerView rv = (RecyclerView)findViewById(R.id.recycler_view);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(llm);
        adapter = new WeatherReportAdapter(mDailyReport);
        rv.setAdapter(adapter);


        askForLocationPermission();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void askForLocationPermission(){
        if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_NETWORK_STATE) !=
                        PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.INTERNET) !=
                        PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.INTERNET
                    },
                    PERMISSIONS_REQUEST_LOCATION);
        } else {
            if(mWeatherTask == null){
                mWeatherTask = new WeatherAPIConsumer(this,mDailyReport);
                mWeatherTask.execute();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length == 4
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED
                        && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    if(mWeatherTask == null){
                        mWeatherTask = new WeatherAPIConsumer(this,mDailyReport);
                        mWeatherTask.execute();
                    }
                } else {
                    Toast.makeText(this,
                            "This application requires location and internet access permission to work with. " +
                                    "Please grant permissions and try again.",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {
        if(mLocationManager != null){
            //Only for Real Life tracking.
            //mLocationManager.removeUpdates(this);
            if(mWeatherTask != null){
                mWeatherTask.cancel(true);
                mWeatherTask = null;
            }
        }
        super.onDestroy();
    }

    public void repaintData(){
        adapter.notifyDataSetChanged();
        if(mWeatherTask != null){
            mWeatherTask.cancel(true);
            mWeatherTask = null;
        }
    }

}
