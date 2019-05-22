import com.google.googlejavaformat.java.FormatterException;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class ReviewGeneratorTest {
    @Test
    public void generateReviewPerformsAutomatedReviewAndInitialisesReviewHandler() throws FormatterException {
        SourceCode code = new SourceCode("int x = 9;");
        InitialReviewResults results = Mockito.mock(InitialReviewResults.class);

        AutomatedCodeHandler ah = Mockito.mock(AutomatedCodeHandler.class);
        Mockito.when(ah.performAutomatedReview(code)).thenReturn(results);

        ReviewGenerator rg = new ReviewGenerator(code);
        DeveloperReviewHandler reviewHandler = rg.generateReviewHandler();

        Mockito.verify(ah, Mockito.times(1)).performAutomatedReview(code);
        assertEquals(results, reviewHandler.getReviewResults());
    }
}
