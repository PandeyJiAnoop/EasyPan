package com.akp.easypan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.app.ActivityCompat;
        import androidx.core.content.ContextCompat;

        import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.pm.PackageManager;
        import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.easypan.Das_BillPaymnet.MobileRecharge;
import com.akp.easypan.fragments.JSONParser;
        import com.akp.easypan.fragments.PaytmChecksum;
        import com.paytm.pgsdk.PaytmClientCertificate;
        import com.paytm.pgsdk.PaytmMerchant;
        import com.paytm.pgsdk.PaytmOrder;
        import com.paytm.pgsdk.PaytmPGService;
        import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

        import org.androidannotations.annotations.rest.Get;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.BufferedReader;
        import java.io.DataOutputStream;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Map;
import java.util.Objects;

public class AddWallet extends AppCompatActivity {
    private static final String CHANNEL_ID = "WAP";
    private static final String INDUSTRY_TYPE_ID = "Retail";
    private static final String MID = "znhMaK89042764491641";
    private static final String WEBSITE = "DEFAULT";
    String custid,Mobile;
    String GetMID,GetWebsite,GetIndstype,GetChannelId,GetCustId,GetMobile,GetEmail,GetTxtAmount,GetOrderId,GetChceksum,GetCallBackUrl;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        custid = sharedPreferences.getString("U_id", "");
        Mobile= sharedPreferences.getString("mobile", "");
        GetMID = getIntent().getStringExtra("store_mid");
        GetWebsite = getIntent().getStringExtra("store_website");
        GetIndstype = getIntent().getStringExtra("store_indstype");
        GetChannelId = getIntent().getStringExtra("store_channelid");
        GetCustId = getIntent().getStringExtra("store_custid");
        GetMobile = getIntent().getStringExtra("store_mobile");
        GetEmail = getIntent().getStringExtra("store_email");
        GetTxtAmount = getIntent().getStringExtra("store_txtamount");
        GetOrderId = getIntent().getStringExtra("store_orderid");
        GetChceksum = getIntent().getStringExtra("store_checksum");
        GetCallBackUrl = getIntent().getStringExtra("store_callback");
//        Toast.makeText(getApplicationContext(), ""+GetChecksum, Toast.LENGTH_LONG).show();
        if (ContextCompat.checkSelfPermission(AddWallet.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddWallet.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initializePaytmPayment();
    }

    void initializePaytmPayment() {
//        PaytmPGService Service = PaytmPGService.getStagingService();
        //use this when using for production
        PaytmPGService Service = PaytmPGService.getProductionService();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", GetMID);
        paramMap.put("ORDER_ID", GetOrderId);
        paramMap.put("CUST_ID", GetCustId);
        paramMap.put("CHANNEL_ID", GetChannelId);
        paramMap.put("TXN_AMOUNT", GetTxtAmount);
        paramMap.put("MOBILE_NO", GetMobile);
        paramMap.put("EMAIL", GetEmail);
        paramMap.put("WEBSITE", GetWebsite);
        paramMap.put("CALLBACK_URL", GetCallBackUrl);
        paramMap.put("CHECKSUMHASH", GetChceksum);
        paramMap.put("INDUSTRY_TYPE_ID", GetIndstype);
        Log.d("resdatta", "" + paramMap);
        //creating a paytm order object using the hashmap
        PaytmOrder order = new PaytmOrder(paramMap);
        Service.initialize(order, null);
//        Service.startPaymentTransaction(AddWallet.this, true, true, AddWallet.this);

        Service.startPaymentTransaction(this, true,
                true, new PaytmPaymentTransactionCallback() {
                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
                        showpopupwindow(inResponse.toString(),GetTxtAmount);
                        Log.d("onTransactionResponse", "1" + inResponse.toString());
                        System.out.println("===== onTransactionResponse " + inResponse.toString());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            if (Objects.equals(inResponse.getString("STATUS"), "TXN_SUCCESS")) {
                                String status = inResponse.getString("STATUS");
                                String transactionid = inResponse.getString("TXNID");
                                String amount = inResponse.getString("TXNAMOUNT");
                                showpopupwindow(transactionid,amount);
                                Log.d("responsepayment", "1" + inResponse.toString());
                                //    Payment Success
                            } else if (!inResponse.getBoolean("STATUS")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddWallet.this);
                                builder.setTitle("Failed!")
                                        .setMessage("Payment Failed")
                                        .setCancelable(false)
                                        .setIcon(R.drawable.cancel)
                                        .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent intent = new Intent(AddWallet.this, dashboard.class);
                                                startActivity(intent);
                                                dialog.cancel();
                                            }
                                        });
                                builder.create().show();
                                Log.d("responsepayment", "2" + inResponse.toString());
                                // //    Payment Failed
                            }
                        }
                    }

                    @Override
                    public void networkNotAvailable() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddWallet.this);
                        builder.setTitle("Network Error!")
                                .setMessage("Payment Failed")
                                .setCancelable(false)
                                .setIcon(R.drawable.cancel)
                                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(AddWallet.this, dashboard.class);
                                        startActivity(intent);
                                        dialog.cancel();
                                    }
                                });
                        builder.create().show();
                        Log.d("responsepayment", "3network");
                        // network error
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddWallet.this);
                        builder.setTitle("Authentication Failed Error!")
                                .setMessage("Payment Failed")
                                .setCancelable(false)
                                .setIcon(R.drawable.cancel)
                                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(AddWallet.this, dashboard.class);
                                        startActivity(intent);
                                        dialog.cancel();
                                    }
                                });
                        builder.create().show();
                        // AuthenticationFailed
                    }

                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddWallet.this);
                        builder.setTitle("UI Error!")
                                .setMessage("Payment Failed")
                                .setCancelable(false)
                                .setIcon(R.drawable.cancel)
                                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(AddWallet.this, dashboard.class);
                                        startActivity(intent);
                                        dialog.cancel();
                                    }
                                });
                        builder.create().show();
                        // UI Error
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddWallet.this);
                        builder.setTitle("Web page loading Error!")
                                .setMessage("Payment Failed")
                                .setCancelable(false)
                                .setIcon(R.drawable.cancel)
                                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(AddWallet.this, dashboard.class);
                                        startActivity(intent);
                                        dialog.cancel();
                                    }
                                });
                        builder.create().show();
                        //  Web page loading error
                    }

                    @Override
                    public void onBackPressedCancelTransaction() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddWallet.this);
                        builder.setTitle("Cancelling Transaction!")
                                .setMessage("Payment Failed")
                                .setCancelable(false)
                                .setIcon(R.drawable.cancel)
                                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(AddWallet.this, dashboard.class);
                                        startActivity(intent);
                                        dialog.cancel();
                                    }
                                });
                        builder.create().show();
                        Log.d("responsepayment", "4back cancel");
                        // on cancelling transaction
                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddWallet.this);
                        builder.setTitle("OnBackPressedCancel Transaction!")
                                .setMessage("Payment Failed")
                                .setCancelable(false)
                                .setIcon(R.drawable.cancel)
                                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(AddWallet.this, dashboard.class);
                                        startActivity(intent);
                                        dialog.cancel();
                                    }
                                });
                        builder.create().show();
                        Log.d("responsepayment", "5cancel"); // maybe same as onBackPressedCancelTransaction()
                    }
                });
   /* public void onTransactionResponse(Bundle inResponse) {
        Toast.makeText(AddWallet.this, inResponse.toString(), Toast.LENGTH_LONG).show();
        Intent intent1=new Intent(getApplicationContext(),dashboard.class);startActivity(intent1);
        Log.d("responsepayment", ""+inResponse.toString());
        String status = inResponse.getString("STATUS");
        String transactionid = inResponse.getString("TXNID");
        String amount = inResponse.getString("TXNAMOUNT");
        if(status!=null)
        {
            showpopupwindow(transactionid,amount);
           *//* AlertDialog.Builder builder = new AlertDialog.Builder(AddWallet.this);
            builder.setTitle("Response!")
                    .setMessage("Payment Successfully\n\n"+status+"\nTransaction Id:- "+transactionid+"\nAmount:- "+amount)
                    .setCancelable(false)
                    .setIcon(R.drawable.checkmark)
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(AddWallet.this, dashboard.class);
                            startActivity(intent);
                            dialog.cancel();
                        }
                    });
            builder.create().show();*//*
        }
        else
        {   Toast.makeText(AddWallet.this, inResponse.toString(), Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(AddWallet.this);
            builder.setTitle("Response!")
                    .setMessage(status)
                    .setCancelable(false)
                    .setIcon(R.drawable.cancel)
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(AddWallet.this, dashboard.class);
                            startActivity(intent);
                            dialog.cancel();
                        }
                    });
            builder.create().show();
            // print log
        }
    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(AddWallet.this, "Network error", Toast.LENGTH_LONG).show();
    }
    
    @Override
    public void clientAuthenticationFailed(String s) {
        Toast.makeText(AddWallet.this, s, Toast.LENGTH_LONG).show();
    }
    @Override
    public void someUIErrorOccurred(String s) {
        Toast.makeText(AddWallet.this, s, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Toast.makeText(AddWallet.this, s, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onBackPressedCancelTransaction() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddWallet.this);
        builder.setTitle("Response!")
                .setMessage("Transaction Cancelled!")
                .setCancelable(false)
                .setIcon(R.drawable.cancel)
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(AddWallet.this, dashboard.class);
                        startActivity(intent);
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }
    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Toast.makeText(AddWallet.this, s + bundle.toString(), Toast.LENGTH_LONG).show();
    }
*/
    }
    private void showpopupwindow(String transactionid, String amount) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(AddWallet.this).inflate(R.layout.fund_successfullycreated_popup, viewGroup, false);
        Button ok = (Button) dialogView.findViewById(R.id.btnDialog);
        TextView id_tv= dialogView.findViewById(R.id.id_tv);
        TextView pass_tv= dialogView.findViewById(R.id.pass_tv);
        id_tv.setText("Operator_ref- "+transactionid);
        pass_tv.setText("Amount- "+amount);
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

}

