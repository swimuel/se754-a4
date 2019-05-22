import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import user.Developer;
import user.Reviewer;

public class TestHighLevelReview {
    private DeveloperConnection developerConnection;
    private Review review;
    private Developer reviewAuthor;
    private Reviewer reviewer;
    private NonDeveloperReviewHandler nonDeveloperReviewHandler;
    private Feedback feedbackForm;
    private String comments, codeChanges;

    @Before
    public void setup() {
        reviewAuthor = new Developer();
        reviewer = new Reviewer();
        review = new Review(Mockito.mock(InitialReviewResults.class), reviewAuthor);
        try {
            review.addReviewer(reviewer);
        } catch (UnauthorizedActionException e) {
            e.printStackTrace();
        }
        developerConnection = Mockito.mock(DeveloperConnection.class);
        nonDeveloperReviewHandler = new NonDeveloperReviewHandler(developerConnection);
        this.comments= "new comment";
        this.codeChanges = "new code change";
    }

    @Test
    public void receiveResultsFromReviewerSide() {
        Mockito.doReturn(review).when(developerConnection).fetchReview();
        Review fetchedReview = nonDeveloperReviewHandler.receiveReview();
        assertEquals(review, fetchedReview);
    }

    @Test
    public void shouldStoreFeedbackOnceReviewSubmitted() {
        this.feedbackForm = nonDeveloperReviewHandler.performReview(comments, codeChanges);
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
        NonDeveloperConnection nonDeveloperConnection = Mockito.mock(NonDeveloperConnection.class);
        Mockito.doReturn(feedbackForm).when(nonDeveloperConnection).fetchFeedback();
        DeveloperReviewHandler developerReviewHandler = new DeveloperReviewHandler(Mockito.mock(Review.class),Mockito.mock(Database.class));
        Feedback receivedFeedback = developerReviewHandler.getFeedback(nonDeveloperConnection);
        Mockito.verify(nonDeveloperConnection, Mockito.times(1)).fetchFeedback();
        assertEquals(receivedFeedback, feedbackForm);
    }

    @Test
    public void approveDevReview() throws UnauthorizedActionException {
        review.setDevEnvironment(true);
        assertFalse( review.getApprovalStatus());
        review.approveReview();
        assertTrue(review.getApprovalStatus());
    }

    @Test(expected = UnauthorizedActionException.class)
    public void approveNonDevReview() throws UnauthorizedActionException {
        review.setDevEnvironment(false);
        assertFalse( review.getApprovalStatus());
        review.approveReview();
        assertTrue(review.getApprovalStatus());
    }
}