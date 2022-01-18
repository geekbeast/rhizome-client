package com.geekbeast.retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class RhizomeByteConverterFactory extends Converter.Factory {
    private static final String BYTE_MIME_TYPE = "application/octet-stream";

    @Override
    public Converter<ResponseBody, byte[]> responseBodyConverter(
            Type type,
            Annotation[] annotations,
            Retrofit retrofit ) {
        if ( isByteArray( type ) ) {
            return ResponseBody::bytes;
        }
        return null;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(
            Type type,
            Annotation[] parameterAnnotations,
            Annotation[] methodAnnotations,
            Retrofit retrofit ) {
        if ( isByteArray( type ) ) {
            return obj -> RequestBody.create( MediaType.parse( BYTE_MIME_TYPE ), (byte[]) obj );
        }
        return null;
    }

    public static boolean isByteArray( Type type ) {
        return ( ( byte[].class ) ).equals( type );
    }
}
