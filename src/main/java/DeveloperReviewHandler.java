import user.Reviewer;

/**
 * Defines the actions that developers can perform on a review.
 * Also manages persistence of the review in the database
 */
public class DeveloperReviewHandler {
    private Review review;
    private Database db;

    public DeveloperReviewHandler(Review review, Database db) {
        this.review = review;
        this.db = db;
        review.setDevEnvironment(true);
    }

    public void addReviewer(Reviewer reviewer) {

    }

    public boolean removeReviewer(Reviewer reviewer) {
        return true;
    }
}
