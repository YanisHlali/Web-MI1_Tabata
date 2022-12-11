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
        // Call the method of the superclass to initialize the activity
        super.onCreate(savedInstanceState);
        // Define the view to use for the activity
        setContentView(R.layout.create_training);
        // Hide the action bar of the application
        getSupportActionBar().hide();
        // Retrieve the training ID from the activity's Intent
        int id = (int) getIntent().getSerializableExtra("id");
        // Retrieve training data from its ID
        getTrainings(id);
        // Retrieve an instance of the database
        mDb = DatabaseClient.getInstance(getApplicationContext());
        // Retrieve the "Modify" button and configure its text and size
        saveView = findViewById(R.id.btCreate);
        saveView.setText("Modifier");
        saveView.setTextSize(16);
        // Add an event handler on the click of the "Edit" button
        saveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When the button is clicked, call the method to save the training changes
                saveTraining();
            }
        });
    }

    public void changeValue(View v) {
        // Extract the complete identifier of the clicked button
        String completeIdButton = v.getResources().getResourceEntryName(v.getId());
        // Determine the operation to perform by separating the button identifier into two parts
        String[] idButton = completeIdButton.split("Button");
        String operation = idButton[0];
        // Determine the identifier of the TextView associated with the button
        String valueId = "value"+idButton[1];

        // Get an instance of Resources to find the associated TextView
        Resources res = getResources();
        int idTextview = res.getIdentifier(valueId, "id", this.getPackageName());
        TextView textview = findViewById(idTextview);
        // Retrieve the current value of the TextView as a string
        String textviewString = textview.getText().toString();

        // Convert the TextView value to integer
        int textviewInt = Integer.parseInt(textviewString);
        // Change the value of the TextView according to the operation previously determined
        if (textviewInt > 0 && operation.equals("delete")) textviewInt -= 1;
        if (textviewInt < 10 && operation.equals("add")) textviewInt += 1;
        // Convert the new value to a string and update the TextView
        textviewString = String.valueOf(textviewInt);
        textview.setText(textviewString);
    }

    private void saveTraining() {
        // Retrieve TextViews
        TextView tvPreparation = findViewById(R.id.valuePreparation);
        TextView tvSequence = findViewById(R.id.valueSequence);
        TextView tvCycle = findViewById(R.id.valueCycle);
        TextView tvWork = findViewById(R.id.valueWork);
        TextView tvRest = findViewById(R.id.valueRest);
        TextView tvLongRest = findViewById(R.id.valueLongRest);

        // Convert TextView values to integers
        int preparation = Integer.parseInt(tvPreparation.getText().toString());
        int sequence = Integer.parseInt(tvSequence.getText().toString());
        int cycle = Integer.parseInt(tvCycle.getText().toString());
        int work = Integer.parseInt(tvWork.getText().toString());
        int rest = Integer.parseInt(tvRest.getText().toString());
        int longRest = Integer.parseInt(tvLongRest.getText().toString());

        // Define inner updateTraining class to perform update in background thread
        class updateTraining extends AsyncTask<Void, Void, Training> {

            @Override
            protected Training doInBackground(Void... voids) {
                // Retrieve ID of Training object to update
                int id = (int) getIntent().getSerializableExtra("id");
                // Update object in database
                mDb.getAppDatabase()
                        .trainingDao()
                        .updateTraining(id,preparation, sequence, cycle, work, rest, longRest);
                // Return null since the Training object is not used in this method
                return null;
            }

            @Override
            protected void onPostExecute(Training training) {
                super.onPostExecute(training);
                // Once update is complete, navigate back to previous screen
                getBack();
                setResult(RESULT_OK);
                finish();
                // Display toast message to indicate that the object was saved
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        // Create and execute updateTraining task
        updateTraining st = new updateTraining();
        st.execute();
    }

    private void getTrainings(int id) {
        // Define inner GetTrainings class to perform database query in background thread
        class GetTrainings extends AsyncTask<Void, Void, Training> {

            @Override
            protected Training doInBackground(Void... voids) {
                // Query database for Training object with specified ID
                Training result = mDb.getAppDatabase()
                        .trainingDao()
                        .getTrainingById(id);
                // Return result of query
                return result;
            }

            @Override
            protected void onPostExecute(Training training) {
                super.onPostExecute(training);
                // Set values of TextViews to properties of Training object
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
        // Create and execute GetTrainings task
        GetTrainings gt = new GetTrainings();
        gt.execute();
    }

    public void getBack() {
        // Retrieve ID of Training object passed to ModifyTraining activity
        int id = (int) getIntent().getSerializableExtra("id");
        // Create intent to navigate to SeeTraining activity
        Intent intent = new Intent(ModifyTraining.this, SeeTraining.class);
        // Pass ID of Training object to SeeTraining activity
        intent.putExtra("id", id);
        // Clear activity stack to prevent user from navigating back to ModifyTraining activity
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Start SeeTraining activity
        this.startActivity(intent);
    }
}
