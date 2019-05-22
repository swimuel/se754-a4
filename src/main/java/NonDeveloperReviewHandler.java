public class NonDeveloperReviewHandler {
    private Review review;
    private DeveloperConnection developerConnection;
    private NonDeveloperConnection nonDeveloperConnection;

    public NonDeveloperReviewHandler(Review review, NonDeveloperConnection nonDeveloperConnection, DeveloperConnection developerConnection) {
        this.review = review;
        this.nonDeveloperConnection = nonDeveloperConnection;
        this.developerConnection = developerConnection;
    }

    public void sendInitialResults() {
        nonDeveloperConnection.sendInitialReviewResults(review.getInitialReviewResults());
    }

    public InitialReviewResults getInitialReviewResults(){
        return review.getInitialReviewResults();
    }

    public void performHighLevelReview(String comments, String codeChanges) {
        review.performReview(comments, codeChanges);
    }

    public void submitFeedback(Feedback feedback) {
//        nonDeveloperConnection.submitFeedback(feedback);
        developerConnection.sendFeedback(feedback);
    }

    public Feedback getNonDeveloperFeedback() {
        return developerConnection.fetchFeedback();
    }

    public Review getReviewFromFeedback() {
        return this.review;
    }
}
