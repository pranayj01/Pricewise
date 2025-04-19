package com.example.try2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.Date;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Locale;

public class ApiClient {
    private static final String BASE_URL = "https://run.mocky.io/v3/";
    private static Retrofit retrofit;

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> {
                try {
                    return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                            .parse(json.getAsString());
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }
            })
            .create();

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}