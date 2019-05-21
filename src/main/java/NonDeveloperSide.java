public interface NonDeveloperSide {
    public void sendDev(Results results, DeveloperSide developer);
    public void sendFeedback(Feedback feedback, DeveloperSide developer);
    public Results fetchResults();
    public Abstraction fetchAbstraction();

}