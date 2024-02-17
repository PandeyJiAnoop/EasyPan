package com.akp.easypan;

import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.RetrofitAPI.ApiService;
import com.akp.RetrofitAPI.ConnectToRetrofit;
import com.akp.RetrofitAPI.GlobalAppApis;
import com.akp.RetrofitAPI.RetrofitCallBackListenar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;

public class RegOTPVerify extends AppCompatActivity {
    TextView tvMobileNo, tvResend;
    //EditText
    EditText etCode1, etCode2, etCode3, etCode4,etCode5,etCode6;
    int check = 0;
    TextView tvTimerText,tvOtp;
    //Button
    Button btnSubmit;
    String Checksum,Mobile;
    private SharedPreferences login_preference;
    private SharedPreferences.Editor login_editor;

    RelativeLayout main_ll;
    TextView tvResendOtp;
    CountDownTimer startTimer;
    String otp;

    ImageView fb,telegram;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_otpverify);
        FindID();
        ClickListner();

    }

    private void ClickListner() {
        startTimer();
        etCode1.addTextChangedListener( new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) etCode2.requestFocus();
            }
        } );
        etCode2.addTextChangedListener( new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) etCode3.requestFocus();
            }
        } );
        etCode3.addTextChangedListener( new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) etCode4.requestFocus();
            }
        } );
        etCode4.addTextChangedListener( new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) etCode5.requestFocus();
            }
        } );
        etCode5.addTextChangedListener( new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) etCode6.requestFocus();
            }
        } );
        etCode6.addTextChangedListener( new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        } );
        etCode1.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (etCode1.getText().toString().trim().isEmpty()) {
                        etCode1.requestFocus();
                    } }
                return false;
            }
        } );
        etCode2.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (etCode2.getText().toString().trim().isEmpty()) {
                        etCode1.requestFocus();
                    } }
                return false;
            }
        } );
        etCode3.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (etCode3.getText().toString().trim().isEmpty()) {
                        etCode2.requestFocus();
                    } }
                return false;
            }
        } );
        etCode4.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (etCode4.getText().toString().trim().isEmpty()) {
                        etCode3.requestFocus();
                    } }
                return false;
            }
        } );
        etCode5.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // You can identify which key pressed buy checking keyCode value
                // with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    // this is for backspace
                    if (etCode5.getText().toString().trim().isEmpty()) {
                        etCode4.requestFocus();
                    }
                }
                return false;
            }
        } );
        etCode6.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // You can identify which key pressed buy checking keyCode value
                // with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    // this is for backspace
                    if (etCode6.getText().toString().trim().isEmpty()) {
                        etCode5.requestFocus();
                    }
                }
                return false;
            }
        } );
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp = etCode1.getText().toString().trim() +
                        etCode2.getText().toString().trim() +
                        etCode3.getText().toString().trim() +
                        etCode4.getText().toString().trim()+etCode5.getText().toString().trim()+etCode6.getText().toString().trim();
                if (otp.length() != 6) {
                    AppUtils.showToastSort( getApplicationContext(),"Enter Valid OTP");
                } else {

                    String input = Mobile+":ip6wgncgt6:nxe7j2f6c0";
                    String strup = input.toUpperCase();
                    String checksum = generateMD5Checksum(strup);
                    Log.d("TAG", "UPPER " + strup +"MD5 checksum of " + input + ": " + checksum);



                    OtpverifyAPI(Mobile,otp,checksum);
//                    if (ReferralCode == null){
//                        Otpverify(otp,"");
//
//                    }
//                    else {
//                        Otpverify(otp,ReferralCode);
//
//                    }
                }
            }


        });
    }

    private void FindID() {
        tvResendOtp=findViewById(R.id.tvResendOtp);
        Checksum = getIntent().getStringExtra("checksum");
        Mobile = getIntent().getStringExtra("mob");

//        ReferralCode = getIntent().getStringExtra("ref");
//        Toast.makeText(getApplicationContext(),Get_OTP,Toast.LENGTH_LONG).show();
        main_ll=findViewById(R.id.main_rl);
        //Button
        btnSubmit = findViewById( R.id.btnSubmit );
        fb = findViewById( R.id.fb );
        telegram = findViewById( R.id.telegram );

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp("7068236202","Welcome To EasyPan");
            }
        });
        telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.eazypan.in/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });



        //EditText
        etCode1 = findViewById(R.id.etCode1);
        etCode2 = findViewById(R.id.etCode2);
        etCode3 = findViewById(R.id.etCode3);
        etCode4 = findViewById(R.id.etCode4);
        etCode5 = findViewById( R.id.etCode5 );
        etCode6 = findViewById( R.id.etCode6 );
        tvOtp = findViewById( R.id.tvOtp );
        tvMobileNo = findViewById( R.id.tvMobileNo );
        tvOtp.setText("Your Otp " + Checksum);
        tvMobileNo.setText("+91 " +Mobile );
    }


    public void OtpverifyAPI(String user_id, String otp,String checksum) {
        String otp1 = new GlobalAppApis().VerifyRegisterOtp(user_id,otp,checksum);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_verifyRegisterOtp(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("OtpverifyAPI","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("status").equalsIgnoreCase("0")){
                        String msg       = object.getString("message");
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    else {
                        String msg       = object.getString("message");
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getApplicationContext(), MainActivityLogin.class);
                        startActivity(intent);
                        Snackbar.make(main_ll, object.getString("message"), Snackbar.LENGTH_LONG).setAction("Action", null).show();}
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, RegOTPVerify.this, call1, "", true);
    }






    private void startTimer() {
        startTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                long sec = (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                tvResendOtp.setText(String.format("( %02d SEC LEFT)", sec));
                if(sec == 1)
                {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tvResendOtp.setText("( 00 SEC LEFT)");
                        }
                    }, 1000);
                }
            }

            public void onFinish() {
                tvResendOtp.setText("Resend OTP");
                tvResendOtp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String input = Mobile+":ip6wgncgt6:nxe7j2f6c0";
                        String strup = input.toUpperCase();
                        String checksum = generateMD5Checksum(strup);
                        Log.d("TAG", "UPPER " + strup +"MD5 checksum of " + input + ": " + checksum);
                        ResendAPI(Mobile,checksum);
//                        if (ReferralCode == null){
//                            ResendOtp("");
//                        }
//                        else {
//                            ResendOtp(ReferralCode);
//                        }
                    }
                });
            }
        }.start();
    }









    public void ResendAPI(String user_id,String checksum) {
        String otp1 = new GlobalAppApis().ResendRegisterOtp(user_id,checksum);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_resendRegisterOtp(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("res","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("status").equalsIgnoreCase("0")){
                        String msg    = object.getString("message");
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    else {
                        String msg   = object.getString("message");
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                        Snackbar.make(main_ll, object.getString("message"), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, RegOTPVerify.this, call1, "", true);
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




    //Whatsap intent akp
    private void openWhatsApp(String numero,String mensaje){
        try{
            PackageManager packageManager = getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone="+ numero +"&text=" + URLEncoder.encode(mensaje, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            }else {
                Toast.makeText(getApplicationContext(),"Whatsapp Not Installed!",Toast.LENGTH_LONG).show();
            }
        } catch(Exception e) {
            Log.e("ERROR WHATSAPP",e.toString());
            Toast.makeText(getApplicationContext(),"Whatsapp Not Installed!",Toast.LENGTH_LONG).show();
        } }



}