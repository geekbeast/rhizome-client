package com.geekbeast.retrofit;


import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;

public class RetrofitTests {
    public static final Map<String, String> M = ImmutableMap.of( "a", "b" );

    @Test
    public void testCreation() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor( chain -> {
                    chain.proceed( chain.request() );
                    return new Response.Builder().build();
                } )
                .build();
        final String baseURL = "http://localhost:8081/";
        Retrofit adapter = new Retrofit.Builder().baseUrl( baseURL ).client( httpClient )
                .addConverterFactory( new RhizomeByteConverterFactory() )
                .addConverterFactory( new RhizomeJacksonConverterFactory() )
                .addCallAdapterFactory( new RhizomeCallAdapterFactory() ).build();
        Api api = adapter.create( Api.class );
        try {
            api.post( M );
        } catch ( RhizomeRetrofitCallFailedException ex ) {
            Assert.assertEquals( ex.getMessage(), "Retrofit call " + baseURL + " failed." );
        }
    }
}
