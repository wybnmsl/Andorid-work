package com.example.paranoidandroid;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private Button btn;
    private TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button)findViewById(R.id.btn1);
        btn.setOnClickListener(new mClick());

        txt = (TextView)findViewById(R.id.txt1);
    }

    class mClick implements View.OnClickListener
    {
        public void onClick(View v)
        {
            MainActivity.this.setTitle(R.string.hello);
            txt.setText("here");
            txt.setBackgroundColor(Color.YELLOW);
            txt.setTextColor(getResources().getColor(R.color.TitleColor));
        }
    }
}
