import user.Reviewer;

public class NonDeveloperReviewHandler {
    private Review review;
    private InitialReviewResults initialReviewResults;
    private DeveloperSide developer;
    private NonDeveloperSide nonDeveloper;

    public NonDeveloperReviewHandler(InitialReviewResults initialReviewResults, Review review, NonDeveloperSide nonDeveloper, DeveloperSide developer) {
        this.initialReviewResults = initialReviewResults;
        this.review = review;
        this.nonDeveloper = nonDeveloper;
        this.developer = developer;
    }

    public void sendInitialResults() {
        developer.sendInitialReviewResults(this.initialReviewResults);
    }

    public InitialReviewResults getInitialReviewResults(){
        return nonDeveloper.getInitialReviewResults();
    }

    public Feedback performHighLevelReview(InitialReviewResults initialReviewResults, Feedback feedbackForm) {
        return feedbackForm.writeFeedback(initialReviewResults.getAbstractions());
    }

    public void submitFeedback(Feedback feedback) {
        nonDeveloper.submitFeedback(feedback);
        nonDeveloper.sendFeedback(feedback, developer);
    }

    public Feedback getNonDeveloperFeedback() {
        return developer.fetchFeedback();
    }

    public Review getReviewFromFeedback() {
        return this.review;
    }
}
