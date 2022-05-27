package com.example.myapplication.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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

import com.example.myapplication.R;
import com.example.myapplication.service.Service;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Success extends Fragment{
    TextView usd;
    TextView eur;
    SharedPreferences sPref1;
    SharedPreferences sPref2;
    final String SAVED_TEXT1 = "saved_text";
    final String SAVED_TEXT2 = "saved_text";
    private SwipeRefreshLayout swipeContainer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_success, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usd = (TextView) view.findViewById(R.id.textView9);
        eur = (TextView) view.findViewById(R.id.textView11);
        usd.setText("  .    ");
        eur.setText("  .    ");
        if(isNetworkConnected()) {
            sPref1 = Success.this.getContext().getSharedPreferences("MyPref1", MODE_PRIVATE);
            String savedText1 = sPref1.getString(SAVED_TEXT1, "");
            sPref2 = Success.this.getContext().getSharedPreferences("MyPref2", MODE_PRIVATE);
            String savedText2 = sPref2.getString(SAVED_TEXT2, "");
            if (savedText1.length() > 0 && savedText2.length() > 0) {
                usd.setText(savedText1);
                eur.setText(savedText2);
            }
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
                                Intent mIntent = new Intent(Success.this.getContext(), Service.class);
                                Service.enqueueWork(Success.this.getContext(), mIntent);
                                sPref1 = Success.this.getContext().getSharedPreferences("MyPref1", MODE_PRIVATE);
                                String savedText1 = sPref1.getString(SAVED_TEXT1, "");
                                sPref2 = Success.this.getContext().getSharedPreferences("MyPref2", MODE_PRIVATE);
                                String savedText2 = sPref2.getString(SAVED_TEXT2, "");
                                if (savedText1.length() > 0 && savedText2.length() > 0) {
                                    usd.setText(savedText1);
                                    eur.setText(savedText2);
                                }
                                swipeContainer.setRefreshing(false);
                            } else {
                                Toast.makeText(Success.this.getActivity(), "No internet connection!", Toast.LENGTH_SHORT).show();
                                swipeContainer.setRefreshing(false);
                            }
                        }
                    }, 1000); // Delay in millis
                }
            });
        } else {
            sPref1 = Success.this.getContext().getSharedPreferences("MyPref1", MODE_PRIVATE);
            String savedText1 = sPref1.getString(SAVED_TEXT1, "");
            sPref2 = Success.this.getContext().getSharedPreferences("MyPref2", MODE_PRIVATE);
            String savedText2 = sPref2.getString(SAVED_TEXT2, "");
            if (savedText1.length() > 0 && savedText2.length() > 0) {
                usd.setText(savedText1);
                eur.setText(savedText2);
            }
            Toast.makeText(Success.this.getActivity(), "No internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}