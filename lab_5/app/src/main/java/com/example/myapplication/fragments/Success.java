package com.example.myapplication.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Success extends Fragment{
    private RequestQueue mQueue;
    TextView usd;
    TextView eur;
    SharedPreferences sPref1;
    SharedPreferences sPref2;
    final String SAVED_TEXT1 = "saved_text";
    final String SAVED_TEXT2 = "saved_text";
    private SwipeRefreshLayout swipeContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_success, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mQueue = Volley.newRequestQueue(Success.this.getActivity());
        usd = (TextView) view.findViewById(R.id.textView9);
        eur = (TextView) view.findViewById(R.id.textView11);
        usd.setText("  .    ");
        eur.setText("  .    ");
        if(isNetworkConnected()) {
            MyTask mt = new MyTask();
            mt.execute();
            swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (isNetworkConnected()) {
                                usd.setText("  .    ");
                                eur.setText("  .    ");
                                MyTask mt = new MyTask();
                                mt.execute();
                                swipeContainer.setRefreshing(false);
                            } else {
                                Toast toast = Toast.makeText(Success.this.getActivity(),"No internet connection!", Toast.LENGTH_SHORT);
                                toast.show();
                                swipeContainer.setRefreshing(false);
                            }
                        }
                    }, 1000); // Delay in millis
                }
            });
        }
        else {
            sPref1 = this.getActivity().getSharedPreferences("MyPref1", MODE_PRIVATE);
            String savedText1 = sPref1.getString(SAVED_TEXT1, "");
            sPref2 = this.getActivity().getSharedPreferences("MyPref2", MODE_PRIVATE);
            String savedText2 = sPref2.getString(SAVED_TEXT2, "");
            if(savedText1.length() > 0 && savedText2.length()>0) {
                usd.setText(savedText1);
                eur.setText(savedText2);
            }
            Toast toast = Toast.makeText(this.getActivity(), "No internet connection!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
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
                    usd.setText(USD);
                    eur.setText(EUR);
                    sPref1 = Success.this.getActivity().getSharedPreferences("MyPref1", MODE_PRIVATE);
                    SharedPreferences.Editor ed1 = sPref1.edit();
                    ed1.putString(SAVED_TEXT1, usd.getText().toString());
                    ed1.commit();
                    sPref2 = Success.this.getActivity().getSharedPreferences("MyPref2", MODE_PRIVATE);
                    SharedPreferences.Editor ed2 = sPref2.edit();
                    ed2.putString(SAVED_TEXT2, eur.getText().toString());
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