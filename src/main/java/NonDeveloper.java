public interface NonDeveloper {
    public void sendDev(Results results, Developer developer);
    public void sendFeedback(Feedback feedback, Developer developer);
    public Results fetchResults();
    public Abstraction fetchAbstraction();
    

}