package com.akp.easypan.SliderMenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.RetrofitAPI.ApiService;
import com.akp.RetrofitAPI.ConnectToRetrofit;
import com.akp.RetrofitAPI.GlobalAppApis;
import com.akp.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.easypan.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;

import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost;

public class MyWallet extends AppCompatActivity {
    TextView apis_bal_tv,wallet_bal_tv;
    private String UserId;
    RelativeLayout header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        header=findViewById(R.id.header);

        String input = UserId+":ip6wgncgt6:nxe7j2f6c0";
        String strup = input.toUpperCase();
        String checksum = generateMD5Checksum(strup);
        Log.d("TAG", "UPPER " + strup +"MD5 checksum of " + input + ": " + checksum);

        findId();
        GetDashboard(UserId,checksum);
    }

    private void findId() {
        apis_bal_tv=findViewById(R.id.apis_bal_tv);
        wallet_bal_tv=findViewById(R.id.wallet_bal_tv);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                        Toast.makeText(MyWallet.this, object.getString("message"), Toast.LENGTH_LONG).show();
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
        }, MyWallet.this, call1, "", true);
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
