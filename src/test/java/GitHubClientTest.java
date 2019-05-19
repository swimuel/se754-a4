import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;
import org.mockito.Mockito;

public class GitHubClientTest {

    @Test
    public void shouldStoreUsernamePasswordAfterSignIn() {
        GitHubClient user = new GitHubClient();
        user.signIn("username", "password");
        // check username has been stored
        assertEquals("username", user.getUsername());
    } 
    
    @Test
    public void shouldRemoveUsernamePasswordAfterSignOut() {
        GitHubClient user = new GitHubClient();
        // need to be signed in to sign out 
        user.signIn("username", "password");
        // sign out 
        user.signOut();
        String username = user.getUsername();
        // Check username is no longer stored
        assertEquals(null, username);
    }

    @Test(expected = BadLoginException.class)
    public void shouldThrowExceptionWhenUsernameAndPasswordAreIncorrect() {
        GitHubClient mockedUser = Mockito.mock(GitHubClient.class);

        Mockito.when(mockedUser.signIn("username", "badPassword")).thenThrow(new BadLoginException());

        mockedUser.signIn("username", "badPassword");

    }

    @Test 
    public void shouldFetchSourceCodeWhenCalled() {
        GitHubClient mockedUser = Mockito.mock(GitHubClient.class);
        mockedUser.signIn("username", "password");
        // create mock that returns a non empty HashMap, thus imitating finding at least one source file
        Mockito.when(mockedUser.fetchSource("Owner", "repoName", "src", null)).thenReturn(new HashMap<String, String>(){
            private static final long serialVersionUID = 1L;
            {
                put("test", "passed");
            }
        });

        HashMap<String,String> files = mockedUser.fetchSource("owner", "repoName", "src", null);

        assertNotEquals(null, files);
    }

    @Test
    public void shouldReturnNullFromFetchIfUserNotSignedIn() {
        GitHubClient user = new GitHubClient();
        HashMap<String, String> files = user.fetchSource("owner", "repoName", "src", null);

        // if the user has not signed in then the fetchSource call should return null
        assertEquals(null, files);
    }

    @Test
    public void shouldFetchSourceFromPullRequest(){
        GitHubClient mockedUser = Mockito.mock(GitHubClient.class);
        mockedUser.signIn("username", "password");
        // create mock that returns a non empty HashMap, thus imitating finding at least one source file
        Mockito.when(mockedUser.fetchSource("Owner", "repoName", "src", null)).thenReturn(new HashMap<String, String>(){
            private static final long serialVersionUID = 1L;
            {
                put("test", "passed");
            }
        });

        HashMap<String, String> files = mockedUser.fetchSource("brevellnash", "Softeng754Assignment3", null, "testBranch");
        assertNotEquals(null, files);
    }

    @Test
    public void shouldReturnNullFromPullRequestFetchIfUserNotSignedIn() {
        GitHubClient user = new GitHubClient();
        HashMap<String, String> files = user.fetchSourceFromPullRequest("owner", "repoName", 1, null);
        // fetchSourceFromPullRequest should return null if user is not signed in
        assertEquals(null, files);
    }

    @Test(timeout = 10000)
    public void shouldReturnImmediatelyWhenStartingListeningForPullRequests() {
        GitHubClient user = new GitHubClient("username", "password");
        user.startListeningForPullRequests("owner", "repo");
    }

    @Test
    public void shouldReturnNegativeOneIfUserIsNotLoggedInAndTriesToListenForPullRequests() {
        GitHubClient user = new GitHubClient();
        int ret = user.startListeningForPullRequests("owner", "repo");
        assertEquals(-1, ret);
    }

    @Test
    public void shouldMergeCodeAfterCodeReviewApproved() {
        // set up required information
        String owner = "username";
        String repoName = "repo";
        int pullRequestNo = 1;
        String commitMessage = "merging";
        GitHubClient mockedGitHubUser = Mockito.mock(GitHubClient.class);
        mockedGitHubUser.signIn("username", "password");

        Mockito.when(mockedGitHubUser.mergeChanges(owner, repoName, pullRequestNo, commitMessage)).thenReturn(0);
        // try the merge
        int ret = mockedGitHubUser.mergeChanges(owner, repoName, pullRequestNo, commitMessage);
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
        GitHubClient mockedGitHubUser = Mockito.mock(GitHubClient.class);
        
        Mockito.when(mockedGitHubUser.mergeChanges(owner, repoName, pullRequestNo, commitMessage)).thenReturn(-1);
        // try the merge
        int ret = mockedGitHubUser.mergeChanges(owner, repoName, pullRequestNo, commitMessage);
        // if user is not logged in should return -1
        assertEquals(-1, ret);
    }

    @Test
    public void shouldPostCommentsToGitHubPullRequestDiscussionPageWhenReviewersMakeComments() {
        GitHubClient mockedUser = Mockito.mock(GitHubClient.class);
        mockedUser.signIn("username", "password");
        // make mock object imitate a successful return from the createPullRequestComment method
        Mockito.when(mockedUser.createPullRequestComment("test comment", "owner", "repo", 1)).thenReturn(0);

        int ret = mockedUser.createPullRequestComment("test comment", "owner", "repo", 1);

        assertEquals(0, ret);
    }

    @Test
    public void shouldNotPostCommentIfUserIsNotSignedIn() {
        GitHubClient mockedUser = Mockito.mock(GitHubClient.class);
        // make mock object imitate an unsuccessful return from the createPullRequestComment method
        Mockito.when(mockedUser.createPullRequestComment("test comment", "owner", "repo", 1)).thenReturn(-1);

        int ret = mockedUser.createPullRequestComment("test comment", "owner", "repo", 1);
        // should return -1 if the user is not signed in
        assertEquals(-1, ret);
    }


    @Test
    public void shouldPostCodeChangeRequestWhenReviewersSubmitReviewContainingTheRequest() {
        GitHubClient mockedUser = Mockito.mock(GitHubClient.class);
        mockedUser.signIn("username", "password");
        Mockito.when(mockedUser.createCodeChangeRequest("owner", "repo", 1, "Please change this")).thenReturn(0);

        int ret = mockedUser.createCodeChangeRequest("owner", "repo", 1, "Please change this");

        assertEquals(0, ret);
    }

    @Test
    public void shouldNotPostCodeChangeRequestWhenUserNotSignedIn() {
        GitHubClient user = new GitHubClient();

        int ret = user.createCodeChangeRequest("owner", "repo", 1, "Please change this");

        assertEquals(-1, ret);
    }

}