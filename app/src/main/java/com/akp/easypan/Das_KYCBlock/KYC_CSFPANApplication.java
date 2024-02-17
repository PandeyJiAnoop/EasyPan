package com.akp.easypan.Das_KYCBlock;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.akp.RetrofitAPI.ApiService;
import com.akp.RetrofitAPI.ConnectToRetrofit;
import com.akp.RetrofitAPI.GlobalAppApis;
import com.akp.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.easypan.R;
import com.akp.easypan.entity.User;
import com.akp.easypan.user_profile;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;

import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost;

public class KYC_CSFPANApplication extends AppCompatActivity {
    SearchableSpinner cat_spinner,gender_sp,address_proof_sp,dob_proof_sp,identity_sp,cat_formtype;
    String catid; ArrayList<String> CatName = new ArrayList<>();
    ArrayList<String> CatId = new ArrayList<>();

    String addressid; ArrayList<String> AddressName = new ArrayList<>();
    ArrayList<String> AddressId = new ArrayList<>();

    String identityid; ArrayList<String> IdentityName = new ArrayList<>();
    ArrayList<String> IdentityId = new ArrayList<>();

    String dobid; ArrayList<String> DOBName = new ArrayList<>();
    ArrayList<String> DOBId = new ArrayList<>();

    String genderid; ArrayList<String> GenderName = new ArrayList<>();
    ArrayList<String> GenderId = new ArrayList<>();

    String formtypeid; ArrayList<String> FormTypeName = new ArrayList<>();
    ArrayList<String> FormTypeId = new ArrayList<>();

    EditText et_dob,f_name_et,m_name_et,l_name_et,father_fname,father_m_name_et,father_l_name_et,mob_et,email_et,
            aadhar_et,name_on_pan_et,name_on_aadhar_et,address_et,pincode_et,pan_processing_fee_et,building_et,post_office_et,location_et,
            city_et,area_code_et,ao_type_et,range_code_et,ao_number_et,pancard_et;
    private String UserId;
    private String checksum;

