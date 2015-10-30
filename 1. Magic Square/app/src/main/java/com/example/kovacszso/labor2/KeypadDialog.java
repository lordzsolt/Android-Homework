package com.example.kovacszso.labor2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class KeypadDialog extends Dialog {

    private static final String TAG = "KeyPad";

    private int _selectedSize;

    private int _result;

    public int getResult() {
        return _result;
    }

    public KeypadDialog(Context context, int _selectedSize) {
        super(context);
        this._selectedSize = _selectedSize;

        LinearLayout baseLayout = new LinearLayout(context);
        baseLayout.setOrientation(LinearLayout.VERTICAL);
        baseLayout.setWeightSum(_selectedSize);
        baseLayout.setBackgroundColor(Color.BLACK);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f);

        for (int row = 0 ; row < _selectedSize ; row++) {
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setWeightSum(_selectedSize);
            layout.setLayoutParams(params);
            baseLayout.addView(layout);
            for (int column = 0; column < _selectedSize; column++) {
                Button button = new Button(context);
                button.setLayoutParams(params);
                final int buttonIndex = buttonIndexFromRowAndColumn(row, column);
                button.setId(buttonIndex);
                button.setText("" + (buttonIndex + 1));
                layout.addView(button);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        _result = buttonIndex + 1;
                        dismiss();
                    }
                });
            }
        }
        this.setContentView(baseLayout);
    }


    private int buttonIndexFromRowAndColumn(int row, int column) {
        return row * _selectedSize + column;
    }
}
