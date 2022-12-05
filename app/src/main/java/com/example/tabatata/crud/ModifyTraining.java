package com.example.tabatata.crud;

import android.content.Intent;
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

public class ModifyTraining extends AppCompatActivity {
    // DATA
    private DatabaseClient mDb;
    // VIEW
    private Button saveView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_training);
        getSupportActionBar().hide();

        int id = (int) getIntent().getSerializableExtra("id");
        getTrainings(id);
        mDb = DatabaseClient.getInstance(getApplicationContext());
        saveView = findViewById(R.id.btCreate);
        saveView.setText("Modifier");
        saveView.setTextSize(16);
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

        int preparation = Integer.parseInt(tvPreparation.getText().toString());
        int sequence = Integer.parseInt(tvSequence.getText().toString());
        int cycle = Integer.parseInt(tvCycle.getText().toString());
        int work = Integer.parseInt(tvWork.getText().toString());
        int rest = Integer.parseInt(tvRest.getText().toString());
        int longRest = Integer.parseInt(tvLongRest.getText().toString());

        class updateTraining extends AsyncTask<Void, Void, Training> {

            @Override
            protected Training doInBackground(Void... voids) {
                int id = (int) getIntent().getSerializableExtra("id");
                mDb.getAppDatabase()
                        .trainingDao()
                        .updateTraining(id,preparation, sequence, cycle, work, rest, longRest);
                return null;
            }

            @Override
            protected void onPostExecute(Training training) {
                super.onPostExecute(training);
                getBack();
                // Quand la tache est créée, on arrête l'activité AddTaskActivity (on l'enleve de la pile d'activités)
                setResult(RESULT_OK);
                finish();
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        //////////////////////////
        // IMPORTANT bien penser à executer la demande asynchrone
        updateTraining st = new updateTraining();
        st.execute();
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

    public void getBack() {
        int id = (int) getIntent().getSerializableExtra("id");
        Intent intent = new Intent(ModifyTraining.this, SeeTraining.class);
        intent.putExtra("id", id);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
    }
}
