package com.example.tabatata.crud;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
        setContentView(R.layout.see_training);
        getSupportActionBar().hide();
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
        Intent intent = new Intent(SeeTraining.this, ModifyTraining.class);
        intent.putExtra("id", id);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
    }

    public void RunTraining(int id) {
        Intent intent = new Intent(SeeTraining.this, Chrono.class);
        intent.putExtra("id", id);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
    }

    private void getTrainings(int id) {
        class GetTrainings extends AsyncTask<Void, Void, Training> {

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

        //////////////////////////
        // IMPORTANT bien penser à executer la demande asynchrone
        // Création d'un objet de type GetTasks et execution de la demande asynchrone
        GetTrainings gt = new GetTrainings();
        gt.execute();
    }

}