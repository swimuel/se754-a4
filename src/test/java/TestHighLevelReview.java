import static org.junit.Assert.*;

import com.google.googlejavaformat.java.FormatterException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import user.Developer;
import user.Reviewer;

public class TestHighLevelReview {

    private DeveloperSide developer;
    private NonDeveloperSide nonDeveloper;
    private Review review;
    private Developer reviewAuthor;
    private AutomatedCodeHandler ah;
    private InitialReviewResults initialReviewResults;
    private Reviewer reviewer;
    private NonDeveloperReviewHandler nonDeveloperReviewHandler;
    private Feedback feedbackForm;

    @Before
    public void setup() {
        ah = new AutomatedCodeHandler(Mockito.mock(Abstracter.class), new Linter(), Mockito.mock(Inspector.class));
        try {
            initialReviewResults = ah.performAutomatedReview(new SourceCode(""));
        } catch (FormatterException e) {
            e.printStackTrace();
        }
        reviewAuthor = new Developer();
        reviewer = new Reviewer();
        review = new Review(reviewAuthor);
        try {
            review.addReviewer(reviewer);
        } catch (UnauthorizedActionException e) {
            e.printStackTrace();
        }
        nonDeveloper = Mockito.mock(NonDeveloperSide.class);
        developer = Mockito.mock(DeveloperSide.class);
        nonDeveloperReviewHandler = new NonDeveloperReviewHandler(initialReviewResults, review, nonDeveloper, developer);
    }

    @Test
    public void sendInitialReviewResults(){
        nonDeveloperReviewHandler.sendInitialResults();
        Mockito.verify(developer, Mockito.times(1)).sendInitialReviewResults(initialReviewResults);
    }

    @Test
    public void receiveResultsFromReviewerSide() {
        Mockito.doReturn(initialReviewResults).when(nonDeveloper).getInitialReviewResults();
        InitialReviewResults fetchedResults = nonDeveloperReviewHandler.getInitialReviewResults();
        Mockito.verify(nonDeveloper, Mockito.times(1)).getInitialReviewResults();
        assertEquals(initialReviewResults, fetchedResults);
    }

    @Test
    public void performHighLevelReview() {
        feedbackForm = Mockito.mock(Feedback.class);
        nonDeveloperReviewHandler.performHighLevelReview(initialReviewResults,feedbackForm);
        Mockito.verify(feedbackForm, Mockito.times(1)).writeFeedback(initialReviewResults.getAbstractions());
    }

    @Test
    public void submitFeedback() {
        nonDeveloperReviewHandler.submitFeedback(feedbackForm);
        Mockito.verify(nonDeveloper, Mockito.times(1)).submitFeedback(feedbackForm);
        Mockito.verify(nonDeveloper, Mockito.times(1)).sendFeedback(feedbackForm,developer);

    }
    @Test
    public void receiveFeedbackFromDevSide() {
        Mockito.doReturn(feedbackForm).when(developer).fetchFeedback();
        Feedback nonDevFeedback = nonDeveloperReviewHandler.getNonDeveloperFeedback();
        Mockito.verify(developer, Mockito.times(1)).fetchFeedback();
        assertEquals(feedbackForm, nonDevFeedback);
    }

    @Test
    public void approveReview() {
        Review receivedReview = nonDeveloperReviewHandler.getReviewFromFeedback();
        assertFalse( receivedReview.getApprovalStatus());
        receivedReview.approveReview();
        assertTrue(receivedReview.getApprovalStatus());
    }
}