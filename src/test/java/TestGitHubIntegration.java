//import static org.junit.Assert.*;
//import org.junit.Test;
//
//public class TestGitHubIntegration {
//
//    @Test
//    public void signInTest() {
//        GitHubIntegration user = new GitHubIntegration();
//        user.signIn("username", "password");
//        // check username has been stored
//        assertEquals(user.getUsername(), "username");
//    }
//
//    @Test
//    public void signOutTest() {
//        GitHubIntegration user = new GitHubIntegration();
//        // need to be signed in to sign out
//        user.signIn("username", "password");
//        // sign out
//        user.signOut();
//        String username = user.getUsername();
//        // Check username is no longer stored
//        assertEquals(username, null);
//    }
//
//    @Test
//    public void pullRequestFetchTest() {
//        fail();
//    }
//
//    @Test
//    public void MergeTest() {
//        // set up required information
//        String user = "username";
//        String repoName = "repo";
//        int pullRequestNo = 1;
//        String commitMessage = "merging";
//        GitHubIntegration gitHubUser = new GitHubIntegration();
//        gitHubUser.signIn("username", "password");
//
//        // try the merge
//        int ret = gitHubUser.mergeChanges(user, repoName, pullRequestNo, commitMessage);
//        // if return value is not zero then it failed in some way
//        assertEquals(ret, 0);
//    }
//
//    @Test
//    public void postCommentTest() {
//        fail();
//    }
//
//}