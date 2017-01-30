package com.dataloom.retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class LoomStringConverterFactory extends Converter.Factory {
    private static final String PLAIN_TEXT_MIME_TYPE = "text/plain";
    private static final Logger logger         = LoggerFactory.getLogger( LoomStringConverterFactory.class );

    @Override
    public Converter<ResponseBody, String> responseBodyConverter(
            Type type,
            Annotation[] annotations,
            Retrofit retrofit ) {
        if ( isString( type ) ) {
            return responseBody -> responseBody.string();
        }
        return null;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(
            Type type,
            Annotation[] parameterAnnotations,
            Annotation[] methodAnnotations,
            Retrofit retrofit ) {
        if ( isString( type ) ) {
            return obj -> RequestBody.create( MediaType.parse( PLAIN_TEXT_MIME_TYPE ), (String) obj );
        }
        return null;
    }

    public static boolean isString( Type type ) {
        return ( (Type) ( String.class ) ).equals( type );
    }
}
