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
        // Retrieve the identifier of the clicked button
        String completeIdButton = v.getResources().getResourceEntryName(v.getId());

        // Separate the identifier into two parts: the operation and the name of the value to change
        String[] idButton = completeIdButton.split("Button");
        String operation = idButton[0];
        String valueId = "value"+idButton[1];

        // Get the TextView object associated with the value to change
        Resources res = getResources();
        int idTextview = res.getIdentifier(valueId, "id", this.getPackageName());
        TextView textview = findViewById(idTextview);
        String textviewString = textview.getText().toString();

        // Convert the value of the TextView object to an integer
        int textviewInt = Integer.parseInt(textviewString);
        // Decreases the value by 1 if the operation is delete
        if (textviewInt > 0 && operation.equals("delete")) {
            // Displays a warning message for the work, cycle and sequence buttons
            if (textviewInt == 1 && (valueId.equals("valueWork") || valueId.equals("valueCycle") || valueId.equals("valueSequence"))) {
                Toast.makeText(this, "1 est la valeur minimum", Toast.LENGTH_SHORT).show();
            } else {
                textviewInt -= 1;
            }
        } else {
            // Increases the value by 1 if the operation is add
            textviewInt += 1;
        }
        // Change the old value by the new one
        textviewString = String.valueOf(textviewInt);
        textview.setText(textviewString);
    }


    private void saveTraining() {
        // Retrieve field values from the user interface
        TextView tvPreparation = findViewById(R.id.valuePreparation);
        TextView tvSequence = findViewById(R.id.valueSequence);
        TextView tvCycle = findViewById(R.id.valueCycle);
        TextView tvWork = findViewById(R.id.valueWork);
        TextView tvRest = findViewById(R.id.valueRest);
        TextView tvLongRest = findViewById(R.id.valueLongRest);

        // Convert field values to strings
        String preparation = tvPreparation.getText().toString();
        String sequence = tvSequence.getText().toString();
        String cycle = tvCycle.getText().toString();
        String work = tvWork.getText().toString();
        String rest = tvRest.getText().toString();
        String longRest = tvLongRest.getText().toString();

        // Define an inner class to register the Training object in the background
       class saveTraining extends AsyncTask<Void, Void, Training> {
            @Override
            protected Training doInBackground(Void... voids) {
                // Create a new Training object with the values of the fields
                Training training = new Training();
                training.setPreparation(Integer.parseInt(preparation));
                training.setSequence(Integer.parseInt(sequence));
                training.setCycle(Integer.parseInt(cycle));
                training.setWork(Integer.parseInt(work));
                training.setRest(Integer.parseInt(rest));
                training.setLongRest(Integer.parseInt(longRest));

                // Get the list of all existing Training objects in the database
                List<Training> allTrainings = mDb.getAppDatabase().trainingDao().getAll();
                // Calculate a number for the new Training object by adding 1 to the total number of objects in the list
                int totalTraining = allTrainings.size()+1;
                // Define a name for the new Training object using the calculated number
                String trainingName = String.valueOf(totalTraining);
                // Insert the new Training object in the database
                training.setName("Entrainement n°" + trainingName);

                mDb.getAppDatabase()
                        .trainingDao()
                        .insert(training);

                return training;
            }

            @Override
            protected void onPostExecute(Training training) {
                super.onPostExecute(training);
                // End the current activity and display a confirmation message to the user
                setResult(RESULT_OK);
                finish();
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        saveTraining st = new saveTraining();
        st.execute();
    }
}