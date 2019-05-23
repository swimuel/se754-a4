import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import user.Developer;

import static org.junit.Assert.assertEquals;

public class HighLevelGeneratorTest {
    private HighLevelGenerator highLevelGenerator;
    private DeveloperConnection developerConnection;
    private Review review;
    private Feedback feedback;
    String comments, codeChange;

    @Before
    public void setup() {
        comments = "new Comment";
        codeChange = "new Code Changes";
        this.developerConnection = Mockito.mock(DeveloperConnection.class);
        this.highLevelGenerator = new HighLevelGenerator(developerConnection);
        this.review = new Review(Mockito.mock(InitialReviewResults.class), new Developer());
    }

    @Test
    public void testReviewReceivedFromDevConnection() {
        Mockito.when(developerConnection.fetchReview()).thenReturn(review);
        Review receivedReview = this.highLevelGenerator.receiveReview();
        assertEquals(receivedReview, review);

    }

    @Test
    public void testCreateFeedbackStoresData() {
        this.feedback = this.highLevelGenerator.createFeedback(comments, codeChange);
        assertEquals(comments, this.feedback.getComments());
        assertEquals(codeChange, this.feedback.getCodeChageReq());
    }

    @Test
    public void testSubmitFeedbackSendsOverConnection() {
        this.highLevelGenerator.submitFeedback(feedback);
        Mockito.verify(developerConnection, Mockito.times(1)).sendFeedback(feedback);
    }

    @Test
    public void testWholeNonDevProcess() {
        Mockito.when(developerConnection.fetchReview()).thenReturn(review);
        Review receivedReview = this.highLevelGenerator.receiveReview();
        assertEquals(receivedReview, review);

        Feedback feedbackFlow = this.highLevelGenerator.createFeedback(comments, codeChange);
        assertEquals(comments, feedbackFlow.getComments());
        assertEquals(codeChange, feedbackFlow.getCodeChageReq());

        this.highLevelGenerator.submitFeedback(feedbackFlow);
        Mockito.verify(developerConnection, Mockito.times(1)).sendFeedback(feedbackFlow);
    }
}
