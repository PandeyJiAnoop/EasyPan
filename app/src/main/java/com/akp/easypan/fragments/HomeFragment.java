package com.akp.easypan.fragments;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.akp.RetrofitAPI.ApiService;
import com.akp.RetrofitAPI.ConnectToRetrofit;
import com.akp.RetrofitAPI.GlobalAppApis;
import com.akp.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.easypan.AddWallet;
import com.akp.easypan.Das_AccountOpening.AO_AxisBank;
import com.akp.easypan.Das_AccountOpening.AO_HDFCBank;
import com.akp.easypan.Das_AccountOpening.AO_JupitarBank;
import com.akp.easypan.Das_AccountOpening.AO_kotakBank;
import com.akp.easypan.Das_BillPaymnet.DTH;
import com.akp.easypan.Das_BillPaymnet.EazyPanBroadBandBllPayment;
import com.akp.easypan.Das_BillPaymnet.EazyPanDigitalBllPayment;
import com.akp.easypan.Das_BillPaymnet.EazyPanElectricityBllPayment;
import com.akp.easypan.Das_BillPaymnet.EazyPanFasttagBllPayment;
import com.akp.easypan.Das_BillPaymnet.EazyPanInsuarnceBllPayment;
import com.akp.easypan.Das_BillPaymnet.EazyPanLPGGasPayment;
import com.akp.easypan.Das_BillPaymnet.EazyPanLandLineBllPayment;
import com.akp.easypan.Das_BillPaymnet.EazyPanLoanRepaymentBllPayment;
import com.akp.easypan.Das_BillPaymnet.EazyPanMuncipialBllPayment;
import com.akp.easypan.Das_BillPaymnet.EazyPanPostPaidBllPayment;
import com.akp.easypan.Das_BillPaymnet.EazyPanWaterBllPayment;
import com.akp.easypan.Das_BillPaymnet.MobileRecharge;
import com.akp.easypan.Das_FindLostDocuments.FL_AadharPrint;
import com.akp.easypan.Das_FindLostDocuments.FL_AyushmanPrint;
import com.akp.easypan.Das_FindLostDocuments.FL_DLPrint;
import com.akp.easypan.Das_FindLostDocuments.FL_FindPanByAdhar;
import com.akp.easypan.Das_GovermentServices.GS_UDTDCard;
import com.akp.easypan.Das_GovermentServices.GS_ViklankPension;
import com.akp.easypan.Das_GovermentServices.GS_VridhaPension;
import com.akp.easypan.Das_GovermentServices.GS_WidowPension;
import com.akp.easypan.Das_KYCBlock.KYC_ApplyNSDLForm;
import com.akp.easypan.Das_KYCBlock.KYC_CSFPANApplication;
import com.akp.easypan.Das_KYCBlock.KYC_TrackPancard;
import com.akp.easypan.Das_KYCBlock.NSDL_KYC_Form;
import com.akp.easypan.Helper.NetworkConnectionHelper;
import com.akp.easypan.Passbook;
import com.akp.easypan.SliderMenu.MyWallet;
import com.akp.easypan.dashboard;
import com.akp.easypan.entity.User;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.akp.easypan.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.paytm.pgsdk.PaytmClientCertificate;
import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import retrofit2.Call;

