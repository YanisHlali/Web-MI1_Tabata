package com.example.tabatata;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Display;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tabatata.crud.CreateTraining;
import com.example.tabatata.crud.ListTraining;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the Display object associated with the screen
        Display display = getWindowManager().getDefaultDisplay();
        // Recovery of the current screen orientation
        int orientation = display.getRotation();
        if (orientation == 0) {
            setContentView(R.layout.home);
        } else {
            setContentView(R.layout.home_horizontal);
        }
        getSupportActionBar().hide();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Vérifiez si l'écran est en mode paysage ou portrait
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.home_horizontal);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.home);
        }
    }

    public void createTraining(View v) {
        Intent intent = new Intent(this, CreateTraining.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
    }

    public void seeTrainings(View v) {
        Intent intent = new Intent(this, ListTraining.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
    }
}