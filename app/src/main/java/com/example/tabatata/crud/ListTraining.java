package com.example.tabatata.crud;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tabatata.R;
import com.example.tabatata.TrainingsAdapter;
import com.example.tabatata.db.DatabaseClient;
import com.example.tabatata.db.Training;

import java.util.ArrayList;
import java.util.List;

public class ListTraining extends AppCompatActivity {

    // DATA
    private DatabaseClient mDb;
    private TrainingsAdapter adapter;

    // VIEW
    private ListView listTraining;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_training);
        getSupportActionBar().hide();

        // Get DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext());

        // Get views
        listTraining = findViewById(R.id.list_training);

        //  Link the adapter to the listView
        adapter = new TrainingsAdapter(this, new ArrayList<Training>());
        listTraining.setAdapter(adapter);

        // Add a click event to the listView
        listTraining.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Recovery of the clicked task using the adapter
                Training training = adapter.getItem(position);
                seeTraining(training.getId());
            }
        });
    }

    private void getTrainings() {
        class GetTrainings extends AsyncTask<Void, Void, List<Training>> {
            // Is executed in the background and retrieves the list of Training objects from the database
            @Override
            protected List<Training> doInBackground(Void... voids) {
                List<Training> taskList = mDb.getAppDatabase()
                        .trainingDao()
                        .getAll();
                return taskList;
            }
            // Is executed once the asynchronous task is finished and updates the adapter with the list of Training objects
            @Override
            protected void onPostExecute(List<Training> trainings) {
                super.onPostExecute(trainings);

                // Update of the adapter with the list of training objects
                adapter.clear();
                adapter.addAll(trainings);
                // Notifies the adapter of the source change
                adapter.notifyDataSetChanged();
            }
        }
        // Creation of a GetTrainings object and execution of the asynchronous request
        GetTrainings gt = new GetTrainings();
        gt.execute();
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Update trainings
        getTrainings();

    }

    public void seeTraining(int id) {
        // Create an intention for the SeeTraining activity
        Intent intent = new Intent(ListTraining.this, SeeTraining.class);
        // Add the course identifier to the intent
        intent.putExtra("id", id);
        // Delete the other activities above it in the activity stack
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Starting the SeeTraining activity using intention
        this.startActivity(intent);
    }
}