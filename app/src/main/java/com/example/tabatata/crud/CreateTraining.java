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

        // Get DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext());
        saveView = findViewById(R.id.btCreate);
        // Associate an event with the save button
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
        // Increment value
        int textviewInt = Integer.parseInt(textviewString);
        if (textviewInt > 0 && operation.equals("delete")) {
            // For work, cycle and sequence button, the mini value is 1
            if (textviewInt == 1 && (valueId.equals("valueWork") || valueId.equals("valueCycle") || valueId.equals("valueSequence"))) {
                Toast.makeText(this, "1 est la valeur minimum", Toast.LENGTH_SHORT).show();
            } else {
                textviewInt -= 1;
            }
        }
        // Decrement value
        if (textviewInt < 10 && operation.equals("add")) {
            textviewInt += 1;
        }
        // Change the old value by the new in layout
        textviewString = String.valueOf(textviewInt);
        textview.setText(textviewString);
    }


    private void saveTraining() {
        // Get all textview
        TextView tvPreparation = findViewById(R.id.valuePreparation);
        TextView tvSequence = findViewById(R.id.valueSequence);
        TextView tvCycle = findViewById(R.id.valueCycle);
        TextView tvWork = findViewById(R.id.valueWork);
        TextView tvRest = findViewById(R.id.valueRest);
        TextView tvLongRest = findViewById(R.id.valueLongRest);
        // Convert all textview to string
        String preparation = tvPreparation.getText().toString();
        String sequence = tvSequence.getText().toString();
        String cycle = tvCycle.getText().toString();
        String work = tvWork.getText().toString();
        String rest = tvRest.getText().toString();
        String longRest = tvLongRest.getText().toString();

       class saveTraining extends AsyncTask<Void, Void, Training> {

            @Override
            protected Training doInBackground(Void... voids) {
                // Create new training
                Training training = new Training();
                training.setPreparation(Integer.parseInt(preparation));
                training.setSequence(Integer.parseInt(sequence));
                training.setCycle(Integer.parseInt(cycle));
                training.setWork(Integer.parseInt(work));
                training.setRest(Integer.parseInt(rest));
                training.setLongRest(Integer.parseInt(longRest));
                // Create name for training based on all training
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
                // When the task is created, we stop the AddTaskActivity (we remove it from the activity stack)
                setResult(RESULT_OK);
                finish();
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }
        saveTraining st = new saveTraining();
        st.execute();
    }
}