package com.distributed.databases;

import com.distributed.databases.tests.CounterTest;
import com.distributed.databases.tests.MongoCounterTest;
import com.mongodb.client.MongoClient;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Oleksandr Havrylenko
 **/
public class Main {
    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static final int THREADS_10 = 10;
    public static final int COUNTER_10000 = 10000;

    public static void main(String[] args) {

        final CounterTest counterTest = new MongoCounterTest();
        counterTest.createData();

        long start = System.nanoTime();

        testDatabaseCounter(THREADS_10, () -> counterTest.test(COUNTER_10000));

        long finish = System.nanoTime();

        logger.info("Final result counter = {} per Duration: {} ms;",
                counterTest.getResult(), (finish - start) / 1_000_000.0);

    }

    private static void testDatabaseCounter(final int threadsNum, Runnable task) {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < threadsNum; i++) {
            Thread thread = new Thread(task);
            thread.start();
            threads.add(thread);
        }

        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                logger.error("Interrupted while waiting for thread completion.", e);
            }
        });
    }
}