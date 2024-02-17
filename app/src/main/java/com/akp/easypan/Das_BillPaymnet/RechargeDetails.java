package com.akp.easypan.Das_BillPaymnet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.akp.easypan.R;
import com.akp.easypan.dashboard;

public class RechargeDetails extends AppCompatActivity {
    String getNumber,getAmount,getRefNo,getTxtId,getMessage,getoperator;
    TextView tv,tv1,tv2,tv3,tv4,tv5;

    ImageView ivBack;

Button btnSubmit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_details);

        getNumber=getIntent().getStringExtra("number");
        getAmount=getIntent().getStringExtra("amount");
        getRefNo=getIntent().getStringExtra("referenceNo");
        getTxtId=getIntent().getStringExtra("transactionId");
        getMessage=getIntent().getStringExtra("message");
        getoperator=getIntent().getStringExtra("operator");

        findViewId();

        tv.setText(getNumber);
        tv5.setText(getoperator);
        tv1.setText(getAmount);
        tv2.setText(getRefNo);
        tv3.setText(getTxtId);
        tv4.setText("Wallet");




    }

    private void findViewId() {
        tv=findViewById(R.id.tv);
        tv1=findViewById(R.id.tv1);
        tv2=findViewById(R.id.tv2);
        tv3=findViewById(R.id.tv3);
        tv4=findViewById(R.id.tv4);
        tv5=findViewById(R.id.tv5);
        ivBack=findViewById(R.id.ivBack);
        btnSubmit=findViewById(R.id.btnSubmit);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), dashboard.class);
                startActivity(intent);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), dashboard.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {

    }
}