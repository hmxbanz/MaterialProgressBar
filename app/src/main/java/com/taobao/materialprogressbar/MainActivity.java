package com.taobao.materialprogressbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.taobao.library.MaterialProgressBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MaterialProgressBar progressBar = (MaterialProgressBar) findViewById(R.id.pb);
        final EditText et = (EditText) findViewById(R.id.et);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setProgress(Float.parseFloat(et.getText().toString()));
            }
        });
    }

}
