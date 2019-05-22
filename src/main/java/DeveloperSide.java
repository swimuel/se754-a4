public interface DeveloperSide {
    public void sendNonDev(InitialReviewResults results, NonDeveloperSide nonDeveloper);
    public void sendAbstraction(Abstraction abstraction, NonDeveloperSide nonDeveloper);
    public InitialReviewResults fetchResults();
    public Feedback fetchFeedback();
}