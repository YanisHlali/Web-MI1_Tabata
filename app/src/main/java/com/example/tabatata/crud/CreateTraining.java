package com.example.tabatata.crud;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tabatata.R;
import com.example.tabatata.db.DatabaseClient;
import com.example.tabatata.db.Training;

import java.util.List;

public class CreateTraining extends AppCompatActivity {
    // DATA
    private DatabaseClient mDb;

    // VIEW
    private Button saveView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_training);
        getSupportActionBar().hide();

        // Récupération du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext());
        saveView = findViewById(R.id.btCreate);
        // Associer un événement au bouton save
        saveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTraining();
            }
        });
    }

    public void changeValue(View v) {
        // Get id
        String completeIdButton = v.getResources().getResourceEntryName(v.getId());
        String[] idButton = completeIdButton.split("Button");
        String operation = idButton[0];
        String valueId = "value"+idButton[1];
        // Get Textview
        Resources res = getResources();
        int idTextview = res.getIdentifier(valueId, "id", this.getPackageName());
        TextView textview = findViewById(idTextview);
        String textviewString = textview.getText().toString();
        // Set new value
        int textviewInt = Integer.parseInt(textviewString);
        if (textviewInt > 0 && operation.equals("delete")) textviewInt -= 1;
        if (textviewInt < 10 && operation.equals("add")) textviewInt += 1;
        textviewString = String.valueOf(textviewInt);
        textview.setText(textviewString);
    }


    private void saveTraining() {
        TextView tvPreparation = findViewById(R.id.valuePreparation);
        TextView tvSequence = findViewById(R.id.valueSequence);
        TextView tvCycle = findViewById(R.id.valueCycle);
        TextView tvWork = findViewById(R.id.valueWork);
        TextView tvRest = findViewById(R.id.valueRest);
        TextView tvLongRest = findViewById(R.id.valueLongRest);

        System.out.println("------------------------ ");

        String preparation = tvPreparation.getText().toString();
        String sequence = tvSequence.getText().toString();
        String cycle = tvCycle.getText().toString();
        String work = tvWork.getText().toString();
        String rest = tvRest.getText().toString();
        String longRest = tvLongRest.getText().toString();

       class saveTraining extends AsyncTask<Void, Void, Training> {

            @Override
            protected Training doInBackground(Void... voids) {

                Training training = new Training();
                training.setPreparation(Integer.parseInt(preparation));
                training.setSequence(Integer.parseInt(sequence));
                training.setCycle(Integer.parseInt(cycle));
                training.setWork(Integer.parseInt(work));
                training.setRest(Integer.parseInt(rest));
                training.setLongRest(Integer.parseInt(longRest));

                List<Training> allTrainings = mDb.getAppDatabase().trainingDao().getAll();
                int totalTraining = allTrainings.size()+1;
                String trainingName = String.valueOf(totalTraining);
                training.setName("Entrainement n°" + trainingName);

                mDb.getAppDatabase()
                        .trainingDao()
                        .insert(training);


                return training;
            }

            @Override
            protected void onPostExecute(Training training) {
                super.onPostExecute(training);

                // Quand la tache est créée, on arrête l'activité AddTaskActivity (on l'enleve de la pile d'activités)
                setResult(RESULT_OK);
                finish();
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        //////////////////////////
        // IMPORTANT bien penser à executer la demande asynchrone
        saveTraining st = new saveTraining();
        st.execute();
    }

    /*public void getTraining(View v) {
        TextView preparation = findViewById(R.id.valuePreparation);
        TextView sequence = findViewById(R.id.valueSequence);

        int totalTraining = getAllTrainings()+1;
        String trainingName = String.valueOf(totalTraining);

        Training training = new Training();
        training.setName("Entrainement n°" + trainingName);
        training.setPreparation(10);
        training.setSequence(10);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "training").allowMainThreadQueries().build();

        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            db.trainingDao().insert(training);
        });

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK + Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);

        Toast.makeText(getApplicationContext(),"Entrainement crée", Toast.LENGTH_SHORT).show();
    }

    public int getAllTrainings() {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "training").allowMainThreadQueries().build();
        TrainingDao trainingDao = db.trainingDao();
        List<Training> trainings = trainingDao.getAll();
        int totalTraining = trainings.size()+1;
        return totalTraining;
    }; */
}