package com.example.tabatata;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SeeTraining extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_training);
        getSupportActionBar().hide();
    }
}