public interface NonDeveloperSide {
    public void sendDev(InitialReviewResults results, DeveloperSide developer);
    public void sendFeedback(Feedback feedback, DeveloperSide developer);
    public InitialReviewResults fetchResults();
    public Abstraction fetchAbstraction();

}