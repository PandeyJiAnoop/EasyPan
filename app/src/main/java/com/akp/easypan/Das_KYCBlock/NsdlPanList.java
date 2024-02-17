package com.akp.easypan.Das_KYCBlock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.RetrofitAPI.ApiService;
import com.akp.RetrofitAPI.ConnectToRetrofit;
import com.akp.RetrofitAPI.GlobalAppApis;
import com.akp.RetrofitAPI.RetrofitCallBackListenar;
import com.akp.easypan.Helper.NetworkConnectionHelper;
import com.akp.easypan.R;
import com.akp.easypan.Utility;
import com.akp.easypan.entity.FilePath;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

import static com.akp.RetrofitAPI.API_Config.getApiClient_ByPost;

public class NsdlPanList extends AppCompatActivity{
    String UserId;
    ImageView menuImg;
    ArrayList<HashMap<String, String>> arrLegalList = new ArrayList<>();
    LegalListAdapter pdfAdapTer;
    RecyclerView cust_recyclerView;
    SwipeRefreshLayout srl_refresh;
    TextView title_tv;
    ImageView norecord_tv;
    String checksum;
    private URL url;
    String dest_file_path = "downloadAcknowledgeSlip.pdf";

    private Dialog alertDialog;
    CircleImageView profile_image;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    String temp;


    //Pdf request code

    //storage permission code
    private static final int PICK_PDF_REQUEST = 1;
    private static final int REQUEST_CODE_PERMISSION = 2;

    //Uri to store the image uri
    private Uri filePath;

    TextView path_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nsdl_pan_list);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
