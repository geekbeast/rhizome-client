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

import com.auth0.json.mgmt.users.User;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.geekbeast.rhizome.configuration.configuration.annotation.ReloadableConfiguration;
import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Json serializable POJO for Auth0 configuration values.
 *
 * @author Matthew Tamayo-Rios &lt;matthew@kryptnostic.com&gt;
 */
// TODO: Implement data serializable or identified data serializable
@ReloadableConfiguration(
        uri = "auth0.yaml" )
public class Auth0Configuration implements Serializable {
    private static final long   serialVersionUID = 3802624515206194125L;
    private static final Logger logger           = LoggerFactory.getLogger( Auth0Configuration.class );

    public static final String CLIENT_ID_FIELD          = "clientId";
    public static final String CLIENT_SECRET_FIELD      = "clientSecret";
    public static final String CONFIGURATIONS_FIELD     = "configurations";
    public static final String DOMAIN_FIELD             = "domain";
    public static final String MANAGEMENT_API_URL_FIELD = "managementApiUrl";
    public static final String NO_SYNC_URL              = "localhost";
    public static final String USERS_FIELD              = "users";

    private final String                                domain;
    private final String                                clientId;
    private final String                                clientSecret;
    private final Set<Auth0AuthenticationConfiguration> configurations;
    private final Set<User>                             users;
    private final String                                managementApiUrl;

    @JsonCreator
    public Auth0Configuration(
            @JsonProperty( DOMAIN_FIELD ) String domain,
            @JsonProperty( CLIENT_ID_FIELD ) String clientId,
            @JsonProperty( CLIENT_SECRET_FIELD ) String clientSecret,
            @JsonProperty( CONFIGURATIONS_FIELD ) Set<Auth0AuthenticationConfiguration> configurations,
            @JsonProperty( USERS_FIELD ) Optional<Set<User>> users,
            @JsonProperty( MANAGEMENT_API_URL_FIELD ) String managementApiUrl ) {
        Preconditions.checkArgument( StringUtils.isNotBlank( domain ), "Domain cannot be blank" );
        Preconditions.checkArgument( StringUtils.isNotBlank( clientId ), "Client ID cannot be blank" );
        Preconditions.checkArgument( StringUtils.isNotBlank( clientSecret ), "Client secret cannot be blank" );
        Preconditions.checkArgument( StringUtils.isNotBlank( managementApiUrl ), "Management API URL cannot be blank" );
        this.domain = domain;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.configurations = configurations;
        this.managementApiUrl = managementApiUrl;
        this.users = users.orElse( ImmutableSet.of() );

        if( users.isPresent() &&  managementApiUrl.contains( "localhost" )) {
            logger.warn("Users were provided, but ignored because managementApi contains localhost");
        }
    }

    @JsonProperty( DOMAIN_FIELD )
    public String getDomain() {
        return domain;
    }

    @JsonProperty( CLIENT_ID_FIELD )
    public String getClientId() {
        return clientId;
    }

    @JsonProperty( CLIENT_SECRET_FIELD )
    public String getClientSecret() {
        return clientSecret;
    }

    @JsonProperty( CONFIGURATIONS_FIELD )
    public Set<Auth0AuthenticationConfiguration> getClients() {
        return configurations;
    }

    @JsonProperty( MANAGEMENT_API_URL_FIELD )
    public String getManagementApiUrl() {
        return managementApiUrl;
    }

    @JsonProperty( USERS_FIELD )
    public Set<User> getUsers() {
        return users;
    }
}
