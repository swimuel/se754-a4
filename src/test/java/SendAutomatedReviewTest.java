import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SendAutomatedReviewTest {

    Developer developer;
    NonDeveloper nonDeveloper;
    Review review;
    Results results;
    User reviewAuthor;
    Abstraction abstraction;
    

    @Before
    public void setup() {
        nonDeveloper = Mockito.mock(NonDeveloper.class);
        developer = Mockito.mock(Developer.class);
        results = Mockito.mock(Results.class);
        abstraction = Mockito.mock(Abstraction.class);
        reviewAuthor = new User(true);
        review = new Review(results, abstraction, reviewAuthor, Mockito.mock(Database.class));
        review.setReviewers(developer, nonDeveloper);
    }

    @Test
    public void sendAutomatedReview() {
        Mockito.doReturn(results).when(nonDeveloper).fetchResults();
        review.sendAutomatedResults(results);
        Results recievedReviewResults = nonDeveloper.fetchResults(); 
        assertEquals(results, recievedReviewResults);
    }

    @Test
    public void sendAbstraction() {
        Mockito.doReturn(abstraction).when(nonDeveloper).fetchAbstraction();
        review.sendAbstraction(abstraction);
        Abstraction recievedAbstraction = nonDeveloper.fetchAbstraction(); 
        assertEquals(abstraction, recievedAbstraction);
    }

    // @Test
    // public void performHighLevelReview() {
    //     Mockito.doReturn(abstraction).when(nonDeveloper).fetchResults();
    //     review.sendAutomatedResults(results);
    //     Results recievedReviewResults = nonDeveloper.fetchResults(); 
    //     assertEquals(results, recievedReviewResults);
    // }

    // @Test
    // public void sendHighLevelReview() {
    //     Mockito.doReturn(results).when(nonDeveloper).fetchResults();
    //     review.sendAutomatedResults(results);
    //     Results recievedReviewResults = nonDeveloper.fetchResults(); 
    //     assertEquals(results, recievedReviewResults);
    // }
}