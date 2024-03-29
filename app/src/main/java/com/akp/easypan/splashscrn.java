package com.akp.easypan;
/**
 * Created by Anoop Pandey on 9696381023.
 */
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.akp.easypan.Helper.NetworkConnectionHelper;
import java.util.Timer;

public class splashscrn extends AppCompatActivity {
    ImageView iv;
    long Delay = 5000;
    // Splash screen timer
    private static final int SPLASH_TIME_OUT = 1500;
    LinearLayout ll_1, ll_2;
    NumberProgressBar npb_progress;
    int progresscount = 0;
    ImageView img_icon;
    String general = "general-setting";
    ImageView staic;
    private String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscrn);

        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");

        Animation uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        Animation downtoup = AnimationUtils.loadAnimation(this, R.anim.downtoup);
        npb_progress = findViewById(R.id.npb_progress);
        img_icon = findViewById(R.id.img_icon);
        staic = findViewById(R.id.staic);
        staic.setAnimation(downtoup);
//        img_icon.setAnimation(uptodown);
        npb_progress.setProgress(progresscount);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progresscount == 100) {
                    /**/
                } else {
                    handler.postDelayed(this, 30);
                    progresscount++;
                    npb_progress.setProgress(progresscount);
                }
            }
        }, 200);


        iv=(ImageView)findViewById(R.id.iv);
        Glide.with(splashscrn.this)
                .load(R.drawable.loadingimg)
                .into(iv);

        Timer RunSplash = new Timer();

        // Task to do when the timer ends
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (NetworkConnectionHelper.isOnline(splashscrn.this)) {

                    if (UserId.equalsIgnoreCase("")){
                        Intent myIntent = new Intent(splashscrn.this, MainActivityLogin.class);
                        startActivity(myIntent);

                    }
                    else {
                        Intent myIntent = new Intent(splashscrn.this,dashboard.class);
                        startActivity(myIntent);

                    }


                    } else {
                       /* User user = User.createobjectFromJson(new SharedPrefereceUserData(getApplicationContext()).getSharedData().getString("User", null));
                        if (user != null && user.getUsertype() != null && user.getUsertype().equalsIgnoreCase("owner")) {
                            startActivity(new Intent(splashscrn.this, Owner_Dashboard.class));
                        } else {*/
                        startActivity(new Intent(splashscrn.this, dashboard.class));
                    }

            }
        }, SPLASH_TIME_OUT);
    }

}
