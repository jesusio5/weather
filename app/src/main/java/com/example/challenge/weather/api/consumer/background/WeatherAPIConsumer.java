package com.example.challenge.weather.api.consumer.background;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.challenge.weather.MainActivity;
import com.example.challenge.weather.raw.structures.DailyWeather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class WeatherAPIConsumer extends AsyncTask<Void, Void, Void> {

    private final String DARK_SKY_API = "https://api.darksky.net/forecast/";
    private final String OPEN_WEATHER_API = "http://api.openweathermap.org/data/2.5/forecast?";
    private final String DARK_SKY_TOKEN = "203bf0976335ed98863b556ed9f61f79";
    private final String OPEN_WEATHER_TOKEN = "1d3c57e69831d18188f531e5a0a32c6c";
    private final WeakReference<ArrayList<DailyWeather>> WEAKLYREPORT;
    private final WeakReference<Context> mContext;
    ProgressDialog proDialog;

    public WeatherAPIConsumer(Context mContext, ArrayList<DailyWeather> weaklyReport) {
        this.WEAKLYREPORT = new WeakReference<>(weaklyReport);
        this.mContext = new WeakReference<>(mContext);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        proDialog = new ProgressDialog(mContext.get());
        proDialog.setMessage("Please wait...");
        proDialog.setCancelable(false);
        proDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        if(!isOnline()){
            Toast.makeText(mContext.get(),"You do not have Internet conection, " +
                    "please connect to Internet to proceed.",Toast.LENGTH_LONG).show();
            return null;
        }
        URL darkSkyUrl = null;
        URL openWeatherUrl = null;
        try {
            String [] location = getLocation_LatLon();
            if(location == null){
                Toast.makeText(mContext.get(),"Couldn't find any location.",Toast.LENGTH_LONG).show();
                return null;
            }
            String darkSkyUri = DARK_SKY_API + DARK_SKY_TOKEN + "/" + location[0] + "," + location[1];
            //System.out.println("TOKEN MAGICO " + darkSkyUri);
            String openWeatherUri =
                    OPEN_WEATHER_API +
                    "apikey=" + OPEN_WEATHER_TOKEN + "&" +
                    "lat=" + location[0] + "&lon=" + location[1];
            //System.out.println("TOKEN MAGICO " + openWeatherUri);
            darkSkyUrl = new URL(darkSkyUri);
            openWeatherUrl = new URL(openWeatherUri);
            HttpsURLConnection darkSkyConn = (HttpsURLConnection) darkSkyUrl.openConnection();
            int responseCode = darkSkyConn.getResponseCode();
            String inputLine;
            String resultDarkSky = "";
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                //System.out.println("TOKEN MAGICO FIRST RESPONSE " + responseCode);
                BufferedReader in = new BufferedReader(new InputStreamReader(darkSkyConn.getInputStream()));
                while ((inputLine = in.readLine()) != null) {
                    resultDarkSky += inputLine;
                }
                //System.out.println("TOKEN MAGICO FIRST RESULT " + resultDarkSky);
            }// else {
                //System.out.println("TOKEN MAGICO RESULT FIRST CODE " + responseCode);
            //}

            HttpURLConnection openWeatherConn = (HttpURLConnection) openWeatherUrl.openConnection();
            responseCode = openWeatherConn.getResponseCode();
            String resultOpenWeather = "";
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //System.out.println("TOKEN MAGICO SECOND RESPONSE " + responseCode);
                BufferedReader in = new BufferedReader(new InputStreamReader(openWeatherConn.getInputStream()));
                while ((inputLine = in.readLine()) != null) {
                    resultOpenWeather += inputLine;
                }
                //System.out.println("TOKEN MAGICO SECOND RESULT " + resultOpenWeather);
                WEAKLYREPORT.get().clear();
                WEAKLYREPORT.get().addAll(
                        parseIntoRecycler(resultDarkSky,resultOpenWeather)
                );
            }// else {
                //System.out.println("TOKEN MAGICO SECOND SECOND CODE " + responseCode);
            //}

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if (proDialog.isShowing()) {
            proDialog.dismiss();
        }
        ((MainActivity) mContext.get()).repaintData();
    }

    private String[] getLocation_LatLon() throws SecurityException {
        ((MainActivity) mContext.get()).mLocationManager =
                (LocationManager) mContext.get()
                        .getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = ((MainActivity) mContext.get())
                .mLocationManager.getProviders(true);
        Location location = null;
        for (String provider : providers) {
            Location l = ((MainActivity) mContext.get())
                    .mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            location = l;
            break;
        }
        //TODO add listener for real life tracking
        String [] out = null;
        if(location != null){
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            out = new String[] {""+latitude,""+longitude};
        }
        return out;
    }

    public boolean isOnline() {
        ConnectivityManager connManager = (ConnectivityManager) (mContext.get()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        return false;
    }

    public ArrayList<DailyWeather> parseIntoRecycler(String jsonDarkSky, String jsonOpenWeather){
        ArrayList<DailyWeather> weakReport = new ArrayList<>();
        JSONObject rawObject;
        String timeStamp = "";
        String summary = "";
        String precipProb = "";
        String tempMin = "";
        String tempMax = "";
        try {
            rawObject = new JSONObject(jsonDarkSky);
            JSONObject DAILY = rawObject.getJSONObject("daily");
            JSONArray DATA = DAILY.getJSONArray("data");
            for (int i = 0; i < 7; i++) {
                JSONObject dailyjson = DATA.getJSONObject(i);
                try{timeStamp = ""+dailyjson.getInt("time");}catch(JSONException e){e.printStackTrace();timeStamp = "";}
                try{summary = dailyjson.getString("summary");}catch(JSONException e){e.printStackTrace();summary = "";}
                try{precipProb = dailyjson.getString("precipProbability");}catch(JSONException e){e.printStackTrace();precipProb = "";}
                try{tempMin = dailyjson.getString("temperatureMin");}catch(JSONException e){e.printStackTrace();tempMin = "";}
                try{tempMax = dailyjson.getString("temperatureMax");}catch(JSONException e){e.printStackTrace();tempMax = "";}
                weakReport.add(
                        new DailyWeather(
                                timeStamp,summary,precipProb,
                                tempMin,tempMax,"","","",""
                                )
                );
            }
        } catch(JSONException e){
            for (int i = 0; i < 7; i++) {
                weakReport.add( new DailyWeather() );
            }
            e.printStackTrace();
            Log.e(this.getClass().getSimpleName(),
                    "Something went wrong with the returned json tried to parse. " + jsonDarkSky);
        }
        try {
            rawObject = new JSONObject(jsonOpenWeather);
            JSONArray LIST = rawObject.getJSONArray("list");
            int refreshed = 0;
            long timestampreviewer = 0;
            try{
                timestampreviewer = Long.parseLong(weakReport.get(0).getmTimeStamp());
            } catch(NumberFormatException nfe){
                return weakReport;
            }
            for (int i = 0; i < LIST.length() && refreshed < 7; i++) {
                JSONObject dailyjson = LIST.getJSONObject(i);
                int timestampOpenWeather = dailyjson.getInt("dt");
                if(timestampOpenWeather < timestampreviewer){
                    continue;
                }
                timestampreviewer += (60*60*24);
                try{
                    JSONObject main = dailyjson.getJSONObject("main");
                    weakReport.get(refreshed).setmSeaLevel(""+main.getInt("sea_level"));
                }catch(JSONException e){e.printStackTrace();}
                try{
                    JSONObject wind = dailyjson.getJSONObject("wind");
                    weakReport.get(refreshed).setmWindSpeed(""+wind.getInt("speed"));
                }catch(JSONException e){e.printStackTrace();}
                try{
                    weakReport.get(refreshed).setmTimeStampString(
                            dailyjson.getString("dt_txt").substring(0,
                                    dailyjson.getString("dt_txt").indexOf(' '))
                    );
                }catch(JSONException e){e.printStackTrace();}
                try{
                    JSONObject clouds = dailyjson.getJSONObject("clouds");
                    weakReport.get(refreshed).setmClouds(""+clouds.getInt("all"));
                }catch(JSONException e){e.printStackTrace();}
                refreshed++;
            }
        } catch(JSONException e){
            e.printStackTrace();
            Log.e(this.getClass().getSimpleName(),
                    "Something went wrong with the returned json tried to parse. " + jsonOpenWeather);
        }
        return weakReport;
    }

}
