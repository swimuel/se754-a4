package nondev;

import common.Review;

public interface DeveloperConnection {
    public Review fetchReview();
    public void sendFeedback(Feedback feedback);
}