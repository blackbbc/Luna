package me.sweetll.luna.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import luna.annotation.State;
import me.sweetll.luna.R;

public class MainActivity extends AppCompatActivity {
    @State int testInt;
    @State Integer testInteger;
    @State boolean testBoolean;
    @State float testFloat;
    @State String testString;
    @State Product product;
    @State ArrayList<Integer> integers;
    @State List<String> strings;
    @State List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LunaMainActivity.onRestoreInstanceState(this, savedInstanceState);

        testInt = 10;
        testInteger = 100;
        testBoolean = true;
        testFloat = 1.2f;
        testString = "Hello World";
        product = new Product("iPhone", 1000);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LunaMainActivity.onSaveInstanceState(this, outState);
    }
}
