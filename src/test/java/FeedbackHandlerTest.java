import com.google.googlejavaformat.java.FormatterException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import user.Developer;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

// ensures developers receive feedback and can approve the review
// which will then trigger interaction with github
public class FeedbackHandlerTest {
    GitHubClient ghc;
    NonDeveloperConnection ndc;
    FeedbackHandler fh;
    UserAction ua;

    @Before
    public void setup() {
        ghc = Mockito.mock(GitHubClient.class);
        ndc = Mockito.mock(NonDeveloperConnection.class);
        ua = Mockito.mock(UserAction.class);
        fh = new FeedbackHandler(ndc, ua, ghc);
    }

    @Test
    public void feedbackReceivedFromNonDeveloperToolAndStored()  {
        Feedback feedback = new Feedback("yes", "ok");
        Mockito.when(ndc.fetchFeedback()).thenReturn(feedback);

        fh.receiveFeedback();

        Mockito.verify(ndc, Mockito.times(1)).fetchFeedback();
        assertEquals(feedback, fh.getFeedback());
    }

    @Test
    public void getsFinalApprovalFromUserAndStoresResult() {
        Feedback feedback = new Feedback("testy", "test");
        fh.setFeedback(feedback);
        Mockito.when(ua.getDeveloperApproval(feedback)).thenReturn(true);

        fh.getDeveloperApproval();

        Mockito.verify(ua, Mockito.times(1)).getDeveloperApproval(feedback);
        assertTrue(fh.getApprovalStatus());
    }

    @Test
    public void shouldPostCommentsAndMergeWhenDeveloperApproves() throws MergeException {
        fh.setApprovalStatus(true);
        fh.setFeedback(new Feedback("hello", "there"));

        fh.finishReview();

        Mockito.verify(ghc, Mockito.times(1)).mergeChanges(anyString(), anyString(), anyInt(), anyString());
        Mockito.verify(ghc, Mockito.times(1)).createPullRequestComment(anyString(), anyString(), anyString(), anyInt());
    }

    @Test
    public void shouldPostCommentsAndNotMergeWhenDeveloperRejects() throws MergeException {
        fh.setApprovalStatus(false);
        fh.setFeedback(new Feedback("hello", "there"));

        fh.finishReview();

        Mockito.verify(ghc, Mockito.never()).mergeChanges(anyString(), anyString(), anyInt(), anyString());
        Mockito.verify(ghc, Mockito.times(1)).createPullRequestComment(anyString(), anyString(), anyString(), anyInt());
    }
}