//        Toast.makeText(getApplicationContext(),UserId,Toast.LENGTH_LONG).show();
        menuImg = findViewById(R.id.menuImg);
        cust_recyclerView = findViewById(R.id.cust_recyclerView);
        srl_refresh = findViewById(R.id.srl_refresh);
        norecord_tv = findViewById(R.id.norecord_tv);
        title_tv = findViewById(R.id.title_tv);
        String input = UserId+":ip6wgncgt6:nxe7j2f6c0";
        String strup = input.toUpperCase();
        checksum = generateMD5Checksum(strup);

        Log.d("TAG", "UPPER " + strup +"MD5 checksum of " + input + ": " + checksum);

        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(NsdlPanList.this)) {
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
                    Toast.makeText(NsdlPanList.this, "Please check your internet connection! try again...", Toast.LENGTH_LONG).show();
                }
            }
        });

        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        NsdlPanListList(UserId,checksum);

    }

    private void NsdlPanListList(String user_id,String checksum) {
        String otp1 = new GlobalAppApis().GetNsdlPanList(user_id,checksum);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_getNsdlPanList(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("API_NsdlPanList",result);
                try {
                    JSONObject objectlist = new JSONObject(result);
                    if (objectlist.getString("status").equalsIgnoreCase("0")){
                        String msg       = objectlist.getString("message");
                        Toast.makeText(getApplicationContext(), objectlist.getString("message"), Toast.LENGTH_LONG).show();
                        norecord_tv.setVisibility(View.VISIBLE);
                        cust_recyclerView.setVisibility(View.GONE);
                    }
                    else {
                        JSONArray Response1 = objectlist.getJSONArray("list");
                        for (int i = 0; i < Response1.length(); i++) {
                            norecord_tv.setVisibility(View.GONE);
                            cust_recyclerView.setVisibility(View.VISIBLE);
                            title_tv.setText("NSDL PAN List("+Response1.length()+")");
                            JSONObject jsonObject = Response1.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("nsdl_id", jsonObject.getString("nsdl_id"));
                            hashlist.put("pan", jsonObject.getString("pan"));
                            hashlist.put("ack", jsonObject.getString("ack"));
                            hashlist.put("fname", jsonObject.getString("fname"));
                            hashlist.put("mname", jsonObject.getString("mname"));
                            hashlist.put("lname", jsonObject.getString("lname"));
                            hashlist.put("mobile", jsonObject.getString("mobile"));
                            hashlist.put("amount", jsonObject.getString("amount"));
                            hashlist.put("request_time", jsonObject.getString("request_time"));
                            hashlist.put("comments", jsonObject.getString("comments"));
                            hashlist.put("status", jsonObject.getString("status"));
                            JSONObject objectlist1 = new JSONObject(jsonObject.getString("action_button"));
                            hashlist.put("view_info", objectlist1.getString("view_info"));
                            hashlist.put("upload_doc", objectlist1.getString("upload_doc"));
                            hashlist.put("acknowledgement_slip", objectlist1.getString("acknowledgement_slip"));
                            Log.d("objectlist1",""+objectlist1);
                            arrLegalList.add(hashlist);
                        }
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(NsdlPanList.this, 1);
                        pdfAdapTer = new LegalListAdapter(NsdlPanList.this, arrLegalList);
                        cust_recyclerView.setLayoutManager(layoutManager);
                        cust_recyclerView.setAdapter(pdfAdapTer);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, NsdlPanList.this, call1, "", true);
    }


    public class LegalListAdapter extends RecyclerView.Adapter<NsdlPanList.LegalList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public LegalListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrLegalList) {
            data = arrLegalList;
        }
        public NsdlPanList.LegalList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new NsdlPanList.LegalList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_nsdlpanlist, parent, false));
        }
        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final NsdlPanList.LegalList holder, final int position) {
            holder.t1.setText(data.get(position).get("fname") + data.get(position).get("mname")+ data.get(position).get("lname") );
            holder.t2.setText(data.get(position).get("mobile"));
            holder.t3.setText(data.get(position).get("pan"));
            holder.t4.setText(data.get(position).get("amount")+" Rs.");
            holder.t5.setText(data.get(position).get("ack"));
            holder.t6.setText(data.get(position).get("status"));
            holder.t7.setText(data.get(position).get("comments"));
            holder.date.setText("Date:- " + data.get(position).get("request_time"));

            if (data.get(position).get("status").equalsIgnoreCase("Completed")){
                holder.t6.setTextColor(getResources().getColor(R.color.green));
            }
           else if (data.get(position).get("status").equalsIgnoreCase("Pending")){
                holder.t6.setTextColor(getResources().getColor(R.color.new_yellow));
            }
            else if (data.get(position).get("status").equalsIgnoreCase("Processing")){
                holder.t6.setTextColor(getResources().getColor(R.color.new_orange));
            }
            else if (data.get(position).get("status").equalsIgnoreCase("Rejected")){
                holder.t6.setTextColor(getResources().getColor(R.color.red));
            }

            if (data.get(position).get("status").equalsIgnoreCase("Completed")){
                holder.print.setBackgroundResource(R.color.green);
                holder.print.setText("Download");
                holder.print.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DownloadAcknowledgeSlip(UserId,data.get(position).get("nsdl_id"),checksum);
                        Log.d("nsdlid",data.get(position).get("nsdl_id"));
                    } });
            }
           else if (data.get(position).get("status").equalsIgnoreCase("Processing")){
                holder.print.setVisibility(View.GONE);
            }
            else if (data.get(position).get("status").equalsIgnoreCase("Rejected")){
                holder.print.setVisibility(View.GONE);
            }
            else {
                holder.print.setText("Upload");
                holder.print.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UploadNsdlPanDocPopup(data.get(position).get("nsdl_id"));

                        Log.d("nsdlid",data.get(position).get("nsdl_id"));
                    } });}
        }
        public int getItemCount() {
            return data.size();
        }
    }



    public class LegalList extends RecyclerView.ViewHolder {
        TextView t1, t2, t3, t4, t5,t6,t7, date;
        Button print;

        public LegalList(View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.tv1);
            t2 = itemView.findViewById(R.id.tv2);
            t3 = itemView.findViewById(R.id.tv3);
            t4 = itemView.findViewById(R.id.tv4);
            t5 = itemView.findViewById(R.id.tv5);
            t6 = itemView.findViewById(R.id.tv6);
            t7 = itemView.findViewById(R.id.tv7);
            date = itemView.findViewById(R.id.date);
            print = itemView.findViewById(R.id.print);
        } }

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

    public void DownloadAcknowledgeSlip(String user_id,String nsdlid,String checksum) {
        String otp1 = new GlobalAppApis().GetDownloadAcknowledgeSlip(user_id,nsdlid,checksum);
        Log.d("DownloadAcknowledgeSlip",otp1);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_downloadAcknowledgeSlip(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("DownloadAcknowledgeSlip","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("status").equalsIgnoreCase("0")){
                        String msg       = object.getString("message");
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    else {
                        try {
                            url = new URL(object.getString("path"));
                        } catch (MalformedURLException e) {
                            Toast.makeText(NsdlPanList.this, "Error:- "+e, Toast.LENGTH_LONG).show();
                            e.printStackTrace();  }
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url + ""));
                        request.setTitle(dest_file_path);
                        request.setMimeType("application/pdf");
                        request.allowScanningByMediaScanner();
                        request.setAllowedOverMetered(true);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "AldoFiles/" + dest_file_path);
                        DownloadManager downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
                        downloadManager.enqueue(request);
                        Toast.makeText(NsdlPanList.this, "Download Successfully.,Saved to your Internal Storage", Toast.LENGTH_LONG).show();
                        finish();
                    } } catch (JSONException e) {
                    e.printStackTrace();
                } }
        }, NsdlPanList.this, call1, "", true);
    }



    private void UploadNsdlPanDocPopup(String nsdlid) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(NsdlPanList.this).inflate(R.layout.uploadnsdlpandoc_popup, viewGroup, false);
        Button ok = (Button) dialogView.findViewById(R.id.btnDialog);
        CoordinatorLayout uplod_rl=dialogView.findViewById(R.id.uplod_rl);
        profile_image=dialogView.findViewById(R.id.profile_image);
        path_tv= dialogView.findViewById(R.id.path_tv);

        uplod_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for and request permission if needed
                if (ContextCompat.checkSelfPermission(NsdlPanList.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NsdlPanList.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_PERMISSION);
                } else {
                    // Permission already granted, initiate PDF picking process
                    showFileChooser();
                }

