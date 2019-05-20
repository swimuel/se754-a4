import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import user.Developer;
import user.Reviewer;

import static org.junit.Assert.assertEquals;

/**
 * Specifies expected behaviour when a developer is interacting with a review.
 */
public class ReviewAllocationPersistenceTest {
    Database db;
    DeveloperReviewHandler rh;
    Review review;

    @Before
    public void setup() {
        db = Mockito.mock(Database.class);
        review = new Review(Mockito.mock(Results.class), new Developer());
        rh = new DeveloperReviewHandler(review, db);
    }

    @Test
    public void reviewCountShouldStartAtZero() {
        Reviewer reviewer = new Reviewer();
        assertEquals(0, reviewer.getReviewCount());
    }
    

    @Test
    public void reviewCountShouldBeUpdatedAndStoredInDatabaseWhenReviewerAllocated() throws UnauthorizedActionException {
        Reviewer reviewer = new Reviewer();
        int reviewCount = reviewer.getReviewCount();

        rh.addReviewer(reviewer);

        assertEquals(reviewCount + 1, reviewer.getReviewCount());
        Mockito.verify(db, Mockito.times(1)).persistReviewer(reviewer);
    }
}
