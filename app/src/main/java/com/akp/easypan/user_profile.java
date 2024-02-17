package com.akp.easypan;
/**
 * Created by Anoop Pandey on 9696381023.
 */
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.RetrofitAPI.ApiService;
import com.akp.RetrofitAPI.ConnectToRetrofit;
import com.akp.RetrofitAPI.GlobalAppApis;
import com.akp.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.easypan.entity.User;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.akp.easypan.Util.Api_Urls;
import com.akp.easypan.Util.Preferences;
import com.bumptech.glide.Glide;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost;

public class user_profile extends AppCompatActivity {
    EditText fullnameet,emailet,aadharet,panet,firm_nameet,pincodeet,addresset;
    TextView tv_mob_No,et_Name;
    Context context;
    Preferences pref;
    Button save;
    SearchableSpinner stateet;
    ArrayList<String> ProductSizeValue = new ArrayList<>();

    String UserId;
    String checksum;
    CircleImageView profile_image;
    private SharedPreferences sharedPreferences;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        context=this.getApplicationContext();
        pref=new Preferences(context);


        findView();

        findViewById(R.id.menuImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fullnameet.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Update Profile",Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent=new Intent(getApplicationContext(),dashboard.class);
                    startActivity(intent);
                } }});


        GetStateList();
        String input = UserId+":ip6wgncgt6:nxe7j2f6c0";
        String strup = input.toUpperCase();
        checksum = generateMD5Checksum(strup);
        Log.d("TAG", "UPPER " + strup +"MD5 checksum of " + input + ": " + checksum);

        stateet.setTitle("");
        GetProfile(UserId,checksum);
    }
    public void findView(){
        et_Name=findViewById(R.id.et_Name);
        tv_mob_No=findViewById(R.id.tv_mob_No);
        fullnameet=findViewById(R.id.fullnameet);
        emailet=findViewById(R.id.emailet);
        aadharet=findViewById(R.id.aadharet);
        panet=findViewById(R.id.panet);
        firm_nameet=findViewById(R.id.firm_nameet);
        pincodeet=findViewById(R.id.pincodeet);

        addresset=findViewById(R.id.addresset);
        save=findViewById(R.id.save);

        profile_image=findViewById(R.id.profile_image);
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        stateet=findViewById(R.id.stateet);
        stateet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String imc_met=stateet.getSelectedItem().toString();
//                Toast.makeText(Oder_Summery_Details.this, imc_met + " selected size", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fullnameet.getText().toString().equalsIgnoreCase("")){
                    fullnameet.setError("Fields can't be blank!");
                    fullnameet.requestFocus();
                }
                else if (emailet.getText().toString().equalsIgnoreCase("")){
                    emailet.setError("Fields can't be blank!");
                    emailet.requestFocus();
                }
                else if (aadharet.getText().toString().equalsIgnoreCase("")){
                    aadharet.setError("Fields can't be blank!");
                    aadharet.requestFocus();
                }
                else if (panet.getText().toString().equalsIgnoreCase("")){
                    panet.setError("Fields can't be blank!");
                    panet.requestFocus();
                }
                else if (firm_nameet.getText().toString().equalsIgnoreCase("")){
                    firm_nameet.setError("Fields can't be blank!");
                    firm_nameet.requestFocus();
                }
                else  if (pincodeet.getText().toString().equalsIgnoreCase("")){
                    pincodeet.setError("Fields can't be blank!");
                    pincodeet.requestFocus();
                } else  if (addresset.getText().toString().equalsIgnoreCase("")){
                    addresset.setError("Fields can't be blank!");
                    addresset.requestFocus();
                }
                else {
                    UpdateProfile(UserId,fullnameet.getText().toString(),emailet.getText().toString(),aadharet.getText().toString(),
                            panet.getText().toString(),firm_nameet.getText().toString(),
                            pincodeet.getText().toString(),stateet.getSelectedItem().toString(),
                            addresset.getText().toString(),temp,checksum);
//                    if (profile_image.getDrawable() == null){
//                        UpdateProfile(UserId,fullnameet.getText().toString(),emailet.getText().toString(),aadharet.getText().toString(),
//                                panet.getText().toString(),firm_nameet.getText().toString(),
//                                pincodeet.getText().toString(),stateet.getSelectedItem().toString(),
//                                addresset.getText().toString(),"",checksum);
//                    }
//                    else {
//                        UpdateProfile(UserId,fullnameet.getText().toString(),emailet.getText().toString(),aadharet.getText().toString(),
//                                panet.getText().toString(),firm_nameet.getText().toString(),
//                                pincodeet.getText().toString(),stateet.getSelectedItem().toString(),
//                                addresset.getText().toString(),temp,checksum);
//                    }


                }

            }
        });
    }


    public void GetStateList() {
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_getStateList();
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("res","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("status").equalsIgnoreCase("0")){
                        String msg       = object.getString("message");
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    else {
                        JSONArray jsonArray = object.getJSONArray("state_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String value = jsonArray.getString(i);
                            ProductSizeValue.add(value);
//                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                            String productSizeValue = jsonObject1.getString("ProductSizeValue");
//                            ProductSizeValue.add(productSizeValue);
                        }
                        stateet.setAdapter(new ArrayAdapter<String>(user_profile.this, android.R.layout.simple_spinner_dropdown_item, ProductSizeValue));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, user_profile.this, call1, "", true);
    }




    public void UpdateProfile(String user_id,String fullname,String email,String aadhar,String pan,String firm_name,String pincode,String state,String address,String profile_image,String checksum ) {
        String otp1 = new GlobalAppApis().UpdateProfile(user_id,fullname,email,aadhar,pan,firm_name,pincode,state,address,profile_image,checksum);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_updateProfile(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("resupdate","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("status").equalsIgnoreCase("0")){
                        String msg       = object.getString("message");
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    else {
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);

                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, user_profile.this, call1, "", true);
    }





    public void GetProfile(String user_id,String checksum) {
        String otp1 = new GlobalAppApis().GetProfile(user_id,checksum);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_getProfile(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("res","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("status").equalsIgnoreCase("0")){
                        String msg       = object.getString("message");
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    else {
                        String fullname       = object.getString("fullname");
                        String email       = object.getString("email");
                        String phone       = object.getString("phone");

                        String aadhar       = object.getString("aadhar");
                        String pan       = object.getString("pan");
                        String firm_name       = object.getString("firm_name");

                        String pincode       = object.getString("pincode");
                        String state       = object.getString("state");
                        String address       = object.getString("address");
                        String profile       = object.getString("profile_image");
                        if (object.getString("profile_image").equalsIgnoreCase("")){
                        }
                        else {
                            Glide.with(user_profile.this).load(object.getString("profile_image")).into(profile_image);
                        }

//                        if (state == null){
//                        }
//                        else {
//                            ProductSizeValue.add(state);
//                            stateet.setEnabled(false);
//                        }

                        et_Name.setText("User Id-("+UserId+")");
                        tv_mob_No.setText("+91- "+phone);
                        fullnameet.setText(fullname);
                        emailet.setText(email);
                        aadharet.setText(aadhar);
                        panet.setText(pan);
                        firm_nameet.setText(firm_name);
                        pincodeet.setText(pincode);
                        addresset.setText(address);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, user_profile.this, call1, "", true);
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



    private void selectImage() {
        final CharSequence[] items = {"Choose from Library",
                "Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(user_profile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(user_profile.this);
                if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        Toast.makeText(getApplicationContext(),""+bm,Toast.LENGTH_LONG).show();
        profile_image.setImageBitmap(bm);
        Bitmap immagex=bm;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] b = baos.toByteArray();
        temp = Base64.encodeToString(b,Base64.DEFAULT);
    }

    @Override
    public void onBackPressed() {

    }
}