package nondev;

import common.Feedback;
import common.Review;

public class NonDeveloperReviewHandler {
    private Review review;
    private DeveloperConnection developerConnection;
    private Feedback feedback;


    public NonDeveloperReviewHandler(DeveloperConnection developerConnection) {
        this.developerConnection = developerConnection;
    }

    public Review receiveReview() {
        review = developerConnection.fetchReview();
        review.setDevEnvironment(false);
        return review;
    }

    public Feedback performReview(String comment, String codeChange) {
        feedback = new Feedback(comment, codeChange);
        return feedback;
    }

    public void submitFeedback(Feedback feedback) {
        developerConnection.sendFeedback(feedback);
    }
}