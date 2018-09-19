package com.openlattice.serializer;

import com.dataloom.mappers.ObjectMappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Base class for writing jackson serialization tests.
 *
 * @param <T> The type whose serialization will be tested.
 */
public abstract class AbstractJacksonSerializationTest<T> {
    protected static final ObjectMapper mapper = ObjectMappers.getJsonMapper();
    protected static final ObjectMapper smile  = ObjectMappers.getSmileMapper();

    protected final Logger logger = LoggerFactory.getLogger( getClass() );

    @Test
    public void testSerdes() throws IOException {
        T data = getSampleData();
        SerializationResult<T> result = serialize( data );
        logResult( result );
        Assert.assertEquals( data, result.deserializeJsonString( getClazz() ) );
        Assert.assertEquals( data, result.deserializeJsonBytes( getClazz() ) );
        Assert.assertEquals( data, result.deserializeSmileBytes( getClazz() ) );
    }

    protected SerializationResult<T> serialize( T data ) throws IOException {
        return new SerializationResult<>( mapper.writeValueAsString( data ),
                mapper.writeValueAsBytes( data ),
                smile.writeValueAsBytes( data ) );
    }

    protected void logResult( SerializationResult<T> result ) {
    }

    protected void configureSerializers() {
    }

    protected abstract T getSampleData() throws IOException;

    protected abstract Class<T> getClazz();

    protected static void registerModule( Consumer<ObjectMapper> c ) {
        c.accept( mapper );
        c.accept( smile );
    }

    protected static class SerializationResult<T> {
        private final String jsonString;
        private final byte[] jsonBytes;
        private final byte[] smileBytes;

        public SerializationResult( String jsonString, byte[] jsonBytes, byte[] smileBytes ) {
            this.jsonString = jsonString;
            this.jsonBytes = Arrays.copyOf( jsonBytes, jsonBytes.length );
            this.smileBytes = Arrays.copyOf( smileBytes, smileBytes.length );
        }

        protected T deserializeJsonString( Class<T> clazz ) throws IOException {
            return mapper.readValue( jsonString, clazz );
        }

        protected T deserializeJsonBytes( Class<T> clazz ) throws IOException {
            return mapper.readValue( jsonBytes, clazz );
        }

        protected T deserializeSmileBytes( Class<T> clazz ) throws IOException {
            return smile.readValue( smileBytes, clazz );
        }

        public String getJsonString() {
            return jsonString;
        }
    }
}
    
