package nondev;

import common.Feedback;
import common.Review;

public interface DeveloperConnection {
    Review fetchReview();
    void sendFeedback(Feedback feedback);
}