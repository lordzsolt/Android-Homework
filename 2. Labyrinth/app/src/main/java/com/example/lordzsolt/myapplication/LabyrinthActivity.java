package com.example.lordzsolt.myapplication;

import android.app.AlertDialog;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class LabyrinthActivity extends AppCompatActivity {

    private LabyrinthModel _labyrinthModel;
    private LabyrinthView _labyrinthView;

    private long _startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labyrinth);

        readLabyrinth(R.array.labyrinthEasy);

        _labyrinthView = new LabyrinthView(this, _labyrinthModel);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        _labyrinthView.setLayoutParams(params);

        _labyrinthView.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                if (_labyrinthModel.tryMoveLeft()) {
                    LabyrinthActivity.this.onBallMove();
                }
            }

            @Override
            public void onSwipeRight() {
                if (_labyrinthModel.tryMoveRight()) {
                    LabyrinthActivity.this.onBallMove();
                }
            }

            @Override
            public void onSwipeUp() {
                if (_labyrinthModel.tryMoveUp()) {
                    LabyrinthActivity.this.onBallMove();
                }
            }

            @Override
            public void onSwipeDown() {
                if (_labyrinthModel.tryMoveDown()) {
                    LabyrinthActivity.this.onBallMove();
                }
            }
        });

        final RelativeLayout baseLayout = (RelativeLayout)findViewById(R.id.BaseLayout);
        baseLayout.addView(_labyrinthView);

        _startTime = System.currentTimeMillis();
    }

    private void readLabyrinth(int resourceId) {
        Resources res = this.getResources();
        String[] labyrinthRows = res.getStringArray(resourceId);

        _labyrinthModel = new LabyrinthModel(labyrinthRows);
    }

    private void onBallMove() {
        _labyrinthView.postInvalidate();
        if (_labyrinthModel.isSolved()) {
            final long solveTime = System.currentTimeMillis() - _startTime;
            new AlertDialog.Builder(this)
                    .setTitle("Congratulations")
                    .setMessage("You have solved the labyrinth correctly in " + (float)solveTime / 1000 + " seconds.")
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }
    }
}
