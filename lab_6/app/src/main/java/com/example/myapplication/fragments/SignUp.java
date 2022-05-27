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
import android.widget.Toast;

import com.example.myapplication.Database;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignUp extends Fragment implements View.OnClickListener{
    Button button3;
    EditText editTextTextEmailAddress;
    EditText editTextTextPassword;
    Database database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button3 = (Button) view.findViewById(R.id.button3);
        button3.setOnClickListener(this);
        editTextTextEmailAddress = (EditText) view.findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword = (EditText) view.findViewById(R.id.editTextTextPassword);
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
    public void onClick(View view) {
        if ((editTextTextEmailAddress.getText().length() > 0) && (editTextTextPassword.getText().length() > 0)) {
            boolean status = database.addUser(convertPassMd5(editTextTextEmailAddress.getText().toString()),convertPassMd5(editTextTextPassword.getText().toString()));
            if(status) {
                Fragment login = new Login();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main,login);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        } else {
            Toast.makeText(this.getActivity(),"Please Insert login and password", Toast.LENGTH_LONG).show();
        }
    }
}