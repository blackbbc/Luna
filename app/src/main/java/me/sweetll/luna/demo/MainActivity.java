package me.sweetll.luna.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import luna.annotation.State;
import me.sweetll.luna.R;

public class MainActivity extends AppCompatActivity {
    int testInt;
    Integer testInteger;
    boolean testBoolean;
    float testFloat;
    String testString;
    Product product;
    @State List<Integer> integers;
    @State List<String> strings;
    @State List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        LunaMainActivity.onSaveInstanceState(this, savedInstanceState);

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
//        LunaMainActivity.onRestoreInstanceState(this, outState);
    }


//    class LunaMainActivity {
//        public void onSaveInstanceState(Bundle outStatue) {
//            outStatue.putInt("testInt", testInt);
//            outStatue.putBoolean("testBoolean", testBoolean);
//            outStatue.putFloat("testFloat", testFloat);
//            outStatue.putString("testString", testString);
//        }

//        public void onRestoreInstanceState(MainActivity activity, Bundle bundle) {
//            activity.testInt = bundle.getInt("testInt");
//            activity.testBoolean = bundle.getBoolean("testBoolean");
//            activity.testFloat = bundle.getFloat("testFloat");
//            activity.testString = bundle.getString("testString");
//        }
//    }
}
