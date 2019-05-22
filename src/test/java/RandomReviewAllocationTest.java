import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import user.Developer;
import user.Reviewer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RandomReviewAllocationTest {
    Database db;
    Review review;
    DeveloperReviewHandler rh;
    Random random;

    @Before
    public void setup() {
        db = Mockito.mock(Database.class);
        review = new Review(Mockito.mock(InitialReviewResults.class), new Developer());
        rh = new DeveloperReviewHandler(review, db);
        random = Mockito.mock(Random.class);
    }


    @Test
    public void shouldAllocateReviewerWithLowestReviewCountWhenRandomCloseTo1() throws UnauthorizedActionException {
        Reviewer lowestCount = new Reviewer(UUID.randomUUID(), 1);

        List<Reviewer> reviewers = new ArrayList<>();
        reviewers.add(lowestCount);
        reviewers.add(new Reviewer(UUID.randomUUID(), 10));
        reviewers.add(new Reviewer(UUID.randomUUID(), 20));
        reviewers.add(new Reviewer(UUID.randomUUID(), 2));

        Mockito.doReturn(reviewers).when(db).getReviewers();
        Mockito.doReturn(0.9999).when(random).nextDouble();

        Reviewer allocated = rh.allocateRandomReviewer(random);

        Mockito.verify(db, Mockito.atLeastOnce()).getReviewers();
        Mockito.verify(db, Mockito.times(1)).addReviewer(review, lowestCount);
        assertTrue(review.getReviewers().contains(lowestCount));
        assertEquals(allocated, lowestCount);
    }

    @Test
    public void shouldAllocateReviewerWithHighestReviewCountWhenRandomGivesZero() throws UnauthorizedActionException {
        Reviewer highestCount = new Reviewer(UUID.randomUUID(), 30);

        List<Reviewer> reviewers = new ArrayList<>();
        reviewers.add(highestCount);
        reviewers.add(new Reviewer(UUID.randomUUID(), 10));
        reviewers.add(new Reviewer(UUID.randomUUID(), 20));
        reviewers.add(new Reviewer(UUID.randomUUID(), 2));

        Mockito.doReturn(reviewers).when(db).getReviewers();
        Mockito.doReturn(0.0).when(random).nextDouble();

        Reviewer allocated = rh.allocateRandomReviewer(random);

        Mockito.verify(db, Mockito.atLeastOnce()).getReviewers();
        Mockito.verify(db, Mockito.times(1)).addReviewer(review, highestCount);
        assertTrue(review.getReviewers().contains(highestCount));
        assertEquals(allocated, highestCount);
    }

    @Test
    public void shouldAllocateReviewerWithMiddleReviewCountWhenRandomGivesAMiddleRange() throws UnauthorizedActionException {
        Reviewer middleCount = new Reviewer(UUID.randomUUID(), 10);

        List<Reviewer> reviewers = new ArrayList<>();
        reviewers.add(middleCount);
        reviewers.add(new Reviewer(UUID.randomUUID(), 15));
        reviewers.add(new Reviewer(UUID.randomUUID(), 2));
        reviewers.add(new Reviewer(UUID.randomUUID(), 2));

        // weights will be [1, 2, 3, 3]
        // so range for 15 should be 1/9 to 3/9
        Mockito.doReturn(reviewers).when(db).getReviewers();
        Mockito.doReturn(1.0 / 9.0).when(random).nextDouble();

        Reviewer allocated = rh.allocateRandomReviewer(random);

        Mockito.verify(db, Mockito.atLeastOnce()).getReviewers();
        Mockito.verify(db, Mockito.times(1)).addReviewer(review, middleCount);
        assertTrue(review.getReviewers().contains(middleCount));
        assertEquals(allocated, middleCount);


        // try the other end of the range
        Mockito.doReturn(2.99 / 9.0).when(random).nextDouble();
        allocated = rh.allocateRandomReviewer(random);
        assertEquals(allocated, middleCount);

        // and a middle value
        Mockito.doReturn(1.98 / 9.0).when(random).nextDouble();
        allocated = rh.allocateRandomReviewer(random);
        assertEquals(allocated, middleCount);
    }
}
