import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class ReviewAllocationCountTest {
    Database db;

    @Before
    public void setup() {
        db = Mockito.mock(Database.class);
    }

    @Test
    public void reviewCountShouldBeInitialisedToZero() {
        User user = new User(false);
        assertEquals(0, user.getReviewCount());
    }

    @Test
    public void reviewCountShouldIncrementAndDBShouldUpdateWhenReviewCompleted() {
        User reviewer = new User(false);
        int reviewCount = reviewer.getReviewCount();

        Review review = new Review(Mockito.mock(Results.class), reviewer, db);
        review.submitReview(reviewer);

        assertEquals(reviewCount + 1, reviewer.getReviewCount());
        Mockito.verify(db, Mockito.times(1)).persistReviewer(reviewer);
    }
}
