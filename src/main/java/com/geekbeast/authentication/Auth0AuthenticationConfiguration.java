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

package com.geekbeast.authentication;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Matthew Tamayo-Rios &lt;matthew@openlattice.com&gt;
 */
public class Auth0AuthenticationConfiguration implements Serializable {
    public static final String ISSUER_FIELD                = "issuer";
    public static final String AUDIENCE_FIELD              = "audience";
    public static final String SECRET_FIELD                = "secret";
    public static final String BASE64_ENCODED_SECRET_FIELD = "base64EncodedSecret";
    public static final String SIGNING_ALGORITHM_FIELD     = "signingAlgorithm";

    private final String  issuer;
    private final String  audience;
    private final String  secret;
    private final boolean base64EncodedSecret;
    private final String  signingAlgorithm;

    @JsonCreator
    public Auth0AuthenticationConfiguration(
            @JsonProperty( ISSUER_FIELD ) String issuer,
            @JsonProperty( AUDIENCE_FIELD ) String audience,
            @JsonProperty( SECRET_FIELD ) String secret,
            @JsonProperty( BASE64_ENCODED_SECRET_FIELD ) Optional<Boolean> base64EncodedSecret,
            @JsonProperty( SIGNING_ALGORITHM_FIELD ) String signingAlgorithm ) {
        Preconditions.checkArgument( StringUtils.isNotBlank( issuer ) );
        Preconditions.checkArgument( StringUtils.isNotBlank( audience ) );
        if( signingAlgorithm.startsWith( "HS" ) ) {
            Preconditions.checkArgument( StringUtils.isNotBlank( secret ) );
        }
        this.issuer = issuer;
        this.audience = audience;
        this.secret = secret;
        this.base64EncodedSecret = base64EncodedSecret.orElse( false );
        this.signingAlgorithm = signingAlgorithm;
    }

    @JsonProperty( ISSUER_FIELD )
    public String getIssuer() {
        return issuer;
    }

    @JsonProperty( AUDIENCE_FIELD )
    public String getAudience() {
        return audience;
    }

    @JsonProperty( SECRET_FIELD )
    public String getSecret() {
        return secret;
    }

    @JsonProperty( BASE64_ENCODED_SECRET_FIELD )
    public boolean isBase64EncodedSecret() {
        return base64EncodedSecret;
    }

    @JsonProperty( SIGNING_ALGORITHM_FIELD )
    public String getSigningAlgorithm() {
        return signingAlgorithm;
    }

    @Override public boolean equals( Object o ) {
        if ( this == o ) {return true;}
        if ( !( o instanceof Auth0AuthenticationConfiguration ) ) {return false;}
        Auth0AuthenticationConfiguration that = (Auth0AuthenticationConfiguration) o;
        return base64EncodedSecret == that.base64EncodedSecret && issuer.equals( that.issuer )
                && audience.equals( that.audience ) && Objects.equals( secret, that.secret )
                && signingAlgorithm.equals( that.signingAlgorithm );
    }

    @Override public int hashCode() {
        return Objects.hash( issuer, audience, secret, base64EncodedSecret, signingAlgorithm );
    }

    @Override public String toString() {
        return "Auth0AuthenticationConfiguration{" +
                "issuer='" + issuer + '\'' +
                ", audience='" + audience + '\'' +
                ", secret='" + secret + '\'' +
                ", base64EncodedSecret=" + base64EncodedSecret +
                '}';
    }
}
