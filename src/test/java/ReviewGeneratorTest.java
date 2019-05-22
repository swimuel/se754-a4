import com.google.googlejavaformat.java.FormatterException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import user.Developer;

import static org.junit.Assert.assertEquals;

public class ReviewGeneratorTest {

    AutomatedCodeHandler ah;
    ReviewGenerator rg;
    UserAction ua;

    @Before
    public void setup() {
        this.ah = Mockito.mock(AutomatedCodeHandler.class);
        this.ua = Mockito.mock(UserAction.class);
        this.rg = new ReviewGenerator(ah, Mockito.mock(Database.class), ua);
    }

    @Test
    public void generateReviewPerformsAutomatedReviewAndInitialisesReviewHandler() throws FormatterException {
        SourceCode code = new SourceCode("int x = 9;");
        Developer author = new Developer();
        InitialReviewResults results = Mockito.mock(InitialReviewResults.class);

        Mockito.when(ah.performAutomatedReview(code)).thenReturn(results);

        DeveloperReviewHandler reviewHandler = rg.generateReviewHandler(code, author);

        Mockito.verify(ah, Mockito.times(1)).performAutomatedReview(code);
        assertEquals(results, reviewHandler.getReviewResults());
    }

    @Test
    public void allocateReviewersCommunicatesWithUserInterface() {
        Review review = new Review(Mockito.mock(InitialReviewResults.class), new Developer());
        DeveloperReviewHandler rh = new DeveloperReviewHandler(review, Mockito.mock(Database.class));

        rg.allocateReviewers(rh);

        Mockito.verify(ua, Mockito.times(1)).allocateReviewers(rh);
    }
}