import static android.content.ContentValues.TAG;
import static android.content.Context.DOWNLOAD_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost;
/**
 * Created by Anoop Pandey on 9696381023.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private OnFragmentInteractionListener mListener;

    String data_res = "{\n" +
            "    \"reqEntityData\": {\n" +
            "        \"txnid\": \"9ffd42d1-b1dc-4fe0-9ca7-932032797671\",\n" +
            "        \"reqTs\": \"2023-7-13T:16:56.487\",\n" +
            "        \"entityId\": \"NSDL1234\",\n" +
            "        \"returnUrl\": \"https:\\\\\\\\172.19.75.53\\\\PanApplication\\\\panRes\",\n" +
            "        \"formData\": \"eyJhcHBsaWNhbnREdG8iOnsiYXBwbGlUeXBlIjoiQSIsImNhdGVnb3J5IjoiQSIsImFhZGhhYXIiOm51bGwsInRpdGxlIjpudWxsLCJsYXN0TmFtZSI6IiIsImZpcnN0TmFtZSI6IiIsIm1pZGRsZU5hbWUiOiIiLCJkb2IiOm51bGwsImdlbmRlciI6IiIsImNvbnNlbnQiOmZhbHNlLCJuYW1lT25QYW5DYXJkIjoiIiwia25vd25CeU90aGVyTmFtZSI6IiIsIm90aGVyVGl0bGUiOm51bGwsIm90aGVyTGFzdE5hbWUiOm51bGwsIm90aGVyRmlyc3ROYW1lIjoiIiwib3RoZXJNaWRkbGVOYW1lIjoiIiwibmFtZUFzUGVyQWFkaGFyIjoiIiwiYXBwbG5Nb2RlIjoiSyJ9LCJwYXJlbnRzRGV0YWlscyI6eyJzaW5nbGVQYXJlbnQiOiIiLCJmYXRoZXJMYXN0TmFtZSI6IiIsImZhdGhlckZpcnN0TmFtZSI6IiIsImZhdGhlck1pZGRsZU5hbWUiOiIiLCJtb3RoZXJMYXN0TmFtZSI6IiIsIm1vdGhlckZpcnN0TmFtZSI6IiIsIm1vdGhlck1pZGRsZU5hbWUiOiIiLCJwYXJlbnROYW1lUHJpbnQiOiIifSwib3RoZXJEZXRhaWxzIjp7InBoeVBhbklzUmVxIjoiIiwic291cmNlT2ZJbmNvbWUiOnsic2FsYXJ5IjpmYWxzZSwib3RoZXJTb3VyY2UiOmZhbHNlLCJidXNpbmVzc1Byb2YiOmZhbHNlLCJidXNpbmVzc1ByZkNvZGUiOiIiLCJub0luY29tZSI6ZmFsc2UsImhvdXNlUHJvIjpmYWxzZSwiY2FwaXRhbEdhaW5zIjpmYWxzZX0sImFkZHJGb3JDb21tdW5pY2F0aW9uIjoiIiwib2ZmaWNlQWRkcmVzcyI6eyJmbGF0Tm8iOiIiLCJuYW1lT2ZQcmVtaXNlcyI6IiIsInJvYWQiOiIiLCJhcmVhIjoiIiwidG93biI6IiIsImNvdW50cnlOYW1lIjoiIiwic3RhdGUiOiJub25lIiwicGluQ29kZSI6IiIsInppcENvZGUiOiIiLCJvZmZpY2VOYW1lIjoiIn0sImlzZENvZGUiOiIiLCJzdGRDb2RlIjoiIiwidGVsT3JNb2JObyI6IiIsImVtYWlsSWQiOiIiLCJwbGFjZSI6IiIsImRhdGUiOm51bGwsInJhVmFsdWUiOiIifSwiYW9Db2RlIjp7ImFyZWFDb2RlIjoiIiwiYW9UeXBlIjoiIiwicmFuZ2VDb2RlIjoiIiwiYW9ObyI6IiJ9fQ==\"\n" +
            "    },\n" +
            "    \"signature\": \"FxPsbsZMLrwyU8pMRrJIRZzRmq5gRWEmboFncELrAAArIzLYNQ/huU3L/pqUdlGTO/7YWQxCJzjBqeoST4w7UkLCZqXekZElq3PORBeJRzSFgVy1QMR/b7FuL/jVBvNx0ueIx+9700g1OKfJRlysXeIPMRRSE/t4r5cjCQAgYpEMyyDOgaDwdtkUyw+SUWm4RCcVjMCdHCSNRg2V0GgCCIRfdcM7Z6zW7SNR0T2DEiN9aT6RdrAtssuJVvIwRsWnOyF3PgMDGt7BPZB/AR2Nx+6JAa5gXoeQdGl1XHKEJLEgpUimaypl2ljD8FcE6HJsVBaMs3Crxl4Te54hXMro3A==\",\n" +
            "    \"validateACK\": false\n" +
            "}";

    Context context;
    DownloadManager manager;
    Activity activity;
    private String mParam1, mParam2;
    private SliderLayout sliderLayout;
    String UserId, Checksum;
    TextView apis_bal_tv, wallet_bal_tv;

    LinearLayout nsdl_kyc_ll, nsdl_ll, aeps_ll, uti_ll;
    LinearLayout mobile_ll, dth_ll, electricity_ll, water_ll;
    LinearLayout aadhar_print_ll, ayushman_print_ll, findpanbyaadhar_ll, dl_print_ll;
    LinearLayout axis_bank_ll, hdfc_bank_ll, jupitar_bank_ll, kotak_bank_ll;
    LinearLayout vridha_pension_ll, viklank_pension_ll, widow_pension_ll, utdt_card_ll;
    private AlertDialog alertDialog, alertDialog1, alertDialogkyc,alertDialog2, alertDialogwallet;
    SwipeRefreshLayout srl_refresh;

    TextView header_tv;
    //    private final ArrayList<HashMap<String, String>> arrLegalList = new ArrayList<>();
//    private DasListAdapter pdfAdapTer;
    RecyclerView cust_recyclerView;
    private ServiceAdapter adapter;
    private RequestQueue requestQueue;
    private List<ServiceA> serviceList;
    //    private PaytmPGService paytmPGService;
//    private static final String MERCHANT_ID = "znhMaK89042764491641";
//    private static final String INDUSTRY_TYPE_ID = "Retail";
//    private static final String CHANNEL_ID = "WAP";
//    private static final String WEBSITE = "DEFAULT";
//    private static final String CALLBACK_URL = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=";
//    private static final String TXN_AMOUNT = "10";
//    private static final String CURRENCY_CODE = "INR";
//    private static final String EMAIL = "customer@example.com";
//    private static final String MOBILE_NUMBER = "1234567890";
//    private String orderIdString;
    private String checksum_paytm, Mobile,checksum;
    private EditText w_rupee_et;
    String txt_username,txt_mob;

    LinearLayout mobilepostpaid_ll,fasttag_ll,landline_ll,broadband_ll,loan_repayment_ll,insurance_ll,digital_ll,muncipial_ll,
            lic_ll,credit_bill,lpg_ll;

    LinearLayout bus_ll,flit_ll,train_ll,hotel_ll;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Fresco.initialize(getActivity());
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        context = this.getActivity();
        sliderLayout = view.findViewById(R.id.imageSlider);
        BottomNavigationView navigation = (BottomNavigationView) view.findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        findViewByIds(view);
        onClickListeners();
        onClickListeners();
//        Calendar c = Calendar.getInstance();
//        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
//        String date = df.format(c.getTime());
//        Random rand = new Random();
//        int min =1000, max= 9999;
//// nextInt as provided by Random is exclusive of the top value so you need to add 1
//        int randomNum = rand.nextInt((max - min) + 1) + min;
//        orderIdString =  date+String.valueOf(randomNum);
//       String callbackurl=CALLBACK_URL+orderIdString;
//        paytmPGService = PaytmPGService.getProductionService();

        String uniqueID = UUID.randomUUID().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        return view;
    }

    ///
    private void findViewByIds(View view) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("U_id", "");
        Checksum = sharedPreferences.getString("Checksum", "");
        Mobile = sharedPreferences.getString("mobile", "");
        wallet_bal_tv = view.findViewById(R.id.wallet_bal_tv);
        apis_bal_tv = view.findViewById(R.id.apis_bal_tv);
        srl_refresh = view.findViewById(R.id.srl_refresh);
        //forBanner();
        setSliderViews();
        String input = UserId + ":ip6wgncgt6:nxe7j2f6c0";
        String strup = input.toUpperCase();
        checksum = generateMD5Checksum(strup);
        Log.d("TAG", "UPPER " + strup + "MD5 checksum of " + input + ": " + checksum);
        cust_recyclerView = view.findViewById(R.id.cust_recyclerView);
        header_tv = view.findViewById(R.id.header_tv);

        String input1 = UserId + ":" + Mobile + ":ip6wgncgt6:nxe7j2f6c0";
        String strup1 = input1.toUpperCase();
        checksum_paytm = generateMD5Checksum(strup1);
        Log.d("TAG", "UPPER " + strup1 + "MD5 checksum of " + input1 + ": " + checksum_paytm);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        cust_recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
//        cust_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        serviceList = new ArrayList<>();
        adapter = new ServiceAdapter(getActivity(), serviceList);
        cust_recyclerView.setAdapter(adapter);

        GetProfile(UserId,checksum);
        fetchData();

        nsdl_kyc_ll = view.findViewById(R.id.nsdl_kyc_ll);
        nsdl_ll = view.findViewById(R.id.nsdl_ll);
        aeps_ll = view.findViewById(R.id.aeps_ll);
        uti_ll = view.findViewById(R.id.uti_ll);
        lpg_ll = view.findViewById(R.id.lpg_ll);

        nsdl_kyc_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"AS PER ITD GUIDELINE WE ARE NOT WORKING WITH MOBILE APP ",Toast.LENGTH_LONG).show();

          /*      //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                ViewGroup viewGroup = view.findViewById(android.R.id.content);
                //then we will inflate the custom alert dialog xml that we created
                View dialogView = LayoutInflater.from(context).inflate(R.layout.nsdl_kyc_popup, viewGroup, false);
                Button kyc_one = (Button) dialogView.findViewById(R.id.kyc_one);
                Button kyc_two = (Button) dialogView.findViewById(R.id.kyc_two);
                Button kyc_three = (Button) dialogView.findViewById(R.id.kyc_three);
                Button kyc_four = (Button) dialogView.findViewById(R.id.kyc_four);
                kyc_one.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // on below line we are checking if the google maps is installed or not by specifying package name of google maps.
                        if (checkInstallation(context, "protean.assisted.ekyc")) {
                            Intent intent = new Intent(getActivity(), NSDL_KYC_Form.class);
                            intent.putExtra("pan_type","new_ekyc");
                            intent.putExtra("pan_name",txt_username);
                            intent.putExtra("pan_mob",txt_mob);
                            startActivity(intent);
                            activity.overridePendingTransition(0, 0);
                            alertDialogkyc.dismiss();
                        } else {
                            try {
                                Intent viewIntent = new Intent("android.intent.action.VIEW",
                                                Uri.parse("https://play.google.com/store/apps/details?id=protean.assisted.ekyc"));
                                startActivity(viewIntent);
                            }catch(Exception e) {
                                Toast.makeText(context,"Unable to Connect Try Again...",
                                        Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                            // on below line displaying toast message if google maps is not installed.
                            Toast.makeText(context, "PAN Service Agency APP is not installed on this device..", Toast.LENGTH_LONG).show();
                            alertDialogkyc.dismiss();
                        }

                    }
                });
                kyc_two.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // on below line we are checking if the google maps is installed or not by specifying package name of google maps.
                        if (checkInstallation(context, "protean.assisted.ekyc")) {
                            Intent intent = new Intent(getActivity(), NSDL_KYC_Form.class);
                            intent.putExtra("pan_type","new_esign");
                            intent.putExtra("pan_name",txt_username);
                            intent.putExtra("pan_mob",txt_mob);
                            startActivity(intent);
                            activity.overridePendingTransition(0, 0);
                            alertDialogkyc.dismiss();
                        } else {
                            try {
                                Intent viewIntent =
                                        new Intent("android.intent.action.VIEW",
                                                Uri.parse("https://play.google.com/store/apps/details?id=protean.assisted.ekyc"));
                                startActivity(viewIntent);
                            }catch(Exception e) {
                                Toast.makeText(context,"Unable to Connect Try Again...",
                                        Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                            // on below line displaying toast message if google maps is not installed.
                            Toast.makeText(context, "PAN Service Agency APP is not installed on this device..", Toast.LENGTH_LONG).show();
                            alertDialogkyc.dismiss();
                        }
                    }
                });
                kyc_three.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // on below line we are checking if the google maps is installed or not by specifying package name of google maps.
                        if (checkInstallation(context, "protean.assisted.ekyc")) {
                            Intent intent = new Intent(getActivity(), NSDL_KYC_Form.class);
                            intent.putExtra("pan_type","cr_ekyc");
                            intent.putExtra("pan_name",txt_username);
                            intent.putExtra("pan_mob",txt_mob);
                            startActivity(intent);
                            activity.overridePendingTransition(0, 0);
                            alertDialogkyc.dismiss();
                        } else {
                            try {
                                Intent viewIntent =
                                        new Intent("android.intent.action.VIEW",
                                                Uri.parse("https://play.google.com/store/apps/details?id=protean.assisted.ekyc"));
                                startActivity(viewIntent);
                            }catch(Exception e) {
                                Toast.makeText(context,"Unable to Connect Try Again...",
                                        Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                            // on below line displaying toast message if google maps is not installed.
                            Toast.makeText(context, "PAN Service Agency APP is not installed on this device..", Toast.LENGTH_LONG).show();
                            alertDialogkyc.dismiss();

                        }

                    }


                });

                kyc_four.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkInstallation(context, "protean.assisted.ekyc")) {
                            Intent intent = new Intent(getActivity(), NSDL_KYC_Form.class);
                            intent.putExtra("pan_type","cr_esign");
                            intent.putExtra("pan_name",txt_username);
                            intent.putExtra("pan_mob",txt_mob);
                            startActivity(intent);
                            activity.overridePendingTransition(0, 0);
                            alertDialogkyc.dismiss();
                        } else {
                            try {
                                Intent viewIntent =
                                        new Intent("android.intent.action.VIEW",
                                                Uri.parse("https://play.google.com/store/apps/details?id=protean.assisted.ekyc"));
                                startActivity(viewIntent);
                            }catch(Exception e) {
                                Toast.makeText(context,"Unable to Connect Try Again...",
                                        Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                            // on below line displaying toast message if google maps is not installed.
                            Toast.makeText(context, "PAN Service Agency APP is not installed on this device..", Toast.LENGTH_LONG).show();
                            alertDialogkyc.dismiss();
                        }

                    }
                });

                //Now we need an AlertDialog.Builder object
                androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(context);
                //setting the view of the builder to our custom view that we already inflated
                builder1.setView(dialogView);
                //finally creating the alert dialog and displaying it
                alertDialogkyc = builder1.create();
                alertDialogkyc.show();*/
            }
        });
        nsdl_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                ViewGroup viewGroup = view.findViewById(android.R.id.content);
                //then we will inflate the custom alert dialog xml that we created
                View dialogView = LayoutInflater.from(context).inflate(R.layout.nsdl_pancard_popup, viewGroup, false);
                Button one = (Button) dialogView.findViewById(R.id.one);
                Button two = (Button) dialogView.findViewById(R.id.two);
                Button three = (Button) dialogView.findViewById(R.id.three);
                one.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), KYC_ApplyNSDLForm.class);
                        startActivity(intent);
                        activity.overridePendingTransition(0, 0);
                        alertDialog.dismiss();
                    }
                });
                two.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), KYC_CSFPANApplication.class);
                        startActivity(intent);
                        activity.overridePendingTransition(0, 0);
                        alertDialog.dismiss();
                    }
                });
                three.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), KYC_TrackPancard.class);
                        startActivity(intent);
                        activity.overridePendingTransition(0, 0);
                        alertDialog.dismiss();
                    }
                });
                //Now we need an AlertDialog.Builder object
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                //setting the view of the builder to our custom view that we already inflated
                builder.setView(dialogView);
                //finally creating the alert dialog and displaying it
                alertDialog = builder.create();
                alertDialog.show();
