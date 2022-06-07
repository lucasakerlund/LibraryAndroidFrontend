package com.example.library;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.library.abstracts.BackendCaller;
import com.example.library.abstracts.Display;
import com.example.library.abstracts.ImageUtils;
import com.example.library.components.SearchBar;
import com.example.library.models.Book;
import com.example.library.models.Library;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BooksActivity extends AppCompatActivity {

    private SearchBar searchBar;
    private ImageButton expandButton;
    private ConstraintLayout filterView;

    private Spinner searchTypeChoice;
    private Spinner libraryChoice;
    private EditText dateChoice;
    private Spinner popularityChoice;
    private Spinner languageChoice;

    private LinearLayout list;

    private boolean filterShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_books);

        searchBar = findViewById(R.id.searchView);
        expandButton = findViewById(R.id.expandButton);
        filterView = findViewById(R.id.filterView);
        searchTypeChoice = findViewById(R.id.searchTypeChoice);
        libraryChoice = findViewById(R.id.libraryChoice);
        dateChoice = findViewById(R.id.dateChoice);
        popularityChoice = findViewById(R.id.popularityChoice);
        languageChoice = findViewById(R.id.languageChoice);
        list = findViewById(R.id.bookList);

        setup();
    }

    private void setup(){
        String[] popularites = {"Ingen sortering", "Någonsin", "År", "Månad", "Vecka"};
        ArrayAdapter<String> popularAdp = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, popularites);
        popularAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        popularityChoice.setAdapter(popularAdp);

        String[] langs = {"Alla", "Svenska", "Engelska"};
        ArrayAdapter<String> langAdp = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, langs);
        langAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageChoice.setAdapter(langAdp);

        String[] searchByItems = {"Titel", "Författare"};
        ArrayAdapter<String> searchByAdp = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, searchByItems);
        searchByAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchTypeChoice.setAdapter(searchByAdp);

        BackendCaller.inst().getLibraries((list) -> {
            runOnUiThread(() -> {
                List<String> libraries = new ArrayList<>();
                libraries.add("Alla");
                for (Library library : list) {
                    libraries.add(library.getName());
                }
                ArrayAdapter<String> libraryAdp = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, libraries);
                libraryAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                libraryChoice.setAdapter(libraryAdp);
                loadBooks();
            });
        });

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchBar.clearFocus();
                loadBooks();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private String translateLanguageChoice(String choice){
        switch(choice.toLowerCase()){
            case "alla":
                return "";
            case "svenska":
                return "SE";
            case "engelska":
                return "EN";
        }
        return "";
    }

    private String translatePopularityChoice(String choice){
        switch(choice.toLowerCase()){
            case "populäritet":
            case "ingen sortering":
                return "";
            case "någonsin":
                return "ALL_TIME";
            case "år":
                return "YEAR";
            case "månad":
                return "MONTH";
            case "vecka":
                return "WEEK";
        }
        return "";
    }

    public void loadBooks(){
        list.removeAllViews();
        String lang = translateLanguageChoice(languageChoice.getSelectedItem().toString());
        String popularSort = translatePopularityChoice(popularityChoice.getSelectedItem().toString());
        if(!correctDateFormat()){
            Display.toast(this, "Formatet på datumet måste vara yyyy-MM-dd", Color.RED);
            return;
        }
        BackendCaller.inst().getBooks(
                lang,
                dateChoice.getText().toString(),
                libraryChoice.getSelectedItem().toString().equalsIgnoreCase("alla") ? "" : libraryChoice.getSelectedItem().toString(),
                searchTypeChoice.getSelectedItem().toString().toLowerCase(),
                searchBar.getQuery().toString(),
                popularSort,
                (books) -> {
                    runOnUiThread(() -> {
                        for (Book book : books) {
                            View view = getLayoutInflater().inflate(R.layout.books_book_item, null, false);
                            TextView titleLabel = view.findViewById(R.id.books_book_title);
                            ImageView imageView = view.findViewById(R.id.books_book_image);
                            ImageUtils.setImage(book.getImageSrc(), imageView);
                            titleLabel.setText(book.getTitle());
                            imageView.setOnClickListener(e -> {
                                Intent i = new Intent(this, AddCopiesActivity.class);
                                i.putExtra("isbn", book.getIsbn());
                                startActivity(i);
                            });

                            list.addView(view);
                        }
                    });
                });
    }

    public void expand(View view){
        if(filterShowing){
            filterView.setVisibility(View.INVISIBLE);
            expandButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24);
        }else {
            filterView.setVisibility(View.VISIBLE);
            expandButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
        }
        filterShowing = !filterShowing;
    }

    private boolean correctDateFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        if(dateChoice.getText().toString().equals("")){
            return true;
        }
        try {
            sdf.parse(dateChoice.getText().toString());
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}