import user.Reviewer;

public interface Database {
    void saveReviewer(Review review, Reviewer reviewer);
    void removeReviewer(Review review, Reviewer reviewer);
    void persistReviewer(Reviewer reviewer);
}
