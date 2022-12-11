package com.example.tabatata.crud;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tabatata.R;
import com.example.tabatata.chrono.Chrono;
import com.example.tabatata.db.DatabaseClient;
import com.example.tabatata.db.Training;

public class SeeTraining extends AppCompatActivity {
    private DatabaseClient mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the Display object associated with the screen
        Display display = getWindowManager().getDefaultDisplay();
        // Recovery of the current screen orientation
        int orientation = display.getRotation();
        if (orientation == 0) {
            setContentView(R.layout.see_training);
        } else {
            setContentView(R.layout.see_training_horizontal);
        }
        getSupportActionBar().hide();

        initializeTrainingUI();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Vérifiez si l'écran est en mode paysage ou portrait
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.see_training_horizontal);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.see_training);
        }
        initializeTrainingUI();
    }

    public void initializeTrainingUI() {
        // Retrieve the identifier of the training to be displayed
        int id = (int) getIntent().getSerializableExtra("id");

        // Retrieve the database object
        mDb = DatabaseClient.getInstance(getApplicationContext());

        // Retrieve data from the workout to be displayed
        getTrainings(id);

        // Retrieve "Modify" and "Play" button objects from the user interface
        Button btModify = findViewById(R.id.btModify);
        Button btPlay = findViewById(R.id.btPlay);
        // Define an event listener for the "Modify" button
        btModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifyTraining(id);
            }
        });
        // Define an event listener for the "Play" button
        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RunTraining(id);
            }
        });
    }

    public void ModifyTraining(int id) {
        // Create an Intent to start the training modification activity
        Intent intent = new Intent(SeeTraining.this, ModifyTraining.class);
        // Add the ID of the training to be modified to the Intent
        intent.putExtra("id", id);
        // Empty the activity history before starting the new activity
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Start the training modification activity
        this.startActivity(intent);
    }

    public void RunTraining(int id) {
        // Create an Intent to start the timing activity
        Intent intent = new Intent(SeeTraining.this, Chrono.class);
        // Add the ID of the workout to be timed to the Intent
        intent.putExtra("id", id);
        // Empty the activity history before starting the new activity
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Start the timing activity
        this.startActivity(intent);
    }

    // Method to retrieve the data of a training from its ID
    private void getTrainings(int id) {
        // AsyncTask class to perform data recovery in the background
        class GetTrainings extends AsyncTask<Void, Void, Training> {
            @Override
            protected Training doInBackground(Void... voids) {
                // Retrieve training data from its ID
                Training result = mDb.getAppDatabase()
                        .trainingDao()
                        .getTrainingById(id);
                return result;
            }

            @Override
            protected void onPostExecute(Training training) {
                super.onPostExecute(training);

                // Update text fields with training data
                TextView tvTitlePage = findViewById(R.id.titlePage);
                tvTitlePage.setText(training.getName());
                TextView tvPreparation = findViewById(R.id.valuePreparation);
                tvPreparation.setText(String.valueOf(training.getPreparation()));
                TextView tvSequence = findViewById(R.id.valueSequence);
                tvSequence.setText(String.valueOf(training.getSequence()));
                TextView tvCycle = findViewById(R.id.valueCycle);
                tvCycle.setText(String.valueOf(training.getCycle()));
                TextView tvWork = findViewById(R.id.valueWork);
                tvWork.setText(String.valueOf(training.getWork()));
                TextView tvRest = findViewById(R.id.valueRest);
                tvRest.setText(String.valueOf(training.getRest()));
                TextView tvLongRest = findViewById(R.id.valueLongRest);
                tvLongRest.setText(String.valueOf(training.getLongRest()));
            }
        }
        // Instantiate and execute the AsyncTask class to retrieve the training data
        GetTrainings gt = new GetTrainings();
        gt.execute();
    }

}