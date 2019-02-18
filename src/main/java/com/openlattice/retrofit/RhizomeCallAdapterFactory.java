package com.openlattice.retrofit;

import com.google.common.base.Charsets;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.rmi.ServerException;
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
                                 responseBody);
                        if( code >=500 && code<600) {
                            throw new ServerException( responseBody );
                        }
                        return null;
                    }
                    return response.body();
                } catch ( IOException e ) {
                    logger.error( "Call failed.", e );
                    return null;
                }
            }

        };
    }

}
