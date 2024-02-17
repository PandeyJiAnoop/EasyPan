package com.akp.RetrofitAPI;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("GetOperatorList")
    Call<String> OperatorService(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("Recharge")
    Call<String> PostRecharge(
            @Body String body);

    @Headers("Content-Type: application/json")
    @GET("getStateList")
    Call<String> API_getStateList();

    @Headers("Content-Type: application/json")
    @GET("getAyushmanParameterTypeList")
    Call<String> API_getAyushmanParameterTypeList();


    @Headers("Content-Type: application/json")
    @GET("getAyushmanStateList")
    Call<String> API_getAyushmanStateList();

    @Headers("Content-Type: application/json")
    @POST("verifyOtp")
    Call<String> API_verifyOtp(
            @Body String body);


    @Headers("Content-Type: application/json")
    @POST("verifyRegisterOtp")
    Call<String> API_verifyRegisterOtp(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("ayushmanPrintList")
    Call<String> API_ayushmanPrintList(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("utiitslAppAutoLoginUrl")
    Call<String> API_AppAutoLoginUrl(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("registerProfile")
    Call<String> API_registerProfile(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("login")
    Call<String> API_login(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("ayushmanPrint")
    Call<String> API_ayushmanPrint(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("resendOtp")
    Call<String> API_resendOtp(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("resendRegisterOtp")
    Call<String> API_resendRegisterOtp(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("providerList")
    Call<String> API_providerList(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("dashboard")
    Call<String> API_dashboard(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("getProfile")
    Call<String> API_getProfile(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("updateProfile")
    Call<String> API_updateProfile(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("passbook")
    Call<String> API_passbook(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("rechargeHistory")
    Call<String> API_rechargeHistory(
            @Body String body);
    @Headers("Content-Type: application/json")
    @POST("addMoneyToWallet")
    Call<String> API_addMoneyToWallet(
            @Body String body);

    @Headers("Content-Type: application/json")
    @GET("eServices")
    Call<String> API_eServices();

    @Headers("Content-Type: application/json")
    @GET("nsdlCategoryList")
    Call<String> API_nsdlCategoryList();

    @Headers("Content-Type: application/json")
    @GET("proofOfAddressList")
    Call<String> API_proofOfAddressList();

    @Headers("Content-Type: application/json")
    @GET("proofOfIdentityList")
    Call<String> API_proofOfIdentityList();

    @Headers("Content-Type: application/json")
    @GET("proofOfDOBList")
    Call<String> API_proofOfDOBList();

    @Headers("Content-Type: application/json")
    @GET("genderList")
    Call<String> API_genderList();

    @Headers("Content-Type: application/json")
    @GET("appTypeList")
    Call<String> API_appTypeList();

    @Headers("Content-Type: application/json")
    @POST("nsdlPanApply")
    Call<String> API_nsdlPanApply(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("getNsdlPanList")
    Call<String> API_getNsdlPanList(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("uploadNsdlPanDoc")
    Call<String> API_uploadNsdlPanDoc(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("downloadAcknowledgeSlip")
    Call<String> API_downloadAcknowledgeSlip(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("eKycNsdlPanApply")
    Call<String> API_eKycNsdlPanApply(
            @Body String body);


    @Headers("Content-Type: application/json")
    @POST("billProviderList")
    Call<String> API_billProviderList(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("payBill")
    Call<String> API_payBill(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("fetchBillDetail")
    Call<String> API_fetchBillDetail(
            @Body String body);

}



