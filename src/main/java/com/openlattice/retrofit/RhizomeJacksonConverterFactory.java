package com.openlattice.retrofit;

import com.dataloom.mappers.ObjectMappers;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.google.common.base.Charsets;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Converter.Factory;
import retrofit2.Retrofit;

/**
 * A {@link Factory} for serialization / deserialization using jackson for retrofit 2.
 *
 */
public class RhizomeJacksonConverterFactory extends Converter.Factory {
    private static final String JSON_MIME_TYPE      = "application/json";
    private static final String JSON_UTF8_MIME_TYPE = JSON_MIME_TYPE + "; charset=UTF-8";

    private static final String PLAIN_TEXT_MIME_TYPE = "text/plain";

    private static final Logger       logger = LoggerFactory.getLogger( RhizomeJacksonConverterFactory.class );
    private final        ObjectMapper objectMapper;

    public RhizomeJacksonConverterFactory() {
        this( ObjectMappers.newJsonMapper() );
    }

    public RhizomeJacksonConverterFactory( ObjectMapper mapper ) {
        this.objectMapper = mapper;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter( Type type, Annotation[] annotations, Retrofit retrofit ) {
        if ( RhizomeByteConverterFactory.isByteArray( type ) ) {
            return null;
        }

        return responseBody -> {
            MediaType contentType = responseBody.contentType();

            if ( contentType == null ) {
                return null;
            }

            String rawContentType = contentType.toString();
            if ( StringUtils.startsWith( rawContentType, JSON_MIME_TYPE ) ) {
                try {
                    return objectMapper.readValue( responseBody.byteStream(),
                            objectMapper.getTypeFactory().constructType( type ) );
                } catch ( IOException e ) {
                    logger.error( "Unable to read deserialize json response.", e );
                    return null;
                }
            } else if ( StringUtils.startsWith( rawContentType,
                    com.google.common.net.MediaType.PLAIN_TEXT_UTF_8.type() ) ) {
                return IOUtils.toString( responseBody.byteStream(), Charsets.UTF_8 );
            }
            return null;
        };
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(
            Type type,
            Annotation[] parameterAnnotations,
            Annotation[] methodAnnotations,
            Retrofit retrofit ) {
        if ( RhizomeByteConverterFactory.isByteArray( type ) ) {
            return null;
        } else if ( type == String.class ) {
            return obj -> RequestBody.create( MediaType.parse( PLAIN_TEXT_MIME_TYPE ), (String) obj );
        }

        return obj -> RequestBody.create( MediaType.parse( JSON_UTF8_MIME_TYPE ),
                objectMapper.writeValueAsBytes( obj ) );
    }
}