package com.akp.easypan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.RetrofitAPI.ApiService;
import com.akp.RetrofitAPI.ConnectToRetrofit;
import com.akp.RetrofitAPI.GlobalAppApis;
import com.akp.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.easypan.Helper.NetworkConnectionHelper;
import com.akp.easypan.SliderMenu.MyWallet;
import com.akp.easypan.entity.User;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;

import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost;

public class Passbook extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    String UserId, RoleType;
    ImageView menuImg;
    ArrayList<HashMap<String, String>> arrLegalList = new ArrayList<>();
    LegalListAdapter pdfAdapTer;
    RecyclerView cust_recyclerView;
    SwipeRefreshLayout srl_refresh;
    private SharedPreferences sharedPreferences;
    TextView search_tv,title_tv;
    public static TextView tvDisplayDate, tvDisplayDate2;
    SearchView et_search;
    ImageView norecord_tv;

    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendarr = Calendar.getInstance();
    final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendarr.set(Calendar.YEAR, year);
            myCalendarr.set(Calendar.MONTH, monthOfYear);
            myCalendarr.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel1();
        }
    };
    private int year,year1;
    private int month,month1;
    private int day,day1;
    String checksum;
    private TextView apis_bal_tv,wallet_bal_tv;
    private Spinner ststus_sp;
    private String item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passbook);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        FindId();
        OnClick();
    }

    private void OnClick() {
        // Spinner click listener
        ststus_sp.setOnItemSelectedListener(Passbook.this);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("All");
        categories.add("Credit");
        categories.add("Debit");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        ststus_sp.setAdapter(dataAdapter);

        setCurrentDateOnView();

        GetDashboard(UserId,checksum);
        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(Passbook.this)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                            srl_refresh.setRefreshing(false);
                        }
                    }, 2000);
                } else {
                    Toast.makeText(Passbook.this, "Please check your internet connection! try again...", Toast.LENGTH_LONG).show();
                }
            }
        });
        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        PassbookList(UserId, tvDisplayDate.getText().toString(), tvDisplayDate2.getText().toString(),"",checksum);

        search_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrLegalList != null){
                    arrLegalList.clear();
//                    pdfAdapTer.notifyDataSetChanged();
                }
                if (item.equalsIgnoreCase("Credit")) {
                    PassbookList(UserId,tvDisplayDate.getText().toString(), tvDisplayDate2.getText().toString(),"cr",checksum);
                }
                else if (item.equalsIgnoreCase("Debit")) {
                    PassbookList(UserId,tvDisplayDate.getText().toString(), tvDisplayDate2.getText().toString(),"dr",checksum);
                }
                else if (item.equalsIgnoreCase("All")) {
                    PassbookList(UserId,tvDisplayDate.getText().toString(), tvDisplayDate2.getText().toString(),"",checksum);
                }
                else {
                    PassbookList(UserId,tvDisplayDate.getText().toString(), tvDisplayDate2.getText().toString(),"",checksum);
                }
