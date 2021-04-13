package com.spring.boot.demo.configuration;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
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
    @Value("${spring.data.mongodb.host:localhost}")
    private String host;

    @Value("${spring.data.mongodb.port:27017}")
    private int port;

    @Value("${spring.data.mongodb.database:embeded_db}")
    private String database;

    @Value("${spring.data.mongodb.authentication-database:admin}")
    private String authDatabase;

    @Value("${spring.data.mongodb.username:root}")
    private String userName;

    @Value("${spring.data.mongodb.password:root}")
    private String passWord;

    @Override
    protected String getDatabaseName() {
        return database;
    }
    @Override
    public Collection getMappingBasePackages() {
        return Collections.singleton("com.spring.boot.demo");
    }

    @Override
    protected void configureClientSettings(MongoClientSettings.Builder builder) {
        if(!userName.equals("root")){
            builder.credential(MongoCredential.createCredential(userName, authDatabase, passWord.toCharArray()))
                    .applyToClusterSettings(b ->
                            b.hosts(Arrays.asList(new ServerAddress(host, port))));
        }

    }

    @Override
    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory databaseFactory, MongoCustomConversions customConversions, MongoMappingContext mappingContext) {
        var dbRefResolver = new DefaultDbRefResolver(mongoDbFactory());
        var converter = new MappingMongoConverter(
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
