package com.example.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.Database;
import com.example.myapplication.R;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends Fragment implements View.OnClickListener {
    Button button;
    Button buttonSignUp;
    EditText editTextTextEmailAddress3;
    EditText editTextTextPassword3;
    Database database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(this);
        buttonSignUp = (Button) view.findViewById(R.id.button2);
        buttonSignUp.setOnClickListener(this);
        editTextTextEmailAddress3 = (EditText) view.findViewById(R.id.editTextTextEmailAddress3);
        editTextTextPassword3 = (EditText) view.findViewById(R.id.editTextTextPassword3);
        database = new Database(this.getActivity());
    }
    public static String convertPassMd5(String pass) {
        String password = null;
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(pass.getBytes(), 0, pass.length());
            StringBuilder passBuilder = new StringBuilder(new BigInteger(1, mdEnc.digest()).toString(16));
            while (passBuilder.length() < 32) {
                passBuilder.insert(0, "0");
            }
            pass = passBuilder.toString();
            password = pass;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return password;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.button:
                if ((editTextTextEmailAddress3.getText().length() > 0) && (editTextTextPassword3.getText().length() > 0)) {
                    int status =Integer.parseInt( database.getLoginData(convertPassMd5(editTextTextEmailAddress3.getText().toString()), convertPassMd5(editTextTextPassword3.getText().toString())));
                    if(status > 0) {
                        Fragment success = new Success();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.main,success);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else {
                        Fragment fail = new Fail();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.main,fail);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }
                break;
            case R.id.button2:
                Fragment signup = new SignUp();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main,signup);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }
}