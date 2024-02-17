package com.akp.easypan;

import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.akp.RetrofitAPI.ApiService;
import com.akp.RetrofitAPI.ConnectToRetrofit;
import com.akp.RetrofitAPI.GlobalAppApis;
import com.akp.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.easypan.RegisterModule.RegisterMobile;
import com.android.volley.RequestQueue;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;

public class NewRegistration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
RelativeLayout header;

    AppCompatButton login_btn;
    TextInputEditText name_et,email_et,mobile_et,aadhar_et,shop_et,pan_et,add_et,state_et,pincode_et,pass_et;
    String checksum;
    private SharedPreferences login_preference;
    private SharedPreferences.Editor login_editor;
    Spinner cat_sp;
    String[] courses = { "RETAILER", "CHANNEL_PARTNER"};
    private String SelectType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_registration);
        findID();
        OnClickEvent();
    }
    private void OnClickEvent() {
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name_et.getText().toString().equalsIgnoreCase("")){
                    name_et.setError("Fields can't be blank!");
                    name_et.requestFocus();
                }
                else if (email_et.getText().toString().equalsIgnoreCase("")){
                    email_et.setError("Fields can't be blank!");
                    email_et.requestFocus();
                }
                else if (mobile_et.getText().toString().equalsIgnoreCase("")){
                    mobile_et.setError("Fields can't be blank!");
                    mobile_et.requestFocus();
                }
                else if (aadhar_et.getText().toString().equalsIgnoreCase("")){
                    aadhar_et.setError("Fields can't be blank!");
                    aadhar_et.requestFocus();
                }
                else if (shop_et.getText().toString().equalsIgnoreCase("")){
                    name_et.setError("Fields can't be blank!");
                    shop_et.requestFocus();
                } else if (pan_et.getText().toString().equalsIgnoreCase("")){
                    pan_et.setError("Fields can't be blank!");
                    pan_et.requestFocus();
                }
                else if (add_et.getText().toString().equalsIgnoreCase("")){
                    add_et.setError("Fields can't be blank!");
                    add_et.requestFocus();
                }
                else if (state_et.getText().toString().equalsIgnoreCase("")){
                    state_et.setError("Fields can't be blank!");
                    state_et.requestFocus();
                }
                else if (pincode_et.getText().toString().equalsIgnoreCase("")){
                    pincode_et.setError("Fields can't be blank!");
                    pincode_et.requestFocus();
                }
                else {
                    String input = mobile_et.getText().toString()+":ip6wgncgt6:nxe7j2f6c0";
                    String strup = input.toUpperCase();
                    checksum = generateMD5Checksum(strup);
                    Log.d("TAG", "UPPER " + strup +"MD5 checksum of " + input + ": " + checksum);
//                    Toast.makeText(LoginScreen.this, "Enter passcode-  12345 ", Toast.LENGTH_LONG).show();
                    RegScreenAPI(mobile_et.getText().toString(),pass_et.getText().toString(),name_et.getText().toString(),email_et.getText().toString(),
                            aadhar_et.getText().toString(),
                            pan_et.getText().toString(),shop_et.getText().toString(),
                            SelectType,pincode_et.getText().toString(),state_et.getText().toString(),add_et.getText().toString(),checksum);
                }
            }
        });

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void findID() {

        login_btn=findViewById(R.id.btn_submit);
        name_et=findViewById(R.id.name_et);
        email_et=findViewById(R.id.email_et);
        mobile_et=findViewById(R.id.mobile_et);
        aadhar_et=findViewById(R.id.aadhar_et);
        shop_et=findViewById(R.id.shop_et);
        pan_et=findViewById(R.id.pan_et);
        add_et=findViewById(R.id.add_et);
        state_et=findViewById(R.id.state_et);
        pincode_et=findViewById(R.id.pincode_et);
        cat_sp=findViewById(R.id.cat_sp);
        header=findViewById(R.id.header);
        pass_et=findViewById(R.id.pass_et);
        cat_sp.setOnItemSelectedListener(this);
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, courses);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cat_sp.setAdapter(ad);

    }


    public void RegScreenAPI(String phone, String password,String fullname,String email,String aadhar,String pan,String shopName,String type,
                             String pincode,String state,String address,String checksum) {
        String otp1 = new GlobalAppApis().RegisterProfile(phone,password,fullname,email,aadhar,pan,
                shopName,type,pincode,state,address,checksum);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_registerProfile(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("API_login","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("status").equalsIgnoreCase("0")){
                        String msg       = object.getString("message");
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    else {
                        String msg       = object.getString("message");
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                        if (msg.equalsIgnoreCase("OTP sent to the registered moble number.")){
                            login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
                            login_editor = login_preference.edit();
                            login_editor.putString("Checksum",checksum);
                            login_editor.putString("mobile",mobile_et.getText().toString());
                            login_editor.commit();
                            Intent intent=new Intent(getApplicationContext(), RegOTPVerify.class);
                            intent.putExtra("mob",mobile_et.getText().toString());
                            intent.putExtra("checksum",checksum);
                            startActivity(intent);
                         }  }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, NewRegistration.this, call1, "", true);
    }


    public static String generateMD5Checksum(String input) {
        try {
            // Create an MD5 MessageDigest instance
            MessageDigest md = MessageDigest.getInstance("MD5");
            // Convert the input string to bytes and feed it to the MessageDigest instance
            md.update(input.getBytes());
            // Generate the MD5 checksum
            byte[] digest = md.digest();
            // Convert the byte array to a hex string
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
        // Performing action when ItemSelected
    // from spinner, Overriding onItemSelected method
    @Override
    public void onItemSelected(AdapterView arg0, View arg1, int position, long id)
    {
        SelectType=cat_sp.getSelectedItem().toString();
//        Toast.makeText(getApplicationContext(),courses[position], Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView arg0)
    {
        // Auto-generated method stub
    }
}