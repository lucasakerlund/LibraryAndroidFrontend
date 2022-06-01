package com.example.library.abstracts;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Display {

    public static void toast(Activity activity, String message, int color){
        activity.runOnUiThread(() -> {
            Toast toast = Toast.makeText(activity, message, Toast.LENGTH_LONG);
            View view = toast.getView();
            TextView text = view.findViewById(android.R.id.message);
            text.setTextColor(color);
            toast.show();
        });
    }

}
