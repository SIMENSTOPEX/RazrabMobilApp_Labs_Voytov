package com.example.myapplication;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.fragments.Fail;
import com.example.myapplication.fragments.Login;
import com.example.myapplication.fragments.SignUp;
import com.example.myapplication.fragments.Success;

public class MainActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.main,new Login());
        ft.commit();
    }
}

