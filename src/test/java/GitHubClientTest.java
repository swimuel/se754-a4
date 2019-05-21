import static org.junit.Assert.*;

import java.util.HashMap;

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
    } 
    
    @Test
    public void shouldRemoveUsernamePasswordAfterSignOut() throws BadLoginException {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        // need to be signed in to sign out 
        user.signIn("username", "password");
        // sign out 
        user.signOut();
        String username = user.getUsername();
        // Check username is no longer stored
        assertEquals(null, username);
    }

    @Test(expected = BadLoginException.class)
    public void shouldThrowExceptionWhenUsernameAndPasswordAreIncorrect() throws BadLoginException {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        Mockito.doThrow(new BadLoginException()).when(mockedConnection).authenticateUser("username", "badPassword");

        GitHubClient user = new GitHubClient(mockedConnection);

        user.signIn("username", "badPassword");
    }

    @Test
    public void shouldNotStoreUsernameIfThereIsABadLoginException() {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        try {
            Mockito.doThrow(new BadLoginException()).when(mockedConnection).authenticateUser("username", "badPassword");        
            user.signIn("username", "badPassword");
        }
        catch(BadLoginException e) { 
            // check that the username was not stored after the login attempt
            String username = user.getUsername();
            assertEquals(null, username);
        }
    }

    @Test 
    public void shouldFetchSourceCodeWhenCalled() throws BadLoginException {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        user.signIn("username", "password");
        // create mock that returns a non empty HashMap, thus imitating finding at least one source file
        Mockito.when(mockedConnection.fetchSource("owner", "repoName", "src", null, "username", "password")).thenReturn(new HashMap<String, String>(){
            private static final long serialVersionUID = 1L;
            {
                put("test", "passed");
            }
        });

        HashMap<String,String> files = user.fetchSource("owner", "repoName", "src", null);

        assertNotEquals(null, files);
    }

    @Test
    public void shouldReturnNullFromFetchIfUserNotSignedIn() {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);

        HashMap<String, String> files = user.fetchSource("owner", "repoName", "src", null);

        // if the user has not signed in then the fetchSource call should return null
        assertEquals(null, files);
    }

    @Test
    public void shouldStoreSourceFilesInGitHubClientAfterFetching() throws BadLoginException {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        user.signIn("username", "password");
        // create mock that returns a non empty HashMap, thus imitating finding at least one source file
        Mockito.when(mockedConnection.fetchSource("owner", "repoName", "src", null, "username", "password")).thenReturn(new HashMap<String, String>(){
            private static final long serialVersionUID = 1L;
            {
                put("test", "passed");
            }
        });

        HashMap<String, String> files = user.fetchSource("owner", "repoName", "src", null);
        HashMap<String, String> storedFiles = user.getSourceFiles();
        assertNotEquals(null, storedFiles);
    }

    @Test
    public void shouldFetchSourceFromPullRequest() throws BadLoginException {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        user.signIn("username", "password");
        // create mock that returns a non empty HashMap, thus imitating finding at least one source file
        Mockito.when(mockedConnection.fetchSourceFromPullRequest("owner", "repoName", 1, "testBranch", "username", "password")).thenReturn(new HashMap<String, String>(){
            private static final long serialVersionUID = 1L;
            {
                put("test", "passed");
            }
        });

        HashMap<String,String> files = user.fetchSourceFromPullRequest("owner", "repoName", 1, "testBranch");

        assertNotEquals(null, files);
    }

    @Test
    public void shouldReturnNullFromPullRequestFetchIfUserNotSignedIn() {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        HashMap<String, String> files = user.fetchSourceFromPullRequest("owner", "repoName", 1, null);
        // fetchSourceFromPullRequest should return null if user is not signed in
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

        HashMap<String, String> files = user.fetchSource("owner", "repoName", "src", null);
        HashMap<String, String> storedFiles = user.getSourceFiles();
        assertNotEquals(null, storedFiles);
    }

    @Test(timeout = 10000)
    public void shouldReturnImmediatelyWhenStartingListeningForPullRequests() throws BadLoginException {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        user.signIn("username", "password");
        user.startListeningForPullRequests("owner", "repo");
    }

    @Test
    public void shouldReturnNegativeOneIfUserIsNotLoggedInAndTriesToListenForPullRequests() {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        int ret = user.startListeningForPullRequests("owner", "repo");
        assertEquals(-1, ret);
    }

    @Test
    public void shouldMergeCodeAfterCodeReviewApproved() throws BadLoginException {
        // set up required information
        String owner = "username";
        String repoName = "repo";
        int pullRequestNo = 1;
        String commitMessage = "merging";
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        user.signIn("username", "password");

        Mockito.when(mockedConnection.mergeChanges(owner, repoName, pullRequestNo, commitMessage, "username", "password")).thenReturn(0);
        // try the merge
        int ret = user.mergeChanges(owner, repoName, pullRequestNo, commitMessage);
        // if return value is not zero then it failed in some way
        assertEquals(ret, 0);
    }

    @Test
    public void shouldFailMergeIfUserIsNotSignedIn() {
        // set up required information
        String owner = "username";
        String repoName = "repo";
        int pullRequestNo = 1;
        String commitMessage = "merging";

        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        
        // try the merge
        int ret = user.mergeChanges(owner, repoName, pullRequestNo, commitMessage);
        // if user is not logged in should return -1
        assertEquals(-1, ret);
    }

    @Test
    public void shouldPostCommentsToGitHubPullRequestDiscussionPageWhenReviewersMakeComments()
            throws BadLoginException {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        user.signIn("username", "password");
        // make mock object imitate a successful return from the createPullRequestComment method
        Mockito.when(mockedConnection.createPullRequestComment("test comment", "owner", "repo", 1, "username", "password")).thenReturn(0);

        int ret = user.createPullRequestComment("test comment", "owner", "repo", 1);

        assertEquals(0, ret);
    }

    @Test
    public void shouldNotPostCommentIfUserIsNotSignedIn() {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);

        int ret = user.createPullRequestComment("test comment", "owner", "repo", 1);
        // should return -1 if the user is not signed in
        assertEquals(-1, ret);
    }


    @Test
    public void shouldPostCodeChangeRequestWhenReviewersSubmitReviewContainingTheRequest() throws BadLoginException {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        user.signIn("username", "password");
        Mockito.when(mockedConnection.createCodeChangeRequest("owner", "repo", 1, "Please change this", "username", "password")).thenReturn(0);

        int ret = user.createCodeChangeRequest("owner", "repo", 1, "Please change this");

        assertEquals(0, ret);
    }

    @Test
    public void shouldNotPostCodeChangeRequestWhenUserNotSignedIn() {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);

        int ret = user.createCodeChangeRequest("owner", "repo", 1, "Please change this");

        assertEquals(-1, ret);
    }

}