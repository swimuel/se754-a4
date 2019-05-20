import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import user.Developer;
import user.Reviewer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class RandomReviewAllocationTest {
    Database db;
    Review review;
    DeveloperReviewHandler rh;
    Random random;

    @Before
    public void setup() {
        db = Mockito.mock(Database.class);
        review = new Review(Mockito.mock(Results.class), Mockito.mock(Abstraction.class), new Developer());
        rh = new DeveloperReviewHandler(review, db);
        random = Mockito.mock(Random.class);
    }


    @Test
    public void shouldAllocateReviewerWithLowestReviewCountWhenRandomCloseTo1() {
        Reviewer lowestCount = new Reviewer(UUID.randomUUID(), 1);

        List<Reviewer> reviewers = new ArrayList<>();
        reviewers.add(lowestCount);
        reviewers.add(new Reviewer(UUID.randomUUID(), 10));
        reviewers.add(new Reviewer(UUID.randomUUID(), 20));
        reviewers.add(new Reviewer(UUID.randomUUID(), 2));

        Mockito.doReturn(reviewers).when(db).getReviewers();
        Mockito.doReturn(0.9999).when(random).nextDouble();

        rh.allocateRandomReviewer();

        Mockito.verify(db, Mockito.atLeastOnce()).getReviewers();
        Mockito.verify(db, Mockito.times(1)).addReviewer(review, lowestCount);
        assertTrue(review.getReviewers().contains(lowestCount));
    }

    @Test
    public void shouldAllocatedReviewerWithHighestReviewCountWhenRandomGivesZero() {

    }
}
