package com.akp.easypan.SliderMenu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.easypan.R;
import com.akp.easypan.Util.Api_Urls;
import com.akp.easypan.Utility;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddFundReport extends AppCompatActivity {
    ImageView menuImg;
    String UserId;
    ImageView image_qr;
    TextView select_tv;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    String temp;
    ImageView img_showProfile;
    EditText amount_et,transcation_number_et,date_time_et;
    AppCompatButton submit_btn;
    RecyclerView rcvList;
    private final ArrayList<HashMap<String, String>> arrFriendsList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private FriendsListAdapter pdfAdapTer;
    ImageView norecord_tv;
    String getAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fund_report);
        getAmount= getIntent().getStringExtra("club_amount");
//        Toast.makeText(getApplicationContext(),""+getAmount,Toast.LENGTH_LONG).show();

        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        menuImg=findViewById(R.id.menuImg);
        image_qr=findViewById(R.id.image_qr);
        amount_et=findViewById(R.id.amount_et);
        transcation_number_et=findViewById(R.id.transcation_number_et);
        date_time_et=findViewById(R.id.date_time_et);
        submit_btn=findViewById(R.id.Add_button);
        rcvList = findViewById(R.id.rcvList);
        norecord_tv=findViewById(R.id.norecord_tv);
        amount_et.setText(getAmount);
        amount_et.setEnabled(false);
        amount_et.setFocusable(false);
        WalletHistoryAPI();

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount_et.getText().toString().equalsIgnoreCase("")){
                    amount_et.setError("Fields can't be blank!");
                    amount_et.requestFocus();
                }
                else if (transcation_number_et.getText().toString().equalsIgnoreCase("")){
                    transcation_number_et.setError("Fields can't be blank!");
                    transcation_number_et.requestFocus();
                }
                else if (date_time_et.getText().toString().equalsIgnoreCase("")){
                    date_time_et.setError("Fields can't be blank!");
                    date_time_et.requestFocus();
                }
                else {
                    ProceedToaddFundAPI();
//                    Toast.makeText(getApplicationContext()," wait for response",Toast.LENGTH_LONG).show();
                }
            }
        });

        select_tv=findViewById(R.id.select_tv);
        img_showProfile=findViewById(R.id.img_showProfile);

        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        WalletAPI("",UserId);
        select_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }


    private void selectImage() {
        final CharSequence[] items = {"Choose from Library", "Cancel" };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddFundReport.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(AddFundReport.this);
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
        img_showProfile.setImageBitmap(bm);
        Bitmap immagex=bm;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] b = baos.toByteArray();
        temp = Base64.encodeToString(b,Base64.DEFAULT);
    }
    public void ProceedToaddFundAPI() {
        final ProgressDialog progressDialog1 = new ProgressDialog(this);
        progressDialog1.setMessage("Loading...");
        progressDialog1.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                 Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog1.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            Toast.makeText(AddFundReport.this, jsonObject.getString("Msg"), Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddFundReport.this).setTitle("Request Saved Successfully..")
                                    .setMessage("Request Sent to Admin Please Wait for Approved!!").setCancelable(false)
                                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent = new Intent(getApplicationContext(), AddFundReport.class);
                                            startActivity(intent);
                                            //startActivity(i);
                                        }});
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    } else {
//                        Toast.makeText(AddFundReport.this, object.getString("Message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog1.dismiss();
//                Toast.makeText(AddFundReport.this, "Something went wrong:-" + error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("MemberId", UserId);
                params.put("Amount", amount_et.getText().toString());
                params.put("TransactionNo", transcation_number_et.getText().toString());
                params.put("RecpFile", temp);
                params.put("PaymentDateTime", date_time_et.getText().toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(AddFundReport.this);
        requestQueue.add(stringRequest);
    }

    public void WalletHistoryAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("1233444","sadsad"+response);
                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        norecord_tv.setVisibility(View.GONE);
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("PKID", jsonObject.getString("PKID"));
                            hashlist.put("MemberId", jsonObject.getString("MemberId"));
                            hashlist.put("Amount", jsonObject.getString("Amount"));
                            hashlist.put("TransactionNo", jsonObject.getString("TransactionNo"));
                            hashlist.put("RecpFile", jsonObject.getString("RecpFile"));
                            hashlist.put("PaymentDateTime", jsonObject.getString("PaymentDateTime"));
                            hashlist.put("EntryBy", jsonObject.getString("EntryBy"));
                            hashlist.put("IsActive", jsonObject.getString("IsActive"));
                            hashlist.put("IsApprove", jsonObject.getString("IsApprove"));
                            hashlist.put("AdminStatus", jsonObject.getString("AdminStatus"));
                            hashlist.put("ApproveBy", jsonObject.getString("ApproveBy"));
                            hashlist.put("ApproveDate", jsonObject.getString("ApproveDate"));
                            hashlist.put("EntryDate", jsonObject.getString("EntryDate"));
                            arrFriendsList.add(hashlist);
                        }
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        pdfAdapTer = new FriendsListAdapter(getApplicationContext(), arrFriendsList);
                        rcvList.setLayoutManager(layoutManager);
                        rcvList.setAdapter(pdfAdapTer);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
//                        Toast.makeText(AddFundReport.this, "No data found", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
//                Toast.makeText(AddFundReport.this, "Something went wrong:-" + error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Member_ID", UserId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(AddFundReport.this);
        requestQueue.add(stringRequest);
    }

    public class FriendsListAdapter extends RecyclerView.Adapter<AddFundReport.FriendsList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public FriendsListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrFriendsList) {
            data = arrFriendsList;
        }
        public AddFundReport.FriendsList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AddFundReport.FriendsList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_wallet_approved, parent, false));
        }
        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final AddFundReport.FriendsList holder, final int position) {
            holder.tv1.setText("Request Amount :-  "+data.get(position).get("Amount")+" Rs."+"\nTransaction No :- "+data.get(position).get("TransactionNo")+"\nPayment DateTime :- "+data.get(position).get("PaymentDateTime"));
            holder.tv2.setText("Approved By :- "+data.get(position).get("ApproveBy"));
            holder.tv3.setText(data.get(position).get("ApproveDate"));
            holder.tv4.setText(data.get(position).get("AdminStatus"));
            if (data.get(position).get("AdminStatus").equalsIgnoreCase("Approve")){
                holder.tv4.setText("Approved");
                holder.tv4.setTextColor(Color.GREEN);
            }
            else {
                holder.tv4.setText("Pending");
                holder.tv4.setTextColor(Color.RED);
            }
        }
        public int getItemCount() {
            return data.size();
        }
    }
    public class FriendsList extends RecyclerView.ViewHolder {
        TextView tv1,tv2,tv3,tv4;
        public FriendsList(View itemView) {
            super(itemView);
            tv1=itemView.findViewById(R.id.tv1);
            tv2=itemView.findViewById(R.id.tv2);
            tv3=itemView.findViewById(R.id.tv3);
            tv4=itemView.findViewById(R.id.tv4);

        }
    }

}