package dev;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import common.Review;
import common.user.Reviewer;
import org.bson.Document;

import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class MongoDatabaseClient implements Database{

    private MongoClient client;
    private MongoDatabase db;
    private MongoCollection<Document> reviewers;

    public MongoDatabaseClient(MongoClient client, String dbName, String collectionName) {
        this.client = client;
        this.db = client.getDatabase(dbName);
        this.reviewers = this.db.getCollection(collectionName);
    }

    @Override
    public void addReviewer(Review review, Reviewer reviewer) {
        // TODO
    }

    @Override
    public void removeReviewer(Review review, Reviewer reviewer) {
        // TODO
    }

    @Override
    public void persistReviewer(Reviewer reviewer) {
        Document dbReviewer = new Document();
        dbReviewer.put("name", reviewer.getName());
        dbReviewer.put("reviewCount", reviewer.getReviewCount());

        this.reviewers.insertOne(dbReviewer);
    }

    @Override
    public Reviewer getReviewer(UUID reviewerId) {
        return null;
    }

    @Override
    public List<Reviewer> getReviewers() {
        return null;
    }
}