//                Log.d("sdsdsdsdsd", tvDisplayDate.getText().toString() + "\n\n" + tvDisplayDate2.getText().toString() + "\n\n" + UserId);
            }
        });
        et_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() >= 2) {
                    arrLegalList.clear();
                    pdfAdapTer.notifyDataSetChanged();
                    PassbookList(UserId,tvDisplayDate.getText().toString(), tvDisplayDate2.getText().toString(),"",checksum);
                } else {
                    arrLegalList.clear();
                    pdfAdapTer.notifyDataSetChanged();
                    PassbookList(UserId,tvDisplayDate.getText().toString(), tvDisplayDate2.getText().toString(),"", checksum);
                }
                return true;
            }
        });
    }

    private void FindId() {
//        Toast.makeText(getApplicationContext(),UserId,Toast.LENGTH_LONG).show();
        et_search = findViewById(R.id.search);
        menuImg = findViewById(R.id.menuImg);
        cust_recyclerView = findViewById(R.id.cust_recyclerView);
        srl_refresh = findViewById(R.id.srl_refresh);
        norecord_tv = findViewById(R.id.norecord_tv);
        search_tv = findViewById(R.id.search_tv);
        title_tv = findViewById(R.id.title_tv);
        String input = UserId+":ip6wgncgt6:nxe7j2f6c0";
        String strup = input.toUpperCase();
        checksum = generateMD5Checksum(strup);
        Log.d("TAG", "UPPER " + strup +"MD5 checksum of " + input + ": " + checksum);
        ststus_sp=findViewById(R.id.stateet);

    }

    private void PassbookList(String user_id,String datefrom,String dateto,String entrytype,String checksum) {
        String otp1 = new GlobalAppApis().GetPassbook(user_id, datefrom, dateto, entrytype,checksum);
        Log.d("API_passbook",otp1);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_passbook(otp1);
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
                        cust_recyclerView.setVisibility(View.GONE);
                    }
                    else {
                        JSONArray Response1 = objectlist.getJSONArray("transac_list");
                        for (int i = 0; i < Response1.length(); i++) {
                            norecord_tv.setVisibility(View.GONE);
                            cust_recyclerView.setVisibility(View.VISIBLE);
//                            title_tv.setText("Passbook History("+Response1.length()+")");
                            JSONObject jsonObject = Response1.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("id", jsonObject.getString("id"));
                            hashlist.put("name", jsonObject.getString("name"));
                            hashlist.put("description", jsonObject.getString("description"));
                            hashlist.put("credit", jsonObject.getString("credit"));
                            hashlist.put("debit", jsonObject.getString("debit"));
                            hashlist.put("balance", jsonObject.getString("balance"));
                            hashlist.put("date_time", jsonObject.getString("date_time"));
                            arrLegalList.add(hashlist);

                        }
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(Passbook.this, 1);
                        pdfAdapTer = new LegalListAdapter(Passbook.this, arrLegalList);
                        cust_recyclerView.setLayoutManager(layoutManager);
                        cust_recyclerView.setAdapter(pdfAdapTer);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Passbook.this, call1, "", true);
    }


    public class LegalListAdapter extends RecyclerView.Adapter<LegalList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public LegalListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrLegalList) {
            data = arrLegalList;
        }

        public Passbook.LegalList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Passbook.LegalList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_passbook, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final Passbook.LegalList holder, final int position) {
            holder.t1.setText(data.get(position).get("name") + "(" + data.get(position).get("id") + ")");
            holder.t2.setText(data.get(position).get("credit"));
            holder.t3.setText(data.get(position).get("debit"));
            holder.t4.setText(data.get(position).get("balance")+" Rs.");
            holder.t5.setText(data.get(position).get("description"));
            holder.date.setText("Date:- " + data.get(position).get("date_time"));
        }

        public int getItemCount() {
            return data.size();
        }
    }

    public class LegalList extends RecyclerView.ViewHolder {
        TextView t1, t2, t3, t4, t5, date;

        public LegalList(View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.tv1);
            t2 = itemView.findViewById(R.id.tv2);
            t3 = itemView.findViewById(R.id.tv3);
            t4 = itemView.findViewById(R.id.tv4);
            t5 = itemView.findViewById(R.id.tv5);
            date = itemView.findViewById(R.id.date);
        }
    }


    // display current date
    public void setCurrentDateOnView() {

        tvDisplayDate = (TextView) findViewById(R.id.mStartTime);
        tvDisplayDate2 = (TextView) findViewById(R.id.mEndTime);
        apis_bal_tv=findViewById(R.id.apis_bal_tv);
        wallet_bal_tv=findViewById(R.id.wallet_bal_tv);

        String datec = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        tvDisplayDate.setText(datec);
        String datec1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        tvDisplayDate2.setText(datec1);

        tvDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePicker;
                year = myCalendar.get(Calendar.YEAR);
                month = myCalendar.get(Calendar.MONTH);
                day = myCalendar.get(Calendar.DAY_OF_MONTH);
                datePicker = new DatePickerDialog(Passbook.this, date, year, month, day);
                // This code is used for disable previous date but you can select the date
//                datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                // This code is used to prevent the date selection
                datePicker.getDatePicker().setCalendarViewShown(false);
                datePicker.show();
            }
        });
        tvDisplayDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePicker1;
                year1 = myCalendarr.get(Calendar.YEAR);
                month1 = myCalendarr.get(Calendar.MONTH);
                day1 = myCalendarr.get(Calendar.DAY_OF_MONTH);
                datePicker1 = new DatePickerDialog(Passbook.this, date1, year1, month1, day1);
                // This code is used for disable previous date but you can select the date
//                datePicker1.getDatePicker().setMinDate(System.currentTimeMillis());
                // This code is used to prevent the date selection
                datePicker1.getDatePicker().setCalendarViewShown(true);
                datePicker1.show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvDisplayDate2.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateLabel1() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvDisplayDate.setText(sdf.format(myCalendarr.getTime()));
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
    public void GetDashboard(String user_id,String checksum) {
        String otp1 = new GlobalAppApis().Dashboard(user_id,checksum);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_dashboard(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("res","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("status").equalsIgnoreCase("0")){
                        String msg       = object.getString("message");
                        Toast.makeText(Passbook.this, object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    else {
                        String phone       = object.getString("phone");
                        String aeps_balance       = object.getString("aeps_balance");
                        String wallet_balance       = object.getString("wallet_balance");
                        apis_bal_tv.setText("Rs. "+aeps_balance);
                        wallet_bal_tv.setText("Rs. "+wallet_balance);
                        String msg       = object.getString("message");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Passbook.this, call1, "", true);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}