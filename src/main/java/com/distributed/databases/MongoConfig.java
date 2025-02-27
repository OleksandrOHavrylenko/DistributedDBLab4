package com.distributed.databases;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

/**
 * @author Oleksandr Havrylenko
 **/
public class MongoConfig {
    private static String MONGO_URI = "mongodb://127.0.0.1:27017,127.0.0.1:27018,127.0.0.1:27019/?replicaSet=rs0";

    public static MongoClient getClient() {
        final MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(MONGO_URI))
                .build();
        final MongoClient mongoClient = MongoClients.create(settings);

        return mongoClient;
    }

}
