package com.example.library;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.library.abstracts.BackendCaller;
import com.example.library.abstracts.StaffDetails;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class HomeActivity extends AppCompatActivity {

    private TextView greetingLabel;
    private Button booksButton;
    private Button scanBarcode;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_home);

        greetingLabel = findViewById(R.id.greetingLabel);
        scanBarcode = findViewById(R.id.booksButton);
        logout = findViewById(R.id.logout);

        greetingLabel.setText("Välkommen " + StaffDetails.inst().getStaff().getFirstName());
    }

    public void goToBooks(View view){
        Intent i = new Intent(this, BooksActivity.class);
        startActivity(i);
    }

    public void scan(View view){
        scanCode();
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null){
            BackendCaller.inst().getBookInformation(result.getContents(), (b) -> {
                if(b == null){
                    runOnUiThread(() -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                        builder.setTitle("Hittade ingen bok :(");
                        builder.setMessage(result.getContents());
                        builder.setNegativeButton("Skanna om", ((dialogInterface, i) -> {
                            scanCode();
                        })).show();
                    });
                    return;
                }
                Intent i = new Intent(this, ScannedBookActivity.class);
                i.putExtra("title", b.getTitle());
                i.putExtra("isbn", b.getIsbn());
                i.putExtra("published", b.getPublished());
                i.putExtra("image", b.getImageSrc());
                startActivity(i);
            });
        }
    });

    public void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    public void logout(View view){
        StaffDetails.inst().setStaff(null);
        finish();
    }

}