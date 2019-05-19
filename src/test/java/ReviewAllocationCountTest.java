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
    public void reviewCountShouldIncrementWhenReviewCompletedByUser() {
        User reviewer = new User(false);
        Review review = new Review(Mockito.mock(Results.class), reviewer, db);

        review.submitReview(reviewer);

        assertEquals(1, reviewer.getReviewCount());
    }
}
