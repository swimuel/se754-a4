import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import user.Developer;
import user.User;

public class SendAutomatedReviewTest {

    DeveloperSide developer;
    NonDeveloperSide nonDeveloper;
    Review review;
    Results results;
    Developer reviewAuthor;
    

    @Before
    public void setup() {
        nonDeveloper = Mockito.mock(NonDeveloperSide.class);
        developer = Mockito.mock(DeveloperSide.class);
        results = Mockito.mock(Results.class);
        reviewAuthor = new Developer();
        review = new Review(results, reviewAuthor, Mockito.mock(Database.class));
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