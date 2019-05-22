public class NonDeveloperReviewHandler {
    private Review review;
    private DeveloperConnection developerConnection;


    public NonDeveloperReviewHandler(DeveloperConnection developerConnection) {
        this.developerConnection = developerConnection;
    }

    public Review receiveReview() {
        review = developerConnection.fetchReview();
        review.setDevEnvironment(false);
        return review;
    }


    public void submitFeedback(Feedback feedback) {
        developerConnection.sendFeedback(feedback);
    }

}
