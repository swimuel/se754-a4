import static org.junit.Assert.*;
import org.junit.Test;

public class TestGitHubIntegration {

    @Test
    public void testSignIn() {
        GitHubIntegration user = new GitHubIntegration();
        user.signIn("username", "password");
        // check username has been stored
        assertEquals(user.getUsername(), "username");
    } 
    
    @Test
    public void testSignOut() {
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
    public void testPullRequestFetch() {
        fail();
    }
    
    @Test
    public void testMerge() {
        fail();
    }

    @Test
    public void testPostComment() {
        fail();
    }

}