//                Intent intent=new Intent(getActivity(), KYC_NSDL.class); startActivity(intent);
            }
        });
        aeps_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Coming Soon!",Toast.LENGTH_LONG).show();
               /* //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                ViewGroup viewGroup = view.findViewById(android.R.id.content);
                //then we will inflate the custom alert dialog xml that we created
                View dialogView = LayoutInflater.from(context).inflate(R.layout.commingsoon_popup, viewGroup, false);
                Button exit = (Button) dialogView.findViewById(R.id.exit);

                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog1.dismiss();
                    }
                });
                //Now we need an AlertDialog.Builder object
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                //setting the view of the builder to our custom view that we already inflated
                builder.setView(dialogView);
                //finally creating the alert dialog and displaying it
                alertDialog1 = builder.create();
                alertDialog1.show();
//                Intent intent=new Intent(getActivity(), KYC_AEPS.class); startActivity(intent);*/
            } });

        uti_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UTIAppAutoLoginUrl(UserId, checksum);
            }
        });

        mobile_ll = view.findViewById(R.id.mobile_ll);
        dth_ll = view.findViewById(R.id.dth_ll);
        electricity_ll = view.findViewById(R.id.electricity_ll);
        water_ll = view.findViewById(R.id.water_ll);


        mobilepostpaid_ll = view.findViewById(R.id.mobilepostpaid_ll);
        fasttag_ll = view.findViewById(R.id.fasttag_ll);
        landline_ll = view.findViewById(R.id.landline_ll);
        broadband_ll = view.findViewById(R.id.broadband_ll);
        loan_repayment_ll = view.findViewById(R.id.loan_repayment_ll);
        insurance_ll = view.findViewById(R.id.insurance_ll);
        digital_ll = view.findViewById(R.id.digital_ll);
        muncipial_ll = view.findViewById(R.id.muncipial_ll);
        lic_ll = view.findViewById(R.id.lic_ll);
        credit_bill = view.findViewById(R.id.credit_bill);

        mobile_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MobileRecharge.class);
                activity.overridePendingTransition(0, 0);
                intent.putExtra("onlyservice", "MOBILE RECHARGE");
                startActivity(intent);
            }
        });
        dth_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DTH.class);
                activity.overridePendingTransition(0, 0);
                intent.putExtra("onlyservice", "DTH");
                startActivity(intent);
            }
        });
        electricity_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Coming Soon!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getActivity(), EazyPanElectricityBllPayment.class);
                activity.overridePendingTransition(0, 0);
                intent.putExtra("onlyservice", "ELECTRICITY");
                startActivity(intent);
            }
        });
        water_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Coming Soon!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), EazyPanWaterBllPayment.class);
                activity.overridePendingTransition(0, 0);
                intent.putExtra("onlyservice", "WATER");
                startActivity(intent);
            }
        });


        mobilepostpaid_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EazyPanPostPaidBllPayment.class);
                activity.overridePendingTransition(0, 0);
                intent.putExtra("onlyservice", "MOBILE POSTPAID");
                startActivity(intent);
            }
        });

        lpg_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EazyPanLPGGasPayment.class);
                activity.overridePendingTransition(0, 0);
                intent.putExtra("onlyservice", "LPG Gas");
                startActivity(intent);
            }
        });
        fasttag_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Coming Soon!", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(getActivity(), EazyPanFasttagBllPayment.class);
