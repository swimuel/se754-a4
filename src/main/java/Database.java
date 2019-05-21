import user.Reviewer;

import java.util.List;
import java.util.UUID;

public interface Database {
    void addReviewer(Review review, Reviewer reviewer);
    void removeReviewer(Review review, Reviewer reviewer);
    void persistReviewer(Reviewer reviewer);
    Reviewer getReviewer(UUID reviewerId);
    List<Reviewer> getReviewers();
}
