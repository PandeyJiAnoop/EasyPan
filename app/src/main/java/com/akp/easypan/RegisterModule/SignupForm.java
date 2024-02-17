package com.akp.easypan.RegisterModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.akp.easypan.R;
import com.akp.easypan.dashboard;

public class SignupForm extends AppCompatActivity {
AppCompatButton reg_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_form);
        reg_btn=findViewById(R.id.reg_btn);
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), dashboard.class);
                startActivity(intent);
            }
        });
    }
}