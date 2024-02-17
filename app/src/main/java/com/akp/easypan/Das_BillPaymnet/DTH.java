package com.akp.easypan.Das_BillPaymnet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.RetrofitAPI.ApiService;
import com.akp.RetrofitAPI.ConnectToRetrofit;
import com.akp.RetrofitAPI.GlobalAppApis;
import com.akp.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.easypan.R;
import com.akp.easypan.dashboard;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost;
import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost1;

public class DTH extends AppCompatActivity {
    ImageView ivBack;
    LinearLayout provider_ll;
    TextView provoider_et;
    String service_name,UserId;
    String getProvider_id,roletype;
    Button btnSubmit;
    TextView txtMarquee,view_plan_tv,plan_dis_tv;

    private SharedPreferences sharedPreferences;
    EditText mob_et,amount_et;
    AlertDialog alertDialog;
    String get_operator_ref,get_payid,getonlyservice,GetMobile,Plan_Person_name;
    TextView title_tv,mob_code_tv;

    SearchableSpinner sp_state;
    String stateid; ArrayList<String> StateName = new ArrayList<>();
    ArrayList<String> StateId = new ArrayList<>();
    private String checksum;
    private GpsTracker gpsTracker;
    String tvLatitude,tvLongitude;
    private String Plan_name,Plan_Amount,Plan_duedate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_t_h);
        FindId();
        OnClickListner();
        statusCheck();
    }

    private void OnClickListner() {
        // Now we will call setSelected() method
        // and pass boolean value as true
        txtMarquee.setSelected(true);
        sp_state.setTitle("");
//        Toast.makeText(getApplicationContext(),getonlyservice,Toast.LENGTH_LONG).show();

        String input11 = UserId+":ip6wgncgt6:nxe7j2f6c0";
        String strup11 = input11.toUpperCase();
        checksum = generateMD5Checksum(strup11);


        sp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    for (int j = 0; j <= StateId.size(); j++) {
                        if (sp_state.getSelectedItem().toString().equalsIgnoreCase(StateName.get(j))) {
                            // position = i;
                            stateid = StateId.get(j - 1);
                            break;
                        } } } }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        GetOperatorList(UserId,"dth",checksum);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mob_et.getText().toString().equalsIgnoreCase("")){
                    mob_et.setError("Fields can't be blank!");
                    mob_et.requestFocus();
                }
                else {
                    if (!amount_et.getText().toString().equalsIgnoreCase("")) {
                        Integer intValue = Integer.valueOf((int) Math.round(Double.valueOf(amount_et.getText().toString())));
                        String finalamount = String.valueOf(intValue);
                        Log.d("sdghsfg", "" + UserId + amount_et.getText().toString() + mob_et.getText().toString() + getProvider_id);
                        Payment(UserId, stateid, finalamount, checksum, mob_et.getText().toString(),Plan_Person_name, Plan_duedate,
                                "dth", tvLatitude, tvLongitude);
                    }
                }
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), dashboard.class);
                startActivity(intent);
            }
        });


        view_plan_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mob_et.getText().toString().equalsIgnoreCase("")){
                    mob_et.setError("Fields can't be blank!");
                    mob_et.requestFocus();
                }
                else {
                    GetAppPlanAPI(UserId,stateid,mob_et.getText().toString(),checksum);
                } }});
    }

    private void FindId() {
        title_tv=findViewById(R.id.title_tv);
        mob_code_tv=findViewById(R.id.mob_code_tv);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        GetMobile= sharedPreferences.getString("mobile", "");
        getonlyservice=getIntent().getStringExtra("onlyservice");
        btnSubmit=findViewById(R.id.btnSubmit);
        sp_state=findViewById(R.id.stateet);
        // casting of textview
        txtMarquee = (TextView) findViewById(R.id.marqueeText);
        view_plan_tv=findViewById(R.id.view_plan_tv);
        plan_dis_tv=findViewById(R.id.plan_dis_tv);
        ivBack=findViewById(R.id.ivBack);
        provider_ll=findViewById(R.id.provider_ll);
        provoider_et=findViewById(R.id.provoider_et);
        mob_et=findViewById(R.id.mob_et);
        amount_et=findViewById(R.id.amount_et);
        title_tv.setText(getonlyservice+" Service");

    }


    private void Payment(String user_id, String service_code, String amount,String checksum,String cust_no,String customer_name,
                         String bill_due_date,String type,String latitude,String longitude) {
        String otp1 = new GlobalAppApis().payBillAPI(user_id,service_code,amount,checksum,cust_no,customer_name,bill_due_date,type,
                latitude,longitude);
        Log.e("PostRecharge",otp1);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_payBill(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.e("PostRecharge",result);
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("0")){
                        String msg       = jsonObject.getString("message");
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(DTH.this);
                        builder.setTitle("Response!")
                                .setMessage(msg)
                                .setCancelable(false)
                                .setIcon(R.drawable.logo)
                                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    } });
                        //                        .setNeutralButton("Middle", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(MyActivity.this, "Middle button clicked!", Toast.LENGTH_LONG).show();
