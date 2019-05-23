import com.google.googlejavaformat.java.FormatterException;
import common.InitialReviewResults;
import common.SourceCode;
import inspection.AutomatedCodeHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import user.Developer;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class ReviewGeneratorTest {

    AutomatedCodeHandler ah;
    ReviewGenerator rg;
    UserAction ua;
    NonDeveloperConnection ndc;
    Review review;

    @Before
    public void setup() {
        this.ah = Mockito.mock(AutomatedCodeHandler.class);
        this.ua = Mockito.mock(UserAction.class);
        this.review = new Review(Mockito.mock(InitialReviewResults.class), new Developer());
        this.ndc = Mockito.mock(NonDeveloperConnection.class);
        this.rg = new ReviewGenerator(ah, Mockito.mock(Database.class), ua, ndc);
    }

    @Test
    public void generateReviewPerformsAutomatedReviewAndInitialisesReviewHandler() throws FormatterException {
        List<SourceCode> code = new ArrayList<SourceCode>();
        code.add(new SourceCode("filename", "int x = 9;"));
        Developer author = new Developer();
        InitialReviewResults results = Mockito.mock(InitialReviewResults.class);

        Mockito.when(ah.performAutomatedReview(code)).thenReturn(results);

        rg.generateReviewHandler(code, author);

        Mockito.verify(ah, Mockito.times(1)).performAutomatedReview(code);
        assertEquals(results, rg.getReviewHandler().getReviewResults());
    }

    @Test
    public void allocateReviewersCommunicatesWithUser() {

        DeveloperReviewHandler rh = new DeveloperReviewHandler(review, Mockito.mock(Database.class));

        rg.allocateReviewers(rh);

        Mockito.verify(ua, Mockito.times(1)).allocateReviewers(rh);
    }

    @Test
    public void sendReviewShouldSendReviewOverConnection() {
        rg.sendReview(review);
        Mockito.verify(ndc, Mockito.times(1)).sendReview(review);
    }

    // ensure ReviewGenerator can automate the entire flow
    @Test
    public void testReviewGenerationAndSendingAllInOne() throws FormatterException {
        List<SourceCode> code = new ArrayList<SourceCode>();
        code.add(new SourceCode("filename", "int x = 9;"));
        Developer author = new Developer();
        rg.generateAndSendReview(code, author);

        Mockito.verify(ah, Mockito.times(1)).performAutomatedReview(code);

        DeveloperReviewHandler rh = rg.getReviewHandler();

        Mockito.verify(ua, Mockito.times(1)).allocateReviewers(rh);
        Mockito.verify(ndc, Mockito.times(1)).sendReview(rh.getReview());
    }



}
