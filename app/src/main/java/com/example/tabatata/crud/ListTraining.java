package com.example.tabatata.crud;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tabatata.R;
import com.example.tabatata.TrainingsAdapter;
import com.example.tabatata.db.DatabaseClient;
import com.example.tabatata.db.Training;

import java.util.ArrayList;
import java.util.List;

public class ListTraining extends AppCompatActivity {

    //
    private static final int REQUEST_CODE_ADD = 0;

    // DATA
    private DatabaseClient mDb;
    private TrainingsAdapter adapter;

    // VIEW
    private Button buttonAdd;
    private ListView listTraining;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_training);
        getSupportActionBar().hide();

        // Récupération du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext());

        // Récupérer les vues
        listTraining = findViewById(R.id.list_training);

        // Lier l'adapter au listView
        adapter = new TrainingsAdapter(this, new ArrayList<Training>());
        listTraining.setAdapter(adapter);


        // EXEMPLE : Ajouter un événement click à la listView
        listTraining.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Récupération de la tâche cliquée à l'aide de l'adapter
                Training training = adapter.getItem(position);

                seeTraining(training.getId());
            }
        });


        /* displayTraining();
        ConstraintLayout constraintLayout = findViewById(R.id.list_training);
        displayTraining(constraintLayout); */
    }

    private void getTrainings() {
        ///////////////////////
        class GetTrainings extends AsyncTask<Void, Void, List<Training>> {

            @Override
            protected List<Training> doInBackground(Void... voids) {
                List<Training> taskList = mDb.getAppDatabase()
                        .trainingDao()
                        .getAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<Training> trainings) {
                super.onPostExecute(trainings);

                // Mettre à jour l'adapter avec la liste de taches
                adapter.clear();
                adapter.addAll(trainings);

                // Now, notify the adapter of the change in source
                adapter.notifyDataSetChanged();
            }
        }

        //////////////////////////
        // IMPORTANT bien penser à executer la demande asynchrone
        // Création d'un objet de type GetTasks et execution de la demande asynchrone
        GetTrainings gt = new GetTrainings();
        gt.execute();
    }


    @Override
    protected void onStart() {
        super.onStart();

        // Mise à jour des taches
        getTrainings();

    }

    public void seeTraining(int id) {
        Intent intent = new Intent(ListTraining.this, SeeTraining.class);
        intent.putExtra("id", id);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
    }
}