//                activity.overridePendingTransition(0, 0);
//                intent.putExtra("onlyservice", "Fasttag");
//                startActivity(intent);
            }
        });
        landline_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Coming Soon!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getActivity(), EazyPanLandLineBllPayment.class);
                activity.overridePendingTransition(0, 0);
                intent.putExtra("onlyservice", "LandLine");
                startActivity(intent);
            }
        });
        broadband_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Coming Soon!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), EazyPanBroadBandBllPayment.class);
                activity.overridePendingTransition(0, 0);
                intent.putExtra("onlyservice", "Broadband");
                startActivity(intent);
            }
        });

        loan_repayment_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EazyPanLoanRepaymentBllPayment.class);
                activity.overridePendingTransition(0, 0);
                intent.putExtra("onlyservice", "Loan Repayment");
                startActivity(intent);
            }
        });
        insurance_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EazyPanInsuarnceBllPayment.class);
                activity.overridePendingTransition(0, 0);
                intent.putExtra("onlyservice", "Insuarnce");
                startActivity(intent);
            }
        });
        digital_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Coming Soon!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getActivity(), EazyPanDigitalBllPayment.class);
                activity.overridePendingTransition(0, 0);
                intent.putExtra("onlyservice", "Digital");
                startActivity(intent);
            }
        });
        muncipial_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Coming Soon!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), EazyPanMuncipialBllPayment.class);
                activity.overridePendingTransition(0, 0);
                intent.putExtra("onlyservice", "Muncipial");
                startActivity(intent);
            }
        });

        lic_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Coming Soon!", Toast.LENGTH_LONG).show();

            }
        });
        credit_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Coming Soon!", Toast.LENGTH_LONG).show();

            }
        });

        bus_ll = view.findViewById(R.id.bus_ll);
        flit_ll = view.findViewById(R.id.flit_ll);
        train_ll = view.findViewById(R.id.train_ll);
        hotel_ll = view.findViewById(R.id.hotel_ll);

        bus_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Coming Soon!", Toast.LENGTH_LONG).show();
            }
        });
        flit_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Coming Soon!", Toast.LENGTH_LONG).show();
            }
        });
        train_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Coming Soon!", Toast.LENGTH_LONG).show();
            }
        });
        hotel_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Coming Soon!", Toast.LENGTH_LONG).show();
            }
        });


        aadhar_print_ll = view.findViewById(R.id.aadhar_print_ll);
        ayushman_print_ll = view.findViewById(R.id.ayushman_print_ll);
        findpanbyaadhar_ll = view.findViewById(R.id.findpanbyaadhar_ll);
        dl_print_ll = view.findViewById(R.id.dl_print_ll);
        aadhar_print_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FL_AadharPrint.class);
                startActivity(intent);
                activity.overridePendingTransition(0, 0);
            }
        });
        ayushman_print_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FL_AyushmanPrint.class);
                startActivity(intent);
                activity.overridePendingTransition(0, 0);
            }
        });
        findpanbyaadhar_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FL_FindPanByAdhar.class);
                startActivity(intent);
                activity.overridePendingTransition(0, 0);
            }
        });
        dl_print_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FL_DLPrint.class);
                startActivity(intent);
                activity.overridePendingTransition(0, 0);
            }
        });


        axis_bank_ll = view.findViewById(R.id.axis_bank_ll);
        hdfc_bank_ll = view.findViewById(R.id.hdfc_bank_ll);
        jupitar_bank_ll = view.findViewById(R.id.jupitar_bank_ll);
        kotak_bank_ll = view.findViewById(R.id.kotak_bank_ll);
        axis_bank_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AO_AxisBank.class);
                startActivity(intent);
                activity.overridePendingTransition(0, 0);
            }
        });
        hdfc_bank_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AO_HDFCBank.class);
                startActivity(intent);
                activity.overridePendingTransition(0, 0);
            }
        });
        jupitar_bank_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AO_JupitarBank.class);
                startActivity(intent);
                activity.overridePendingTransition(0, 0);
            }
        });
        kotak_bank_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AO_kotakBank.class);
                startActivity(intent);
                activity.overridePendingTransition(0, 0);
            }
        });


        vridha_pension_ll = view.findViewById(R.id.vridha_pension_ll);
        widow_pension_ll = view.findViewById(R.id.widow_pension_ll);
        viklank_pension_ll = view.findViewById(R.id.viklank_pension_ll);
        utdt_card_ll = view.findViewById(R.id.utdt_card_ll);
        vridha_pension_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GS_VridhaPension.class);
                startActivity(intent);
                activity.overridePendingTransition(0, 0);
            }
        });
        widow_pension_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GS_WidowPension.class);
                startActivity(intent);
                activity.overridePendingTransition(0, 0);
            }
        });
        viklank_pension_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GS_ViklankPension.class);
                startActivity(intent);
                activity.overridePendingTransition(0, 0);
            }
        });
        utdt_card_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GS_UDTDCard.class);
                startActivity(intent);
                activity.overridePendingTransition(0, 0);
            }
        });

        GetDashboard(UserId, checksum);
    }

    private void onClickListeners() {
        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(getActivity())) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            activity.finish();
                            activity.overridePendingTransition(0, 0);
                            startActivity(activity.getIntent());
                            activity.overridePendingTransition(0, 0);
                            srl_refresh.setRefreshing(false);
                        }
                    }, 2000);
                } else {
                    Toast.makeText(getActivity(), "Please check your internet connection! try again...", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_add:

                    AddWalletAmount();


//                    Intent intent=new Intent(getContext(), AddFundReport.class);
//                    startActivity(intent);
                    return true;
                case R.id.navigation_me:
                    Intent intent1 = new Intent(getContext(), Passbook.class);
                    startActivity(intent1);
                    return true;
            }
            return false;
        }
    };

    private void AddWalletAmount() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(context).inflate(R.layout.addwallet_popup, viewGroup, false);
        AppCompatButton exit = (AppCompatButton) dialogView.findViewById(R.id.w_submit_btn);
        w_rupee_et = (EditText) dialogView.findViewById(R.id.w_rupee_et);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (w_rupee_et.getText().toString().equalsIgnoreCase("")) {
                    w_rupee_et.setError("Enter Amount");
                    w_rupee_et.requestFocus();
                } else {
                    GetChecksumAPI(UserId, Mobile, "admin@gmail.com", w_rupee_et.getText().toString(), checksum_paytm);
                    alertDialogwallet.dismiss();
                }}});
        //Now we need an AlertDialog.Builder object
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialogwallet = builder.create();
        alertDialogwallet.show();
    }

    private void setSliderViews() {
        for (int i = 0; i <= 5; i++) {
            SliderView sliderView = new SliderView(context);
            switch (i) {
                case 0:
                    sliderView.setImageDrawable(R.drawable.b_1);
                    sliderView.setDescription("Welcome To\n" + "Payzeepay");
                    break;
                case 1:
                    sliderView.setImageDrawable(R.drawable.b_2);
//                    sliderView.setDescription("सच होगा सपना");
                    break;
                case 2:
                    sliderView.setImageDrawable(R.drawable.b_3);
                    break;
                case 3:
                    sliderView.setImageDrawable(R.drawable.b_4);
                    break;
                case 4:
                    sliderView.setImageDrawable(R.drawable.b_5);
                    break;
                case 5:
                    sliderView.setImageDrawable(R.drawable.b_6);
                    break;
            }
            sliderView.setImageScaleType(ImageView.ScaleType.FIT_XY);
            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Toast.makeText(context, "This is slider " + (finalI + 1), Toast.LENGTH_LONG).show();
                }
            });
            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);
        }
    }


    public void GetDashboard(String user_id, String checksum) {
        String otp1 = new GlobalAppApis().Dashboard(user_id, checksum);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_dashboard(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("res", "cxc" + result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("status").equalsIgnoreCase("0")) {
                        String msg = object.getString("message");
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        String phone = object.getString("phone");
                        String aeps_balance = object.getString("aeps_balance");
                        String wallet_balance = object.getString("wallet_balance");
                        apis_bal_tv.setText("Rs. " + aeps_balance);
                        wallet_bal_tv.setText("Rs. " + wallet_balance);
                        String msg = object.getString("message");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, getActivity(), call1, "", true);
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


    private void launch(String packageName, String className) {
        if (context == null || packageName == null || className == null) return;
        ComponentName component = new ComponentName(packageName, className);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(component);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            Log.v(TAG, e.toString());
            Toast.makeText(getActivity(), "Package Not Found!!\n Install Apk Firstly", Toast.LENGTH_LONG).show();
            Log.d("dhfghfghfh", e.toString());
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=protean.assisted.ekyc")));

        }

    }




  /*  private void initiatePayment(String amt) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", MERCHANT_ID);
        paramMap.put("ORDER_ID", orderIdString); // Unique order ID for each transaction
        paramMap.put("CUST_ID", UserId); // Customer ID (optional)
        paramMap.put("INDUSTRY_TYPE_ID", INDUSTRY_TYPE_ID); // Industry type (e.g., "Retail", "Education")
        paramMap.put("CHANNEL_ID", CHANNEL_ID); // Channel ID (e.g., "WAP", "WEB")
        paramMap.put("TXN_AMOUNT", amt); // Transaction amount
        paramMap.put("CURRENCY_CODE", CURRENCY_CODE); // Currency code (e.g., "INR")
        paramMap.put("WEBSITE", WEBSITE); // Merchant website URL
        paramMap.put("CALLBACK_URL", callbackurl); // Callback URL for transaction response
        paramMap.put("EMAIL", EMAIL); // Customer email ID
        paramMap.put("MOBILE_NO", MOBILE_NUMBER); // C

        try {
            // Generate CHECKSUMHASH using PaytmChecksum utility
            StringBuilder paramsBuilder = new StringBuilder();
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                paramsBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            String params = paramsBuilder.toString();
            String merchantKey = "ynOww7VF6I%S#eL3"; // Replace with your actual merchant key
            String checksumHash = PaytmChecksum.generateSignature(params, merchantKey);
            Log.d("reschaceksum",checksumHash);
            paramMap.put("CHECKSUMHASH", checksumHash);
            HashMap<String, String> paramMapHash = new HashMap<>(paramMap);
            // Create PaytmOrder and start payment transaction
            PaytmOrder order = new PaytmOrder(paramMapHash);
            paytmPGService.initialize(order, null);
            paytmPGService.startPaymentTransaction(getActivity(), true, true, new PaytmPaymentTransactionCallback() {
                public void onTransactionResponse(Bundle bundle) {
                    // Transaction response
                    String message = "Transaction Successful: " + bundle.getString("RESPMSG");
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                }

                public void networkNotAvailable() {
                    // Network not available error
                    Toast.makeText(getActivity(), "Network Not Available", Toast.LENGTH_LONG).show();
                }

                public void clientAuthenticationFailed(String errorMessage) {
                    // Authentication failed error
                    Toast.makeText(getActivity(), "Authentication Failed: " + errorMessage, Toast.LENGTH_LONG).show();
                }

                public void someUIErrorOccurred(String errorMessage) {
                    // UI error occurred
                    Toast.makeText(getActivity(), "UI Error Occurred: " + errorMessage, Toast.LENGTH_LONG).show();
                }

                public void onErrorLoadingWebPage(int errorCode, String description, String failingUrl) {
                    // Web page loading error
                    Toast.makeText(getActivity(), "Web Page Loading Error: " + description, Toast.LENGTH_LONG).show();
                }

                public void onBackPressedCancelTransaction() {
                    // User canceled the transaction
                    Toast.makeText(getActivity(), "Transaction Cancelled", Toast.LENGTH_LONG).show();
                }

                public void onTransactionCancel(String errorMessage, Bundle bundle) {
                    // Transaction cancelled with error
                    Toast.makeText(getActivity(), "Transaction Cancelled: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            });
            // Start payment transaction
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/







/*
    private void initOrderId() {
            PaytmPGService Service = PaytmPGService.getStagingService();
            Map<String, String> paramMap = new HashMap<String, String>();

            // these are mandatory parameters

            paramMap.put("ORDER_ID",orderIdString);
            paramMap.put("MID", "znhMaK89042764491641");
            paramMap.put("CUST_ID", UserId);
            paramMap.put("CHANNEL_ID", "WAP");
            paramMap.put("INDUSTRY_TYPE_ID", "Retail");
            paramMap.put("WEBSITE", "DEFAULT");
            paramMap.put("TXN_AMOUNT", "10");
            paramMap.put("THEME", "https://www.eazypan.in/");
            paramMap.put("EMAIL", "demo@gmail.com");
            paramMap.put("MOBILE_NO", "9696381023");
            PaytmOrder Order = new PaytmOrder(paramMap);

            PaytmMerchant Merchant = new PaytmMerchant(
                    "https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp",
                    "https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");

            Service.initialize(Order, Merchant, null);

            Service.startPaymentTransaction(context, true, true,
                    new PaytmPaymentTransactionCallback() {
                        @Override
                        public void someUIErrorOccurred(String inErrorMessage) {
                            // Some UI Error Occurred in Payment Gateway Activity.
                            // // This may be due to initialization of views in
                            // Payment Gateway Activity or may be due to //
                            // initialization of webview. // Error Message details
                            // the error occurred.
                        }

                        @Override
                        public void onTransactionSuccess(Bundle inResponse) {
                            // After successful transaction this method gets called.
                            // // Response bundle contains the merchant response
                            // parameters.
                            Log.d("LOG", "Payment Transaction is successful " + inResponse);
                            Toast.makeText(context, "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onTransactionFailure(String inErrorMessage,
                                                         Bundle inResponse) {
                            // This method gets called if transaction failed. //
                            // Here in this case transaction is completed, but with
                            // a failure. // Error Message describes the reason for
                            // failure. // Response bundle contains the merchant
                            // response parameters.
                            Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                            Toast.makeText(context, "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void networkNotAvailable() { // If network is not
                            // available, then this
                            // method gets called.
                        }

                        @Override
                        public void clientAuthenticationFailed(String inErrorMessage) {
                            // This method gets called if client authentication
                            // failed. // Failure may be due to following reasons //
                            // 1. Server error or downtime.
                            // 2. Server unable to
                            // generate checksum or checksum response is not in
                            // proper format.
                            // 3. Server failed to authenticate
                            // that client. That is value of payt_STATUS is 2. //
                            // Error Message describes the reason for failure.
                        }

                        @Override
                        public void onErrorLoadingWebPage(int iniErrorCode,
                                                          String inErrorMessage, String inFailingUrl) {

                        }

                        // had to be added: NOTE
                        @Override
                        public void onBackPressedCancelTransaction() {
                            // TODO Auto-generated method stub
                        }

                    });
        }*/


    private void GetChecksumAPI(String user_id, String phone, String email, String amt, String checksum) {
        String otp1 = new GlobalAppApis().GetaddMoneyToWallet(user_id, phone, email, amt, checksum);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_addMoneyToWallet(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.e("addMoney", result);
                try {
                    JSONObject aobject = new JSONObject(result);
                    JSONObject eServicesList = aobject.getJSONObject("paytmParams");
                    Intent intent = new Intent(getActivity(), AddWallet.class);
                    intent.putExtra("store_mid", eServicesList.getString("MID"));
                    intent.putExtra("store_website", eServicesList.getString("WEBSITE"));
                    intent.putExtra("store_indstype", eServicesList.getString("INDUSTRY_TYPE_ID"));
                    intent.putExtra("store_channelid", eServicesList.getString("CHANNEL_ID"));
                    intent.putExtra("store_custid", eServicesList.getString("CUST_ID"));

                    intent.putExtra("store_mobile", eServicesList.getString("MOBILE_NO"));
                    intent.putExtra("store_email", eServicesList.getString("EMAIL"));
                    intent.putExtra("store_txtamount", eServicesList.getString("TXN_AMOUNT"));

                    intent.putExtra("store_orderid", aobject.getString("order_id"));
                    intent.putExtra("store_checksum", aobject.getString("checksum"));
                    intent.putExtra("store_callback", aobject.getString("callback_url"));
                    startActivity(intent);


//                    Log.e("addMoney",""+aobject);
//                    if (aobject.getString("message").equalsIgnoreCase("Checksum generated successfully")){
//                        Intent intent=new Intent(getActivity(),AddWallet.class);
//                        intent.putExtra("store_order_id",aobject.getString("order_id"));
//                        intent.putExtra("store_checksum",aobject.getString("checksum"));
//                        intent.putExtra("store_amount",w_rupee_et.getText().toString());
//                        startActivity(intent);
//                        Toast.makeText(getActivity(), aobject.getString("message"), Toast.LENGTH_LONG).show();
//                        alertDialogwallet.dismiss();
//                    }
//                    else {
//                        Toast.makeText(getActivity(), aobject.getString("message"), Toast.LENGTH_LONG).show();
//                        alertDialogwallet.dismiss();
//
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, getActivity(), call1, "", true);
    }




   /* public void fetchServices() {
        String url = "https://eazypan.in/app/eServices";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {  Log.d("eServicesList",""+response);
                        try {
                            JSONObject eServicesList = response.getJSONObject("eServicesList");
                            JSONArray bankingArray = eServicesList.getJSONArray("Banking");
                            for (int i = 0; i < bankingArray.length(); i++) {
                                JSONObject bankingObject = bankingArray.getJSONObject(i);
                                HashMap<String, String> hashlist = new HashMap();
                                hashlist.put("id", bankingObject.getString("id"));
                                hashlist.put("service_type", bankingObject.getString("service_type"));
                                hashlist.put("name", bankingObject.getString("name"));
                                hashlist.put("url", bankingObject.getString("url"));
                                hashlist.put("image", bankingObject.getString("image"));
                                arrLegalList.add(hashlist);
                            }
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,true);
                            cust_recyclerView.setLayoutManager(layoutManager);
//                            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
                            pdfAdapTer = new DasListAdapter(getActivity(), arrLegalList);
//                            cust_recyclerView.setLayoutManager(layoutManager);
                            cust_recyclerView.setAdapter(pdfAdapTer);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        // Add the request to the Volley request queue
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }*/


    private void fetchData() {
        String url = "https://eazypan.in/app/eServices"; // Replace with the actual API endpoint

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            if (status == 1) {
                                JSONObject eServicesList = response.getJSONObject("eServicesList");
                                // Create a string to store the category names
                                StringBuilder categoryNames = new StringBuilder();
                                // Iterate through each service category
                                Iterator<String> categoryIterator = eServicesList.keys();
                                while (categoryIterator.hasNext()) {
                                    String category = categoryIterator.next();
                                    categoryNames.append(category);
                                    if (categoryIterator.hasNext()) {
                                        categoryNames.append(", ");
                                    }
                                    JSONArray categoryArray = eServicesList.getJSONArray(category);
                                    // Iterate through each service item in the category
                                    for (int i = 0; i < categoryArray.length(); i++) {
                                        JSONObject serviceObject = categoryArray.getJSONObject(i);
                                        // Create an EService object and set its values
                                        ServiceA eService = new ServiceA();
                                        eService.setId(serviceObject.getString("id"));
                                        eService.setServiceType(serviceObject.getString("service_type"));
                                        eService.setName(serviceObject.getString("name"));
                                        eService.setUrl(serviceObject.getString("url"));
                                        eService.setImage(serviceObject.getString("image"));
                                        eService.setCreatedTime(serviceObject.getString("created_time"));
                                        // Add the EService object to the list
                                        serviceList.add(eService);
                                    }
                                }
//                                title_tv.setText(categoryNames.toString());
                                // Notify the adapter that the data has changed
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        // Add the request to the Volley request queue
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
        private Context context;
        private List<ServiceA> serviceList;
        public ServiceAdapter(Context context, List<ServiceA> serviceList) {
            this.context = context;
            this.serviceList = serviceList;
        }
        @NonNull
        @Override
        public ServiceAdapter.ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_das, parent, false);
            return new ServiceAdapter.ServiceViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull ServiceAdapter.ServiceViewHolder holder, int position) {
            ServiceA service = serviceList.get(position);
            holder.bind(service);
        }
        @Override
        public int getItemCount() {
            return serviceList.size();
        }
        public class ServiceViewHolder extends RecyclerView.ViewHolder {
            TextView txtServiceName;
            ImageView imgService;
            public ServiceViewHolder(@NonNull View itemView) {
                super(itemView);
                imgService = itemView.findViewById(R.id.imgService);
                txtServiceName = itemView.findViewById(R.id.txtServiceName);
            }
            public void bind(ServiceA service) {
//                title_tv.setText(service.getServiceType());
                txtServiceName.setText(service.getName());
                Glide.with(context).load(service.getImage()).into(imgService);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), AO_AxisBank.class);
                        intent.putExtra("path", service.getUrl());
                        intent.putExtra("t_name", service.getName());
                        startActivity(intent);
                    }
                });}}
    }

    public void UTIAppAutoLoginUrl(String id, String checksum) {
        String otp1 = new GlobalAppApis().AppAutoLoginUrl(id, checksum);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_AppAutoLoginUrl(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("UTIAppAutoLoginUrl", "cxc" + result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("status").equalsIgnoreCase("0")) {
                        String msg = object.getString("message");
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(getActivity(), Das_UTI.class);
                        intent.putExtra("path", object.getString("auto_login_url"));
                        startActivity(intent);
                        activity.overridePendingTransition(0, 0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, getActivity(), call1, "", true);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {
                String requredText = data.getExtras().getString("assisted_status");
                Log.d("req_data",requredText);
//                button.setText(requredText);
            }
        }
    }



    public void GetProfile(String user_id,String checksum) {
        String otp1 = new GlobalAppApis().GetProfile(user_id,checksum);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_getProfile(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("GetProfile","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("status").equalsIgnoreCase("0")){
                        String msg       = object.getString("message");
//                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    else {
                        txt_username=object.getString("fullname");
                        txt_mob=object.getString("phone");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, getActivity(), call1, "", true);
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




 /*   public class DasListAdapter extends RecyclerView.Adapter<LegalList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public DasListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrLegalList) {
            data = arrLegalList;
        }

        public LegalList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new LegalList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_das, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final LegalList holder, final int position) {
            header_tv.setText(data.get(position).get("service_type"));
            holder.txtServiceName.setText(data.get(position).get("name"));
            if (data.get(position).get("image").equalsIgnoreCase("")){
                Toast.makeText(context,"Image not found!", Toast.LENGTH_LONG).show();
            }
            else {
                Glide.with(context).load(data.get(position).get("image")).into(holder.imgService);
            }
            holder.imgService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(),AO_AxisBank.class);
                    intent.putExtra("path",data.get(position).get("url"));
                    intent.putExtra("t_name",data.get(position).get("name"));
                    startActivity(intent);
                }
            });

        }

        public int getItemCount() {
            return data.size();
        }
    }

    public class LegalList extends RecyclerView.ViewHolder {
        TextView  txtServiceName;
        ImageView imgService;

        public LegalList(View itemView) {
            super(itemView);
            imgService = itemView.findViewById(R.id.imgService);
            txtServiceName = itemView.findViewById(R.id.txtServiceName);
        }
    }
*/



