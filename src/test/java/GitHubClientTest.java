import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.HashMap;

import com.google.googlejavaformat.java.FormatterException;

import org.junit.Test;
import org.mockito.Mockito;

public class GitHubClientTest {
    
    @Test
    public void shouldStoreUsernamePasswordAfterSignIn() throws BadLoginException {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        user.signIn("username", "password");

        // check username has been stored
        assertEquals("username", user.getUsername());
        Mockito.verify(mockedConnection).authenticateUser("username", "password");
    }

    @Test(expected = BadLoginException.class)
    public void shouldThrowExceptionWhenUsernameAndPasswordAreIncorrect() throws BadLoginException {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        Mockito.doThrow(new BadLoginException()).when(mockedConnection).authenticateUser("username", "badPassword");

        GitHubClient user = new GitHubClient(mockedConnection);

        user.signIn("username", "badPassword");
    }

    @Test
    public void shouldNotStoreUsernameIfThereIsABadLoginException() throws BadLoginException {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        try {
            Mockito.doThrow(new BadLoginException()).when(mockedConnection).authenticateUser("username", "badPassword");
            user.signIn("username", "badPassword");
            
        } catch (BadLoginException e) {
            // check that the username was not stored after the login attempt
            Mockito.verify(mockedConnection).authenticateUser("username", "badPassword");
            String username = user.getUsername();
            assertEquals(null, username);
        }
    }

    @Test
    public void shouldRemoveUsernamePasswordAfterSignOut() throws BadLoginException {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        // need to be signed in to sign out
        user.signIn("username", "password");
        Mockito.verify(mockedConnection).authenticateUser("username", "password");
        // sign out
        user.signOut();
        String username = user.getUsername();
        // Check username is no longer stored
        assertEquals(null, username);
    }

    @Test
    public void shouldFetchSourceCodeWhenCalled() throws BadLoginException {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        user.signIn("username", "password");
        // create mock that returns a non empty HashMap, thus imitating finding at least
        // one source file
        Mockito.when(mockedConnection.fetchSource("owner", "repoName", "src", null, "username", "password")).thenReturn(new HashMap<String, String>() {
            private static final long serialVersionUID = 1L;
            {
                put("test", "passed");
            }
        });

        HashMap<String, String> files = user.fetchSource("owner", "repoName", "src", null);
        Mockito.verify(mockedConnection).fetchSource("owner", "repoName", "src", null, "username", "password");

        assertNotEquals(null, files);
    }

    @Test
    public void shouldReturnNullFromFetchIfUserNotSignedIn() {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);

        HashMap<String, String> files = user.fetchSource("owner", "repoName", "src", null);

        //dont bother contacting github if user is not signed in
        Mockito.verify(mockedConnection, Mockito.never()).fetchSource("owner", "repoName", "src", null, "username", "password");

        // if the user has not signed in then the fetchSource call should return null
        assertEquals(null, files);
    }

    @Test
    public void shouldStoreSourceFilesInGitHubClientAfterFetching() throws BadLoginException {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        user.signIn("username", "password");
        // create mock that returns a non empty HashMap, thus imitating finding at least
        // one source file
        Mockito.when(mockedConnection.fetchSource("owner", "repoName", "src", null, "username", "password")).thenReturn(new HashMap<String, String>() {
            private static final long serialVersionUID = 1L;
            {
                put("test", "passed");
            }
        });

        HashMap<String, String> files = user.fetchSource("owner", "repoName", "src", null);
        //check mock was called
        Mockito.verify(mockedConnection).fetchSource("owner", "repoName", "src", null, "username", "password");
        HashMap<String, String> storedFiles = user.getSourceFiles();
        assertNotEquals(null, storedFiles);
    }

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

        HashMap<String, String> files = user.fetchSourceFromPullRequest("owner", "repoName", 1, "testBranch");
        Mockito.verify(mockedConnection).fetchSourceFromPullRequest("owner", "repoName", 1, "testBranch", "username", "password");

        assertNotEquals(null, files);
    }

    @Test
    public void shouldReturnNullFromPullRequestFetchIfUserNotSignedIn() {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        AutomatedCodeHandler mockedHandler = Mockito.mock(AutomatedCodeHandler.class);
        HashMap<String, String> files = user.fetchSourceFromPullRequest("owner", "repoName", 1, null, mockedHandler);

        // check did not make github request
        Mockito.verify(mockedConnection, Mockito.never()).fetchSourceFromPullRequest("owner", "repoName", 1, "testBranch", "username", "password");
        assertEquals(null, files);
    }

    @Test
    public void shouldStoreSourceFilesInGitHubClientAfterFetchingFromPullRequest() throws BadLoginException {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        user.signIn("username", "password");
        // create mock that returns a non empty HashMap, thus imitating finding at least one source file
        Mockito.when(mockedConnection.fetchSourceFromPullRequest("owner", "repoName", 1, null, "username", "password")).thenReturn(new HashMap<String, String>(){
            private static final long serialVersionUID = 1L;
            {
                put("test", "passed");
            }
        });

        HashMap<String, String> files = user.fetchSourceFromPullRequest("owner", "repoName", "src", null);
        HashMap<String, String> storedFiles = user.getSourceFiles();

        // check github connection was invoked 
        Mockito.verify(mockedConnection).fetchSourceFromPullRequest("owner", "repoName", 1, null, "username", "password");
        assertNotEquals(null, storedFiles);
    }

    @Test(timeout = 10000)
    public void shouldReturnImmediatelyWhenStartingListeningForPullRequests() throws BadLoginException {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        user.signIn("username", "password");
        user.startListeningForPullRequests("owner", "repo");
        Mockito.verify(mockedConnection).startListeningForPullRequests(user, "username", "password", "owner", "repo", 0);
    }

    @Test
    public void shouldReturnNegativeOneIfUserIsNotLoggedInAndTriesToListenForPullRequests() {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        int ret = user.startListeningForPullRequests("owner", "repo");
        Mockito.verify(mockedConnection, Mockito.never()).startListeningForPullRequests(user, "username", "password", "owner", "repo", 0);
        assertEquals(-1, ret);
    }
}