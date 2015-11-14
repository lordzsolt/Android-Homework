package com.example.lordzsolt.myapplication;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LabyrinthActivity extends AppCompatActivity {

    private LabyrinthModel _labyrinthModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labyrinth);

        readLabyrinth(R.array.labyrinthEasy);
    }

    private void readLabyrinth(int resourceId) {
        Resources res = this.getResources();
        String[] labyrinthRows = res.getStringArray(resourceId);

        _labyrinthModel = new LabyrinthModel(labyrinthRows);
    }
}
