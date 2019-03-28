package com.notes.config;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -03 - 28 18:10
 */
public class MongoAppender /*extends AppenderSkeleton*/ {
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<BasicDBObject> logsCollection;
    private String connectionUrl;
    private String databaseName;
    private String collectionName;

   /* @Override
    protected void append(LoggingEvent loggingEvent) {
        if(mongoDatabase == null) {
            MongoClientURI connectionString = new MongoClientURI(connectionUrl);
            mongoClient = new MongoClient(connectionString);
            mongoDatabase = mongoClient.getDatabase(databaseName);
            logsCollection = mongoDatabase.getCollection(collectionName, BasicDBObject.class);
        }
        logsCollection.insertOne((BasicDBObject) loggingEvent.getMessage());
    }
    @Override
    public void close() {
        if(mongoClient != null) {
            mongoClient.close();
        }
    }
    @Override
    public boolean requiresLayout() {
        return false;
    }*/

}
