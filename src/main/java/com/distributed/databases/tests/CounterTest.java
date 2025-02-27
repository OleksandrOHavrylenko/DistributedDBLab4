package com.distributed.databases.tests;

import com.distributed.databases.MongoConfig;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Oleksandr Havrylenko
 **/
public interface CounterTest {
    Logger logger = LoggerFactory.getLogger(CounterTest.class);
    String OBJECT_ID = "67c069399215af4351c1064d";
    String DB_NAME = "shop";
    String LIKES_COLLECTION = "likes";
    String LIKES_COUNT = "likesCount";

    String getDescription();

    default void createData() {
        try(MongoClient mongoClient = MongoConfig.getClient()) {
            Document like = new Document("_id", new ObjectId(OBJECT_ID))
                    .append("item", 10)
                    .append(LIKES_COUNT, 0L);

            MongoDatabase shopDatabase = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> likesCollection = shopDatabase.getCollection(LIKES_COLLECTION);

            Bson query  = Filters.eq("_id", new ObjectId(OBJECT_ID));
            Bson update = Updates.set(LIKES_COUNT, 0L);
            UpdateResult updateResult = likesCollection.updateOne(query, update);
            logger.info("Find updateResult : {}", updateResult);

            if (updateResult.getMatchedCount() != 1L) {
                InsertOneResult insertOneResult = likesCollection.insertOne(like);
                logger.info("Inserted like item: {} with _id: {}", like, insertOneResult.getInsertedId());
            }

            logger.info("Updated like item with _id: {}", like);
        }
    }

    void test(final int maxCounterVal);

    default long getResult() {
        try(MongoClient mongoClient = MongoConfig.getClient()) {
            MongoDatabase shopDatabase = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> likesCollection = shopDatabase.getCollection(LIKES_COLLECTION);
            Document document = likesCollection.find(Filters.eq("_id", new ObjectId(OBJECT_ID))).first();
            if(document != null) {
                return document.getLong(LIKES_COUNT);
            }

            return -1L;
        }
    }
}
