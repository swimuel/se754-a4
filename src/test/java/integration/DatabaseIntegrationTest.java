package integration;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import common.Review;
import common.UnauthorizedActionException;
import common.user.Reviewer;
import dev.Database;
import dev.DeveloperReviewHandler;
import dev.MongoDatabaseClient;
import org.bson.Document;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static com.mongodb.client.model.Filters.eq;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;

public class DatabaseIntegrationTest {
    private static final String DB_NAME = "se754-integration";
    private static final String REVIEWER_COLLECTION = "reviewers";
    private static final String CONN_STRING = "mongodb+srv://se754-g18:f7iyN%25ylRX3%40@cluster0-34zvj.mongodb.net/test?retryWrites=true&w=majority";

    private static MongoClient client = MongoClients.create(CONN_STRING);

    @BeforeClass
    public static void setup() {
        // add fixture data here
    }

    @AfterClass
    public static void cleanReviewers() {
        MongoDatabase db = client.getDatabase(DB_NAME);
        MongoCollection<Document> reviewers = db.getCollection(REVIEWER_COLLECTION);
        reviewers.deleteMany(new Document());
    }

    @Test
    public void newReviewerShouldBePersistedToDatabaseWithCountInitializedCorrectly() throws UnauthorizedActionException {
        MongoDatabase mongodb = client.getDatabase(DB_NAME);
        MongoCollection<Document> reviewers = mongodb.getCollection(REVIEWER_COLLECTION);

        Database db = new MongoDatabaseClient(client, DB_NAME, REVIEWER_COLLECTION);
        DeveloperReviewHandler rh = new DeveloperReviewHandler(Mockito.mock(Review.class), db);

        String reviewerName = "Testy McTestFace";
        Document persistedReviewer = reviewers.find(eq("name", reviewerName)).first();
        assertNull(persistedReviewer);

        Reviewer reviewer = new Reviewer(reviewerName);
        rh.addReviewer(reviewer);

        persistedReviewer = reviewers.find(eq("name", reviewerName)).first();
        assertEquals(reviewerName, persistedReviewer.get("name"));
        assertEquals(1, persistedReviewer.get("reviewCount"));
    }

    @Test
    public void existingReviewerShouldBePersistedAndCountShouldBeIncremented() {

    }
}
