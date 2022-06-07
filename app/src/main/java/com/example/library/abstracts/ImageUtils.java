package com.example.library.abstracts;

import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageUtils {

    public static void setImage(String image, ImageView view){
        Picasso.get().load(image.equals("") ? "https://cdn.bookauthority.org/dist/images/book-cover-not-available.6b5a104fa66be4eec4fd16aebd34fe04.png" : image).into(view);
    }

}