//                            }
//                        });
                        builder.create().show();
                    }
                    else {
                        get_operator_ref=jsonObject.getString("message");
                        showpopupwindow();
//                        Intent intent=new Intent(DTH.this,RechargeDetails.class);
//                        intent.putExtra("number",jsonObject.getString("number"));
//                        intent.putExtra("amount",jsonObject.getString("amount"));
//                        intent.putExtra("referenceNo",jsonObject.getString("referenceNo"));
//                        intent.putExtra("transactionId",jsonObject.getString("transactionId"));
//                        intent.putExtra("message",jsonObject.getString("message"));
//                        startActivity(intent);
//                        Toast.makeText(getApplicationContext(),jsonObject.getString("Message"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        }, DTH.this, call1, "", true);
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


    public void GetOperatorList(String uid,String type, String a_checksum) {
        String otp1 = new GlobalAppApis().providerList(uid,type,a_checksum);
        Log.d("dthresotp1","cxc"+otp1);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_providerList(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("dthres","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("status").equalsIgnoreCase("0")){
                        String msg       = object.getString("message");
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    else {
                        StateName.add("Select Operator");
                        JSONArray jsonArray = object.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            StateId.add(jsonObject1.getString("id"));
                            String statename = jsonObject1.getString("name");
                            StateName.add(statename);
                        }
                        sp_state.setAdapter(new ArrayAdapter<String>(DTH.this, android.R.layout.simple_spinner_dropdown_item, StateName));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, DTH.this, call1, "", true);
    }


    //***************Location Status Check***********
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
        else {
            getLocation();
        }
    }
    //***************Location Get Service***********
    public void getLocation(){
        gpsTracker = new GpsTracker(DTH.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            tvLatitude=(String.valueOf(latitude));
            tvLongitude=(String.valueOf(longitude));
//            Toast.makeText(getApplicationContext(),"Lat:-"+tvLatitude+"\n\nLong:- "+tvLongitude,Toast.LENGTH_LONG).show();
        }else{
            gpsTracker.showSettingsAlert();
        }
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(DTH.this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

    }

    private void showpopupwindow() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(DTH.this).inflate(R.layout.successfullycreated_popup, viewGroup, false);
        Button ok = (Button) dialogView.findViewById(R.id.btnDialog);
        TextView txt_msg = dialogView.findViewById(R.id.txt_msg);
        TextView id_tv = dialogView.findViewById(R.id.id_tv);
        TextView pass_tv = dialogView.findViewById(R.id.pass_tv);
        id_tv.setText("Operator_ref- " + get_operator_ref + " (" + UserId + ")");
//        pass_tv.setText("Payid- "+get_payid);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        //Now we need an AlertDialog.Builder object
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();
        alertDialog.show();
    }


    public void GetAppPlanAPI(String user_id,String service_code,String cust_no,String checksum) {
        String otp1 = new GlobalAppApis().fetchBillDetailAPI(user_id,service_code,cust_no,checksum);
        Log.d("fetchBillDetailAPI","cxc"+otp1);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_fetchBillDetail(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("GetAppPlanAPI","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("status").equalsIgnoreCase("0")){
                        String msg       = object.getString("message");
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                        plan_dis_tv.setText(object.getString("message"));
                    }
                    else {
                        JSONObject jsonObject1 = object.getJSONObject("billInfo");
                        plan_dis_tv.setText("Name:- "+jsonObject1.getString("name")
                                +"\nDue Date:- "+jsonObject1.getString("duedate")
                                +"\nAmount:- "+jsonObject1.getString("amount"));
                        Plan_name=jsonObject1.getString("name");
                        Plan_Amount=jsonObject1.getString("amount");
                        Plan_duedate=jsonObject1.getString("duedate");
                        Plan_Person_name=jsonObject1.getString("name");
                        amount_et.setText(Plan_Amount);
                        amount_et.setClickable(false);
                        amount_et.setFocusable(false);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        }, DTH.this, call1, "", true);
    }


}