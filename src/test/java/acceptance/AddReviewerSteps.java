package acceptance;

import com.mongodb.client.MongoCollection;
import common.Review;
import common.UnauthorizedActionException;
import common.user.Developer;
import common.user.Reviewer;
import dev.Database;
import dev.DeveloperReviewHandler;
import dev.MongoDatabaseClient;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClients;
import org.mockito.Mockito;

public class AddReviewerSteps {

    Database database;
    MongoCollection<Document> reviewers;
    Reviewer rev;

    private static final String DB_NAME = "se754-integration";
    private static final String REVIEWER_COLLECTION = "reviewers";
    private static final String CONN_STRING = "mongodb+srv://se754-g18:f7iyN%25ylRX3%40@cluster0-34zvj.mongodb.net/test?retryWrites=true&w=majority";
    private static MongoClient client = MongoClients.create(CONN_STRING);

    @Given("there is a new reviewer who has completed $reviewCount reviews")
    public void givenThereIsANewReviewer(int reviewCount) {
        rev = new Reviewer("Harry Potter", reviewCount);
    }

    @When("a developer assigns a code review to the reviewer")
    public void whenAReviewerIsAssigned() throws UnauthorizedActionException {
        database = new MongoDatabaseClient(client, DB_NAME, REVIEWER_COLLECTION);
        reviewers = client.getDatabase(DB_NAME).getCollection(REVIEWER_COLLECTION);
        DeveloperReviewHandler rh = new DeveloperReviewHandler(Mockito.mock(Review.class), database);
        rh.addReviewer(rev);
    }

    @Then("the new review count stored in the database for that reviewer is $databaseCount")
    public void thenThereAreTwoReviewersAssignedToTheReview(int databaseCount){
        Document dbReviewer = reviewers.find(eq("name", "Harry Potter")).first();
        assertEquals(databaseCount, dbReviewer.get("reviewCount"));
    }

}