package com.huertaalexis.visualcrossingweatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

public class FutureForecast extends AppCompatActivity {

    private static final String weatherURL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline";
    public static String location = MainActivity.getLoc();
    public static String units = MainActivity.getUnits();
    private static final String yourAPIKey = "YF575A9ZPABER6ZGXBB98XJYM";

    private final String TAG = getClass().getSimpleName();
    private RequestQueue queue;
    private long start;

    private ArrayList<Forecast> forecastArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FutureAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future_forecast);

        queue = Volley.newRequestQueue(this);

        recyclerView = findViewById(R.id.fRecycler);
        mAdapter = new FutureAdapter(forecastArrayList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        units = MainActivity.getUnits();
        location = MainActivity.getLoc();
        doDownload();
    }
    @Override
    protected void onPause() {
        forecastArrayList.clear();
        super.onPause();
    }
    @Override
    protected void onResume(){
        units = MainActivity.getUnits();
        location = MainActivity.getLoc();
        doDownload();
        super.onResume();
    }

    private int getIcon(String iV){
        int iconID =
                this.getResources().getIdentifier(iV, "drawable", this.getPackageName());
        if (iconID == 0) {
            Log.d(TAG, "parseCurrentRecord: CANNOT FIND ICON " + iV);
        }
        return iconID;
    }
    public void updateData(ArrayList<Forecast> cList){
        mAdapter.notifyItemRangeChanged(0, cList.size());
    }

    private void doDownload() {
        forecastArrayList.clear();
        setTitle(location + " 15 Day");
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

                            for(int i=0; i<=14; i++){
                                JSONObject forcastEnt = response.getJSONArray("days").getJSONObject(i);
                                JSONArray forcastHour = forcastEnt.getJSONArray("hours");
                                JSONObject morningP = forcastHour.getJSONObject(8);
                                JSONObject afternoonP = forcastHour.getJSONObject(13);
                                JSONObject eveningP = forcastHour.getJSONObject(17);
                                JSONObject nightP = forcastHour.getJSONObject(23);

                                String date = forcastEnt.getString("datetimeEpoch");
                                String tempmax = forcastEnt.getString("tempmax");
                                String tempmin = forcastEnt.getString("tempmin");
                                String tempMaxFor = String.format("%.0f°" + (MainActivity.getTemp() ? "F" : "C"),Double.parseDouble(tempmax));
                                String tempMinFor = String.format("%.0f°" + (MainActivity.getTemp() ? "F" : "C"),Double.parseDouble(tempmin));
                                String temps = String.format(tempMaxFor + "/" + tempMinFor);
                                String desc = forcastEnt.getString("description");
                                String cond = forcastEnt.getString("precipprob");
                                String uv = forcastEnt.getString("uvindex");
                                String ic = forcastEnt.getString("icon");
                                String mP = morningP.getString("temp");
                                String aP = afternoonP.getString("temp");
                                String eP = eveningP.getString("temp");
                                String nP = nightP.getString("temp");

                                String icon = ic.replace("-","_");
                                int newIcon = getIcon(icon);

                                forecastArrayList.add(new Forecast(date, temps, newIcon, desc, cond, uv, mP, aP, eP, nP));
                            }
                            updateData(forecastArrayList);
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


}