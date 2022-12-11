package com.example.tabatata.chrono;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.sip.SipManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tabatata.R;
import com.example.tabatata.db.DatabaseClient;
import com.example.tabatata.db.Training;

import java.util.ArrayList;

public abstract class Chrono extends AppCompatActivity implements OnUpdateListener {

    // VIEW
    private TextView timerValue;

    public SipManager manager = null;

    // DATA
    private Compteur compteur;
    private DatabaseClient mDb;
    private boolean isPaused = true;
    // Getter and setter chronoValue
    private int chronoValue;
    private int getChronoValue() { return this.chronoValue; }
    private void setChronoValue(int i) { this.chronoValue = i; }
    // Getter and setter index
    private int index;
    private int getIndex() { return this.index; }
    private void setIndex(int i) { this.index = i; }

    // This method is called when the activity is first created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call the onCreate() method of the superclass
        super.onCreate(savedInstanceState);

        // Hide the action bar at the top of the screen
        getSupportActionBar().hide();

        // Retrieve the Display object associated with the screen
        Display display = getWindowManager().getDefaultDisplay();
        // Recovery of the current screen orientation
        int orientation = display.getRotation();
        if (orientation == 0) {
            setContentView(R.layout.run_training);
        } else {
            setContentView(R.layout.run_training_horizontal);
        }

