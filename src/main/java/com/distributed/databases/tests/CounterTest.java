package com.distributed.databases.tests;

import com.distributed.databases.MongoConfig;
import com.mongodb.client.MongoClient;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Oleksandr Havrylenko
 **/
public interface CounterTest {
    Logger logger = LoggerFactory.getLogger(CounterTest.class);
    String KEY = "counter";

    String getDescription();

    default void createData() {
        try(MongoClient mongoClient = MongoConfig.getClient()) {
            List<Document> databases = mongoClient.listDatabases().into(new ArrayList<>());
            databases.forEach(db -> System.out.println(db.toJson()));
        }
    }

    void test(final int maxCounterVal);

    default long getResult() {
        return 1L;
    }
}
