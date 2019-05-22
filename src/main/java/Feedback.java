import java.util.List;

public interface Feedback {
    public Feedback writeFeedback(List<Abstraction> initialReviewResults);
    public Review getReviewFromFeedback();
}