package com.example.library.components;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.SearchView;

public class SearchBar extends SearchView {

    public SearchBar(Context context) {
        super(context);
    }

    public SearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnQueryTextListener(OnQueryTextListener listener) {
        super.setOnQueryTextListener(listener);
        SearchView.SearchAutoComplete mSearchSrcTextView = this.findViewById(androidx.appcompat.R.id.search_src_text);
        mSearchSrcTextView.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(listener != null){
                listener.onQueryTextSubmit(getQuery().toString());
            }
            return true;
        });
    }
}
