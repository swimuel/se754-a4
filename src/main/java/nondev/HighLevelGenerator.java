package nondev;

import common.Feedback;
import common.Review;

public class HighLevelGenerator {
    private DeveloperConnection developerConnection;
    private Review review;
    private Feedback feedback;

    public  HighLevelGenerator(DeveloperConnection developerConnection) {
        this.developerConnection = developerConnection;

    }

    public Review receiveReview() {
        this.review = developerConnection.fetchReview();
        return this.review;
    }

    public Feedback createFeedback(String comments, String codeChanges) {
        this.feedback = new Feedback(comments, codeChanges);
        return this.feedback;
    }

    public void submitFeedback(Feedback feedback) {
        this.developerConnection.sendFeedback(feedback);
    }

}
