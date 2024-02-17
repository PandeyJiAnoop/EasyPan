package com.akp.RetrofitAPI;
/**
 * Created by Anoop pandey-9696381023.
 */
import android.util.Log;
import android.view.autofill.AutofillId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GlobalAppApis {
    public String Operator(String servicename) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("ServiceName", servicename);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }
    public String PostRechargeAPI(String user_id, String phone, String service_code, String amount,String customer_phone,String checksum) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("user_id", user_id);
            jsonObject1.put("phone", phone);
            jsonObject1.put("service_code", service_code);
            jsonObject1.put("amount", amount);
            jsonObject1.put("customer_phone", customer_phone);
            jsonObject1.put("checksum", checksum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }


    public String Login(String phone, String checksum) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("phone", phone);
            jsonObject1.put("checksum", checksum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String RegisterProfile(String phone, String password,String fullname,String email,String aadhar,String pan,String shopName,String type,
                                  String pincode,String state,String address,String checksum) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("phone", phone);
            jsonObject1.put("password", password);
            jsonObject1.put("fullname", fullname);
            jsonObject1.put("email", email);
            jsonObject1.put("aadhar", aadhar);
            jsonObject1.put("pan", pan);
            jsonObject1.put("shopName", shopName);
            jsonObject1.put("type", type);
            jsonObject1.put("pincode", pincode);
            jsonObject1.put("state", state);
            jsonObject1.put("address", address);
            jsonObject1.put("checksum", checksum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }
    public String ayushmanPrint(String uid,String stateid,String pmrid, String checksum) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("user_id", uid);
            jsonObject1.put("state_id", stateid);
            jsonObject1.put("pmrssmid", pmrid);
            jsonObject1.put("checksum", checksum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String providerList(String uid,String type, String checksum) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("user_id", uid);
            jsonObject1.put("type", type);
            jsonObject1.put("checksum", checksum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }
    public String NSDLCatList() {
        JSONObject jsonObject1 = new JSONObject();
        return jsonObject1.toString();
    }

    public String AyushmanPrintList(String id, String name,String phone,String stateid,String parmid,String param,String checksum) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("user_id", id);
            jsonObject1.put("fullname", name);
            jsonObject1.put("phone", phone);
            jsonObject1.put("state_id", stateid);
            jsonObject1.put("pram", parmid);
            jsonObject1.put("parameter", param);
            jsonObject1.put("checksum", checksum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String AppAutoLoginUrl(String id,String checksum) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("user_id", id);
            jsonObject1.put("checksum", checksum);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String VerifyOtp (String user_id, String otp,String checksum) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("user_id", user_id);
            jsonObject1.put("otp", otp);
            jsonObject1.put("checksum", checksum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }


    public String VerifyRegisterOtp (String phone, String otp,String checksum) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("phone", phone);
            jsonObject1.put("otp", otp);
            jsonObject1.put("checksum", checksum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }


    public String ResendOtp (String user_id,String checksum) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("user_id", user_id);
            jsonObject1.put("checksum", checksum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String ResendRegisterOtp (String phone,String checksum) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("phone", phone);
            jsonObject1.put("checksum", checksum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }
    public String Dashboard (String user_id,String checksum) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("user_id", user_id);
            jsonObject1.put("checksum", checksum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String GetProfile (String user_id,String checksum) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("user_id", user_id);
            jsonObject1.put("checksum", checksum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String UpdateProfile (String user_id,String fullname,String email,String aadhar,String pan,String firm_name,String pincode,String state,String address,String profile_image,String checksum) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("user_id", user_id);
            jsonObject1.put("fullname", fullname);
            jsonObject1.put("email", email);
            jsonObject1.put("aadhar", aadhar);
            jsonObject1.put("pan", pan);
            jsonObject1.put("firm_name", firm_name);
            jsonObject1.put("pincode", pincode);
            jsonObject1.put("state", state);
            jsonObject1.put("address", address);
            jsonObject1.put("profile_image", profile_image);
            jsonObject1.put("checksum", checksum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String GetPassbook (String user_id,String datefrom,String dateto,String entrytype,String checksum) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("user_id", user_id);
            jsonObject1.put("date_from", datefrom);
            jsonObject1.put("date_to", dateto);
            jsonObject1.put("page_no", "1");
            jsonObject1.put("page_size", "20");
            jsonObject1.put("entry_type", entrytype);
            jsonObject1.put("checksum", checksum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String GetaddMoneyToWallet (String user_id,String phone,String email,String amt,String checksum) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("user_id", user_id);
            jsonObject1.put("phone", phone);
            jsonObject1.put("email", email);
            jsonObject1.put("amount", amt);
            jsonObject1.put("gateway", "paytm");
            jsonObject1.put("checksum", checksum);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String GetnsdlPanApply(String user_id,String status,String pan,String category,String gender,
                                  String dob,String fname,String mname,String lname,String ffname,String fmname,String flname,
                                  String mobile,String email,String aadhar,String nopan,String napa,String poad,String podob,
                                  String poid,String pincode,String state,String village,String post_office,String locality,String district,
                                  String area_code,String ao_type,String range_code,String ao_no,String is_illiterate,String app_type,String ref_title,
                                  String fname1,String mname1,String lname1,String ref_pincode,String state1,String ref_address1,String ref_address2,
                                  String ref_address3,String ref_address4,String city1,String checksum) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("user_id", user_id);
            jsonObject1.put("status", status);
            jsonObject1.put("pan", pan);
            jsonObject1.put("category", category);
            jsonObject1.put("gender", gender);
            jsonObject1.put("dob", dob);

            jsonObject1.put("fname", fname);
            jsonObject1.put("mname", mname);
            jsonObject1.put("lname", lname);
            jsonObject1.put("ffname", ffname);
            jsonObject1.put("fmname", fmname);
            jsonObject1.put("flname", flname);

            jsonObject1.put("mobile", mobile);
            jsonObject1.put("email", email);
            jsonObject1.put("aadhar", aadhar);
            jsonObject1.put("nopan", nopan);
            jsonObject1.put("napa", napa);
            jsonObject1.put("poad", poad);
            jsonObject1.put("podob", podob);

            jsonObject1.put("poid", poid);
            jsonObject1.put("pincode", pincode);
            jsonObject1.put("state", state);
            jsonObject1.put("village", village);
            jsonObject1.put("post_office", post_office);
            jsonObject1.put("locality", locality);
            jsonObject1.put("district", district);

            jsonObject1.put("area_code", area_code);
            jsonObject1.put("ao_type", ao_type);
            jsonObject1.put("range_code", range_code);
            jsonObject1.put("ao_no", ao_no);
            jsonObject1.put("is_illiterate", is_illiterate);
            jsonObject1.put("app_type", app_type);
            jsonObject1.put("ref_title", ref_title);

            jsonObject1.put("fname1", fname1);
            jsonObject1.put("mname1", mname1);
            jsonObject1.put("lname1", lname1);
            jsonObject1.put("ref_pincode", ref_pincode);
            jsonObject1.put("state1", state1);
            jsonObject1.put("ref_address1", ref_address1);
            jsonObject1.put("ref_address2", ref_address2);

            jsonObject1.put("ref_address3", ref_address3);
            jsonObject1.put("ref_address4", ref_address4);
            jsonObject1.put("city1", city1);
            jsonObject1.put("checksum", checksum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String GetnsdlPanApply1(String user_id,String status,String pan,String category,String gender,
                                  String dob,String fname,String mname,String lname,String ffname,String fmname,String flname,
                                  String mobile,String email,String aadhar,String nopan,String napa,String poad,String podob,
                                  String poid,String pincode,String state,String village,String post_office,String locality,String district,
                                  String area_code,String ao_type,String range_code,String ao_no,String is_illiterate,String app_type,String ref_title,
                                  String fname1,String mname1,String lname1,String ref_pincode,String state1,String ref_address1,String ref_address2,
                                  String ref_address3,String ref_address4,String city1,String checksum) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("user_id", user_id);
            jsonObject1.put("status", status);
            jsonObject1.put("pan", pan);
            jsonObject1.put("category", category);
            jsonObject1.put("gender", gender);
            jsonObject1.put("dob", dob);

            jsonObject1.put("fname", fname);
            jsonObject1.put("mname", mname);
            jsonObject1.put("lname", lname);
            jsonObject1.put("ffname", ffname);
            jsonObject1.put("fmname", fmname);
            jsonObject1.put("flname", flname);

            jsonObject1.put("mobile", mobile);
            jsonObject1.put("email", email);
            jsonObject1.put("aadhar", aadhar);
            jsonObject1.put("nopan", nopan);
            jsonObject1.put("napa", napa);
            jsonObject1.put("poad", poad);
            jsonObject1.put("podob", podob);

            jsonObject1.put("poid", poid);
            jsonObject1.put("pincode", pincode);
            jsonObject1.put("state", state);
            jsonObject1.put("village", village);
            jsonObject1.put("post_office", post_office);
            jsonObject1.put("locality", locality);
            jsonObject1.put("district", district);

            jsonObject1.put("area_code", area_code);
            jsonObject1.put("ao_type", ao_type);
            jsonObject1.put("range_code", range_code);
            jsonObject1.put("ao_no", ao_no);
            jsonObject1.put("is_illiterate", is_illiterate);
            jsonObject1.put("app_type", app_type);
            jsonObject1.put("ref_title", ref_title);

            jsonObject1.put("fname1", fname1);
            jsonObject1.put("mname1", mname1);
            jsonObject1.put("lname1", lname1);
            jsonObject1.put("ref_pincode", ref_pincode);
            jsonObject1.put("state1", state1);
            jsonObject1.put("ref_address1", ref_address1);
            jsonObject1.put("ref_address2", ref_address2);

            jsonObject1.put("ref_address3", ref_address3);
            jsonObject1.put("ref_address4", ref_address4);
            jsonObject1.put("city1", city1);
            jsonObject1.put("checksum", checksum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String GetNsdlPanList (String user_id,String checksum) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("user_id", user_id);
            jsonObject1.put("page_no", "1");
            jsonObject1.put("page_size", "10");
            jsonObject1.put("checksum", checksum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String GetuploadNsdlPanDoc (String user_id,String nsdlid,String doc_content,String checksum) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("user_id", user_id);
            jsonObject1.put("nsdl_id", nsdlid);
            jsonObject1.put("doc_content", doc_content);
            jsonObject1.put("checksum", checksum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }
    public String GetDownloadAcknowledgeSlip (String user_id,String nsdlid,String checksum) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("user_id", user_id);
            jsonObject1.put("nsdl_id", nsdlid);
            jsonObject1.put("checksum", checksum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }


    public String EKycNsdlPanApplyAPI(String id,String phone,String name,String cust_phone,String cust_name,String pan_type,String checksum) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("user_id", id);
            jsonObject1.put("phone", phone);
            jsonObject1.put("fullname", name);
            jsonObject1.put("customer_phone", cust_phone);
            jsonObject1.put("customer_fullname", cust_name);
            jsonObject1.put("pan_type", pan_type);
            jsonObject1.put("checksum", checksum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }



    public String payBillAPI(String user_id, String service_code, String amount,String checksum,String cust_no,String customer_name,
                             String bill_due_date,String type,String latitude,String longitude) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("user_id", user_id);
            jsonObject1.put("service_code", service_code);
            jsonObject1.put("amount", amount);
            jsonObject1.put("checksum", checksum);


            jsonObject1.put("cust_no", cust_no);
            jsonObject1.put("customer_name", customer_name);
            jsonObject1.put("bill_due_date", bill_due_date);
            jsonObject1.put("type", type);
            jsonObject1.put("latitude", latitude);
            jsonObject1.put("longitude", longitude);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    public String fetchBillDetailAPI (String user_id,String service_code,String cust_no,String checksum) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("user_id", user_id);
            jsonObject1.put("service_code", service_code);
            jsonObject1.put("cust_no", cust_no);
            jsonObject1.put("checksum", checksum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }


    public String RechargeHistory (String user_id,String type,String checksum) {
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("user_id", user_id);
            jsonObject1.put("type", type);
            jsonObject1.put("checksum", checksum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

}
