import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestCodeReviewAllocation {

    Review review;

    @Before
    public void setup() {
        review = new Review(Mockito.mock(Results.class));
    }

    @Test
    public void shouldSuccessfullyAddSingleNonDeveloperReviewer() throws UnauthorizedActionException {
        User nonDevReviewer = new User();
        review.addReviewer(nonDevReviewer);

        assertEquals(nonDevReviewer, review.getReviewers());
    }

    @Test
    public void shouldSuccessfullyAddMultipleNonDeveloperReviewers() throws UnauthorizedActionException{
        User nonDevReviewer1 = new User();
        User nonDevReviewer2 = new User();
        review.addReviewer(nonDevReviewer1);
        review.addReviewer(nonDevReviewer2);

        List<User> reviewers = new ArrayList();
        reviewers.add(nonDevReviewer1);
        reviewers.add(nonDevReviewer2);

        assertEquals(reviewers, review.getReviewers());
    }

    @Test(expected = UnauthorizedActionException.class)
    public void shouldNotAllowDeveloperReviewer() throws UnauthorizedActionException{
        User devReviewer = new User();
        review.addReviewer(devReviewer);
    }
}
