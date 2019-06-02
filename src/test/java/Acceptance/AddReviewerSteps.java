package Acceptance;

import common.Review;
import common.user.Developer;
import common.user.Reviewer;
import dev.Database;
import dev.MongoDatabaseClient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClients;

public class AddReviewerSteps {

    Developer dev;
    Database database;
    Review review;
    Reviewer rev;

    private static final String DB_NAME = "se754-integration";
    private static final String REVIEWER_COLLECTION = "reviewers";
    private static final String CONN_STRING = "mongodb+srv://se754-g18:f7iyN%25ylRX3%40@cluster0-34zvj.mongodb.net/test?retryWrites=true&w=majority";
    private static MongoClient client = MongoClients.create(CONN_STRING);

    @Given("there is a reviewer that is assigned to $reviewCount code reviews")
    public void givenThereIsOneReviewerAssigned(int reviewCount) {
        dev = new Developer();
        MongoDatabase mongodb = client.getDatabase(DB_NAME);
        database = new MongoDatabaseClient(client, DB_NAME, REVIEWER_COLLECTION);
        Reviewer rev = new Reviewer();
        review = new Review(null, null);
    }

    @When("a developer assigns $reviewNo code review to the reviewer")
    public void whenOneMoreReviewerIsAdded(int reviewNo){
        database.addReviewer(review, rev);
    }

    @Then("there are $databaseCount reviews assigned to the reviewer")
    public void thenThereAreTwoReviewersAssignedToTheReview(int databaseCount){
        List<Reviewer> reviewers = database.getReviewers();
        assertEquals(databaseCount, reviewers.get(0).getReviewCount());
    }

}