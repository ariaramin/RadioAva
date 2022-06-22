package com.ariaramin.radioava.utils;

import android.content.Context;
import android.widget.Toast;


public final class Constants {


    public static void raiseError(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
