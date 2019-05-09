import java.io.IOException;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.RepositoryService;

public class GitHubIntegration {

    private String _username;
    private String _password;

    public GitHubIntegration() {
        _username = null;
        _password = null;
    }


    /**
     * Sets the fields for that user so that they can be used to perform other tasks
     * 
     * @param username username fo the user
     * @param password user password
     */
    public void signIn(String username, String password) {
        _username = username;
        _password = password;
    }


    /**
     * Removes the username and password so that any further user actions 
     * will fail
     */
    public void signOut() {
        _username = null;
        _password = null;
    }



    public static void main(String [] args) {

        // GitHubClient client = new GitHubClient();
        // client.setOAuth2Token("d7ee4fc6478de24e8985f747edf4b71f6df01377");

        // System.out.println(client.getUser());

        RepositoryService service = new RepositoryService();
        try {
            for (Repository repo : service.getRepositories("swimuel"))
                System.out.println(repo.getName());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * Gets the username 
     * @return String username
     */
    public String getUsername() {
        return _username;
    }

}