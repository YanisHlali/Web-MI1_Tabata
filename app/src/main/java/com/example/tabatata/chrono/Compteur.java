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

            public long counterTime;
            private CountDownTimer timer;
            private ArrayList<Integer> arrayTraining;
            private int indexArrayTraining;
            private ArrayList<String> arrayTrainingField;

            public Compteur(Context context) {
                this.context = context;
            }

            public void setCurrentTime(float i) {
                this.counterTime = (long) i;
            }

            public long getCurrentTime() {
                return this.counterTime;
            }

            public void setArrayTraining(ArrayList<Integer> array) {
                this.arrayTraining = array;
            }

            public ArrayList<Integer> getArrayTraining() { return this.arrayTraining; }

            public void setIndexArrayTraining(int index) { this.indexArrayTraining = index; }

            public int getIndexArrayTraining() { return this.indexArrayTraining; }

            public void setArrayTrainingField(ArrayList<String> array) { this.arrayTrainingField = array; }

            public ArrayList<String> getArrayTrainingField() { return this.arrayTrainingField; }


            // Start the counter
            public void startTimer() {
                // Displays the next action the user must perform
                displayNextAction();
                // Initializes the counter time to the current workout time
                System.out.println("CHRONO : " + getCurrentTime());
                if (getCurrentTime() == 0) setCurrentTime(getArrayTraining().get(0) * 1000);

                counterTime = getCurrentTime();
                // If the timer doesn't exist ...
                if (timer == null) {
                    // ... CountDownTimer is created
                    timer = new CountDownTimer(getCurrentTime(), 10) {

                        // Callback fired on regular interval
                        public void onTick(long millisUntilFinished) {
                            counterTime = millisUntilFinished;
                            update();
                        }

                        // Callback fired when the time is up
                        public void onFinish() {
                            changeValue();
                            // Displays the next action the user must perform
                            displayNextAction();
                            // Redefine the counterTime of timer
                            timer = null;

                            update();
                            startTimer();
                        }

                    }.start();   // Start the countdown
                }
            }

            public void changeValue() {
                // Assigning the timer to the next action
                setIndexArrayTraining(getIndexArrayTraining() + 1);
                setCurrentTime(getArrayTraining().get(getIndexArrayTraining()) * 1000);
                // The description of the timer changes to the following description
                TextView tvChrono = (TextView) ((Activity)context).findViewById(R.id.description);
                tvChrono.setText(getArrayTrainingField().get(getIndexArrayTraining()));
            }

            public void displayNextAction() {
                // Retrieving TextViews to display
                TextView tvNextAction = (TextView) ((Activity)context).findViewById(R.id.nextAction);
                TextView tvSecondAction = (TextView) ((Activity)context).findViewById(R.id.nextSecondAction);
                TextView tvThirdAction = (TextView) ((Activity)context).findViewById(R.id.nextThirdAction);
                // Setting up a list to store TextViews
                ArrayList<TextView> textViews = new ArrayList<>();
                textViews.add(tvNextAction);
                textViews.add(tvSecondAction);
                textViews.add(tvThirdAction);
                // Loop over each TextView to define its content
                for (int i = 0; i < textViews.size(); i++) {
                    int j = i+1;
                    // If we have reached the end of the list of formations, we remove the text
                    if (getIndexArrayTraining()+j >= getArrayTrainingField().size()) {
                        textViews.get(i).setTextColor(343434);
                    }
                    // Otherwise we continue to display the list
                    else {
                        String nameNextTraining = getArrayTrainingField().get(getIndexArrayTraining()+j);
                        String valueNextTraining = String.valueOf(getArrayTraining().get(getIndexArrayTraining()+j));
                        textViews.get(i).setText("➜ " + nameNextTraining + " (" + valueNextTraining + "s)");
                        textViews.get(i).setTextColor(Color.WHITE);
                    }
                }
            }

            // Pause the counter
            public void pause() {
                if (timer != null) {
                    stop(); // Stop the counter
                    update();
                }
            }


            // Reset the counter to the initial value
            public void reset() {
                // Stop the counter
                if (timer != null) stop();
                // Reset
                counterTime = getArrayTraining().get(getIndexArrayTraining()) * 1000;
                // Update
                update();

            }

            // Stop the CountDownTimer object and delete it
            private void stop() {
                timer.cancel();
                timer = null;
            }

            public int getMinutes() {
                return (int) (counterTime / 1000)/60;
            }

            public int getSecondes() {
                int secs = (int) (counterTime / 1000);
                return secs % 60;
            }

            public int getMillisecondes() {
                return (int) (counterTime % 1000);
            }
        }
