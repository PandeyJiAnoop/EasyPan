package com.akp.easypan.Das_FindLostDocuments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.RetrofitAPI.ApiService;
import com.akp.RetrofitAPI.ConnectToRetrofit;
import com.akp.RetrofitAPI.GlobalAppApis;
import com.akp.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.easypan.MainActivityLogin;
import com.akp.easypan.OTPVerify;
import com.akp.easypan.Passbook;
import com.akp.easypan.R;
import com.akp.easypan.entity.User;
import com.akp.easypan.user_profile;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost;

public class FL_FindPanByAdhar extends AppCompatActivity {

    ArrayList<String> StateName = new ArrayList<>();
    ArrayList<String> StateId = new ArrayList<>();

    ArrayList<String> StateName1 = new ArrayList<>();
    ArrayList<String> StateId1 = new ArrayList<>();

    ArrayList<HashMap<String, String>> arrLegalList = new ArrayList<>();
    LegalListAdapter pdfAdapTer;
    SearchableSpinner sp_state;
    SearchableSpinner spinner;
    String stateid,stateid1,UserId,Mobile,checksum_paytm,checksum_paytm1;
    EditText mob_et;
    RecyclerView cust_recyclerView;
    ImageView norecord_tv;      private URL url;
    String dest_file_path = "aayushmancard.pdf";
    String Fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_l__find_pan_by_adhar);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        Mobile= sharedPreferences.getString("mobile", "");
        String input1 = UserId+":"+Mobile+":ip6wgncgt6:nxe7j2f6c0";
        String strup1 = input1.toUpperCase();
        checksum_paytm = generateMD5Checksum(strup1);


        String input11 = UserId+":ip6wgncgt6:nxe7j2f6c0";
        String strup11 = input11.toUpperCase();
        checksum_paytm1 = generateMD5Checksum(strup11);


        mob_et=findViewById(R.id.mob_et);
        sp_state=findViewById(R.id.stateet);
        Button btn_submit=findViewById(R.id.btn_submit);
        RelativeLayout header=findViewById(R.id.header);

        cust_recyclerView = findViewById(R.id.cust_recyclerView);
        norecord_tv = findViewById(R.id.norecord_tv);

        spinner = findViewById(R.id.spinner);
        spinner.setTitle("");
        sp_state.setTitle("");

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        GetProfile(UserId,checksum_paytm1);


        GETAyushmanStateList();
        GetAyushmanParameterTypeList();
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mob_et.getText().toString().equalsIgnoreCase("")){
                    mob_et.setError("Fields can't be blank!");
                    mob_et.requestFocus();
                }
                else {
                    arrLegalList.clear();
                    AyushmanPrintListAPI(UserId,Fullname,Mobile,stateid,stateid1,mob_et.getText().toString(),checksum_paytm);
                    Log.d("ayush",UserId+","+Fullname+","+Mobile+","+stateid+","+stateid1+","+checksum_paytm);
//                    Toast.makeText(getApplicationContext(),"Submit Successfully!",Toast.LENGTH_LONG).show();
                }
            }
        });

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


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    for (int j = 0; j <= StateId1.size(); j++) {
                        if (spinner.getSelectedItem().toString().equalsIgnoreCase(StateName1.get(j))) {
                            // position = i;
                            stateid1 = StateId1.get(j - 1);
                            break;
                        } } } }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }



    void GETAyushmanStateList() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://eazypan.in/app/getAyushmanStateList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonString = response;
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    StateName.add("Select State");
                    JSONArray jsonArray = jsonObject.getJSONArray("state_list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        StateId.add(jsonObject1.getString("id"));
                        String statename = jsonObject1.getString("name");
                        StateName.add(statename);
                    }

                    sp_state.setAdapter(new ArrayAdapter<String>(FL_FindPanByAdhar.this, android.R.layout.simple_spinner_dropdown_item, StateName));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(FL_FindPanByAdhar.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }



    void GetAyushmanParameterTypeList() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://eazypan.in/app/getAyushmanParameterTypeList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonString = response;
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    StateName1.add("Select Parameter");
                    JSONArray jsonArray = jsonObject.getJSONArray("parameter_type");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        StateId1.add(jsonObject1.getString("id"));
                        String statename = jsonObject1.getString("name");
                        StateName1.add(statename);
                    }

                    spinner.setAdapter(new ArrayAdapter<String>(FL_FindPanByAdhar.this, android.R.layout.simple_spinner_dropdown_item, StateName1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(FL_FindPanByAdhar.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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



    public void AyushmanPrintListAPI(String id, String name,String phone,String stateid,String parmid,String param,String checksum) {
        String otp1 = new GlobalAppApis().AyushmanPrintList(id,name,phone,stateid,parmid,param,checksum);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_ayushmanPrintList(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("AyushmanPrintList","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("status").equalsIgnoreCase("0")){
                        String msg       = object.getString("message");
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    else {
                        JSONArray Response1 = object.getJSONArray("card_list");
                        for (int i = 0; i < Response1.length(); i++) {
                            norecord_tv.setVisibility(View.GONE);
                            cust_recyclerView.setVisibility(View.VISIBLE);
//                            title_tv.setText("Passbook History("+Response1.length()+")");
                            JSONObject jsonObject = Response1.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("fullname", jsonObject.getString("fullname"));
                            hashlist.put("father_name", jsonObject.getString("father_name"));
                            hashlist.put("created_time", jsonObject.getString("created_time"));
                            hashlist.put("state_id", jsonObject.getString("state_id"));
                            hashlist.put("pmrssmid", jsonObject.getString("pmrssmid"));
                            arrLegalList.add(hashlist);
                        }
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(FL_FindPanByAdhar.this, 1);
                        pdfAdapTer = new LegalListAdapter(FL_FindPanByAdhar.this, arrLegalList);
                        cust_recyclerView.setLayoutManager(layoutManager);
                        cust_recyclerView.setAdapter(pdfAdapTer);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, FL_FindPanByAdhar.this, call1, "", true);
    }


    public class LegalListAdapter extends RecyclerView.Adapter<LegalList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public LegalListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrLegalList) {
            data = arrLegalList;
        }

        public LegalList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new LegalList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_findpanbyadhar, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final LegalList holder, final int position) {
            holder.tv.setText(data.get(position).get("fullname"));
            holder.t1.setText(data.get(position).get("father_name"));
            holder.t2.setText(data.get(position).get("created_time"));
            holder.t3.setText(data.get(position).get("pmrssmid"));
            holder.print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetAyushmanPrint(UserId,data.get(position).get("state_id"),data.get(position).get("pmrssmid"),checksum_paytm1);
                }
            });

        }

        public int getItemCount() {
            return data.size();
        }
    }

    public class LegalList extends RecyclerView.ViewHolder {
        TextView tv,t1, t2, t3;
        Button print;


        public LegalList(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
            t1 = itemView.findViewById(R.id.tv1);
            t2 = itemView.findViewById(R.id.tv2);
            t3 = itemView.findViewById(R.id.tv3);
            print = itemView.findViewById(R.id.print);
        }
    }




    public void GetAyushmanPrint(String uid,String stateid,String pmrid, String checksum) {
        String otp1 = new GlobalAppApis().ayushmanPrint(uid,stateid,pmrid,checksum);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_ayushmanPrint(otp1);
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
                        try {
                            url = new URL(object.getString("card_content"));
                        } catch (MalformedURLException e) {
                            Toast.makeText(FL_FindPanByAdhar.this, "Error:- "+e, Toast.LENGTH_LONG).show();

                            e.printStackTrace();
                        }
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url + ""));
                        request.setTitle(dest_file_path);
                        request.setMimeType("application/pdf");
                        request.allowScanningByMediaScanner();
                        request.setAllowedOverMetered(true);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "AldoFiles/" + dest_file_path);
                        DownloadManager downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
                        downloadManager.enqueue(request);
                        Toast.makeText(FL_FindPanByAdhar.this, "Download Successfully.,Saved to your Internal Storage", Toast.LENGTH_LONG).show();
                        finish();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, FL_FindPanByAdhar.this, call1, "", true);
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
                        Fullname       = object.getString("fullname");
                        String email       = object.getString("email");
                        String phone       = object.getString("phone");

                        String aadhar       = object.getString("aadhar");
                        String pan       = object.getString("pan");
                        String firm_name       = object.getString("firm_name");

                        String pincode       = object.getString("pincode");
                        String state       = object.getString("state");
                        String address       = object.getString("address");
                        String profile       = object.getString("profile_image");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, FL_FindPanByAdhar.this, call1, "", true);
    }

}