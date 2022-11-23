package com.example.tabatata;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.room.Room;

import com.example.tabatata.db.AppDatabase;
import com.example.tabatata.db.Training;

import java.util.List;

public class ListTraining extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_training);
        getSupportActionBar().hide();
        /* displayTraining(); */
        displayTraining();
    }

    public void displayTraining() {
        ConstraintLayout constraintLayout = findViewById(R.id.list_training);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "training").allowMainThreadQueries().build();

        List<Training> trainingList = db.taskDao().getAll();
        int totalTraining = trainingList.size();
        for (int i = 0; i < totalTraining; i++) {
            TextView textView = new TextView(this);
            if (trainingList.get(i).getName() == null) textView.setText("Entrainement Intitrée n°"+i);
            else textView.setText(trainingList.get(i).getName());
            textView.setTextSize(28);
            textView.setTextColor(Integer.parseInt("FFFFFF", 16)+0xFF000000);
            textView.setPadding(50, 400+(i*200), 200, 0);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    seeTraining(db, textView.getText());
                }
            });
            constraintLayout.addView(textView);
        }
    }
    public void seeTraining(AppDatabase db, CharSequence textView) {
        /*Training training = db.taskDao().getTrainingByName(textView.getText().toString());*/
        System.out.println("---------------------------------------------");
        System.out.println(textView);
        /*System.out.println(training.getId());*/
        System.out.println("---------------------------------------------");
        Intent intent = new Intent(this, SeeTraining.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
    }
}