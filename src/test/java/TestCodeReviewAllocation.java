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

public class TestCodeReviewAllocation {

    Review review;
    Developer reviewAuthor;
    Database db;

    @Before
    public void setup() {
        reviewAuthor = new Developer();
        db = Mockito.mock(Database.class); // interface then mock it to control what is returned
        review = new Review(Mockito.mock(Results.class), reviewAuthor, db);
    }

    @Test
    public void shouldSuccessfullyAddSingleReviewer()
            throws UnauthorizedActionException {
        Reviewer nonDevReviewer = new Reviewer();
        review.addReviewer(nonDevReviewer);

        List<Reviewer> expectedReviewers = new ArrayList<>();
        expectedReviewers.add(nonDevReviewer);

        assertEquals(expectedReviewers, review.getReviewers());
        Mockito.verify(db, Mockito.times(1)).saveReviewer(Mockito.eq(review), Mockito.any(Reviewer.class));
    }

    @Test
    public void shouldSuccessfullyAddMultipleReviewers()
            throws UnauthorizedActionException {
        Reviewer reviewer1 = new Reviewer();
        Reviewer reviewer2 = new Reviewer();
        review.addReviewer(reviewer1);
        review.addReviewer(reviewer2);

        List<User> reviewers = new ArrayList();
        reviewers.add(reviewer1);
        reviewers.add(reviewer2);

        assertEquals(reviewers, review.getReviewers());
        Mockito.verify(db, Mockito.times(1)).saveReviewer(review, reviewer1);
        Mockito.verify(db, Mockito.times(1)).saveReviewer(review, reviewer2);
    }

    @Test(expected = UnauthorizedActionException.class)
    public void shouldNotAllowNonDeveloperToAddReviewer() throws UnauthorizedActionException {
        review.setDevEnvironment(false);
        Reviewer reviewer = new Reviewer();
        try {
            review.addReviewer(reviewer);
        } catch (UnauthorizedActionException e) {
            Mockito.verify(db, Mockito.never()).saveReviewer(Mockito.eq(review), Mockito.any(Reviewer.class));
            throw e;
        }
    }

    @Test
    public void shouldSuccessfullyDeleteExistingReviewer() throws UnauthorizedActionException {
        Reviewer reviewer = new Reviewer();
        review.addReviewer(reviewer);

        boolean success = review.removeReviewer(reviewer);

        assertTrue(success);
        assertEquals(new ArrayList<User>(), review.getReviewers());
        Mockito.verify(db, Mockito.times(1)).removeReviewer(Mockito.eq(review), Mockito.any(Reviewer.class));
    }

    @Test
    public void shouldFailToDeleteNonExistingReviewer() throws UnauthorizedActionException {
        Reviewer reviewer = new Reviewer();
        review.addReviewer(reviewer);

        Reviewer nonExistentReviewer = new Reviewer();
        boolean success = review.removeReviewer(nonExistentReviewer);

        assertFalse(success);
        assertEquals(1, review.getReviewers().size());
        Mockito.verify(db, Mockito.never()).removeReviewer(Mockito.eq(review), Mockito.any(Reviewer.class));
    }

    @Test(expected = UnauthorizedActionException.class)
    public void shouldNotAllowNonDeveloperToRemoveReviewer() throws UnauthorizedActionException, InvalidReviewerException {
        Reviewer reviewer = new Reviewer();
        review.addReviewer(reviewer);
        review.setDevEnvironment(false);

        try {
            review.removeReviewer(reviewer);
        } catch (UnauthorizedActionException e) {
            assertEquals(1, review.getReviewers().size());
            Mockito.verify(db, Mockito.never()).removeReviewer(Mockito.eq(review), Mockito.any(Reviewer.class));
            throw e;
        }
    }
}
