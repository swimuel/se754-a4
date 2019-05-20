import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import user.Developer;
import user.Reviewer;

import static org.junit.Assert.assertEquals;

public class ReviewAllocationCountTest {
    Database db;

    @Before
    public void setup() {
        db = Mockito.mock(Database.class);
    }

    @Test
    public void reviewCountShouldBeInitialisedToZero() {
        Reviewer reviewer = new Reviewer();
        assertEquals(0, reviewer.getReviewCount());
    }

    @Test
    public void reviewCountShouldIncrementAndDBShouldUpdateWhenReviewCompleted() {
        Reviewer reviewer = new Reviewer();
        int reviewCount = reviewer.getReviewCount();

        Review review = new Review(Mockito.mock(Results.class), new Developer(), db);
        review.submitReview(reviewer);

        assertEquals(reviewCount + 1, reviewer.getReviewCount());
        Mockito.verify(db, Mockito.times(1)).persistReviewer(reviewer);
    }
}
