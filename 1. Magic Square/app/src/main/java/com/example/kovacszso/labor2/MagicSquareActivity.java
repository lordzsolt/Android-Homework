package com.example.kovacszso.labor2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MagicSquareActivity extends AppCompatActivity
        implements DialogInterface.OnDismissListener{

    private int _selectedSize;

    private Button _selectedButton;

    private MagicSquareModel _magicSquareModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magic_square);

        final Bundle bundle = this.getIntent().getExtras();
        _selectedSize = bundle.getInt(Constants.kSIZE_Key);
        _magicSquareModel = new MagicSquareModel(_selectedSize);

        final LinearLayout baseLayout = (LinearLayout)findViewById(R.id.BaseLayout);
        baseLayout.setWeightSum(_selectedSize);

        for (int row = 0 ; row < _selectedSize ; row++) {
            final LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setWeightSum(_selectedSize);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f);
            layout.setLayoutParams(params);
            baseLayout.addView(layout);
            for (int column = 0 ; column < _selectedSize ; column++) {
                final Button button = new Button(this);
                button.setLayoutParams(params);
                button.setId(buttonIndexFromRowAndColumn(row, column));
                layout.addView(button);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        _selectedButton = button;
                        final KeypadDialog keypadDialog = new KeypadDialog(MagicSquareActivity.this, _selectedSize);
                        keypadDialog.setOnDismissListener(MagicSquareActivity.this);
                        keypadDialog.show();
                    }
                });
            }
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        KeypadDialog closedKeypadDialog = (KeypadDialog)dialog;
        final int result = closedKeypadDialog.getResult();
        if (result == 0) {
            //Nothing was selected
            return;
        }
        _selectedButton.setText("" + result);

        _magicSquareModel.updateTile(_selectedButton.getId(), result);

        _selectedButton = null;
        if (_magicSquareModel.isSolvedCorrectly()) {
            new AlertDialog.Builder(this)
                    .setTitle("Congratulations")
                    .setMessage("You have solved the puzzle correctly.")
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_magic_square, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int buttonIndexFromRowAndColumn(int row, int column) {
        return row * _selectedSize + column;
    }

    private Button buttonWithIndex(int index) {
        return (Button)findViewById(index);
    }
}
