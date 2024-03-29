import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.HashMap;

import com.google.googlejavaformat.java.FormatterException;

import dev.ReviewGenerator;
import dev.github.BadLoginException;
import dev.github.GitHubClient;
import dev.github.GitHubConnection;
import dev.github.MergeException;
import org.junit.Test;
import org.mockito.Mockito;

import common.user.Developer;

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
        Mockito.when(mockedConnection.fetchSource("owner", "repoName", "src", null, "username", "password"))
                .thenReturn(new HashMap<String, String>() {
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

        // dont bother contacting github if common.user is not signed in
        Mockito.verify(mockedConnection, Mockito.never()).fetchSource("owner", "repoName", "src", null, "username",
                "password");

        // if the common.user has not signed in then the fetchSource call should return null
        assertEquals(null, files);
    }

    @Test
    public void shouldStoreSourceFilesInGitHubClientAfterFetching() throws BadLoginException {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        user.signIn("username", "password");
        // create mock that returns a non empty HashMap, thus imitating finding at least
        // one source file
        Mockito.when(mockedConnection.fetchSource("owner", "repoName", "src", null, "username", "password"))
                .thenReturn(new HashMap<String, String>() {
                    private static final long serialVersionUID = 1L;
                    {
                        put("test", "passed");
                    }
                });

        HashMap<String, String> files = user.fetchSource("owner", "repoName", "src", null);
        // check mock was called
        Mockito.verify(mockedConnection).fetchSource("owner", "repoName", "src", null, "username", "password");
        HashMap<String, String> storedFiles = user.getSourceFiles();
        assertNotEquals(null, storedFiles);
    }

    @Test
    public void shouldNotStoreSourceFilesInGitHubClientAfterFailingFetch() throws BadLoginException {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        user.signIn("username", "password");
        // create mock that returns null, thus imitating finding no source files
        Mockito.when(mockedConnection.fetchSource("owner", "repoName", "src", null, "username", "password"))
                .thenReturn(null);

        HashMap<String, String> files = user.fetchSource("owner", "repoName", "src", null);
        // check mock was called
        Mockito.verify(mockedConnection).fetchSource("owner", "repoName", "src", null, "username", "password");
        HashMap<String, String> storedFiles = user.getSourceFiles();
        assertEquals(new HashMap<String, String>(), storedFiles);
    }

    @Test
    public void shouldFetchSourceFromPullRequest() throws BadLoginException, FormatterException {
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

        assertNotEquals(null, files);
    }

    @Test
    public void shouldReturnNullFromPullRequestFetchIfUserNotSignedIn() throws FormatterException {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);

        ReviewGenerator mockedGenerator = Mockito.mock(ReviewGenerator.class);
        Developer dev = new Developer();
        HashMap<String, String> files = user.fetchSourceFromPullRequest("owner", "repoName", 1, null, mockedGenerator, dev);

        // check did not make github request
        Mockito.verify(mockedConnection, Mockito.never()).fetchSourceFromPullRequest("owner", "repoName", 1, "testBranch", "username", "password");
        assertEquals(null, files);
    }

    @Test
    public void shouldStoreSourceFilesInGitHubClientAfterFetchingFromPullRequest() throws BadLoginException, FormatterException {
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

        ReviewGenerator mockedGenerator = Mockito.mock(ReviewGenerator.class);
        Developer dev = new Developer();

        HashMap<String, String> files = user.fetchSourceFromPullRequest("owner", "repoName", 1, null, mockedGenerator, dev);
        HashMap<String, String> storedFiles = user.getSourceFiles();

        // check github connection was invoked 
        Mockito.verify(mockedConnection).fetchSourceFromPullRequest("owner", "repoName", 1, null, "username", "password");
        assertNotEquals(null, storedFiles);
    }

    @Test
    public void shouldNotStoreSourceFilesInGitHubClientAfterFailingFetchFromPullRequest() throws BadLoginException, FormatterException {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        user.signIn("username", "password");
        // create mock that returns null, thus imitating finding no source files
        Mockito.when(mockedConnection.fetchSourceFromPullRequest("owner", "repoName", 1, null, "username", "password")).thenReturn(null);
        ReviewGenerator mockedGenerator = Mockito.mock(ReviewGenerator.class);
        Developer dev = new Developer();

        HashMap<String, String> files = user.fetchSourceFromPullRequest("owner", "repoName", 1, null, mockedGenerator, dev);
        HashMap<String, String> storedFiles = user.getSourceFiles();

        // check github connection was invoked 
        Mockito.verify(mockedConnection).fetchSourceFromPullRequest("owner", "repoName", 1, null, "username", "password");
        assertEquals(new HashMap<String, String>(), storedFiles);
    }

    @Test(timeout = 10000)
    public void shouldReturnImmediatelyWhenStartingListeningForPullRequests() throws BadLoginException {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        user.signIn("username", "password");

        ReviewGenerator mockedGenerator = Mockito.mock(ReviewGenerator.class);
        user.startListeningForPullRequests("owner", "repo", mockedGenerator);

        Mockito.verify(mockedConnection).startListeningForPullRequests(user, "username", "password", "owner", "repo", 0, mockedGenerator);
    }

    @Test
    public void shouldReturnNegativeOneIfUserIsNotLoggedInAndTriesToListenForPullRequests() {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        ReviewGenerator mockedGenerator = Mockito.mock(ReviewGenerator.class);
        int ret = user.startListeningForPullRequests("owner", "repo", mockedGenerator);
        Mockito.verify(mockedConnection, Mockito.never()).startListeningForPullRequests(user, "username", "password", "owner", "repo", 0, mockedGenerator);
        assertEquals(-1, ret);
    }

    @Test
    public void shouldMergeCodeAfterCodeReviewApproved() throws BadLoginException, MergeException {
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
		
		Mockito.verify(mockedConnection).mergeChanges(owner, repoName, pullRequestNo, commitMessage, "username", "password");
        // if return value is not zero then it failed in some way
        assertEquals(ret, 0);
    }

    @Test
    public void shouldFailMergeIfUserIsNotSignedIn() throws MergeException {
        // set up required information
        String owner = "username";
        String repoName = "repo";
        int pullRequestNo = 1;
        String commitMessage = "merging";

        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        
        // try the merge
        int ret = user.mergeChanges(owner, repoName, pullRequestNo, commitMessage);
		
		// check github request not made if common.user not signed in
		Mockito.verify(mockedConnection, Mockito.never()).mergeChanges(owner, repoName, pullRequestNo, commitMessage, "username", "password");
        // if common.user is not logged in should return -1
        assertEquals(-1, ret);
    }

    @Test(expected = MergeException.class)
    public void shouldThrowNewMergeExceptionWhenMergeFails() throws BadLoginException, MergeException {
        // set up required information
        String owner = "username";
        String repoName = "repo";
        int pullRequestNo = 1;
        String commitMessage = "merging";
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        user.signIn("username", "password");

        Mockito.when(mockedConnection.mergeChanges(owner, repoName, pullRequestNo, commitMessage, "username", "password")).thenThrow(new MergeException());
        // try the merge
        try {
            int ret = user.mergeChanges(owner, repoName, pullRequestNo, commitMessage);
            
            Mockito.verify(mockedConnection).mergeChanges(owner, repoName, pullRequestNo, commitMessage, "username", "password");
        }
        catch(MergeException e) {
            throw new MergeException();
        }
    }

    @Test
    public void shouldSetMostRecentPullRequestNumberWhenCalled() {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);

        user.setMostRecentPullRequestNo(25);
        int pullNo = user.getMostRecentPullRequestNo();

        assertEquals(25, pullNo);
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
		
		Mockito.verify(mockedConnection).createPullRequestComment("test comment", "owner", "repo", 1, "username", "password");

        assertEquals(0, ret);
    }

    @Test
    public void shouldNotPostCommentIfUserIsNotSignedIn() {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);

        int ret = user.createPullRequestComment("test comment", "owner", "repo", 1);
		
		// check github request not made if common.user not signed in
		Mockito.verify(mockedConnection, Mockito.never()).createPullRequestComment("test comment", "owner", "repo", 1, "username", "password");
		
        // should return -1 if the common.user is not signed in
        assertEquals(-1, ret);
    }

    @Test
    public void shouldPostCodeChangeRequestWhenReviewersSubmitReviewContainingTheRequest() throws BadLoginException {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        user.signIn("username", "password");
        Mockito.when(mockedConnection.createCodeChangeRequest("owner", "repo", 1, "Please change this", "username", "password")).thenReturn(0);

        int ret = user.createCodeChangeRequest("owner", "repo", 1, "Please change this");
		
		Mockito.verify(mockedConnection).createCodeChangeRequest("owner", "repo", 1, "Please change this", "username", "password");

        assertEquals(0, ret);
    }

    @Test
    public void shouldNotPostCodeChangeRequestWhenUserNotSignedIn() {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);

        int ret = user.createCodeChangeRequest("owner", "repo", 1, "Please change this");
		
		//check github request not made if common.user not signed in
		Mockito.verify(mockedConnection, Mockito.never()).createCodeChangeRequest("owner", "repo", 1, "Please change this", "username", "password");

        assertEquals(-1, ret);
    }

    @Test
    public void shouldClearSourceFilesFromGitHubClientWhenCalled() throws BadLoginException {
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
        // clear the source files
        user.clearSourceFiles();
        HashMap<String, String> storedFiles = user.getSourceFiles();
        // check the files map is empty
        assert(storedFiles.size() == 0);
    }
}