package com.example.lordzsolt.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
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

    private static final String SHARED_PREFERENCES_KEY_COLOR = "ColorKey";

    private LabyrinthModel _labyrinthModel;
    private LabyrinthView _labyrinthView;

    private long _startTime;

    private Integer _level;
    private List<Integer> _labirynthStrings;

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
        if (id == R.id.MENU_COLOR) {
            this.showColorPickerAlert();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labyrinth);

        Integer[] array = {R.array.labyrinthEasy, R.array.labyrinthMedium, R.array.labyrinthDifficult};

        _level = 0;
        _labirynthStrings = new ArrayList<>(Arrays.asList(array));

        startNewLabyrinth();
        this.loadColor();
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

    private void showColorPickerAlert() {
        final CharSequence[] items = { "Red", "Green", "Blue", "Magenta"};
        final int colors[]={Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a color");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                LabyrinthActivity.this._labyrinthView.setWallColor(colors[item]);
                saveColor(colors[item]);
                _labyrinthView.postInvalidate();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void saveColor(int color) {
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SHARED_PREFERENCES_KEY_COLOR, color);
        editor.apply();
    }

    private void loadColor() {
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        int color = sharedPreferences.getInt(SHARED_PREFERENCES_KEY_COLOR, Color.GREEN);
        _labyrinthView.setWallColor(color);
    }
}
