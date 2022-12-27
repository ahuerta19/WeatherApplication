package com.huertaalexis.visualcrossingweatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Future;


public class MainActivity extends AppCompatActivity {

    private static final String weatherURL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline";
    public static String location = "Chicago, Illinois";
    public static String units = "us";
    private static final String yourAPIKey = "YF575A9ZPABER6ZGXBB98XJYM";

    private final String TAG = getClass().getSimpleName();
    private RequestQueue queue;
    private long start;

    private TextView dateV;
    private ImageView imageV;
    private boolean fahrenheit = true;
    private TextView currentTempView;
    private TextView feelsLikeView;
    private TextView cloudCoverageView;
    private TextView windDirectionView;
    private TextView humidityView;
    private TextView uvIndexView;
    private TextView visibilityView;
    private TextView morningView;
    private TextView afternoonView;
    private TextView eveningView;
    private TextView nightView;
    private TextView sunriseView;
    private TextView sunsetView;
    private TextView t12, t13, t14, t15;

    private ArrayList<Weather> weatherArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private WeatherAdapter mAdapter;
    private SwipeRefreshLayout swiper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherArrayList.clear();
        recyclerView = findViewById(R.id.recycler);
        mAdapter = new WeatherAdapter(weatherArrayList, this);
        recyclerView.setAdapter(mAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));

        t12 = findViewById(R.id.textView12); t13 = findViewById(R.id.textView13); t14 = findViewById(R.id.textView14); t15 = findViewById(R.id.textView15);
        dateV = findViewById(R.id.dateView);
        imageV = findViewById(R.id.imageView);
        currentTempView = findViewById(R.id.currentTemp);
        feelsLikeView = findViewById(R.id.feelsLike);
        cloudCoverageView = findViewById(R.id.cloudCoverage);
        windDirectionView = findViewById(R.id.windDirection);
        humidityView = findViewById(R.id.humidity);
        uvIndexView = findViewById(R.id.uvIndex);
        visibilityView = findViewById(R.id.visibilityDistance);
        morningView = findViewById(R.id.morningBox);
        afternoonView = findViewById(R.id.afternoonBox);
        eveningView = findViewById(R.id.eveningBox);
        nightView = findViewById(R.id.nightBox);
        sunriseView = findViewById(R.id.sunriseTime);
        sunsetView = findViewById(R.id.sunsetTime);
        swiper = findViewById(R.id.visibility);

        queue = Volley.newRequestQueue(this);

        if (hasNetworkConnection()) {
            doDownload();
            swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    doDownload();
                    swiper.setRefreshing(false); // This stops the busy-circle
                }
            });
        }else{
            swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    swiper.setRefreshing(false); // This stops the busy-circle
                }
            });
            dateV.setText("No internet connection");
            t12.setText("");
            t13.setText("");
            t14.setText("");
            t15.setText("");
        }

    }



    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.home_bar, menu);
        return true;
    }


    boolean tog = true;

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(hasNetworkConnection()){

            if (item.getItemId() == R.id.fTemp){
                if(tog){
                    item.setIcon(R.drawable.units_c);
                    fahrenheit = false;
                    f = false;
                    units = "metric";
                    doDownload();
                }else{
                    item.setIcon(R.drawable.units_f);
                    fahrenheit = true;
                    f = true;
                    units = "us";
                    doDownload();
                }
                tog = !tog;
                return true;
            }else if (item.getItemId() == R.id.locationButton){
                LayoutInflater inflater = LayoutInflater.from(this);
                @SuppressLint("InflateParams") final View view = inflater.inflate(R.layout.location_layout, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("For US Locations, enter as  'City', or 'City, State'" + "\n \n" + "For international locations" +
                        " enter as 'City, Country");
                builder.setTitle("Enter a location");

                builder.setView(view);

                builder.setPositiveButton("Ok", (dialog, id) -> {
                    EditText input  = view.findViewById(R.id.locationInput);
                    location = input.getText().toString();
                    doDownload();
                });
                builder.setNegativeButton("Cancel", (dialog, id) -> {

                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }else if (item.getItemId() == R.id.futureForecast) {
                Intent intent = new Intent(MainActivity.this, FutureForecast.class);
                intent.putExtra("OPENING CLASS", MainActivity.class.getSimpleName());
                startActivity(intent);
                return true;
            }else{
                return super.onOptionsItemSelected(item);
            }
        }else{
            Toast.makeText(this, "No Internet Connection! Can not use this function", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public void getWeather(View v) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        doDownload();
    }
    private String formatEpoch(long epoch){
        Date dateTime = new Date(epoch * 1000); // Java time values need milliseconds
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        String timeStr = timeFormat.format(dateTime);
        return timeStr;
    }
    public void updateData(ArrayList<Weather> cList) {
        mAdapter.notifyItemRangeChanged(0, cList.size());
    }
    public static Boolean f = true;
    public static Boolean getTemp(){
        return f;
    }
    public static String getLoc(){
        return location;
    }
    public static String getUnits(){
        return units;
    }

    private String formatDate(long epoch){
        Date dateTime = new Date(epoch * 1000); // Java time values need milliseconds
        SimpleDateFormat fullDate = new SimpleDateFormat("EEE MMM dd h:mm a, yyyy", Locale.getDefault());
        String fullDateStr = fullDate.format(dateTime); // Thu Sep 29 12:00 AM, 2022
        return fullDateStr;
    }
    private String formatH(long epoch){
        Date datetime = new Date(epoch*1000);
        SimpleDateFormat fullDate = new SimpleDateFormat("h:mm a", Locale.getDefault());
        String fullDateStr = fullDate.format(datetime);
        return fullDateStr;
    }

    private int getIcon(String iV){
        int iconID =
                this.getResources().getIdentifier(iV, "drawable", this.getPackageName());
        if (iconID == 0) {
            Log.d(TAG, "parseCurrentRecord: CANNOT FIND ICON " + iV);
        }
        return iconID;
    }

    private void doDownload() {

        //Build URL
        Uri.Builder buildURL = Uri.parse(weatherURL).buildUpon();
        buildURL.appendPath(location);
        buildURL.appendQueryParameter("unitGroup", units);
        buildURL.appendQueryParameter("key", yourAPIKey);
        String urlUsed = buildURL.build().toString();

        //Used to debug URL
        Log.d(TAG, "doDownload: " + urlUsed);

        start = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        int hour24hrs = calendar.get(Calendar.HOUR_OF_DAY);

        Response.Listener<JSONObject> listener =
                new Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            weatherArrayList.clear();
                            setTitle(location);
                            JSONObject weather = response.getJSONObject("currentConditions");

                            JSONObject dayPeriod = response.getJSONArray("days").getJSONObject(0);
                            JSONArray hourArray = dayPeriod.getJSONArray("hours");

                            JSONObject morningP = hourArray.getJSONObject(8);
                            JSONObject afternoonP = hourArray.getJSONObject(13);
                            JSONObject eveningP = hourArray.getJSONObject(17);
                            JSONObject nightP = hourArray.getJSONObject(23);

                            String temp = weather.getString("temp");
                            String feels = weather.getString("feelslike");
                            String cloudCoverage = weather.getString("conditions");
                            String cloudPer = weather.getString("cloudcover");
                            String windDirection = weather.getString("winddir");
                            String windSpeed = weather.getString("windspeed");
                            String windGust = weather.getString("windgust");
                            String humidity = weather.getString("humidity");
                            String uvindex = weather.getString("uvindex");
                            String vis = weather.getString("visibility");
                            String sunR = weather.getString("sunriseEpoch");
                            String sunS = weather.getString("sunsetEpoch");
                            String mV = morningP.getString("temp");
                            String aV = afternoonP.getString("temp");
                            String eV = eveningP.getString("temp");
                            String nV = nightP.getString("temp");
                            String dV = weather.getString("datetimeEpoch");
                            String iV = weather.getString("icon");

                            iV = iV.replace("-","_");


                            Picasso.get().load(getIcon(iV))
                                            .into(imageV);
                            currentTempView.setText(String.format("%.0f° " + (fahrenheit ? "F" : "C"),Double.parseDouble(temp)));
                            feelsLikeView.setText(String.format("Feels Like: " + "%.0f° " + (fahrenheit ? "F" : "C"),Double.parseDouble(feels)));
                            cloudCoverageView.setText(cloudCoverage + " (" + String.format("%.0f", Double.parseDouble(cloudPer)) + "%)");
                            humidityView.setText("Humidity: " + String.format("%.0f",Double.parseDouble(humidity)) + "%");
                            uvIndexView.setText("UV Index: " + String.format("%.0f",Double.parseDouble(uvindex)));
                            visibilityView.setText("Visibility: " + String.format("%.0f",Double.parseDouble(vis)));
                            morningView.setText((String.format("%.0f° " + (fahrenheit ? "F" : "C"),Double.parseDouble(mV))));
                            afternoonView.setText((String.format("%.0f° " + (fahrenheit ? "F" : "C"),Double.parseDouble(aV))));
                            eveningView.setText((String.format("%.0f° " + (fahrenheit ? "F" : "C"),Double.parseDouble(eV))));
                            nightView.setText((String.format("%.0f° " + (fahrenheit ? "F" : "C"),Double.parseDouble(nV))));


                            double convertWind = Double.parseDouble(windDirection);
                            if (windGust == "null"){
                                windDirectionView.setText("Winds: " + getDirections(convertWind) + " at " + String.format("%.0f", Double.parseDouble(windSpeed)) + (fahrenheit ? " mph with no wind gusts." : " km/h with no wind gusts"));
                            }else {
                                windDirectionView.setText("Winds: " + getDirections(convertWind) + " at " + String.format("%.0f", Double.parseDouble(windSpeed)) + (fahrenheit ? " mph with winds gusting to " : " km/h with winds gusting to ") + String.format("%.0f", Double.parseDouble(windGust)) + (fahrenheit ? " mph" : " km/h"));
                            }
                            long convertSun = Long.parseLong(sunR);
                            long covertSet = Long.parseLong(sunS);
                            long convertDate = Long.parseLong(dV);
                            sunriseView.setText("Sunrise: " + formatEpoch(convertSun));
                            sunsetView.setText("Sunset: " + formatEpoch(covertSet));
                            dateV.setText(formatDate(convertDate));

                            JSONObject forecastP = response.getJSONArray("days").getJSONObject(0);
                            JSONArray hourForecast = forecastP.getJSONArray("hours");

                            for(int i=hour24hrs+1; i<=23; i++){
                                JSONObject hourF = hourForecast.getJSONObject(i);
                                //String day = hourF.getString("datetimeEpoch");
                                String day = "Today";
                                String time = hourF.getString("datetimeEpoch");
                                String ic = hourF.getString("icon");
                                String tmp = hourF.getString("temp");
                                String cDesc = hourF.getString("conditions");

                                String icon = ic.replace("-","_");
                                int newIcon = getIcon(icon);

                                weatherArrayList.add(new Weather(day,time,newIcon,tmp,cDesc));
                            }
                            for(int i = 1; i<=3; i++){
                                JSONObject forecastP2 = response.getJSONArray("days").getJSONObject(i);
                                JSONArray hourForecast2 = forecastP2.getJSONArray("hours");
                                for(int j = 0; j<=23; j++){
                                    JSONObject hourF2 = hourForecast2.getJSONObject(j);
                                    String day = hourF2.getString("datetimeEpoch");
                                    String time = hourF2.getString("datetimeEpoch");
                                    String ic = hourF2.getString("icon");
                                    String tmp = hourF2.getString("temp");
                                    String cDesc = hourF2.getString("conditions");

                                    String icon = ic.replace("-","_");
                                    int newIcon = getIcon(icon);

                                    weatherArrayList.add(new Weather(day,time,newIcon,tmp,cDesc));
                                }
                            }
                            updateData(weatherArrayList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                };
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                    setTitle("Duration: " + (System.currentTimeMillis() - start));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlUsed,
                        null, listener, error);
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

    }


    private String getDirections(double degrees){
        if (degrees >= 337.5 || degrees < 22.5)
            return "N";
        if (degrees >= 22.5 && degrees < 67.5)
            return "NE";
        if (degrees >= 67.5 && degrees < 112.5)
            return "E";
        if (degrees >= 112.5 && degrees < 157.5)
            return "SE";
        if (degrees >= 157.5 && degrees < 202.5)
            return "S";
        if (degrees >= 202.5 && degrees < 247.5)
            return "SW";
        if (degrees >= 247.5 && degrees < 292.5)
            return "W";
        if (degrees >= 292.5 && degrees < 337.5)
            return "NW";
        return "X"; // We'll use 'X' as the default if we get a bad value
    }
}