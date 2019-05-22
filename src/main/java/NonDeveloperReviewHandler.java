public class NonDeveloperReviewHandler {
    private static Review review;
    private DeveloperConnection developerConnection;


    public NonDeveloperReviewHandler(DeveloperConnection developerConnection) {
        this.developerConnection = developerConnection;
    }

    public Review receiveReview() {
        review = developerConnection.fetchReview();
        review.setDevEnvironment(false);
        return review;
    }

    public void performHighLevelReview(String comments, String codeChanges) throws UnauthorizedActionException {
        review.performReview(comments, codeChanges);
    }

    public void submitFeedback(Feedback feedback) {
        developerConnection.sendFeedback(feedback);
    }

}
