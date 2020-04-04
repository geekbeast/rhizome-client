/*
 * Copyright (C) 2018. OpenLattice, Inc.
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
 *
 */

package com.openlattice.serializer;

import com.dataloom.mappers.ObjectMappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base class for jackson serialization tests that require compatibility with YAML.
 * @param <T> The type whose serialization will be tested.
 */
public abstract class AbstractJacksonYamlSerializationTest<T> extends AbstractJacksonSerializationTest<T> {
    protected static final ObjectMapper yaml = ObjectMappers.getYamlMapper();

    @Override
    @Test
    public void testSerdes() throws IOException {
        T data = getSampleData();
        YamlSerializationResult<T> result = serialize( data );
        logResult( result );
        Assert.assertTrue( compareElements( data, result.deserializeJsonString( getClazz() )));
        Assert.assertTrue( compareElements( data, result.deserializeJsonBytes( getClazz() )));
        Assert.assertTrue( compareElements( data, result.deserializeSmileBytes( getClazz() )));
        Assert.assertTrue( compareElements( data, result.deserializeYamlString( getClazz() )));
    }

    @Override
    protected YamlSerializationResult<T> serialize( T data ) throws IOException {
        return new YamlSerializationResult<>( mapper.writeValueAsString( data ),
                mapper.writeValueAsBytes( data ),
                smile.writeValueAsBytes( data ),
                yaml.writeValueAsString( data ));
    }

    protected void logResult( YamlSerializationResult<T> result ) {
       super.logResult( result );
    }

    protected abstract T getSampleData() throws IOException;

    protected abstract Class<T> getClazz();

    protected boolean compareElements( @NotNull T first, @NotNull  T second ) {
        return Objects.equals( first, second );
    }

    public static void registerModule( Consumer<ObjectMapper> c ) {
        AbstractJacksonSerializationTest.registerModule( c );
        c.accept( yaml );
    }

    protected static class YamlSerializationResult<T> extends SerializationResult<T> {
        private final String yamlString;

        public YamlSerializationResult( String jsonString, byte[] jsonBytes, byte[] smileBytes, String yamlString ) {
            super( jsonString, jsonBytes, smileBytes );
            this.yamlString = yamlString;
        }

        protected T deserializeYamlString( Class<T> clazz ) throws IOException {
            return yaml.readValue( yamlString, clazz );
        }

        public String getYamlString() {
            return yamlString;
        }
    }
}
    
