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
import okhttp3.Credentials;
import okhttp3.OkHttpClient;

/**
 * @author Matthew Tamayo-Rios &lt;matthew@openlattice.com&gt;
 */
public class RetrofitBuilders {

    public static OkHttpClient.Builder withProxyAndBasicAuth(
            String username,
            String password,
            String proxyHost,
            int proxyPort ) {
        String authToken = Credentials.basic( username, password );

        return new OkHttpClient.Builder().proxy( buildProxy( proxyHost, proxyPort ) )
                .addInterceptor( chain -> chain
                        .proceed( chain.request().newBuilder().addHeader( "Authorization", authToken ).build() ) );

    }

    public static Proxy buildProxy( String proxyHost, int proxyPort ) {
        return new Proxy( Type.SOCKS, new InetSocketAddress( proxyHost, proxyPort ) );
    }
}
