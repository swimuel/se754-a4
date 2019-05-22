import static org.junit.Assert.assertEquals;

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
        } catch (BadLoginException e) {
            // check that the username was not stored after the login attempt
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
        // sign out
        user.signOut();
        String username = user.getUsername();
        // Check username is no longer stored
        assertEquals(null, username);
    }
}