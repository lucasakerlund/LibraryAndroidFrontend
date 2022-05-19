package com.example.library;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.library.abstracts.BackendCaller;

public class MainActivity extends AppCompatActivity {

    private static MainActivity instance;

    private EditText emailInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);

    }

    public static MainActivity inst(){
        return instance;
    }

    public void login(View view){
        view.setEnabled(false);
        new Thread(() -> {
            BackendCaller.inst().test();
        }).start();
        ((Button)view).setText("Loggar in");
    }

}