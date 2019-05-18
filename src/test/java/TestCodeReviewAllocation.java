import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestCodeReviewAllocation {

    Review review;
    User reviewAuthor;
    Database db;

    @Before
    public void setup() {
        db = Mockito.mock(Database.class); // interface then mock it to control what is returned
        review = new Review(Mockito.mock(Results.class), Mockito.mock(Abstraction.class),reviewAuthor, db);
    }

    @Test
    public void shouldSuccessfullyAddSingleNonDeveloperReviewer()
            throws InvalidReviewerException, UnauthorizedActionException {
        User nonDevReviewer = new User(false);
        review.addReviewer(nonDevReviewer);

        List<User> expectedReviewers = new ArrayList<>();
        expectedReviewers.add(nonDevReviewer);

        assertEquals(expectedReviewers, review.getReviewers());
        Mockito.verify(db, Mockito.times(1)).saveReviewer(Mockito.eq(review), Mockito.any(User.class));
    }

    @Test
    public void shouldSuccessfullyAddMultipleNonDeveloperReviewers()
            throws InvalidReviewerException, UnauthorizedActionException {
        User nonDevReviewer1 = new User(false);
        User nonDevReviewer2 = new User(false);
        review.addReviewer(nonDevReviewer1);
        review.addReviewer(nonDevReviewer2);

        List<User> reviewers = new ArrayList();
        reviewers.add(nonDevReviewer1);
        reviewers.add(nonDevReviewer2);

        assertEquals(reviewers, review.getReviewers());
        Mockito.verify(db, Mockito.times(2)).saveReviewer(Mockito.eq(review), Mockito.any(User.class));
    }

    @Test(expected = InvalidReviewerException.class)
    public void shouldNotAllowDeveloperReviewer()
            throws InvalidReviewerException, UnauthorizedActionException {

        User devReviewer = new User(true);
        try {
            review.addReviewer(devReviewer);
        } catch (InvalidReviewerException e) {
            Mockito.verify(db, Mockito.never()).saveReviewer(Mockito.eq(review), Mockito.any(User.class));
            throw new InvalidReviewerException();
        }
    }

    @Test(expected = UnauthorizedActionException.class)
    public void shouldNotAllowNonDeveloperToAddReviewer()
            throws UnauthorizedActionException, InvalidReviewerException {
        review.setDevEnvironment(false);
        User nonDevReviewer = new User(false);
        try {
            review.addReviewer(nonDevReviewer);
        } catch (UnauthorizedActionException e) {
            Mockito.verify(db, Mockito.never()).saveReviewer(Mockito.eq(review), Mockito.any(User.class));
            throw new UnauthorizedActionException();
        }
    }
}
