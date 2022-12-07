package com.example.tabatata.chrono;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tabatata.R;
import com.example.tabatata.db.DatabaseClient;
import com.example.tabatata.db.Training;

import java.util.ArrayList;

public class Chrono extends AppCompatActivity implements OnUpdateListener {

    // VIEW
    private TextView timerValue;

    // DATA
    private Compteur compteur;
    private DatabaseClient mDb;
    private boolean isPaused;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.run_training);
        getSupportActionBar().hide();

        // Get view
        timerValue = (TextView) findViewById(R.id.valueChrono);
        // Get database
        mDb = DatabaseClient.getInstance(getApplicationContext());

        // Initialize the Counter object
        compteur = new Compteur(this);

        // Subscribe the activity to the counter to "follow" the events
        compteur.addOnUpdateListener(this);

        // Graphic update
        miseAJour();
    }

    public void runTraining(View view) {
        class RunTraining extends AsyncTask<Void, Void, Training> {

            int id = (int) getIntent().getSerializableExtra("id");
            @Override
            protected Training doInBackground(Void... voids) {
                Training result = mDb.getAppDatabase()
                        .trainingDao()
                        .getTrainingById(id);
                return result;
            }

            @Override
            protected void onPostExecute(Training training) {
                super.onPostExecute(training);

                int preparation = training.getPreparation();
                int sequence = training.getSequence();
                int work = training.getWork();
                int cycle = training.getCycle();
                int rest = training.getRest();
                int longRest = training.getLongRest();

                ArrayList<Integer> trainingFeature = new ArrayList<>();
                ArrayList<String> trainingFeatureField = new ArrayList<>();

                trainingFeature.add(preparation);
                trainingFeatureField.add("Préparation");
                for (int i = 0; i < sequence; i++) {
                    trainingFeature.add(work);
                    trainingFeatureField.add("Travail");
                    trainingFeature.add(rest);
                    trainingFeatureField.add("Repos");
                }
                trainingFeature.add(sequence);
                trainingFeatureField.add("Séquence");


                compteur.setArrayTraining(trainingFeature);
                compteur.setIndexArrayTraining(0);
                compteur.setArrayTrainingField(trainingFeatureField);
                // compteur.setCurrentTime(compteur.getArrayTraining().get(compteur.getIndexArrayTraining()) * 1000);

                TextView tvDescription = findViewById(R.id.description);
                tvDescription.setText(trainingFeatureField.get(0));
                tvDescription.setTextColor(Color.WHITE);

                TextView tvChrono = findViewById(R.id.valueChrono);
                tvChrono.setTextColor(Color.WHITE);

                compteur.startTimer();
            }
        }

        RunTraining gt = new RunTraining();
        gt.execute();
    }


    // Lancer le compteur
    public void onStart(View view) {
        // Change isPaused status
        if (isPaused) isPaused = false;
        else isPaused = true;
        // If the button is press for the first time ...
        if (compteur.getCurrentTime() == 0) {
            // ... the training is launched
            runTraining(view);
            changeImageButton("play");
        } else { // ... otherwise the counter pauses or continues
            if (isPaused) {
                compteur.startTimer();
                changeImageButton("play");
            } else {
                pause();
                changeImageButton("pause");
            }
        }
    }

    public void pause() {
        compteur.pause();
        // Save time
        TextView tvChrono = findViewById(R.id.valueChrono);
        String[] times = tvChrono.getText().toString().split(":");
        float time = Float.parseFloat(times[0]) + Float.parseFloat(times[1]);
        compteur.setCurrentTime(time * 1000);
    }

    public void changeImageButton(String status) {
        ImageButton playButton = findViewById(R.id.playButton);
        Drawable drawable;
        if (status == "pause") {
            drawable = getResources().getDrawable(R.drawable.play_button);
        } else {
            drawable = getResources().getDrawable(R.drawable.pause_button);
        }
        playButton.setImageDrawable(drawable);
    }


    // Remettre à zéro le compteur
    public void onReset(View view) {
        compteur.reset();
    }

    // Mise à jour graphique
    private void miseAJour() {
        String minute = String.valueOf(compteur.getMinutes());;
        if (compteur.getMinutes() < 10) {
            minute = "0" + minute;
        }
        String miliseconds = String.valueOf(compteur.getMillisecondes());
        String[] milis = miliseconds.split("");
        // Affichage des informations du compteur
        timerValue.setText("" + minute + ":"
                + String.format("%02d", compteur.getSecondes()));

    }

    /**
     * Méthode appelée à chaque update du compteur (l'activité est abonnée au compteur)
     *
     */
    @Override
    public void onUpdate() {
        miseAJour();
    }
}
