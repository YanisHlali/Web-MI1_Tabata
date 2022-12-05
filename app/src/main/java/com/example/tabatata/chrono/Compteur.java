package com.example.tabatata.chrono;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.example.tabatata.R;

import java.util.ArrayList;

public class Compteur extends UpdateSource {
    Context context;
    // CONSTANTE
    private final static long INITIAL_TIME = 5000;

    // DATA
    private long updatedTime;
    private CountDownTimer timer;   // https://developer.android.com/reference/android/os/CountDownTimer.html

    private ArrayList<Integer> arrayTime;
    private int indexArrayTime;
    private ArrayList<String> arrayTimeField;

    public Compteur(Context context) {
        this.context = context;
    }

    public void setTime(float i) {
        this.updatedTime = (long) i;
    }

    public long getTime() {
        return this.updatedTime;
    }

    public void setArrayTime(ArrayList<Integer> array) {
        this.arrayTime = array;
    }

    public ArrayList<Integer> getArrayTime() {
        return this.arrayTime;
    }

    public void setIndexArrayTime(int index) {
        this.indexArrayTime = index;
    }

    public int getIndexArrayTime() {
        return this.indexArrayTime;
    }

    public void setArrayTimeField(ArrayList<String> array) { this.arrayTimeField = array; }

    public ArrayList<String> getArrayTimeField() { return this.arrayTimeField; }


    // Lancer le compteur
    public void start() {
        displayNextAction();
        updatedTime = getTime();
        if (timer == null) {

            // Créer le CountDownTimer
            timer = new CountDownTimer(updatedTime, 10) {

                // Callback fired on regular interval
                public void onTick(long millisUntilFinished) {
                    updatedTime = millisUntilFinished;

                    // Mise à jour
                    update();
                }

                // Callback fired when the time is up
                public void onFinish() {
                    System.out.println("----------------------------");
                    System.out.println(getArrayTime().get(getIndexArrayTime()) * 1000);
                    setIndexArrayTime(getIndexArrayTime() + 1);
                    int index = getIndexArrayTime();
                    updatedTime = getArrayTime().get(index) * 1000;
                    TextView tvChrono = (TextView) ((Activity)context).findViewById(R.id.description);
                    tvChrono.setText(getArrayTimeField().get(getIndexArrayTime()));
                    displayNextAction();

                    // Mise à jour
                    update();
                    start();
                }

            }.start();   // Start the countdown
        }

    }

    public void displayNextAction() {
        TextView tvNextAction = (TextView) ((Activity)context).findViewById(R.id.nextAction);
        tvNextAction.setTextColor(Color.WHITE);
        tvNextAction.setText("➜ " + getArrayTimeField().get(getIndexArrayTime()+1));
    }

    // Mettre en pause le compteur
    public void pause() {
        if (timer != null) {
            // Arreter le timer
            stop();

            // Mise à jour
            update();
        }
    }


    // Remettre à le compteur à la valeur initiale
    public void reset() {

        if (timer != null) {

            // Arreter le timer
            stop();
        }

        // Réinitialiser
        updatedTime = INITIAL_TIME;

        // Mise à jour
        update();

    }

    // Arrete l'objet CountDownTimer et l'efface
    private void stop() {
        timer.cancel();
        timer = null;
    }

    public int getMinutes() {
        return (int) (updatedTime / 1000)/60;
    }

    public int getSecondes() {
        int secs = (int) (updatedTime / 1000);
        return secs % 60;
    }

    public int getMillisecondes() {
        return (int) (updatedTime % 1000);
    }
}
