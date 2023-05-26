package com.geekbeast.mappers.mappers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.blackbird.BlackbirdModule;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import java.util.Map;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.collect.Maps;

public final class ObjectMappers {
    public enum Mapper {
        YAML,
        SMILE,
        JSON
    }

    // TODO: Add options that for configuring serialization types supported.

    private static final Map<Mapper, ObjectMapper> mappers = Maps.newEnumMap( Mapper.class );

    static {
        mappers.put( Mapper.YAML, createYamlMapper() );
        mappers.put( Mapper.SMILE, createSmileMapper() );
        mappers.put( Mapper.JSON, createJsonMapper() );
    }

    private ObjectMappers() {}

    protected static ObjectMapper createYamlMapper() {
        ObjectMapper yamlMapper = new ObjectMapper( new YAMLFactory() );
        yamlMapper.registerModule( new Jdk8Module() );
        yamlMapper.registerModule( new GuavaModule() );
        yamlMapper.registerModule( new JavaTimeModule() );
        yamlMapper.registerModule( new BlackbirdModule() );
        yamlMapper.registerModule( new JodaModule() );
        yamlMapper.registerModule( new KotlinModule() );
        yamlMapper.configure( SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false );
        yamlMapper.disable( DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE );
        return yamlMapper;
    }

    protected static ObjectMapper createSmileMapper() {
        ObjectMapper smileMapper = new ObjectMapper( new SmileFactory() );
        smileMapper.registerModule( new Jdk8Module() );
        smileMapper.registerModule( new GuavaModule() );
        smileMapper.registerModule( new JavaTimeModule() );
        smileMapper.registerModule( new BlackbirdModule() );
        smileMapper.registerModule( new JodaModule() );
        smileMapper.registerModule( new KotlinModule() );
        smileMapper.configure( SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false );
        smileMapper.disable( DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE );
        return smileMapper;
    }

    protected static ObjectMapper createJsonMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule( new Jdk8Module() );
        mapper.registerModule( new JavaTimeModule() );
        mapper.registerModule( new GuavaModule() );
        mapper.registerModule( new JodaModule() );
        mapper.registerModule( new BlackbirdModule() );
        mapper.registerModule( new KotlinModule() );
        mapper.configure( SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false );
        mapper.disable( DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE );
        return mapper;
    }

    public static ObjectMapper getYamlMapper() {
        return ObjectMappers.getMapper( Mapper.YAML );
    }

    public static ObjectMapper getSmileMapper() {
        return ObjectMappers.getMapper( Mapper.SMILE );
    }

    public static ObjectMapper getJsonMapper() {
        return ObjectMappers.getMapper( Mapper.JSON );
    }

    public static ObjectMapper newJsonMapper() {
        return createJsonMapper();
    }

    public static ObjectMapper getMapper( Mapper type ) {
        return mappers.get( type );
    }

    public static void foreach( Consumer<ObjectMapper> object ) {
        mappers.values().forEach( object );
    }
}
