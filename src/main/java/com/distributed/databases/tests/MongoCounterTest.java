package com.distributed.databases.tests;

import com.distributed.databases.MongoConfig;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 * @author Oleksandr Havrylenko
 **/
public class MongoCounterTest implements CounterTest {
    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public void test(int maxCounterVal) {
        try(MongoClient mongoClient = MongoConfig.getClient()) {
            MongoDatabase shopDatabase = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> likesCollection = shopDatabase.getCollection(LIKES_COLLECTION);
            Bson query  = Filters.eq("_id", new ObjectId(OBJECT_ID));
            Bson update = Updates.inc(LIKES_COUNT, 1L);

            for (int i = 0; i < maxCounterVal; i++) {
                likesCollection.findOneAndUpdate(query, update);
            }
        }

    }
}
