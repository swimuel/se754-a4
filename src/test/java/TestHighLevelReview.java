import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import com.google.googlejavaformat.java.FormatterException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import user.Developer;
import user.Reviewer;

public class TestHighLevelReview {
    private DeveloperConnection developerConnection;
    private NonDeveloperConnection nonDeveloperConnection;
    private Review review;
    private Developer reviewAuthor;
    private AutomatedCodeHandler ah;
    private InitialReviewResults initialReviewResults;
    private Reviewer reviewer;
    private NonDeveloperReviewHandler nonDeveloperReviewHandler;
    private Feedback feedbackForm;
    private String comments, codeChanges;

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
        review = new Review(reviewAuthor, initialReviewResults);
        try {
            review.addReviewer(reviewer);
        } catch (UnauthorizedActionException e) {
            e.printStackTrace();
        }

        nonDeveloperConnection = Mockito.mock(NonDeveloperConnection.class);
        developerConnection = Mockito.mock(DeveloperConnection.class);

        nonDeveloperReviewHandler = new NonDeveloperReviewHandler(review, nonDeveloperConnection, developerConnection);
    }

    @Test
    public void sendInitialReviewResults(){
        nonDeveloperReviewHandler.sendInitialResults();
        Mockito.verify(nonDeveloperConnection, Mockito.times(1)).sendInitialReviewResults(initialReviewResults);
    }

    @Test
    public void receiveResultsFromReviewerSide() {
        InitialReviewResults fetchedResults = review.getInitialReviewResults();
        assertEquals(initialReviewResults, fetchedResults);
    }

    @Test
    public void performHighLevelReview() {
        this.comments= "new comment";
        this.codeChanges = "new code change";
        nonDeveloperReviewHandler.performHighLevelReview(comments, codeChanges);
        this.feedbackForm = review.getFeedback();
        assertEquals(feedbackForm.getComments(), comments);
        assertEquals(feedbackForm.getCodeChageReq(), codeChanges);
    }

    @Test
    public void submitFeedback() {
        nonDeveloperReviewHandler.submitFeedback(feedbackForm);
        Mockito.verify(developerConnection, Mockito.times(1)).sendFeedback(feedbackForm);
    }

    @Test
    public void receiveFeedbackFromDevSide() {
        Feedback receivedFeedback = review.getFeedback();
        assertEquals(receivedFeedback, feedbackForm);
    }

    @Test
    public void approveReview() {
        assertFalse( review.getApprovalStatus());
        review.approveReview();
        assertTrue(review.getApprovalStatus());
    }
}