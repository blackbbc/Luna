package me.sweetll.luna.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import luna.annotation.State;
import me.sweetll.luna.R;

public class SecondActivity extends AppCompatActivity {
    @State int testInt;
    @State boolean testBoolean;
    @State float testFloat;
    @State String testString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        testInt = 10;
        testBoolean = true;
        testFloat = 1.2f;
        testString = "Hello World";
    }
}
