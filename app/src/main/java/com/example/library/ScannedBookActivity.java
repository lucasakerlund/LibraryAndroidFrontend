package com.example.library;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.library.abstracts.BackendCaller;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.URL;

public class ScannedBookActivity extends AppCompatActivity {

    private ImageView imageView;

    private TextView errorLabel;

    private TextView bookTitle;
    private TextView bookIsbn;
    private TextView bookPublished;

    private Button rescanButton;
    private Button addButton;

    private String isbn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_scannedbook);

        imageView = findViewById(R.id.imageView);
        errorLabel = findViewById(R.id.bookViewError);
        bookTitle = findViewById(R.id.bookTitleValue);
        bookIsbn = findViewById(R.id.bookIsbnValue);
        bookPublished = findViewById(R.id.bookPublishedValue);
        rescanButton = findViewById(R.id.rescanButton);
        addButton = findViewById(R.id.addButton);

        this.isbn = getIntent().getStringExtra("isbn");

        Picasso.get().load(getIntent().getStringExtra("image")).into(imageView);
        bookTitle.setText(getIntent().getStringExtra("title"));
        bookIsbn.setText(isbn);
        bookPublished.setText(getIntent().getStringExtra("published"));
    }

    public void add(View view){
        BackendCaller.inst().addBook(isbn, (b) -> {

        });
        Intent i = new Intent(this, AddCopiesActivity.class);
        i.putExtra("isbn", getIntent().getStringExtra("isbn"));
        startActivity(i);
    }

    public void rescan(View view){
        onBackPressed();
        finish();
    }

}