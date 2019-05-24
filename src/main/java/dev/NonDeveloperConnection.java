package dev;

import common.Feedback;
import common.Review;

public interface NonDeveloperConnection {
    void sendReview(Review review);
    Feedback fetchFeedback();

}