        initializeTrainingUI();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Check if the screen is in landscape or portrait mode
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setChronoValue((int) compteur.getCurrentTime());
            setIndex(compteur.getIndexArrayTraining());
            setContentView(R.layout.run_training_horizontal);
            rotationScreen();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setChronoValue((int) compteur.getCurrentTime());
            setIndex(compteur.getIndexArrayTraining());
            setContentView(R.layout.run_training);
            rotationScreen();
        }
        initializeTrainingUI();
    }

    public void initializeTrainingUI() {
        // Get a reference to the valueChrono TextView in the layout
        timerValue = (TextView) findViewById(R.id.valueChrono);

        // Get an instance of the DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext());

        // Create a Compteur object and pass it the current activity
        compteur = new Compteur(this);

        // Set the current activity as a listener for updates to the Compteur object
        compteur.addOnUpdateListener(this);

        update();
    }

    public void rotationScreen() {
        runTraining(getCurrentFocus());
        // Get a reference to the description TextView in the layout
        TextView tvDescription = findViewById(R.id.description);
        // Set the text of the description TextView to the description of the first training feature
        tvDescription.setTextColor(Color.WHITE);

        // Get a reference to the valueChrono TextView in the layout
        TextView tvChrono = findViewById(R.id.valueChrono);
        tvChrono.setTextColor(Color.WHITE);

        changeImageButton("play");
    }

    // This method is called when the runTraining button is clicked
    public void runTraining(View view) {
        // This class extends the AsyncTask class and is used to retrieve a Training object from the database
        class RunTraining extends AsyncTask<Void, Void, Training> {
            // Get the ID of the training to retrieve from the Intent
            int id = (int) getIntent().getSerializableExtra("id");

            // This method runs in the background and retrieves the Training object with the specified ID
            @Override
            protected Training doInBackground(Void... voids) {
                // Get the Training object with the specified ID
                Training result = mDb.getAppDatabase()
                        .trainingDao()
                        .getTrainingById(id);
                // Return the Training object
                return result;
            }

            // This method is called when the Training object is retrieved
            @Override
            protected void onPostExecute(Training training) {
                // Call the onPostExecute() method of the superclass
                super.onPostExecute(training);

                // Get the values of the training features from the Training object
                int preparation = training.getPreparation();
                int sequence = training.getSequence();
                int work = training.getWork();
                int cycle = training.getCycle();
                int rest = training.getRest();
                int longRest = training.getLongRest();

                // Create two ArrayLists to store the training feature values and their corresponding descriptions
                ArrayList<Integer> trainingFeature = new ArrayList<>();
                ArrayList<String> trainingFeatureField = new ArrayList<>();

                // Add the preparation value and its corresponding description to the ArrayLists
                trainingFeature.add(preparation);
                trainingFeatureField.add("Préparation");
                // Loop through the number of sequences
                for (int i = 0; i < sequence; i++) {
                    for (int j = 0; j < cycle; j++) {
                        // Add the work value and its corresponding description to the ArrayLists
                        trainingFeature.add(work);
                        trainingFeatureField.add("Travail");
                        // Add the rest value and its corresponding description to the ArrayLists
                        trainingFeature.add(rest);
                        trainingFeatureField.add("Repos");
                    }
                    trainingFeature.add(longRest);
                    trainingFeatureField.add("Repos long");
                }

                // Set the training feature values and their corresponding descriptions in the Compteur object
                compteur.setArrayTraining(trainingFeature);
                if (getIndex() > 0) {
                    compteur.setIndexArrayTraining(getIndex());
                }
                else {
                    setIndex(0);
                }
                compteur.setIndexArrayTraining(getIndex());
                // Set the initial index of the Compteur object to 0 (the first training feature)
                compteur.setArrayTrainingField(trainingFeatureField);

                // Get a reference to the description TextView in the layout
                TextView tvDescription = findViewById(R.id.description);
                // Set the text of the description TextView to the description of the first training feature
                tvDescription.setText(trainingFeatureField.get(getIndex()));
                tvDescription.setTextColor(Color.WHITE);

                // Get a reference to the valueChrono TextView in the layout
                TextView tvChrono = findViewById(R.id.valueChrono);
                tvChrono.setTextColor(Color.WHITE);

                if (getChronoValue() > 0) {
                    compteur.setCurrentTime(getChronoValue());
                }

                compteur.startTimer();
            }
        }
        // Creation of a RunTraining object and execution of the asynchronous request
        RunTraining gt = new RunTraining();
        gt.execute();
    }


    // This method is called when the onStart button is clicked
    public void onStart(View view) {
        // If the timer is currently paused, set isPaused to false
        if (isPaused) isPaused = false;
        // Otherwise, set isPaused to true
        else isPaused = true;

        // If the current time of the Compteur object is 0 (the timer has not started yet)
        if (compteur.getCurrentTime() == 0) {
            // Start the training by calling the runTraining() method
            runTraining(view);
            // Change the image of the onStart button to the play image
            changeImageButton("play");
        }
        // Otherwise, if the timer is currently paused
        else {
            if (isPaused) {
                // Start the timer
                compteur.startTimer();
                // Change the image of the onStart button to the play image
                changeImageButton("play");
            }
            // Otherwise (the timer is currently running)
            else {
                // Pause the timer
                pause();
                // Change the image of the onStart button to the pause image
                changeImageButton("pause");
            }
        }
    }

    // This method is used to pause the timer
    public void pause() {
        // Pause the timer in the Compteur object
        compteur.pause();

        // Get a reference to the valueChrono TextView in the layout
        TextView tvChrono = findViewById(R.id.valueChrono);
        // Split the text of the valueChrono TextView by the ":" character
        String[] times = tvChrono.getText().toString().split(":");
        // Convert the minutes and seconds strings to floats and add them together
        float time = Float.parseFloat(times[0]) + Float.parseFloat(times[1]);
        // Set the current time in the Compteur object to the time in milliseconds
        compteur.setCurrentTime(time * 1000);
    }

    public void getCurrentChrono() {
        // Get a reference to the valueChrono TextView in the layout
        TextView tvChrono = findViewById(R.id.valueChrono);
        // Split the text of the valueChrono TextView by the ":" character
        String[] times = tvChrono.getText().toString().split(":");
        // Convert the minutes and seconds strings to floats and add them together
        float time = Float.parseFloat(times[0]) + Float.parseFloat(times[1]);
        // Set the current time in the Compteur object to the time in milliseconds
        compteur.setCurrentTime(time * 1000);
    }

    // This method is used to change the image of the playButton ImageButton
    public void changeImageButton(String status) {
        // Get a reference to the playButton ImageButton in the layout
        ImageButton playButton = findViewById(R.id.playButton);
        // Create a Drawable object to hold the new image
        Drawable drawable;
        // If the status is "pause"
        if (status == "pause") {
            // Set the drawable to the play_button image
            drawable = getResources().getDrawable(R.drawable.play_button);
        }
        // Otherwise the status is not "pause"
        else {
            // Set the drawable to the pause_button image
            drawable = getResources().getDrawable(R.drawable.pause_button);
        }
        // Set the image of the playButton ImageButton to the drawable
        playButton.setImageDrawable(drawable);
    }


    // This method is called when the onReset button is clicked
    public void onReset(View view) {
        compteur.reset();
    }


    private void update() {
        String minute = String.valueOf(compteur.getMinutes());;
        if (compteur.getMinutes() < 10) {
            minute = "0" + minute;
        }
        String miliseconds = String.valueOf(compteur.getMillisecondes());
        String[] milis = miliseconds.split("");

        timerValue.setText("" + minute + ":"
                + String.format("%02d", compteur.getSecondes()));

    }

    @Override
    public void onUpdate() {
        update();
    }
}
