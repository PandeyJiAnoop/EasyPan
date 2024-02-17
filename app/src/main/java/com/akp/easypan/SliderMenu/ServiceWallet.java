package com.akp.easypan.SliderMenu;

import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.RetrofitAPI.ApiService;
import com.akp.RetrofitAPI.ConnectToRetrofit;
import com.akp.RetrofitAPI.GlobalAppApis;
import com.akp.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.easypan.Passbook;
import com.akp.easypan.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

public class ServiceWallet extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    ImageView back_btn;
    RecyclerView rcvList;
    private final ArrayList<HashMap<String, String>> arrFriendsList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private FriendsListAdapter pdfAdapTer;
    String UserId;
    ImageView norecord_tv;
    TextView title_tv;

    int i=0;
    Spinner ststus_sp;
    private String checksum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_wallet);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        String input = UserId+":ip6wgncgt6:nxe7j2f6c0";
        String strup = input.toUpperCase();
        checksum = generateMD5Checksum(strup);
        Log.d("TAG", "UPPER " + strup +"MD5 checksum of " + input + ": " + checksum);

        Log.v("addadada", UserId);
        title_tv= findViewById(R.id.title_tv);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rcvList = findViewById(R.id.rcvList);
        norecord_tv=findViewById(R.id.norecord_tv);

        ststus_sp=findViewById(R.id.ststus_sp);

        // Spinner click listener
        ststus_sp.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("mobile");
//        categories.add("Price Drop");
        categories.add("dth");
        categories.add("electricity");
        categories.add("data-card");
        categories.add("digital-voucher");
        categories.add("emi");
        categories.add("gas-lpg");
        categories.add("broadband");
        categories.add("insurance");
        categories.add("landline");
        categories.add("municipal");
        categories.add("water");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        ststus_sp.setAdapter(dataAdapter);

//        GetUserListAPI();
    }


    private void GetUserListAPI(String type) {
        String otp1 = new GlobalAppApis().RechargeHistory(UserId,type,checksum);
        Log.d("API_passbook",otp1);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_rechargeHistory(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("API_passbook",result);
                try {
                    JSONObject objectlist = new JSONObject(result);
                    if (objectlist.getString("status").equalsIgnoreCase("0")){
                        String msg       = objectlist.getString("message");
                        Toast.makeText(getApplicationContext(), objectlist.getString("message"), Toast.LENGTH_LONG).show();
                        norecord_tv.setVisibility(View.VISIBLE);
                        rcvList.setVisibility(View.GONE);
                    }
                    else {
                        rcvList.setVisibility(View.VISIBLE);
                        JSONArray Response1 = objectlist.getJSONArray("rechargeHistory");
                        for (int i = 0; i < Response1.length(); i++) {
                            title_tv.setText("Recharge Report(" + Response1.length() + ")");
                            JSONObject jsonObject = Response1.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("refNo", jsonObject.getString("refNo"));
                            hashlist.put("transID", jsonObject.getString("transID"));
                            hashlist.put("status", jsonObject.getString("status"));
                            hashlist.put("custNo", jsonObject.getString("custNo"));
                            hashlist.put("custName", jsonObject.getString("custName"));
                            hashlist.put("amount", jsonObject.getString("amount"));
                            hashlist.put("remark", jsonObject.getString("remark"));
                            hashlist.put("operatorName", jsonObject.getString("operatorName"));
                            hashlist.put("eventTime", jsonObject.getString("eventTime"));
                            arrFriendsList.add(hashlist);
                        }
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        pdfAdapTer = new FriendsListAdapter(getApplicationContext(), arrFriendsList);
                        rcvList.setLayoutManager(layoutManager);
                        rcvList.setAdapter(pdfAdapTer);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, ServiceWallet.this, call1, "", true);
    }



    public class FriendsListAdapter extends RecyclerView.Adapter<FriendsList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public FriendsListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrFriendsList) {
            data = arrFriendsList;
        }
        public FriendsList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FriendsList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_service_report, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final FriendsList holder, final int position) {
            holder.tv.setText(String.valueOf(i+1));
            holder.tv1.setText(arrFriendsList.get(position).get("operatorName"));
            holder.tv2.setText(arrFriendsList.get(position).get("transID"));
            holder.tv3.setText(arrFriendsList.get(position).get("amount"));
            holder.tv4.setText(arrFriendsList.get(position).get("eventTime"));
            holder.tv6.setText(arrFriendsList.get(position).get("remark"));
            holder.tv5.setText(arrFriendsList.get(position).get("custNo")+"("+arrFriendsList.get(position).get("custNo")+")");
        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class FriendsList extends RecyclerView.ViewHolder {
        TextView tv,tv1,tv2,tv3,tv4,tv5,tv6;
        public FriendsList(View itemView) {
            super(itemView);
            tv=itemView.findViewById(R.id.tv);
            tv1=itemView.findViewById(R.id.tv1);
            tv2=itemView.findViewById(R.id.tv2);
            tv3=itemView.findViewById(R.id.tv3);
            tv4=itemView.findViewById(R.id.tv4);
            tv5=itemView.findViewById(R.id.tv5);
            tv6=itemView.findViewById(R.id.tv6);
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item1 = parent.getItemAtPosition(position).toString();
        arrFriendsList.clear();
        GetUserListAPI(item1);

        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
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
}