package com.example.janit.waesche;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.github.lzyzsd.circleprogress.DonutProgress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Typeface weatherFont;
    private Handler handler;
    private TextView currentTemperatureField;
    private TextView weatherIcon;
    RelativeLayout wetter;
    RelativeLayout wäsche;
    private PropertyReader assetsPropertyReader;
    private Properties p;
    private int value1;
    private int value2;


    public MainActivity()
    {
        handler = new Handler();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assetsPropertyReader = new PropertyReader(this);
        p = assetsPropertyReader.getProperties("api.properties");





        wetter = (RelativeLayout) findViewById(R.id.wetter);
        wäsche = (RelativeLayout) findViewById(R.id.progress);


        wäsche.setVisibility(View.VISIBLE);
        wetter.setVisibility(View.INVISIBLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DonutProgress donutProgress = (DonutProgress) findViewById(R.id.donut_progress);
        Thermometer thermometer = (Thermometer) findViewById(R.id.thermo);
        final TextView TemperaturView = (TextView) findViewById(R.id.textViewTemperatur);
        currentTemperatureField = (TextView) findViewById(R.id.current_temperature_field);
        weatherIcon = (TextView) findViewById(R.id.weather_icon);
        weatherFont = Typeface.createFromAsset(this.getAssets(), "fonts/weather.ttf");
        weatherIcon.setTypeface(weatherFont);



        setSupportActionBar(toolbar);



        updateWeatherData("Berlin, DE");
        updatePercent();



        donutProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DonutProgress dp = (DonutProgress) v;
                dp.setProgress(value1);





            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Refreshed", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                updateWeatherData("Berlin, DE");
                updatePercent();

                BarChart chart = (BarChart) findViewById(R.id.chart);
                Log.d("chart", chart.toString());

                BarData data = new BarData(getXAxisValues(), getDataSet());
                chart.setData(data);
                chart.setDescription("My Chart");
                chart.animateXY(2000, 2000);
                chart.invalidate();
                TemperaturView.setText(value2 + "°C");



                String tittle = "yay";
                String subject = "Ihre Wäsche";
                String body = "Ist zu " + value1 + "% fertig";

                NotificationManager notif = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notify = new Notification.Builder
                        (getApplicationContext()).setContentTitle(tittle).setContentText(body).
                        setContentTitle(subject).setSmallIcon(R.drawable.ic_menu_camera).build();

                notify.flags |= Notification.FLAG_AUTO_CANCEL;
                notif.notify(0, notify);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(value2, 0);
        valueSet1.add(v1e1);





        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Brand 1");
        barDataSet1.setColor(Color.rgb(0, 155, 0));

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("Temperatur");
        return xAxis;
    }


    private void updateWeatherData(final String city){
      final  Activity activity = this;
        new Thread(){
            public void run(){
                final JSONObject json = WeatherFetch.getJSON(activity, city, p.getProperty("WEATHERAPI"));
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(activity,
                                    activity.getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    private void updatePercent(){
        final  Activity activity = this;
        new Thread(){
            public void run(){
                final JSONObject json = DataFetch.getJSON(activity, p.getProperty("AIRTABLE"));
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(activity,
                                    activity.getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            renderWeather(json);
                        }
                    });
                }
                try {
                    if (json != null) {
                        JSONArray records = json.getJSONArray("records");
                        JSONObject record = records.getJSONObject(records.length() - 1);
                        JSONObject fields = record.getJSONObject("fields");
                        value1 = fields.getInt("Value1");
                        value2 = fields.getInt("Value2");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    private void renderWeather(JSONObject json){
        try {


            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");

            currentTemperatureField.setText(
                    String.format("%.2f", main.getDouble("temp"))+ " ℃");

            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);

        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = this.getString(R.string.weather_sunny);
            } else {
                icon = this.getString(R.string.weather_clear_night);
            }
        } else {
            switch(id) {
                case 2 : icon = this.getString(R.string.weather_thunder);
                    break;
                case 3 : icon = this.getString(R.string.weather_drizzle);
                    break;
                case 7 : icon = this.getString(R.string.wi_day_fog);
                    break;
                case 8 : icon = this.getString(R.string.weather_cloudy);
                    break;
                case 6 : icon = this.getString(R.string.weather_snowy);
                    break;
                case 5 : icon = this.getString(R.string.weather_rainy);
                    break;
                default: icon = this.getString(R.string.wi_alien);
                    break;
            }
        }
        weatherIcon.setText(icon);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            wäsche.setVisibility(View.VISIBLE);
            wetter.setVisibility(View.INVISIBLE);
        } else if (id == R.id.nav_gallery) {
            wäsche.setVisibility(View.INVISIBLE);
            wetter.setVisibility(View.VISIBLE);

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
