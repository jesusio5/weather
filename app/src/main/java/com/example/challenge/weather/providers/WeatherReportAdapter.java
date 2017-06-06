package com.example.challenge.weather.providers;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.challenge.weather.R;
import com.example.challenge.weather.raw.structures.DailyWeather;

import java.util.List;


public class WeatherReportAdapter extends RecyclerView.Adapter<WeatherReportAdapter.WeatherViewHolder> {

    List<DailyWeather> dailys;

    public WeatherReportAdapter(List<DailyWeather> dailys){
        this.dailys = dailys;
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weather_cardview, viewGroup, false);
        WeatherViewHolder pvh = new WeatherViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder weatherViewHolder, int i) {
        weatherViewHolder.txt_sealevel.setText("sl: " + dailys.get(i).getmSeaLevel());
        weatherViewHolder.txt_temperature.setText(dailys.get(i).getmTempMin()
                + "/" + dailys.get(i).getmTempMax());
        weatherViewHolder.txt_date.setText(dailys.get(i).getmTimeStampString());
        weatherViewHolder.txt_summary.setText(dailys.get(i).getmSummary());
        String preciProb = dailys.get(i).getmPrecipProb();
        if(preciProb.equals("")){
            preciProb = "0";
        }
        String cloudy = dailys.get(i).getmClouds();
        if(cloudy.equals("")){
            cloudy = "0";
        }
        if(Double.parseDouble(preciProb) > 50){
            weatherViewHolder.img_weather_photo.setImageResource(R.drawable.rainy);
        } else if(Double.parseDouble(cloudy) > 30){
            weatherViewHolder.img_weather_photo.setImageResource(R.drawable.cloudy);
        } else {
            weatherViewHolder.img_weather_photo.setImageResource(R.drawable.sunny);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return dailys.size();
    }

    public static class WeatherViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView img_weather_photo;
        TextView txt_sealevel;
        TextView txt_temperature;
        TextView txt_date;
        TextView txt_summary;

        WeatherViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cardview_id);
            img_weather_photo = (ImageView)itemView.findViewById(R.id.cardview_weather_photo);
            txt_sealevel = (TextView)itemView.findViewById(R.id.cardview_sea_level);
            txt_temperature = (TextView)itemView.findViewById(R.id.cardview_temperature);
            txt_date = (TextView)itemView.findViewById(R.id.cardview_date);
            txt_summary = (TextView)itemView.findViewById(R.id.cardview_summary);
        }
    }

}
