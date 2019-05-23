import java.util.HashMap;

import com.google.googlejavaformat.java.FormatterException;

import common.SourceCode;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import user.Developer;

public class GitHubIntegrationFlowTest {

    @Test
    public void shouldStartReviewProcessAfterFetchingSourceFromPullRequest() throws BadLoginException, FormatterException {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        user.signIn("username", "password");
        // create mock that returns a non empty HashMap, thus imitating finding at least
        // one source file
        Mockito.when(mockedConnection.fetchSourceFromPullRequest("owner", "repoName", 1, "testBranch", "username", "password")).thenReturn(new HashMap<String, String>() {
            private static final long serialVersionUID = 1L;
            {
                put("test", "passed");
            }
        });
        ReviewGenerator mockedGenerator = Mockito.mock(ReviewGenerator.class);
        Developer dev = new Developer();

        HashMap<String, String> files = user.fetchSourceFromPullRequest("owner", "repoName", 1, "testBranch", mockedGenerator, dev);
        Mockito.verify(mockedConnection).fetchSourceFromPullRequest("owner", "repoName", 1, "testBranch", "username", "password");

        Mockito.verify(mockedGenerator).generateAndSendReview((Matchers.anyListOf(SourceCode.class)), Mockito.any(Developer.class));
    }

    @Test
    public void shouldNotStartReviewProcessIfNoSourceCodeIsRetrieved() throws BadLoginException, FormatterException {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        user.signIn("username", "password");
        // create mock that returns a non empty HashMap, thus imitating finding at least
        // one source file
        Mockito.when(mockedConnection.fetchSourceFromPullRequest("owner", "repoName", 1, "testBranch", "username", "password")).thenReturn(null);
        ReviewGenerator mockedGenerator = Mockito.mock(ReviewGenerator.class);
        Developer dev = new Developer();

        HashMap<String, String> files = user.fetchSourceFromPullRequest("owner", "repoName", 1, "testBranch", mockedGenerator, dev);
        Mockito.verify(mockedConnection).fetchSourceFromPullRequest("owner", "repoName", 1, "testBranch", "username", "password");

        Mockito.verify(mockedGenerator, Mockito.never()).generateAndSendReview(Matchers.anyListOf(SourceCode.class), Mockito.any(Developer.class));
    }

}
