package com.openlattice;

import com.amazonaws.services.s3.AmazonS3;
import com.dataloom.mappers.ObjectMappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.kryptnostic.rhizome.configuration.annotation.ReloadableConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * Created by mtamayo on 7/3/17.
 */
public class ResourceConfigurationLoader {
    private static final Logger       logger = LoggerFactory.getLogger( ResourceConfigurationLoader.class );
    private static final ObjectMapper mapper = ObjectMappers.getYamlMapper();

    @Nullable
    public static ObjectMapper getYamlMapper() {
        return mapper;
    }

    public static <T> T loadConfiguration( Class<T> clazz ) {
        String uri = getReloadableConfigurationUri( clazz );
        return loadConfigurationFromResource( uri, clazz );
    }

    public static <T> T loadConfigurationFromS3(
            AmazonS3 s3,
            String bucket,
            String folder,
            Class<T> clazz ) {
        T s = null;
        String key = getReloadableConfigurationUri( clazz );
        String yamlString = null;
        try {
            try {
                yamlString = IOUtils.toString( s3.getObject( bucket, folder + key ).getObjectContent(), Charset.defaultCharset() );
            } catch ( IOException | IllegalArgumentException e ) {
                logger.debug( "Failed to load resource from " + key, e );
            }
            if ( StringUtils.isBlank( yamlString ) ) {
                throw new IOException( "Unable to read configuration from classpath." );
            }
            s = mapper.readValue( yamlString, clazz );
        } catch ( IOException e ) {
            logger.debug( "Failed to load default configuration for " + key, e );
        }

        return s;
    }

    public static String getReloadableConfigurationUri( Class<?> clazz ) {
        ReloadableConfiguration config = clazz.getAnnotation( ReloadableConfiguration.class );
        if ( config != null ) {
            if ( StringUtils.isBlank( config.uri() ) ) {
                return clazz.getCanonicalName();
            } else {
                return config.uri();
            }
        } else {
            return null;
        }
    }

    public static <T> T loadConfigurationFromFile( String path, Class<T> clazz ) {
        T s = null;
        try {
            Path p = Path.of(path, getReloadableConfigurationUri( clazz ));
            File f = p.toFile();
            s = mapper.readValue( f, clazz );
        } catch ( IOException e ) {
            logger.error( "Failed to load configuration from {} for {}: {}", path, clazz, e );
        }
        return s;
    }

    public static <T> T loadConfigurationFromResource( String uri, Class<T> clazz ) {
        T s = null;
        String yamlString = null;
        try {
            try {
                URL resource = Resources.getResource( uri );
                yamlString = Resources.toString( resource, StandardCharsets.UTF_8 );
                if ( StringUtils.isBlank( yamlString ) ) {
                    throw new IOException( "Unable to read configuration from classpath." );
                }
            } catch ( IOException | IllegalArgumentException e ) {
                logger.warn( "Failed to load resource from " + uri, e );
            }
            s = mapper.readValue( yamlString, clazz );
        } catch ( IOException e ) {
            logger.error( "Failed to load default configuration for " + uri, e );
        }

        return s;
    }
}
