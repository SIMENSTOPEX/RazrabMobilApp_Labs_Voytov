package com.example.myapplication.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.fragments.Success;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Service extends JobIntentService {
    private RequestQueue mQueue;
    SharedPreferences sPref1;
    SharedPreferences sPref2;

    // TODO: Rename parameters
    final String SAVED_TEXT1 = "saved_text";
    final String SAVED_TEXT2 = "saved_text";
    final static int job_id = 95;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, Service.class, job_id, intent);
    }

    private boolean isNetworkConnected(){
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        while (true) {
            mQueue = Volley.newRequestQueue(this);
            if (isNetworkConnected()) {
                MyTask mt = new MyTask();
                mt.execute();
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class MyTask extends AsyncTask<Void,Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            ArrayList<String> array = new ArrayList<String>();
            String url = "https://www.cbr-xml-daily.ru/daily_json.js";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    String USD = null;
                    String EUR = null;
                    try {
                        JSONObject jsonObject = response.getJSONObject("Valute");
                        JSONObject jsonObject1 = jsonObject.getJSONObject("USD");
                        USD = jsonObject1.getString("Value");
                        JSONObject jsonObject2 = jsonObject.getJSONObject("EUR");
                        EUR = jsonObject2.getString("Value");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sPref1 = Service.this.getSharedPreferences("MyPref1", MODE_PRIVATE);
                    SharedPreferences.Editor ed1 = sPref1.edit();
                    ed1.putString(SAVED_TEXT1, USD);
                    ed1.commit();
                    sPref2 = Service.this.getSharedPreferences("MyPref2", MODE_PRIVATE);
                    SharedPreferences.Editor ed2 = sPref2.edit();
                    ed2.putString(SAVED_TEXT2, EUR);
                    ed2.commit();
                }
            } ,new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            mQueue.add(request);
            return array;
        }
    }
}