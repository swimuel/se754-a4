import com.google.googlejavaformat.java.FormatterException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import user.Developer;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

// ensures developers receive feedback and can approve the review
// which will then trigger interaction with github
public class FeedbackHandlerTest {
    GitHubClient ghc;
    NonDeveloperConnection ndc;
    FeedbackHandler fh;

    @Before
    public void setup() {
        ghc = Mockito.mock(GitHubClient.class);
        ndc = Mockito.mock(NonDeveloperConnection.class);
        fh = new FeedbackHandler(ndc);
    }

    @Test
    public void feedbackReceivedFromNonDeveloperToolAndStored()  {
        Feedback feedback = new Feedback("yes", "ok");
        Mockito.when(ndc.fetchFeedback()).thenReturn(feedback);

        fh.receiveFeedback();

        Mockito.verify(ndc, Mockito.times(1)).fetchFeedback();
        assertEquals(feedback, fh.getFeedback());
    }

    
}
