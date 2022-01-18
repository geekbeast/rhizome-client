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

package com.geekbeast.rhizome.configuration.configuration.amazon;

import com.amazonaws.regions.Regions;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class AwsLaunchConfiguration implements AmazonLaunchConfiguration {
    public static final  String                      BUCKET_FIELD   = "bucket";
    public static final  String                      FOLDER_FIELD   = "folder";
    public static final  String                      REGION_FIELD   = "region";
    private static final String                      DEFAULT_FOLDER = "";
    private final        String                      bucket;
    private final        String                      folder;
    private final        Optional<Regions> region;

    @JsonCreator
    public AwsLaunchConfiguration(
            @JsonProperty( BUCKET_FIELD ) String bucket,
            @JsonProperty( FOLDER_FIELD ) Optional<String> folder,
            @JsonProperty( REGION_FIELD ) Optional<String> region ) {
        Preconditions.checkArgument( StringUtils.isNotBlank( bucket ),
                "S3 bucket for configuration must be specified." );
        this.bucket = bucket;
        String rawFolder = folder.orElse( DEFAULT_FOLDER );

        while ( StringUtils.endsWith( rawFolder, "/" ) ) {
            StringUtils.removeEnd( rawFolder, "/" );
        }
        // We shouldn't prefix
        if ( StringUtils.isNotBlank( rawFolder ) ) {
            this.folder = rawFolder + "/";
        } else {
            this.folder = rawFolder;
        }

        this.region = region.map( Regions::fromName );
    }

    @Override
    @JsonProperty( REGION_FIELD )
    public Optional<Regions> getRegion() {
        return region;
    }

    @Override public String toString() {
        return "AwsLaunchConfiguration{" +
                "bucket='" + bucket + '\'' +
                ", folder='" + folder + '\'' +
                ", region=" + region +
                '}';
    }

    @Override
    @JsonProperty( BUCKET_FIELD )
    public String getBucket() {
        return bucket;
    }

    @Override
    @JsonProperty( FOLDER_FIELD )
    public String getFolder() {
        return folder;
    }

}
