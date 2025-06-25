package com.myTara567.app.serverApi;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface apiInterfaces {

    @POST("api-get-app-key")
    Call<JsonObject> getAppKey(@Body JsonObject env_type);

    @POST("api-user-registration")
    Call<JsonObject> userRegistration(@Body JsonObject user_details);

    @POST("api-check-mobile")
    Call<JsonObject> mobileCheck(@Body JsonObject mobile_check);


    @POST("api-add-user-bank-details")
    Call<JsonObject> addBankDetails(@Body JsonObject jsonObject);




    @POST("api-otp-sent")
    Call<JsonObject> sendotp(@Body JsonObject send_otp);


    @POST("api-user-login")
    Call<JsonObject> userLogin(@Body JsonObject userDetails);

    @POST("api-forget-check-mobile")
    Call<JsonObject> forgetUser(@Body JsonObject forgetUser);

    @POST("api-update-pin")
    Call<JsonObject> updateMPIN(@Body JsonObject updatePin);

    @POST("api-resend-otp")
    Call<JsonObject> resendOtp(@Body JsonObject user_detail);

    @POST("api-forgot-password")
    Call<JsonObject> forgetPassword(@Body JsonObject user_detail);

    @POST("api-check-security-pin")
    Call<JsonObject> checkMPin(@Body JsonObject checkPin);

    @POST("api-get-dashboard-data")
    Call<JsonObject> dashBoard(@Body JsonObject dashboard);

    @POST("api-get-slider-images")
    Call<JsonObject> sliderImage(@Body JsonObject slideImg);

    @POST("api-bid-history-data")
    Call<JsonObject> bid_history(@Body JsonObject bidHist);

    @POST("api-wining-history-data")
    Call<JsonObject> win_history(@Body JsonObject winHist);

    @POST("api-get-profile")
    Call<JsonObject> get_profile(@Body JsonObject getProfile);

    @POST("api-profile-update")
    Call<JsonObject> update_profile(@Body JsonObject updateProfile);

    @POST("api-change-password")
    Call<JsonObject> change_password(@Body JsonObject changePassword);

    @POST("api-fund-request-add")
    Call<JsonObject> api_fund_request_add(@Body JsonObject apiFundRequestAdd);

    @POST("api-game-rates")
    Call<JsonObject> game_rates(@Body JsonObject gameRates);

    @POST("api-starline-game-rates")
    Call<JsonObject> starLine_game_rates(@Body JsonObject jsonObject);

    @POST("api-starline-game")
    Call<JsonObject> starLine_game_details(@Body JsonObject jsonObject);

    @POST("api-starline-bid-history-data")
    Call<JsonObject> starLine_game_bidHist(@Body JsonObject jsonObject);

    @POST("api-starline-wining-history-data")
    Call<JsonObject> starLine_game_winHist(@Body JsonObject jsonObject);

    @POST("api-get-current-date")
    Call<JsonObject> getCurrentDate(@Body JsonObject jsonObject);

    @POST("api-user-wallet-balance")
    Call<JsonObject> getWalletBalance(@Body JsonObject jsonObject);

    @POST("api-submit-bid")
    Call<JsonObject> submitBid(@Body JsonObject jsonObject);

    @POST("api-get-social-data")
    Call<JsonObject> getSocialData(@Body JsonObject jsonObject);

    @POST("api-starline-submit-bid")
    Call<JsonObject> starLineSubmitBid(@Body JsonObject jsonObject);

    @POST("api-wallet-transaction-history")
    Call<JsonObject> walletTransactionHistory(@Body JsonObject jsonObject);





    @POST("api-get-auto-deposit-list")
    Call<JsonObject> depositHistoryList(@Body JsonObject jsonObject);

    @POST("api-fund-request-history")
    Call<JsonObject> fundRequestHistory(@Body JsonObject jsonObject);


    @POST("api-check-user-for-transfer-amt")
    Call<JsonObject> checkUserForTransferAmt(@Body JsonObject jsonObject);

    @POST("api-user-transfer-wallet-balance")
    Call<JsonObject> transferWalletBalance(@Body JsonObject jsonObject);

    @POST("api-get-user-payment-details")
    Call<JsonObject> userPaymentDetails(@Body JsonObject jsonObject);

    @POST("api-user-payment-method-list")
    Call<JsonObject> userPaymentMethodList(@Body JsonObject jsonObject);

    @POST("api-user-withdraw-transaction-history")
    Call<JsonObject> withdrawTransactionHist(@Body JsonObject jsonObject);

    @POST("api-last-fund-request-detail")
    Call<JsonObject> lastFundRequestDetail(@Body JsonObject jsonObject);

    @POST("api-user-withdraw-fund-request")
    Call<JsonObject> walletWithdrawalRequest(@Body JsonObject jsonObject);

    @POST("api-get-contact-details")
    Call<JsonObject> contactDetails(@Body JsonObject jsonObject);

    @POST("api-validate-bank")
    Call<JsonObject> validateBank(@Body JsonObject jsonObject);

    @POST("api-add-user-upi-details")
    Call<JsonObject> addUserUpiDetails(@Body JsonObject jsonObject);

    @POST("api-get-auto-deposit-list")
    Call<JsonObject> getAutoDepositList(@Body JsonObject jsonObject);

    @POST("api-admin-bank-details")
    Call<JsonObject> adminBankDetails(@Body JsonObject jsonObject);

    @POST("api-add-money-via-upi")
    Call<JsonObject> addMoneyViaUpi(@Body JsonObject jsonObject);

    @POST("api-galidisswar-game-rates")
    Call<JsonObject> galiDesawarRates(@Body JsonObject jsonObject);

    @POST("api-galidisswar-game")
    Call<JsonObject> galiDesawarGames(@Body JsonObject jsonObject);

    @POST("api-galidisswar-submit-bid")
    Call<JsonObject> galiDesawarSubmitBid(@Body JsonObject jsonObject);

    @POST("api-galidisswar-bid-history-data")
    Call<JsonObject> galidisswar_game_bidHist(@Body JsonObject jsonObject);

    @POST("api-galidisswar-wining-history-data")
    Call<JsonObject> galidisswar_game_winHist(@Body JsonObject jsonObject);

    @POST("api-how-to-play")
    Call<JsonObject> how_to_play(@Body JsonObject jsonObject);

    @POST("api-get-notification")
    Call<JsonObject> getNotification(@Body JsonObject jsonObject);
}
