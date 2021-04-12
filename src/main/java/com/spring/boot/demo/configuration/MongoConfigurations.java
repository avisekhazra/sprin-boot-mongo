package com.spring.boot.demo.configuration;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.spring.boot.demo.converters.ZoneDateWriteConverter;
import com.spring.boot.demo.converters.ZonedDateReadConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.*;

@Configuration
public class MongoConfigurations extends AbstractMongoClientConfiguration {

    @Autowired
    private Environment env;
    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private int port;

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Value("${spring.data.mongodb.authentication-database}")
    private String authDatabase;

    @Override
    protected String getDatabaseName() {
        return env.getProperty("spring.data.mongodb.database");
    }
    @Override
    public Collection getMappingBasePackages() {
        return Collections.singleton("com.spring.boot.demo");
    }

    @Override
    protected void configureClientSettings(MongoClientSettings.Builder builder) {
        String userName = env.getProperty("spring.data.mongodb.username");
        String passWord = env.getProperty("spring.data.mongodb.password");
        builder.credential(MongoCredential.createCredential(userName, authDatabase, passWord.toCharArray()))
                .applyToClusterSettings(b ->
                        b.hosts(Arrays.asList(new ServerAddress(host, port))));
    }

    @Override
    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory databaseFactory, MongoCustomConversions customConversions, MongoMappingContext mappingContext) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory());
        MappingMongoConverter converter = new MappingMongoConverter(
                dbRefResolver, mappingContext);
        //Set this to remove the _class from the persistent layers
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        //Custom Mapping converter registry
        converter.setCustomConversions(customConversions());
        return converter;
    }

    /* Implement customConversions()
    @Override
    public MongoCustomConversions customConversions() {
        List<Converter<?,?>> converters = new ArrayList<>();
        converters.add(new ZoneDateWriteConverter());
        converters.add(new ZonedDateReadConverter());
        return new MongoCustomConversions(converters);
    }*/
}
