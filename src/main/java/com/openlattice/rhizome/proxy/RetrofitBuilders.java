/*
 * Copyright (C) 2017. OpenLattice, Inc
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * You can contact the owner of the copyright at support@openlattice.com
 *
 */

package com.openlattice.rhizome.proxy;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.concurrent.TimeUnit;

import com.dataloom.mappers.ObjectMappers;
import com.openlattice.retrofit.RhizomeByteConverterFactory;
import com.openlattice.retrofit.RhizomeCallAdapterFactory;
import com.openlattice.retrofit.RhizomeJacksonConverterFactory;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author Matthew Tamayo-Rios &lt;matthew@openlattice.com&gt;
 */
public class RetrofitBuilders {
    public static Retrofit.Builder proxiedJacksonWithBasicAuthRetrofit(
            String url,
            String username,
            String password,
            String proxyHost,
            int proxyPort ) {
        Retrofit.Builder builder = createBaseRhizomeRetrofitBuilder( url,
                okHttpClientWithBasicAuthAndProxy( username, password, proxyHost, proxyPort ).build() );
        return decorateWithRhizomeFactories( builder );
    }

    public static OkHttpClient.Builder okHttpClientWithBasicAuthAndProxy(
            String username,
            String password,
            String proxyHost,
            int proxyPort ) {
        OkHttpClient.Builder builder = okHttpClient();
        builder = withProxy( builder, proxyHost, proxyPort );
        return withBasicAuth( builder, username, password );
    }

    public static OkHttpClient.Builder okHttpClient() {
        return new OkHttpClient.Builder()
                .readTimeout( 0, TimeUnit.MILLISECONDS )
                .writeTimeout( 0, TimeUnit.MILLISECONDS )
                .connectTimeout( 0, TimeUnit.MILLISECONDS );
    }

    public static OkHttpClient.Builder withProxy( OkHttpClient.Builder builder, String proxyHost, int proxyPort ) {
        return builder.proxy( buildProxy( proxyHost, proxyPort ) );
    }

    public static final OkHttpClient.Builder withBasicAuth(
            OkHttpClient.Builder builder,
            String username,
            String password ) {
        String authToken = Credentials.basic( username, password );
        return builder
                .addInterceptor( chain -> chain
                        .proceed( chain.request().newBuilder().addHeader( "Authorization", authToken ).build() ) );
    }

    public static Proxy buildProxy( String proxyHost, int proxyPort ) {
        return new Proxy( Type.SOCKS, new InetSocketAddress( proxyHost, proxyPort ) );
    }

    public static final Retrofit.Builder decorateWithRhizomeFactories( Retrofit.Builder builder ) {
        return builder.addConverterFactory( new RhizomeByteConverterFactory() )
                .addConverterFactory( new RhizomeJacksonConverterFactory( ObjectMappers.getJsonMapper() ) )
                .addCallAdapterFactory( new RhizomeCallAdapterFactory() );
    }

    public static final Retrofit.Builder createBaseRhizomeRetrofitBuilder( String baseUrl, OkHttpClient httpClient ) {
        return new Retrofit.Builder().baseUrl( baseUrl ).client( httpClient );
    }
}
