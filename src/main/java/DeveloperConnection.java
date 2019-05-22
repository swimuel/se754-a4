public interface DeveloperConnection {
    public Feedback fetchFeedback();
    public void sendFeedback(Feedback feedback);
}