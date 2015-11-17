package com.example.lordzsolt.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.view.Window;
import android.widget.RelativeLayout;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;


public class LabyrinthActivity extends AppCompatActivity {

    private LabyrinthModel _labyrinthModel;
    private LabyrinthView _labyrinthView;

    private long _startTime;
    private String[] _labyrinthString;

    private Timer _moveTimer;
    private int _moveIndex;

    private boolean _computerControlled;

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

        if (id == R.id.MENU_CONTROL) {
            this.showControlOptions();
            return true;
        }

        if (id == R.id.MENU_LEVEL) {
            this.showLevelPicker();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labyrinth);

        startNewLabyrinth();
    }

    private void readLabyrinth(int resourceId) {
        Resources res = this.getResources();
        String[] labyrinthRows = res.getStringArray(resourceId);

        _labyrinthModel = new LabyrinthModel(labyrinthRows);
    }

    private void onBallMove() {
        _labyrinthView.postInvalidate();
        if (_labyrinthModel.isSolved() && !_computerControlled) {
            final long solveTime = System.currentTimeMillis() - _startTime;
            new AlertDialog.Builder(this)
                    .setTitle("Congratulations")
                    .setMessage("You have solved the labyrinth correctly in " + (float)solveTime / 1000 + " seconds.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LabyrinthActivity.this.startNewLabyrinth();
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            LabyrinthActivity.this.startNewLabyrinth();
                        }
                    })
                    .show();
        }
    }

    private void startNewLabyrinth() {
        stopTimer();
        final RelativeLayout baseLayout = (RelativeLayout)findViewById(R.id.BaseLayout);
        if (_labyrinthView != null) {
            baseLayout.removeView(_labyrinthView);
        }

        if (_labyrinthString == null) {
            Resources res = this.getResources();
            _labyrinthString = res.getStringArray(R.array.labyrinthEasy);
        }

        _labyrinthModel = new LabyrinthModel(_labyrinthString);

        _labyrinthView = new LabyrinthView(this, _labyrinthModel);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        _labyrinthView.setLayoutParams(params);

        _labyrinthView.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                if (_computerControlled) {
                    return;
                }
                if (_labyrinthModel.tryMoveLeft()) {
                    LabyrinthActivity.this.onBallMove();
                }
            }

            @Override
            public void onSwipeRight() {
                if (_computerControlled) {
                    return;
                }
                if (_labyrinthModel.tryMoveRight()) {
                    LabyrinthActivity.this.onBallMove();
                }
            }

            @Override
            public void onSwipeUp() {
                if (_computerControlled) {
                    return;
                }
                if (_labyrinthModel.tryMoveUp()) {
                    LabyrinthActivity.this.onBallMove();
                }
            }

            @Override
            public void onSwipeDown() {
                if (_computerControlled) {
                    return;
                }
                if (_labyrinthModel.tryMoveDown()) {
                    LabyrinthActivity.this.onBallMove();
                }
            }
        });

        this.loadColor();
        baseLayout.addView(_labyrinthView);

        _startTime = System.currentTimeMillis();
    }

    private void showColorPickerAlert() {
        final CharSequence[] items = { "Red", "Green", "Blue", "Magenta"};
        final int colors[]={Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a color");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
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
        editor.putInt(Constants.SHARED_PREFERENCES_KEY_COLOR, color);
        editor.apply();
    }

    private void loadColor() {
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        int color = sharedPreferences.getInt(Constants.SHARED_PREFERENCES_KEY_COLOR, Color.GREEN);
        _labyrinthView.setWallColor(color);
    }


    private void showControlOptions() {
        final CharSequence[] items = { "Human", "Computer"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Control Type");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    humanControl();
                }
                else {
                    computerControl();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void humanControl() {
        _moveIndex = 0;
        if (_computerControlled && _labyrinthModel.isSolved()) {
            startNewLabyrinth();
        }
        _computerControlled = false;
    }

    private void computerControl() {
        _computerControlled = true;
        LabyrinthSolver solver = new LabyrinthSolver(_labyrinthString, _labyrinthModel.getBallRow(), _labyrinthModel.getBallColumn());

        final int[] directions = solver.solution();
        if (directions == null) {
            _computerControlled = false;
            return;
        }
        _moveIndex = 0;
        _moveTimer = new Timer();
        _moveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (_moveIndex == directions.length) {
                    stopTimer();
                    return;
                }
                switch (directions[_moveIndex]) {
                    case LabyrinthSolver.MoveDirection.LEFT: {
                        _labyrinthModel.tryMoveLeft();
                        LabyrinthActivity.this.onBallMove();
                        break;
                    }
                    case LabyrinthSolver.MoveDirection.UP: {
                        _labyrinthModel.tryMoveUp();
                        LabyrinthActivity.this.onBallMove();
                        break;
                    }
                    case LabyrinthSolver.MoveDirection.DOWN: {
                        _labyrinthModel.tryMoveDown();
                        LabyrinthActivity.this.onBallMove();
                        break;
                    }
                    case LabyrinthSolver.MoveDirection.RIGHT: {
                        _labyrinthModel.tryMoveRight();
                        LabyrinthActivity.this.onBallMove();
                        break;
                    }
                }
                _moveIndex++;
            }
        }, 0, 500);
    }

    private void stopTimer() {
        if (_moveTimer != null) {
            _moveTimer.cancel();
            _moveTimer.purge();
            _moveTimer = null;
        }
    }

    private void showLevelPicker() {
        final CharSequence[] items = { "Local", "Online"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select level source");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    showLocalLevels();
                }
                else {
                    downloadLevels();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showLocalLevels() {
        final CharSequence[] items = { "Easy", "Medium", "Hard"};
        final int levels[] = {R.array.labyrinthEasy, R.array.labyrinthMedium, R.array.labyrinthDifficult};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Level");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int item) {
                Resources res = LabyrinthActivity.this.getResources();
                _labyrinthString = res.getStringArray(levels[item]);
                startNewLabyrinth();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void downloadLevels() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Downloading");
        progressDialog.show();
        String downloadURL = Constants.WEBSERVICE_BASE_URL + Constants.WEBSERVICE_ENDPOINT_AVAILABLE_LEVELS;
        Downloader downloader = new Downloader(downloadURL, new Downloader.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                progressDialog.dismiss();
                String[] levelList = output.split("#");
                showDownloadedLevels(levelList);
            }
        });
        downloader.execute();
    }

    private void showDownloadedLevels(final String[] levels) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select level");
        builder.setItems(levels, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                downloadLevel(levels[item]);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void downloadLevel(String level) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Downloading");
        progressDialog.show();

        String downloadURL = Constants.WEBSERVICE_BASE_URL + Constants.WEBSERVICE_ENDPOINT_LEVEL + level;
        Downloader downloader = new Downloader(downloadURL, new Downloader.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                progressDialog.dismiss();
                if (output == null) {
                    return;
                }
                String[] components = output.split("#");
                StringBuilder builder = new StringBuilder(components[components.length - 1].trim());
                builder.setCharAt(builder.length() - 1, '3');
                components[components.length - 1] = builder.toString();

                _labyrinthString = Arrays.copyOfRange(components, 2, components.length);
                LabyrinthActivity.this.startNewLabyrinth();
            }
        });
        downloader.execute();
    }
}
