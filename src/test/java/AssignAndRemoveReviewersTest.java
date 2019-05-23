import common.InitialReviewResults;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import user.Developer;
import user.Reviewer;
import user.User;

import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Specifies the expected behaviour of adding and removing reviewers to a review under
 * certain conditions.
 */
public class AssignAndRemoveReviewersTest {

    Review review;
    Developer reviewAuthor;

    @Before
    public void setup() {
        reviewAuthor = new Developer();
        review = new Review(Mockito.mock(InitialReviewResults.class), reviewAuthor);
    }

    @Test
    public void shouldSuccessfullyAddSingleReviewer() throws UnauthorizedActionException {
        Reviewer nonDevReviewer = new Reviewer();
        review.addReviewer(nonDevReviewer);

        List<Reviewer> expectedReviewers = new ArrayList<>();
        expectedReviewers.add(nonDevReviewer);

        assertEquals(expectedReviewers, review.getReviewers());
    }

    @Test
    public void shouldSuccessfullyAddMultipleReviewers() throws UnauthorizedActionException {
        Reviewer reviewer1 = new Reviewer();
        Reviewer reviewer2 = new Reviewer();
        review.addReviewer(reviewer1);
        review.addReviewer(reviewer2);

        List<User> reviewers = new ArrayList();
        reviewers.add(reviewer1);
        reviewers.add(reviewer2);

        assertEquals(reviewers, review.getReviewers());
    }

    @Test(expected = UnauthorizedActionException.class)
    public void shouldNotAllowNonDeveloperToAddReviewer() throws UnauthorizedActionException {
        review.setDevEnvironment(false);
        Reviewer reviewer = new Reviewer();
        review.addReviewer(reviewer);
    }

    @Test
    public void shouldSuccessfullyDeleteExistingReviewer() throws UnauthorizedActionException {
        Reviewer reviewer = new Reviewer();
        review.addReviewer(reviewer);

        boolean success = review.removeReviewer(reviewer);

        assertTrue(success);
        assertEquals(new ArrayList<User>(), review.getReviewers());
    }

    @Test
    public void shouldFailToDeleteNonExistingReviewer() throws UnauthorizedActionException {
        Reviewer reviewer = new Reviewer();
        review.addReviewer(reviewer);

        Reviewer nonExistentReviewer = new Reviewer();
        boolean success = review.removeReviewer(nonExistentReviewer);

        assertFalse(success);
        assertEquals(1, review.getReviewers().size());
    }

    @Test(expected = UnauthorizedActionException.class)
    public void shouldNotAllowNonDeveloperToRemoveReviewer() throws UnauthorizedActionException {
        Reviewer reviewer = new Reviewer();
        review.addReviewer(reviewer);
        review.setDevEnvironment(false);

        try {
            review.removeReviewer(reviewer);
        } catch (UnauthorizedActionException e) {
            assertEquals(1, review.getReviewers().size());
            throw e;
        }
    }
}
