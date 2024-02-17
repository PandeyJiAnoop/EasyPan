package com.akp.easypan.Das_KYCBlock;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.akp.RetrofitAPI.ApiService;
import com.akp.RetrofitAPI.ConnectToRetrofit;
import com.akp.RetrofitAPI.GlobalAppApis;
import com.akp.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.easypan.Das_FindLostDocuments.FL_AyushmanPrint;
import com.akp.easypan.R;
import com.akp.easypan.fragments.Das_UTI;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;

import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost;

public class NSDL_KYC_Form extends AppCompatActivity {
    EditText number_et,name_et;
    Button btn_submit;
    private String UserId,Getpan_type,Getpan_name,Getpan_mob,Mobile;
    String checksum_paytm;
    RelativeLayout header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_n_s_d_l__k_y_c__form);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        Getpan_type=getIntent().getStringExtra("pan_type");
        Getpan_name=getIntent().getStringExtra("pan_name");
        Getpan_mob=getIntent().getStringExtra("pan_mob");
        Mobile = sharedPreferences.getString("mobile", "");

        String input1 = UserId + ":" + Mobile + ":ip6wgncgt6:nxe7j2f6c0";
        String strup1 = input1.toUpperCase();
        checksum_paytm = generateMD5Checksum(strup1);
        Log.d("TAG", "UPPER " + strup1 + "MD5 checksum of " + input1 + ": " + checksum_paytm);


        btn_submit=findViewById(R.id.btn_submit);
        number_et=findViewById(R.id.number_et);
        name_et=findViewById(R.id.name_et);
        header=findViewById(R.id.header);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number_et.getText().toString().equalsIgnoreCase("")){
                    number_et.setError("Fields can't be blank!");
                    number_et.requestFocus();
                }
                else if (name_et.getText().toString().equalsIgnoreCase("")){
                    name_et.setError("Fields can't be blank!");
                    name_et.requestFocus();
                }
                else {
                    EKycNsdlPanApply(UserId,Mobile,Getpan_name,number_et.getText().toString(),name_et.getText().toString(),Getpan_type,checksum_paytm);
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

    public void EKycNsdlPanApply(String id,String phone,String name,String cust_phone,String cust_name,String pan_type,String checksum) {
        String otp1 = new GlobalAppApis().EKycNsdlPanApplyAPI(id, phone,name,cust_phone,cust_name,pan_type,checksum);
        Log.d("UTIAppAutoLoginUrl", "cxc" + otp1);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_eKycNsdlPanApply(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("UTIAppAutoLoginUrl", "cxc" + result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("status").equalsIgnoreCase("0")) {
                        String msg = object.getString("message");
                        Toast.makeText(NSDL_KYC_Form.this, object.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        JSONObject reqEntityData = object.getJSONObject("reqEntityData");
                        String txnid = reqEntityData.getString("txnid");
                        String reqTs = reqEntityData.getString("reqTs");
                        String entityId = reqEntityData.getString("entityId");
                        String dscProvider = reqEntityData.getString("dscProvider");
                        String dscSerialNumber = reqEntityData.getString("dscSerialNumber");
                        String dscExpiryDate = reqEntityData.getString("dscExpiryDate");
                        String returnUrl = reqEntityData.getString("returnUrl");
                        String formData = reqEntityData.getString("formData");
                        String authKey = reqEntityData.getString("authKey");

                        String signature = object.getString("signature");
                        JSONObject combinedObject = new JSONObject();
                        combinedObject.put("reqEntityData", reqEntityData);
                        combinedObject.put("signature", signature);

                        String combinedString = combinedObject.toString();

                        // on below line displaying a toast message if maps is installed.
                        String packageName = "protean.assisted.ekyc";
                        String className = "protean.assisted.ekyc.MainActivity";
                        Intent intent = new Intent();
                        intent.setClassName(packageName, className);
                        intent.putExtra("data", combinedString);
                        Log.d("combinedString", combinedString);
                        startActivityForResult(intent, 2);

                     /*   // on below line we are checking if the google maps is installed or not by specifying package name of google maps.
                        if (checkInstallation(NSDL_KYC_Form.this, "protean.assisted.ekyc")) {
                            // on below line displaying a toast message if maps is installed.
                            String packageName = "protean.assisted.ekyc";
                            String className = "protean.assisted.ekyc.MainActivity";
                            Intent intent = new Intent();
                            intent.setClassName(packageName, className);
                            intent.putExtra("data", combinedString);
                            Log.d("combinedString", combinedString);
                            startActivityForResult(intent, 2);
                        } else {
                            try {
                                Intent viewIntent =
                                        new Intent("android.intent.action.VIEW",
                                                Uri.parse("https://play.google.com/store/apps/details?id=protean.assisted.ekyc"));
                                startActivity(viewIntent);
                            }catch(Exception e) {
                                Toast.makeText(getApplicationContext(),"Unable to Connect Try Again...",
                                        Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                            // on below line displaying toast message if google maps is not installed.
                            Toast.makeText(NSDL_KYC_Form.this, "PAN Service Agency APP is not installed on this device..", Toast.LENGTH_LONG).show();
                        }*/




                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, NSDL_KYC_Form.this, call1, "", true);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {
                String requredText = data.getExtras().getString("assisted_status");
                AlertDialog.Builder builder = new AlertDialog.Builder(NSDL_KYC_Form.this);
                builder.setTitle("Response:- ")
                        .setMessage(requredText)
                        .setCancelable(false)
                        .setIcon(R.drawable.logo)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.create().show();
                Log.d("req_data",requredText);
            }
        }
    }



    public static boolean checkInstallation(Context context, String packageName) {
        // on below line creating a variable for package manager.
        PackageManager pm = context.getPackageManager();
        try {
            // on below line getting package info
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            // on below line returning true if package is installed.
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            // returning false if package is not installed on device.
            return false;
        }
    }
}

