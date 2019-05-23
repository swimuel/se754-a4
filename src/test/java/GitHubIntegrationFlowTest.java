import java.util.HashMap;

import org.junit.Test;
import org.mockito.Mockito;

import user.Developer;

public class GitHubIntegrationFlowTest {

    @Test
    public void shouldFetchSourceFromPullRequest() throws BadLoginException {
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
        SourceCode sourcCode = new SourceCode("test", "passed");
        Mockito.verify(mockedGenerator).generateAndSendReview(files, dev);

    }

}