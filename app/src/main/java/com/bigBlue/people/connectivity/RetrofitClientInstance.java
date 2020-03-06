package com.bigBlue.people.connectivity;

import com.bigBlue.people.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The type Retrofit client instance.
 */
public class RetrofitClientInstance {

    private static Retrofit retrofit;


    /**
     * Gets retrofit instance.
     *
     * @return the retrofit instance
     */
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}