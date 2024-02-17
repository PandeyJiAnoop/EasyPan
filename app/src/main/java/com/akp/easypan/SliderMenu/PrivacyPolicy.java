package com.akp.easypan.SliderMenu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.akp.easypan.R;

public class PrivacyPolicy extends AppCompatActivity {
ImageView sliding_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        sliding_menu=findViewById(R.id.sliding_menu);
        sliding_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}