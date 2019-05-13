import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

public class GitHubIntegrationTest {

    @Test
    public void shouldStoreUsernamePasswordAfterSignIn() {
        GitHubIntegration user = new GitHubIntegration();
        user.signIn("username", "password");
        // check username has been stored
        assertEquals(user.getUsername(), "username");
    } 
    
    @Test
    public void shouldRemoveUsernamePasswordAfterSignOut() {
        GitHubIntegration user = new GitHubIntegration();
        // need to be signed in to sign out 
        user.signIn("username", "password");
        // sign out 
        user.signOut();
        String username = user.getUsername();
        // Check username is no longer stored
        assertEquals(username, null);
    }

    @Test 
    public void shouldFetchSourceCodeOnPullRequest() {
        fail();
    }
    
    @Test
    public void shouldMergeCodeAfterCodeReviewApproved() {
        // set up required information
        String owner = "username";
        String repoName = "repo";
        int pullRequestNo = 1;
        String commitMessage = "merging";
        GitHubIntegration mockedGitHubUser = Mockito.mock(GitHubIntegration.class);
        mockedGitHubUser.signIn("username", "password");
        Mockito.when(mockedGitHubUser.mergeChanges(owner, repoName, pullRequestNo, commitMessage)).thenReturn(0);
        // try the merge
        int ret = mockedGitHubUser.mergeChanges(owner, repoName, pullRequestNo, commitMessage);
        // if return value is not zero then it failed in some way
        assertEquals(ret, 0);
    }

    @Test
    public void shouldPostCommentsToGitHubPullRequestDiscussionPageWhenReviewersMakeComments() {
        fail();
    }

}