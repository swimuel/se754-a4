public interface DeveloperSide {
    public void sendNonDev(Results results, NonDeveloperSide nonDeveloper);
    public void sendAbstraction(Abstraction abstraction, NonDeveloperSide nonDeveloper);
    public Results fetchResults();
    public Feedback fetchFeedback();
    public void sendInitialReviewResults(InitialReviewResults initialReviewResults);
}