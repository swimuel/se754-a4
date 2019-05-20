public interface Database {
    void saveReviewer(Review review, User reviewer);
    void removeReviewer(Review review, User reviewer);
}
