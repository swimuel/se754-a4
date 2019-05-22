import org.junit.Test;

public class GitHubClientTest {
    
    @Test
    public void shouldStoreUsernamePasswordAfterSignIn() throws BadLoginException {
        GitHubConnection mockedConnection = Mockito.mock(GitHubConnection.class);
        GitHubClient user = new GitHubClient(mockedConnection);
        user.signIn("username", "password");

        // check username has been stored
        assertEquals("username", user.getUsername());
    }
}