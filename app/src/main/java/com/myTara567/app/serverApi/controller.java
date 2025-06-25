package com.myTara567.app.serverApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class controller {
   public static String url = "https://app.tara567.co/";
   public static String chatsupport = "https://tawk.to/chat/676e7a1daf5bfec1dbe2fa41/1ig3oq1ue";
    public static controller clintObj;
    public static Retrofit retrofit;


    controller() {
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized controller getInstance() {
        if (clintObj == null) {
            clintObj = new controller();
        }
        return clintObj;
    }

    public apiInterfaces getApi() {
        return retrofit.create(apiInterfaces.class);
    }
}
