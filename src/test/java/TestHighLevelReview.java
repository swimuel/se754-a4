import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import user.Developer;
import user.Reviewer;

public class TestHighLevelReview {
    private DeveloperConnection developerConnection;
    private static Review review;
    private Developer reviewAuthor;
    private Reviewer reviewer;
    private NonDeveloperReviewHandler nonDeveloperReviewHandler;
    private Feedback feedbackForm;
    private String comments, codeChanges;

    @Before
    public void setup() {
        reviewAuthor = new Developer();
        reviewer = new Reviewer();
        review = new Review(reviewAuthor, Mockito.mock(InitialReviewResults.class));
        try {
            review.addReviewer(reviewer);
        } catch (UnauthorizedActionException e) {
            e.printStackTrace();
        }
        developerConnection = Mockito.mock(DeveloperConnection.class);
        nonDeveloperReviewHandler = new NonDeveloperReviewHandler(developerConnection);
    }

    @Test
    public void receiveResultsFromReviewerSide() {
        Mockito.doReturn(review).when(developerConnection).fetchReview();
        Review fetchedReview = nonDeveloperReviewHandler.receiveReview();
        assertEquals(review, fetchedReview);
    }

    @Test
    public void shouldStoreFeedbackOnceReviewSubmitted() {
        this.comments= "new comment";
        this.codeChanges = "new code change";
        try {
            this.nonDeveloperReviewHandler.performHighLevelReview(comments, codeChanges);
        } catch (UnauthorizedActionException e) {
            e.printStackTrace();
        }
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
    public void approveReview() throws UnauthorizedActionException {
        review.setDevEnvironment(true);
        assertFalse( review.getApprovalStatus());
        review.approveReview();
        assertTrue(review.getApprovalStatus());
    }
}