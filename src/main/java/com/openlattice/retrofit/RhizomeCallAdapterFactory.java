package com.openlattice.retrofit;

import com.google.common.base.Charsets;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.annotation.Nullable;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RhizomeCallAdapterFactory extends CallAdapter.Factory {
    private static final Logger logger = LoggerFactory.getLogger( RhizomeCallAdapterFactory.class );

    @Override
    public CallAdapter<?, ?> get( Type returnType, Annotation[] annotations, Retrofit retrofit ) {
        return new CallAdapter<Object, Object>() {
            @Override
            public Type responseType() {
                return returnType;
            }

            @Override public @Nullable Object adapt( Call call ) {
                try {
                    Response response = call.execute();
                    final var code = response.code();
                    if ( code >= 400 ) {
                        final var responseBody = IOUtils.toString( response.errorBody().byteStream(), Charsets.UTF_8 );
                        logger.error( "Call failed with code {} and message {} and error body {}",
                                response.code(),
                                response.message(),
                                responseBody );
                        if ( code >= 400 && code < 600 ) {
                            final var message = call.request().url().toString();
                            throw new RhizomeRetrofitCallException( "Retrofit API call to " + message + "failed",
                                    responseBody,
                                    response.code() );
                        }
                        return null;
                    }
                    return response.body();
                } catch ( IOException e ) {
                    final var message = call.request().url().toString();
                    throw new RhizomeRetrofitCallFailedException( "Retrofit call " + message + " failed.", e );

                }
            }

        };
    }

}
