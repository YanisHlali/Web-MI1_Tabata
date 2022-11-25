package com.example.tabatata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tabatata.db.Training;

import java.util.List;

public class TrainingsAdapter extends ArrayAdapter<Training> {

    public TrainingsAdapter(Context mCtx, List<Training> taskList) {
        super(mCtx, R.layout.template_training, taskList);
    }

    /**
     * Remplit une ligne de la listView avec les informations de la multiplication associée
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Récupération de la multiplication
        final Training training = getItem(position);

        // Charge le template XML
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.template_training, parent, false);

        // Récupération des objets graphiques dans le template
        TextView textViewTask = (TextView) rowView.findViewById(R.id.textViewTraining);

        //
        textViewTask.setText(training.getName());

        //
        return rowView;
    }

}