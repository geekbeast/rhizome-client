package com.openlattice.retrofit;


import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;

public class RetrofitTests {
    public static final Map<String, String> M = ImmutableMap.of("a","b");
    @Test
    public void testCreation() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor( chain -> {
                    chain.proceed( chain.request() );
                    return new Response.Builder().build();
                } )
                .build();
        Retrofit adapter = new Retrofit.Builder().baseUrl( "http://localhost:8081/rhizome/api/" ).client( httpClient )
                .addConverterFactory( new LoomByteConverterFactory() )
                .addConverterFactory( new LoomJacksonConverterFactory() )
                .addCallAdapterFactory( new LoomCallAdapterFactory() ).build();
        Api api = adapter.create( Api.class );
        api.post( M );

    }
}
