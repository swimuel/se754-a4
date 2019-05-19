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
    public void testCountInitialisedToZero() {
        User user = new User(false);
        assertEquals(0, user.getReviewCount());
    }
}
