import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestCodeReviewAllocation {

    Review review;
    User reviewAuthor;

    @Before
    public void setup() {
        review = new Review(Mockito.mock(Results.class), reviewAuthor);
    }

    @Test
    public void shouldSuccessfullyAddSingleNonDeveloperReviewer()
            throws InvalidReviewerException, UnauthorizedActionException {
        User nonDevReviewer = new User(false);
        review.addReviewer(nonDevReviewer);

        List<User> expectedReviewers = new ArrayList<>();
        expectedReviewers.add(nonDevReviewer);

        assertEquals(expectedReviewers, review.getReviewers());
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
    }

    @Test(expected = InvalidReviewerException.class)
    public void shouldNotAllowDeveloperReviewer()
            throws InvalidReviewerException, UnauthorizedActionException {
        User devReviewer = new User(true);
        review.addReviewer(devReviewer);
    }

    @Test(expected = UnauthorizedActionException.class)
    public void shouldNotAllowNonDeveloperToAddReviewer()
            throws UnauthorizedActionException, InvalidReviewerException {
        review.setDevEnvironment(false);
        User nonDevReviewer = new User(false);
        review.addReviewer(nonDevReviewer);
    }
}
