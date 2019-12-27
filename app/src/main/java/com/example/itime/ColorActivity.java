package com.example.itime;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


public class ColorActivity extends AppCompatActivity {

    private static final int COLORBK = 201;
    private ImageButton btn_yes, btn_no;
    private int color_main;
    private View view;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_activity);

        color_main = ContextCompat.getColor(this, R.color.colorPrimary);

        view = findViewById(R.id.guesswhat);
        btn_yes = (ImageButton)findViewById(R.id.color_yes);
        btn_no = (ImageButton)findViewById(R.id.color_no);

        ColorPickerView picker = (ColorPickerView) findViewById(R.id.colorPickerView);
        picker.setIndicatorColor(Color.WHITE);
        picker.setOrientation(ColorPickerView.Orientation.HORIZONTAL);
        picker.setColors(Color.RED, Color.rgb(255, 80, 0), Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.rgb(255, 0, 255));
        picker.setOnColorPickerChangeListener(new ColorPickerView.OnColorPickerChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onColorChanged(ColorPickerView picker, int color) {
                // TODO
                color_main = color;
                view.setBackgroundColor(color);
                ColorActivity.this.getWindow().setColorMode(color);
                btn_yes.getBackground().setTint(color);
                btn_no.getBackground().setTint(color);
            }

            @Override
            public void onStartTrackingTouch(ColorPickerView picker) {
                // TODO
            }

            @Override
            public void onStopTrackingTouch(ColorPickerView picker) {
                // TODO
            }
        });

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                ColorActivity.this.getWindow().setColorMode(color_main);
                Intent intent = new Intent();
                intent.putExtra("color", color_main);
                setResult(COLORBK, intent);
                finish();
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("color", ContextCompat.
                        getColor(ColorActivity.this, R.color.colorPrimary));
                setResult(COLORBK, intent);
                finish();
            }
        });
    }
}
