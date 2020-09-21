package com.example.layout_behavior_test;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class MyTextView extends TextView {
    public MyTextView(@NonNull Context context) {
        super(context);

        this.setTextSize(50);
        this.setText("Create TextView");
    }
}
