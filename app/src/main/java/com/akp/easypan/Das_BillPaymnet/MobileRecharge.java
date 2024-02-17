package com.akp.easypan.Das_BillPaymnet;

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
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost;

public class MobileRecharge extends AppCompatActivity {
    ImageView ivBack;
    LinearLayout provider_ll;
    TextView provoider_et;
    String service_name, UserId, checksum_paytm;
    String getProvider_id, roletype;
    Button btnSubmit;
    private SharedPreferences sharedPreferences;
    EditText mob_et, amount_et;
    AlertDialog alertDialog;
    String get_operator_ref, get_payid, getonlyservice, GetMobile;
    TextView title_tv, mob_code_tv;
    TextView txtMarquee, view_plan_tv, plan_dis_tv;
    private Dialog alertDialog1;
    private final ArrayList<HashMap<String, String>> arrLegalList = new ArrayList<>();
    private LegalListAdapter pdfAdapTer;
    RecyclerView plan_rv;
    SearchableSpinner sp_state;
    String stateid;
    ArrayList<String> StateName = new ArrayList<>();
    ArrayList<String> StateId = new ArrayList<>();
    private String checksum;
    private GpsTracker gpsTracker;
    String tvLatitude, tvLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_recharge);
        FindId();
        OnClickListner();
    }

    private void OnClickListner() {
        // Now we will call setSelected() method
        // and pass boolean value as true
        txtMarquee.setSelected(true);
        sp_state.setTitle("");
//        Toast.makeText(getApplicationContext(),getonlyservice,Toast.LENGTH_LONG).show();

        if (getonlyservice.equalsIgnoreCase("MOBILE RECHARGE")) {
            mob_et.setHint("Enter your mobile number");
            mob_code_tv.setVisibility(View.VISIBLE);
            mob_et.setInputType(InputType.TYPE_CLASS_NUMBER);
            view_plan_tv.setVisibility(View.VISIBLE);
            mob_et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        } else {
            mob_et.setHint("Enter your Unique Id Number");
            mob_code_tv.setVisibility(View.GONE);
            view_plan_tv.setVisibility(View.GONE);
        }
        String input11 = UserId + ":ip6wgncgt6:nxe7j2f6c0";
        String strup11 = input11.toUpperCase();
        checksum = generateMD5Checksum(strup11);

        String input = UserId + ":" + GetMobile + ":ip6wgncgt6:nxe7j2f6c0";
        String strup = input.toUpperCase();
        checksum_paytm = generateMD5Checksum(strup);

        sp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    for (int j = 0; j <= StateId.size(); j++) {
                        if (sp_state.getSelectedItem().toString().equalsIgnoreCase(StateName.get(j))) {
                            // position = i;
                            stateid = StateId.get(j - 1);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        view_plan_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp_state.getSelectedItem().toString().equalsIgnoreCase("AIRTEL")) {
                    GetAppPlanAPI("A");
                } else if (sp_state.getSelectedItem().toString().equalsIgnoreCase("VODAFONE")) {
                    GetAppPlanAPI("V");
                } else if (sp_state.getSelectedItem().toString().equalsIgnoreCase("IDEA")) {
                    GetAppPlanAPI("I");
                } else if (sp_state.getSelectedItem().toString().equalsIgnoreCase("BSNL")) {
                    GetAppPlanAPI("BT");
                } else if (sp_state.getSelectedItem().toString().equalsIgnoreCase("RELIANCE JIO")) {
                    GetAppPlanAPI("J");
                }
            }
        });

        provoider_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getonlyservice.equalsIgnoreCase("MOBILE RECHARGE")) {
                    Intent intent = new Intent(getApplicationContext(), Mobile.class);
                    startActivity(intent);
                } else if (getonlyservice.equalsIgnoreCase("DTH")) {
                    Intent intent = new Intent(getApplicationContext(), DTH.class);
                    startActivity(intent);
                } else if (getonlyservice.equalsIgnoreCase("ELECTRICITY")) {
                    Intent intent = new Intent(getApplicationContext(), Electricity.class);
                    startActivity(intent);
                } else if (getonlyservice.equalsIgnoreCase("WATER")) {
                    Intent intent = new Intent(getApplicationContext(), Water.class);
                    startActivity(intent);
                }
            }
        });

        if (service_name == null) {
        } else {
            provoider_et.setText(service_name);
        }
        GetOperatorList(UserId, "mobile", checksum);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mob_et.getText().toString().equalsIgnoreCase("")) {
                    mob_et.setError("Fields can't be blank!");
                    mob_et.requestFocus();
                } else {
                    Log.d("sdghsfg", "" + UserId + amount_et.getText().toString() + mob_et.getText().toString() + getProvider_id);
                    Payment(UserId, GetMobile, stateid, amount_et.getText().toString(), mob_et.getText().toString(), checksum_paytm);
                }
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), dashboard.class);
                startActivity(intent);
            }
        });
    }

    private void FindId() {
        title_tv = findViewById(R.id.title_tv);
        mob_code_tv = findViewById(R.id.mob_code_tv);
        plan_rv = (RecyclerView) findViewById(R.id.plan_rv);
        roletype = getIntent().getStringExtra("RoleType");
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("U_id", "");
        GetMobile = sharedPreferences.getString("mobile", "");
        service_name = getIntent().getStringExtra("servicename");
        getProvider_id = getIntent().getStringExtra("provider_id");
        getonlyservice = getIntent().getStringExtra("onlyservice");
        plan_dis_tv = findViewById(R.id.plan_dis_tv);
        btnSubmit = findViewById(R.id.btnSubmit);
        sp_state = findViewById(R.id.stateet);
        // casting of textview
        txtMarquee = (TextView) findViewById(R.id.marqueeText);

        ivBack = findViewById(R.id.ivBack);
        provider_ll = findViewById(R.id.provider_ll);
        provoider_et = findViewById(R.id.provoider_et);
        mob_et = findViewById(R.id.mob_et);
        amount_et = findViewById(R.id.amount_et);
        title_tv.setText(getonlyservice + " Service");
        view_plan_tv = findViewById(R.id.view_plan_tv);
        mob_et.setText(GetMobile);

    }

   /* private void ViewAllPlanPopup() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(MobileRecharge.this).inflate(R.layout.viewplan_popup, viewGroup, false);
        plan_rv = (RecyclerView) dialogView.findViewById(R.id.plan_rv);

        if (sp_state.getSelectedItem().toString().equalsIgnoreCase("AIRTEL")){
            GetAppPlanAPI("A");
        }
        else if (sp_state.getSelectedItem().toString().equalsIgnoreCase("VODAFONE")){
            GetAppPlanAPI("V");
        }
        else if (sp_state.getSelectedItem().toString().equalsIgnoreCase("IDEA")){
            GetAppPlanAPI("I");
        }
        else if (sp_state.getSelectedItem().toString().equalsIgnoreCase("BSNL")){
            GetAppPlanAPI("BT");
        }
        else if (sp_state.getSelectedItem().toString().equalsIgnoreCase("RELIANCE JIO")){
            GetAppPlanAPI("J");
        }


        //Now we need an AlertDialog.Builder object
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MobileRecharge.this);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog1 = builder.create();
        alertDialog1.show();
//                Intent intent=new Intent(getActivity(), KYC_AEPS.class); startActivity(intent);
    }*/

    public void PAYMENTAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(MobileRecharge.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://msc.mscpaycash.com/api/telecom/v1/payment?api_token=98bUAIRRHAH1klt3Nr5hk2hRSQQqAv60uiezDnXOw980C9vTTzVWjSAvdPZk&number=" + mob_et.getText().toString() + "&amount=" + amount_et.getText().toString() + "&provider_id=9" + "&client_id=" + UserId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                        get_operator_ref = jsonObject.getString("operator_ref");
                        get_payid = jsonObject.getString("payid");
                        Intent intent = new Intent(MobileRecharge.this, RechargeDetails.class);
                        intent.putExtra("get_operator_ref", get_operator_ref);
                        intent.putExtra("get_payid", get_payid);
                        intent.putExtra("mobile", GetMobile);
                        intent.putExtra("operator", sp_state.getSelectedItem().toString());
                        startActivity(intent);
//                        showpopupwindow();
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MobileRecharge.this);
        requestQueue.add(stringRequest);
    }

    private void showpopupwindow() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(MobileRecharge.this).inflate(R.layout.successfullycreated_popup, viewGroup, false);
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

    private void Payment(String user_id, String phone, String service_code, String amount, String customer_phone, String checksum) {
        String otp1 = new GlobalAppApis().PostRechargeAPI(user_id, phone, service_code, amount, customer_phone, checksum);
        Log.e("PostRecharge", otp1);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.PostRecharge(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.e("PostRecharge", result);
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("0")) {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(MobileRecharge.this);
                        builder.setTitle("Response!")
                                .setMessage(msg)
                                .setCancelable(false)
                                .setIcon(R.drawable.logo)
                                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        //                        .setNeutralButton("Middle", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(MyActivity.this, "Middle button clicked!", Toast.LENGTH_LONG).show();
//                            }
//                        });
                        builder.create().show();
                    } else {
                        get_operator_ref = jsonObject.getString("message");
//                        showpopupwindow();
                        Intent intent = new Intent(MobileRecharge.this, RechargeDetails.class);
                        intent.putExtra("number", jsonObject.getString("number"));
                        intent.putExtra("amount", jsonObject.getString("amount"));
                        intent.putExtra("referenceNo", jsonObject.getString("referenceNo"));
                        intent.putExtra("transactionId", jsonObject.getString("transactionId"));
                        intent.putExtra("message", jsonObject.getString("message"));
                        intent.putExtra("operator", sp_state.getSelectedItem().toString());
                        startActivity(intent);
//                        Toast.makeText(getApplicationContext(),jsonObject.getString("Message"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, MobileRecharge.this, call1, "", true);
    }


    public void GetAppPlanAPI(String op_code) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://amazepay.net/api/Amazepay/OPERATOR_CODE?operatorcode=" + op_code+"&MobileNo="+GetMobile, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d("res_plan", response);
                try {
                    JSONObject objectlist = new JSONObject(response);
                    JSONObject Responobj = objectlist.getJSONObject("Response");
                    if (Responobj.getString("status").equalsIgnoreCase("true")){
                        JSONObject Respon = Responobj.getJSONObject("data");
                        JSONArray Response1 = Respon.getJSONArray("plans");
                        for (int i = 0; i < Response1.length(); i++) {
//                            title_tv.setText("Passbook History("+Response1.length()+")");
                            JSONObject jsonObject = Response1.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("planType", jsonObject.getString("planType"));
                            hashlist.put("planCode", jsonObject.getString("planCode"));
                            hashlist.put("amount", jsonObject.getString("amount"));
                            hashlist.put("validity", jsonObject.getString("validity"));
                            hashlist.put("planName", jsonObject.getString("planName"));
                            hashlist.put("planDescription", jsonObject.getString("planDescription"));
                            arrLegalList.add(hashlist);
                        }
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MobileRecharge.this, 1);
                        pdfAdapTer = new LegalListAdapter(MobileRecharge.this, arrLegalList);
                        plan_rv.setLayoutManager(layoutManager);
                        plan_rv.setAdapter(pdfAdapTer);
                    }else {
                        Toast.makeText(MobileRecharge.this, Responobj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }

                catch (JSONException e) {
                    e.printStackTrace();
                }

                //  Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MobileRecharge.this, "Check From Your Operator(Airtel/Jio etc.) App", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MobileRecharge.this);
        requestQueue.add(stringRequest);

    }


    public class LegalListAdapter extends RecyclerView.Adapter<LegalList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public LegalListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrLegalList) {
            data = arrLegalList;
        }

        public LegalList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new LegalList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_viewplan, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final LegalList holder, final int position) {
            holder.head_rb.setText(data.get(position).get("planName"));
            holder.des_tv.setText(data.get(position).get("planDescription"));
            holder.tv.setText("Plan Amount:- " + data.get(position).get("amount"));
            holder.tv1.setText("Validity:- " + data.get(position).get("validity"));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.head_rb.setChecked(true);
//                    alertDialog1.dismiss();
                    amount_et.setText(data.get(position).get("amount"));
                    plan_dis_tv.setText(data.get(position).get("planDescription"));
                }
            });
        }

        public int getItemCount() {
            return data.size();
        }
    }

    public class LegalList extends RecyclerView.ViewHolder {
        TextView des_tv, tv1, tv;
        RadioButton head_rb;

        public LegalList(View itemView) {
            super(itemView);
            head_rb = itemView.findViewById(R.id.head_rb);
            des_tv = itemView.findViewById(R.id.des_tv);
            tv = itemView.findViewById(R.id.tv);
            tv1 = itemView.findViewById(R.id.tv1);
        }
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


    public void GetOperatorList(String uid, String type, String a_checksum) {
        String otp1 = new GlobalAppApis().providerList(uid, type, a_checksum);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_providerList(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("res", "cxc" + result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("status").equalsIgnoreCase("0")) {
                        String msg = object.getString("message");
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        StateName.add("Select Operator");
                        JSONArray jsonArray = object.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            StateId.add(jsonObject1.getString("id"));
                            String statename = jsonObject1.getString("name");
                            StateName.add(statename);
                        }

                        sp_state.setAdapter(new ArrayAdapter<String>(MobileRecharge.this, android.R.layout.simple_spinner_dropdown_item, StateName));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, MobileRecharge.this, call1, "", true);
    }

}