package com.example.tabatata;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.tabatata.db.AppDatabase;
import com.example.tabatata.db.Training;
import com.example.tabatata.db.TrainingDao;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CreateTraining extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_training);
        getSupportActionBar().hide();
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

    public void getTraining(View v) {
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
            db.taskDao().insert(training);
        });

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK + Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);

        Toast.makeText(getApplicationContext(),"Entrainement crée", Toast.LENGTH_SHORT).show();
    }

    public int getAllTrainings() {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "training").allowMainThreadQueries().build();
        TrainingDao trainingDao = db.taskDao();
        List<Training> trainings = trainingDao.getAll();
        int totalTraining = trainings.size()+1;
        return totalTraining;
    };
}