import common.InitialReviewResults;
import common.UnauthorizedActionException;
import dev.Database;
import dev.DeveloperReviewHandler;
import common.Review;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import common.user.Developer;
import common.user.Reviewer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Specifies expected behaviour when a developer is interacting with a review.
 */
public class ReviewAllocationPersistenceTest {
    Database db;
    DeveloperReviewHandler rh;
    Review review;
    Reviewer reviewer;

    @Before
    public void setup() {
        db = Mockito.mock(Database.class);
        review = new Review(Mockito.mock(InitialReviewResults.class), new Developer());
        rh = new DeveloperReviewHandler(review, db);
        reviewer = new Reviewer();
    }

    @Test
    public void reviewerInformationIsStoredInDatabaseAndReviewWhenAllocated() throws UnauthorizedActionException {
        rh.addReviewer(reviewer);

        List<Reviewer> reviewers = new ArrayList<>();
        reviewers.add(reviewer);

        assertEquals(reviewers, review.getReviewers());
        Mockito.verify(db, Mockito.times(1)).addReviewer(review, reviewer);
    }

    @Test
    public void assignedReviewerRemovedFromDatabaseWhenUnassigned() throws UnauthorizedActionException {
        rh.addReviewer(reviewer);
        boolean success = rh.removeReviewer(reviewer);

        assertTrue(success);
        Mockito.verify(db, Mockito.times(1)).removeReviewer(review, reviewer);
    }

    @Test
    public void unassignedReviewerNotRemovedFromDatabaseWhenUnassigned() throws UnauthorizedActionException {
        boolean success = rh.removeReviewer(reviewer);

        assertFalse(success);
        Mockito.verify(db, Mockito.never()).removeReviewer(review, reviewer);
    }

    @Test
    public void reviewCountShouldStartAtZero() {
        assertEquals(0, reviewer.getReviewCount());
    }

    @Test
    public void reviewCountShouldBeUpdatedAndStoredInDatabaseWhenReviewerAllocated() throws UnauthorizedActionException {
        int reviewCount = reviewer.getReviewCount();

        rh.addReviewer(reviewer);

        assertEquals(reviewCount + 1, reviewer.getReviewCount());
        Mockito.verify(db, Mockito.times(1)).persistReviewer(reviewer);
    }
}
