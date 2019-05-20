import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TestHighLevelReview {

    Developer developer;
    NonDeveloper nonDeveloper;
    Review review, highLevelReview;
    Results results, nonDevResults;
    User reviewAuthor;
    Abstraction abstraction;

    @Before
    public void setup() {
        nonDeveloper = Mockito.mock(NonDeveloper.class);
        developer = Mockito.mock(Developer.class);
        results = Mockito.mock(Results.class);
        abstraction = Mockito.mock(Abstraction.class);
        nonDevResults = Mockito.mock(Results.class);
        reviewAuthor = new User(true);
        review = new Review(results, abstraction, reviewAuthor, Mockito.mock(Database.class));
        highLevelReview = new Review(nonDevResults, abstraction, reviewAuthor, Mockito.mock(Database.class));
        review.setReviewers(developer, nonDeveloper);
    }

    @Test
    public void sendAutomatedReview() {
        Mockito.doReturn(results).when(nonDeveloper).fetchResults();
        review.sendAutomatedResults(results);
        Results receivedReviewResults = nonDeveloper.fetchResults(); 
        assertEquals(results, receivedReviewResults);
    }

    @Test
    public void sendAbstraction() {
        Mockito.doReturn(abstraction).when(nonDeveloper).fetchAbstraction();
        review.sendAbstraction(abstraction);
        Abstraction receivedAbstraction = nonDeveloper.fetchAbstraction(); 
        assertEquals(abstraction, receivedAbstraction);
    }

    @Test
    public void performHighLevelReview() {        
        Results highLevelResults = highLevelReview.performReview(); 
        assertEquals(nonDevResults, highLevelResults);
    }

    @Test
    public void sendHighLevelReview() {
        Feedback feedback = Mockito.mock(Feedback.class);
        Mockito.doReturn(nonDevResults).when(developer).fetchResults();
        Mockito.doReturn(feedback).when(developer).fetchFeedback();
        review.sendHighLevelResults(nonDevResults);
        review.sendFeedback(feedback);
        Results receivedReviewResults = developer.fetchResults(); 
        Feedback receivedFeedback = developer.fetchFeedback(); 
        assertEquals(nonDevResults, receivedReviewResults);
        assertEquals(feedback, receivedFeedback);
    }

    @Test
    public void approveReview() {
        Mockito.doReturn(highLevelReview).when(nonDevResults).getReview();
        Review receivedReview = nonDevResults.getReview();
        assertEquals(false, receivedReview.getApprovalStatus());
        receivedReview.approveReview();
        assertEquals(true, receivedReview.getApprovalStatus());
    }
}