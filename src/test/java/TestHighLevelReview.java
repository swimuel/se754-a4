import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import user.Developer;

public class TestHighLevelReview {

    DeveloperSide developer;
    NonDeveloperSide nonDeveloper;
    Review review, highLevelReview;
    InitialReviewResults results, nonDevResults;
    // User reviewAuthor;
    Developer reviewAuthor;
    Abstraction abstraction;

    @Before
    public void setup() {
        nonDeveloper = Mockito.mock(NonDeveloperSide.class);
        developer = Mockito.mock(DeveloperSide.class);
        results = Mockito.mock(InitialReviewResults.class);
        abstraction = Mockito.mock(Abstraction.class);
        nonDevResults = Mockito.mock(InitialReviewResults.class);
        // reviewAuthor = new User(true);
        review = new Review(results, abstraction, reviewAuthor);
        highLevelReview = new Review(nonDevResults, abstraction, reviewAuthor);
        review.setReviewers(developer, nonDeveloper);
    }

    @Test
    public void sendAutomatedReview() {
        Mockito.doReturn(results).when(nonDeveloper).fetchResults();
        review.sendAutomatedResults(results);
        InitialReviewResults receivedReviewResults = nonDeveloper.fetchResults();
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
        InitialReviewResults highLevelResults = highLevelReview.performReview();
        assertEquals(nonDevResults, highLevelResults);
    }

    @Test
    public void sendHighLevelReview() {
        Feedback feedback = Mockito.mock(Feedback.class);
        Mockito.doReturn(nonDevResults).when(developer).fetchResults();
        Mockito.doReturn(feedback).when(developer).fetchFeedback();
        review.sendHighLevelResults(nonDevResults);
        review.sendFeedback(feedback);
        InitialReviewResults receivedReviewResults = developer.fetchResults();
        Feedback receivedFeedback = developer.fetchFeedback(); 
        assertEquals(nonDevResults, receivedReviewResults);
        assertEquals(feedback, receivedFeedback);
    }

//    @Test
//    public void approveReview() {
//        Mockito.doReturn(highLevelReview).when(nonDevResults).getReview();
//        Review receivedReview = nonDevResults.getReview();
//        assertEquals(false, receivedReview.getApprovalStatus());
//        receivedReview.approveReview();
//        assertEquals(true, receivedReview.getApprovalStatus());
//    }
}