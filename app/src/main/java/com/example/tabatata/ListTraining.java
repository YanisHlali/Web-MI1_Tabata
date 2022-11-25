package com.example.tabatata;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

                // Message
                Toast.makeText(ListTraining.this, "Click : " + training.getName(), Toast.LENGTH_SHORT).show();
            }
        });


        /* displayTraining();
        ConstraintLayout constraintLayout = findViewById(R.id.list_training);
        displayTraining(constraintLayout); */
    }

    private void getTrainings() {
        ///////////////////////
        // Classe asynchrone permettant de récupérer des taches et de mettre à jour le listView de l'activité
        class GetTrainings extends AsyncTask<Void, Void, List<Training>> {

            @Override
            protected List<Training> doInBackground(Void... voids) {
                List<Training> taskList = mDb.getAppDatabase()
                        .trainingDao()
                        .getAll();
                System.out.println("------------------------------------------");
                System.out.println(taskList);
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

    /* public void displayTraining() {
        ListView constraintLayout = findViewById(R.id.list_training);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "training").allowMainThreadQueries().build();

        List<Training> trainingList = db.taskDao().getAll();
        int totalTraining = trainingList.size();
        for (int i = 0; i < totalTraining; i++) {
            TextView textView = new TextView(this);
            if (trainingList.get(i).getName() == null) textView.setText("Entrainement Intitrée n°"+i);
            else textView.setText(trainingList.get(i).getName());
            textView.setTextSize(28);
            textView.setId(i);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            textView.setTextColor(Integer.parseInt("FFFFFF", 16)+0xFF000000);
            textView.setPadding(50, 400+(i*200), 200, 0);
            constraintLayout.addView(textView);
        }; */
}