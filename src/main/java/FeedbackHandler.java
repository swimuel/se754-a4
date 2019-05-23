public class FeedbackHandler {
    NonDeveloperConnection ndc;
    Feedback feedback;

    public FeedbackHandler(NonDeveloperConnection ndc) {
        this.ndc = ndc;
        this.feedback = null;
    }

    public void receiveFeedback() {
        this.feedback = ndc.fetchFeedback();
    }

    public Feedback getFeedback() {
        return this.feedback;
    }
}
