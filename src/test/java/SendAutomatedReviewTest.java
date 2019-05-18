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
    

    @Before
    public void setup() {
        nonDeveloper = Mockito.mock(NonDeveloper.class);
        developer = Mockito.mock(Developer.class);
        results = Mockito.mock(Results.class);
        reviewAuthor = new User(true);
        review = new Review(results, reviewAuthor);
        review.setReviewers(developer, nonDeveloper);
    }

    @Test
    public void sendAutomatedReview() {
        Mockito.doReturn(results).when(nonDeveloper).fetchResults();
        review.sendAutomatedResults(results);
        Results recievedReviewResults = nonDeveloper.fetchResults(); 
        assertEquals(results, recievedReviewResults);
    }
}