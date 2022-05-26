package com.example.library;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.library.abstracts.BackendCaller;
import com.example.library.abstracts.StaffDetails;
import com.example.library.models.Staff;

public class MainActivity extends AppCompatActivity {

    private static MainActivity instance;

    private TextView errorLabel;
    private EditText usernameInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        errorLabel = findViewById(R.id.errorLabel);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);

    }

    public static MainActivity inst(){
        return instance;
    }

    public void login(View view){
        view.setEnabled(false);
        BackendCaller.inst().loginCustomer(usernameInput.getText().toString(), passwordInput.getText().toString(), (staff) -> {
            if(staff == null){
                this.runOnUiThread(() -> {
                    view.setEnabled(true);
                    errorLabel.setVisibility(View.VISIBLE);
                });
                return;
            }
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
            this.runOnUiThread(() -> {
                usernameInput.setText("");
                passwordInput.setText("");
                errorLabel.setVisibility(View.INVISIBLE);
                usernameInput.requestFocus();
                view.setEnabled(true);
            });
        });
        ((Button)view).setText("Loggar in");
    }

}