    SearchableSpinner stateet;
    ArrayList<String> ProductSizeValue = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_k_y_c__c_s_f_p_a_n_application);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        findID();

        NSDLCategoryList();
        ProofOfAddressList();
        ProofOfIdentityList();
        ProofOfDOBList();
        GenderList();
        AppTypeList();
        GetStateList();

        String input = UserId+":ip6wgncgt6:nxe7j2f6c0";
        String strup = input.toUpperCase();
        checksum = generateMD5Checksum(strup);
        Log.d("TAG", "UPPER " + strup +"MD5 checksum of " + input + ": " + checksum);

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
    }

    private void findID() {
        gender_sp= findViewById(R.id.gender_sp);
        cat_spinner = findViewById(R.id.cat_spinner);
        address_proof_sp = findViewById(R.id.address_proof_sp);
        dob_proof_sp = findViewById(R.id.dob_proof_sp);
        identity_sp = findViewById(R.id.identity_sp);
        cat_formtype = findViewById(R.id.cat_formtype);

        et_dob=findViewById(R.id.dob_et);
        f_name_et=findViewById(R.id.f_name_et);
        m_name_et=findViewById(R.id.m_name_et);
        l_name_et=findViewById(R.id.l_name_et);
        father_fname=findViewById(R.id.father_fname);
        father_m_name_et=findViewById(R.id.father_m_name_et);
        father_l_name_et=findViewById(R.id.father_l_name_et);
        mob_et=findViewById(R.id.mob_et);
        email_et=findViewById(R.id.email_et);
        aadhar_et=findViewById(R.id.aadhar_et);
        name_on_pan_et=findViewById(R.id.name_on_pan_et);
        name_on_aadhar_et=findViewById(R.id.name_on_aadhar_et);
        address_et=findViewById(R.id.address_et);
        pincode_et=findViewById(R.id.pincode_et);
        pan_processing_fee_et=findViewById(R.id.pan_processing_fee_et);
        building_et=findViewById(R.id.building_et);
        post_office_et=findViewById(R.id.post_office_et);
        location_et=findViewById(R.id.location_et);
        city_et=findViewById(R.id.city_et);
        area_code_et=findViewById(R.id.area_code_et);
        ao_type_et=findViewById(R.id.ao_type_et);
        range_code_et=findViewById(R.id.range_code_et);
        ao_number_et=findViewById(R.id.ao_number_et);
        pancard_et=findViewById(R.id.pancard_et);

        AppCompatButton reg_btn=findViewById(R.id.reg_btn);
       ImageView sliding_menu=findViewById(R.id.sliding_menu);

        sliding_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_dob.getText().toString().equalsIgnoreCase("")){
                    et_dob.setError("Fields can't be blank!");
                    et_dob.requestFocus();
                }
                else if (f_name_et.getText().toString().equalsIgnoreCase("")){
                    f_name_et.setError("Fields can't be blank!");
                    f_name_et.requestFocus();
                }
                else if (l_name_et.getText().toString().equalsIgnoreCase("")){
                    l_name_et.setError("Fields can't be blank!");
                    l_name_et.requestFocus();
                }
                else if (father_fname.getText().toString().equalsIgnoreCase("")){
                    father_fname.setError("Fields can't be blank!");
                    father_fname.requestFocus();
                }
                else if (father_l_name_et.getText().toString().equalsIgnoreCase("")){
                    father_l_name_et.setError("Fields can't be blank!");
                    father_l_name_et.requestFocus();
                }
                else if (mob_et.getText().toString().equalsIgnoreCase("")){
                    mob_et.setError("Fields can't be blank!");
                    mob_et.requestFocus();
                }
                else if (email_et.getText().toString().equalsIgnoreCase("")){
                    email_et.setError("Fields can't be blank!");
                    email_et.requestFocus();
                }
                else if (aadhar_et.getText().toString().equalsIgnoreCase("")){
                    aadhar_et.setError("Fields can't be blank!");
                    aadhar_et.requestFocus();
                }
                else if (name_on_pan_et.getText().toString().equalsIgnoreCase("")){
                    name_on_pan_et.setError("Fields can't be blank!");
                    name_on_pan_et.requestFocus();
                }
                else if (name_on_aadhar_et.getText().toString().equalsIgnoreCase("")){
                    name_on_aadhar_et.setError("Fields can't be blank!");
                    name_on_aadhar_et.requestFocus();
                }
                else if (address_et.getText().toString().equalsIgnoreCase("")){
                    address_et.setError("Fields can't be blank!");
                    address_et.requestFocus();
                }
                else if (pincode_et.getText().toString().equalsIgnoreCase("")){
                    pincode_et.setError("Fields can't be blank!");
                    pincode_et.requestFocus();
                }
                else if (pan_processing_fee_et.getText().toString().equalsIgnoreCase("")){
                    pan_processing_fee_et.setError("Fields can't be blank!");
                    pan_processing_fee_et.requestFocus();
                }
                else if (building_et.getText().toString().equalsIgnoreCase("")){
                    building_et.setError("Fields can't be blank!");
                    building_et.requestFocus();
                }
                else if (post_office_et.getText().toString().equalsIgnoreCase("")){
                    post_office_et.setError("Fields can't be blank!");
                    post_office_et.requestFocus();
                }
                else if (location_et.getText().toString().equalsIgnoreCase("")){
                    location_et.setError("Fields can't be blank!");
                    location_et.requestFocus();
                }
                else if (city_et.getText().toString().equalsIgnoreCase("")){
                    city_et.setError("Fields can't be blank!");
                    city_et.requestFocus();
                }
                else if (area_code_et.getText().toString().equalsIgnoreCase("")){
                    area_code_et.setError("Fields can't be blank!");
                    area_code_et.requestFocus();
                }
                else if (ao_type_et.getText().toString().equalsIgnoreCase("")){
                    ao_type_et.setError("Fields can't be blank!");
                    ao_type_et.requestFocus();
                }
                else if (range_code_et.getText().toString().equalsIgnoreCase("")){
                    range_code_et.setError("Fields can't be blank!");
                    range_code_et.requestFocus();
                }
                else if (ao_number_et.getText().toString().equalsIgnoreCase("")){
                    ao_number_et.setError("Fields can't be blank!");
                    ao_number_et.requestFocus();
                }
                else {
                    if (cat_spinner.getSelectedItem().toString().equalsIgnoreCase("Select Category")){
                        Toast.makeText(getApplicationContext(),"Select Category ",Toast.LENGTH_LONG).show();

                    }
                    else if (gender_sp.getSelectedItem().toString().equalsIgnoreCase("Select Gender")){
                        Toast.makeText(getApplicationContext(),"Select Gender ",Toast.LENGTH_LONG).show();

                    }
                    else if (address_proof_sp.getSelectedItem().toString().equalsIgnoreCase("Select Address Proof")){
                        Toast.makeText(getApplicationContext(),"Select Address Proof ",Toast.LENGTH_LONG).show();

                    }
                    else if (dob_proof_sp.getSelectedItem().toString().equalsIgnoreCase("Select DOB Proof")){
                        Toast.makeText(getApplicationContext(),"Select DOB Proof ",Toast.LENGTH_LONG).show();

                    }
                    else if (identity_sp.getSelectedItem().toString().equalsIgnoreCase("Select Identity Proof")){
                        Toast.makeText(getApplicationContext(),"Select Identity Proof ",Toast.LENGTH_LONG).show();

                    }
                    else if (cat_formtype.getSelectedItem().toString().equalsIgnoreCase("Select AppType")){
                        Toast.makeText(getApplicationContext(),"Select AppType ",Toast.LENGTH_LONG).show();
                    }
                    else {
                        NsdlPanApply(UserId,"2",pancard_et.getText().toString(),catid,genderid,et_dob.getText().toString(),f_name_et.getText().toString(),
                                m_name_et.getText().toString(),l_name_et.getText().toString(),father_fname.getText().toString(),father_m_name_et.getText().toString(),
                                father_l_name_et.getText().toString(),mob_et.getText().toString(),email_et.getText().toString(),
                                aadhar_et.getText().toString(),name_on_pan_et.getText().toString(),name_on_aadhar_et.getText().toString(),
                                addressid,dobid,identityid,pincode_et.getText().toString(),stateet.getSelectedItem().toString(),building_et.getText().toString(),
                                post_office_et.getText().toString(),location_et.getText().toString(),city_et.getText().toString(),
                                area_code_et.getText().toString(),ao_type_et.getText().toString(),range_code_et.getText().toString(),ao_number_et.getText().toString(),
                                "0",formtypeid,"","","","","","","","","","","",
                                checksum);
                    } }}});

        // on below line we are adding click listener for our pick date button
        et_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        KYC_CSFPANApplication.this,  R.style.MyTimePickerDialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                et_dob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            } });


        cat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    for (int j = 0; j <= CatId.size(); j++) {
                        if (cat_spinner.getSelectedItem().toString().equalsIgnoreCase(CatName.get(j))) {
                            // position = i;
                            catid = CatId.get(j - 1);
                            break;
                        } } } }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        address_proof_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    for (int j = 0; j <= AddressId.size(); j++) {
                        if (address_proof_sp.getSelectedItem().toString().equalsIgnoreCase(AddressName.get(j))) {
                            // position = i;
                            addressid = AddressId.get(j - 1);
                            break;
                        } } } }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        identity_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    for (int j = 0; j <= IdentityId.size(); j++) {
                        if (identity_sp.getSelectedItem().toString().equalsIgnoreCase(IdentityName.get(j))) {
                            // position = i;
                            identityid = IdentityId.get(j - 1);
                            break;
                        } } } }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        dob_proof_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    for (int j = 0; j <= DOBId.size(); j++) {
                        if (dob_proof_sp.getSelectedItem().toString().equalsIgnoreCase(DOBName.get(j))) {
                            // position = i;
                            dobid = DOBId.get(j - 1);
                            break;
                        } } } }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        gender_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    for (int j = 0; j <= GenderId.size(); j++) {
                        if (gender_sp.getSelectedItem().toString().equalsIgnoreCase(GenderName.get(j))) {
                            // position = i;
                            genderid = GenderId.get(j - 1);
                            break;
                        } } } }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        cat_formtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    for (int j = 0; j <= FormTypeId.size(); j++) {
                        if (cat_formtype.getSelectedItem().toString().equalsIgnoreCase(FormTypeName.get(j))) {
                            // position = i;
                            formtypeid = FormTypeId.get(j - 1);
                            break;
                        } } } }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void NSDLCategoryList() {
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_nsdlCategoryList();
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
                        CatName.add("Select Category");
                        JSONArray jsonArray = object.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CatId.add(jsonArray.getString(i));
                            String statename = jsonArray.getString(i);
                            CatName.add(statename);
                        }
                        cat_spinner.setAdapter(new ArrayAdapter<String>(KYC_CSFPANApplication.this, android.R.layout.simple_spinner_dropdown_item, CatName));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, KYC_CSFPANApplication.this, call1, "", true);
    }

    public void ProofOfAddressList() {
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_proofOfAddressList();
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
                        AddressName.add("Select Address Proof");
                        JSONArray jsonArray = object.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            AddressId.add(jsonArray.getString(i));
                            String statename = jsonArray.getString(i);
                            AddressName.add(statename);
                        }
                        address_proof_sp.setAdapter(new ArrayAdapter<String>(KYC_CSFPANApplication.this, android.R.layout.simple_spinner_dropdown_item, AddressName));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, KYC_CSFPANApplication.this, call1, "", true);
    }


    public void ProofOfIdentityList() {
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_proofOfIdentityList();
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
                        IdentityName.add("Select Identity Proof");
                        JSONArray jsonArray = object.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            IdentityId.add(jsonArray.getString(i));
                            String statename = jsonArray.getString(i);
                            IdentityName.add(statename);
                        }
                        identity_sp.setAdapter(new ArrayAdapter<String>(KYC_CSFPANApplication.this, android.R.layout.simple_spinner_dropdown_item, IdentityName));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, KYC_CSFPANApplication.this, call1, "", true);
    }


    public void ProofOfDOBList() {
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_proofOfDOBList();
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
                        DOBName.add("Select DOB Proof");
                        JSONArray jsonArray = object.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            DOBId.add(jsonArray.getString(i));
                            String statename = jsonArray.getString(i);
                            DOBName.add(statename);
                        }
                        dob_proof_sp.setAdapter(new ArrayAdapter<String>(KYC_CSFPANApplication.this, android.R.layout.simple_spinner_dropdown_item, DOBName));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, KYC_CSFPANApplication.this, call1, "", true);
    }



    public void GenderList() {
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_genderList();
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
                        GenderName.add("Select Gender");
                        JSONArray jsonArray = object.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            GenderId.add(jsonArray.getString(i));
                            String statename = jsonArray.getString(i);
                            GenderName.add(statename);
                        }
                        gender_sp.setAdapter(new ArrayAdapter<String>(KYC_CSFPANApplication.this, android.R.layout.simple_spinner_dropdown_item, GenderName));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, KYC_CSFPANApplication.this, call1, "", true);
    }


    public void AppTypeList() {
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_appTypeList();
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
                        FormTypeName.add("Select AppType");
                        JSONArray jsonArray = object.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            FormTypeId.add(jsonArray.getString(i));
                            String statename = jsonArray.getString(i);
                            FormTypeName.add(statename);
                        }
                        cat_formtype.setAdapter(new ArrayAdapter<String>(KYC_CSFPANApplication.this, android.R.layout.simple_spinner_dropdown_item, FormTypeName));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, KYC_CSFPANApplication.this, call1, "", true);
    }




    public void NsdlPanApply(String user_id,String status,String pan,String category,String gender,
                              String dob,String fname,String mname,String lname,String ffname,String fmname,String flname,
                              String mobile,String email,String aadhar,String nopan,String napa,String poad,String podob,
                              String poid,String pincode,String state,String village,String post_office,String locality,String district,
                              String area_code,String ao_type,String range_code,String ao_no,String is_illiterate,String app_type,String ref_title,
                              String fname1,String mname1,String lname1,String ref_pincode,String state1,String ref_address1,String ref_address2,
                              String ref_address3,String ref_address4,String city1,String checksum) {
        String otp1 = new GlobalAppApis().GetnsdlPanApply1(user_id,status,pan,category,gender,dob,fname,mname,lname,ffname,fmname,flname,
                mobile,email,aadhar,nopan,napa,poad,podob,poid,pincode,state,village,post_office,locality,district,
                area_code,ao_type,range_code,ao_no,is_illiterate,app_type,ref_title,fname1,mname1,lname1,ref_pincode,state1,ref_address1,
                ref_address2,ref_address3,ref_address4,city1,checksum);
        Log.d("pancorrection","cxc"+otp1);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_nsdlPanApply(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("rescorrection","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("status").equalsIgnoreCase("0")){
                        String msg       = object.getString("message");
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(KYC_CSFPANApplication.this).setTitle("Response:-")
                                .setMessage(msg).setCancelable(false)
                                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                        //startActivity(i);
                                    }});
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(KYC_CSFPANApplication.this).setTitle("Error Response:-")
                                .setMessage(object.getString("message")).setCancelable(false)
                                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                        //startActivity(i);
                                    }});
                        AlertDialog alert = builder.create();
                        alert.show();
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, KYC_CSFPANApplication.this, call1, "", true);
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
                        stateet.setAdapter(new ArrayAdapter<String>(KYC_CSFPANApplication.this, android.R.layout.simple_spinner_dropdown_item, ProductSizeValue));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, KYC_CSFPANApplication.this, call1, "", true);
    }
}