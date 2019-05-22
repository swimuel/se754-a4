public interface NonDeveloperSide {
    public void sendDev(Results results, DeveloperSide developer);
    public void sendFeedback(Feedback feedback, DeveloperSide developer);
    public void submitFeedback(Feedback feedback);
    public Results fetchResults();
    public InitialReviewResults getInitialReviewResults();
    public Abstraction fetchAbstraction();


}