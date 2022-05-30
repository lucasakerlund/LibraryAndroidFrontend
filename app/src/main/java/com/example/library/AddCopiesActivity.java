package com.example.library;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.library.abstracts.BackendCaller;
import com.example.library.models.CopyItem;
import com.example.library.models.Library;

import org.w3c.dom.Text;

import java.util.HashMap;

public class AddCopiesActivity extends AppCompatActivity {

    private TextView errorLabel;

    private LinearLayout list;

    private String isbn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_copies);

        errorLabel = findViewById(R.id.add_copies_error);
        list = findViewById(R.id.library_list);
        isbn = getIntent().getStringExtra("isbn");

        setup();
    }

    private void setup(){
        loadContent();
    }

    public void loadContent(){
        list.removeAllViews();
        BackendCaller.inst().getLibraries((libraryList) -> {
            runOnUiThread(() -> {
                for (Library library : libraryList) {
                    addLibrary(library);
                }
            });
        });
    }

    public void addLibrary(Library library){
        View view = getLayoutInflater().inflate(R.layout.library_item, null, false);
        TextView nameLabel = (TextView) view.findViewById(R.id.manage_copies_library_name);
        TextView amountLabel = (TextView) view.findViewById(R.id.add_copies_amount);
        View addButton = (View) view.findViewById(R.id.add_copies_button);
        LinearLayout copiesList = (LinearLayout) view.findViewById(R.id.copies_list);
        nameLabel.setText(library.getName());
        amountLabel.setText("0");
        addButton.setOnClickListener(l -> {
            int amount = Integer.parseInt(amountLabel.getText().toString());
            if(amount <= 0 || amount > 100){
                errorLabel.setText("Antalet böcker måste vara mellan 1-100");
                errorLabel.setVisibility(View.VISIBLE);
                return;
            }
            errorLabel.setVisibility(View.INVISIBLE);
            addCopies(library.getLibraryId(), amount);
        });

        BackendCaller.inst().getCopiesInLibrary(library.getLibraryId(), isbn, (copyItems) -> {
            runOnUiThread(() -> {
                for (CopyItem copyItem : copyItems) {
                    View copy = getLayoutInflater().inflate(R.layout.copies_item, null, false);
                    TextView idLabel = (TextView) copy.findViewById(R.id.copy_id);
                    TextView statusLabel = (TextView) copy.findViewById(R.id.copy_status);
                    View removeButton = (View) copy.findViewById(R.id.copy_remove_button);
                    idLabel.setText(copyItem.getBookId()+"");
                    statusLabel.setText(copyItem.isAvailable() ? "TILLGÄNGLIG" : "UTLÅNAD");
                    removeButton.setEnabled(copyItem.isAvailable());
                    removeButton.setOnClickListener(l -> {
                        //BackendCaller ta bort boken.
                    });
                    copiesList.addView(copy);
                }
            });
        });


        list.addView(view);
    }

    public void addCopies(int libraryId, int amount){
        BackendCaller.inst().addCopies(isbn, libraryId, amount, (b) -> {
            runOnUiThread(() -> {
                if(!b){
                    errorLabel.setText("Något fel inträffade.");
                    errorLabel.setVisibility(View.VISIBLE);
                    return;
                }
                errorLabel.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "Böckerna blev tillagda", Toast.LENGTH_LONG).show();
                loadContent();
            });
        });
    }

}