//                selectImage();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedFilePath = FileUtils.getPathFromURI(NsdlPanList.this, filePath);
                Log.d("akp_paths", String.valueOf(filePath));
                // Assuming contentUri is the content:// URI you want to convert
                String base64String = FileUtils.convertContentUriToBase64(NsdlPanList.this, filePath);
                if (base64String != null) {
                    Log.d("Base64String", base64String);
                    UploadDocument(UserId, nsdlid, base64String, checksum);
                    // Use the base64String as needed
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to get file path", Toast.LENGTH_SHORT).show();
                }
            } });
        //Now we need an AlertDialog.Builder object
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void UploadDocument(String user_id,String nsdlid,String doc_content,String checksum) {
        String otp1 = new GlobalAppApis().GetuploadNsdlPanDoc(user_id,nsdlid,doc_content,checksum);
        Log.d("otp1","cxc"+otp1);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.API_uploadNsdlPanDoc(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("resupdate","cxc"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("status").equalsIgnoreCase("0")){
                        String msg       = object.getString("message");
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                        alertDialog.dismiss();
                    }
                    else {
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        alertDialog.dismiss();
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, NsdlPanList.this, call1, "", true);
    }


    private void selectImage() {
        final CharSequence[] items = {"Choose from Library",
                "Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(NsdlPanList.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(NsdlPanList.this);
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

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == SELECT_FILE)
//                onSelectFromGalleryResult(data);
//        }
//    }

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
        profile_image.setImageBitmap(bm);
        Bitmap immagex=bm;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 20, baos);
        byte[] b = baos.toByteArray();
        temp = Base64.encodeToString(b,Base64.DEFAULT);
    }

    @Override
    public void onBackPressed() {

    }


    //Requesting permission






    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            // Log the file path
            Log.d("akp_path", String.valueOf(filePath));

            // Perform actions with the selected PDF file here if needed
        } else {
            Toast.makeText(this, "Oops! You denied the permission or didn't select a PDF file.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with PDF picking
                showFileChooser();
            } else {
                Toast.makeText(this, "Permission denied. Cannot pick PDF without permission.", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    //This method will be called when the user will tap on allow or deny
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        //Checking the request code of our request
//        if (requestCode == STORAGE_PERMISSION_CODE) {
//
//            //If permission is granted
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //Displaying a toast
//                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
//            } else {
//                //Displaying another toast if permission is not granted
//                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
//            }
//        }
//    }



        public String convertToBase64(String filePath) {
            File file = new File(filePath);
            try {
                // Read the PDF file into a byte array
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] bytes = new byte[(int) file.length()];
                fileInputStream.read(bytes);
                fileInputStream.close();

                // Convert the byte array to Base64
                String base64 = Base64.encodeToString(bytes, Base64.DEFAULT);
                return base64;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


}
