package com.example.library;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.URL;

public class ScannedbookActivity extends AppCompatActivity {

    private ImageView imageView;

    private TextView bookTitle;
    private TextView bookIsbn;
    private TextView bookPublished;

    private Button rescanButton;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scannedbook);

        imageView = findViewById(R.id.imageView);
        bookTitle = findViewById(R.id.bookTitleValue);
        bookIsbn = findViewById(R.id.bookIsbnValue);
        bookPublished = findViewById(R.id.bookPublishedValue);
        rescanButton = findViewById(R.id.rescanButton);
        addButton = findViewById(R.id.addButton);

        Picasso.get().load(getIntent().getStringExtra("image")).into(imageView);
        bookTitle.setText(getIntent().getStringExtra("title"));
        bookIsbn.setText(getIntent().getStringExtra("isbn"));
        bookPublished.setText(getIntent().getStringExtra("published"));
    }

    public void add(View view){

    }

    public void rescan(View view){
        onBackPressed();
        finish();
    }

}