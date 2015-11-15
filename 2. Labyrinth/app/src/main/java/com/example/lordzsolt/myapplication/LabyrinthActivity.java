package com.example.lordzsolt.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class LabyrinthActivity extends AppCompatActivity {

    private LabyrinthModel _labyrinthModel;
    private LabyrinthView _labyrinthView;

    private long _startTime;

    private Integer _level;
    private List<Integer> _labirynthStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labyrinth);

        Integer[] array = {R.array.labyrinthEasy, R.array.labyrinthMedium, R.array.labyrinthDifficult};

        _level = 0;
        _labirynthStrings = new ArrayList<>(Arrays.asList(array));

        startNewLabyrinth();
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
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LabyrinthActivity.this.increaseLevel();
                            LabyrinthActivity.this.startNewLabyrinth();
                        }
                    })
                    .show();
        }
    }

    private void startNewLabyrinth() {
        final RelativeLayout baseLayout = (RelativeLayout)findViewById(R.id.BaseLayout);
        if (_labyrinthView != null) {
            baseLayout.removeView(_labyrinthView);
        }

        Log.d("LAB","" + R.array.labyrinthEasy);
        Log.d("LAB","" + _labirynthStrings.get(_level));
        readLabyrinth(_labirynthStrings.get(_level));

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

        baseLayout.addView(_labyrinthView);

        _startTime = System.currentTimeMillis();
    }

    private void increaseLevel() {
        if (_level < _labirynthStrings.size() - 1) {
            _level++;
        }
        else {
            _level = 0;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
