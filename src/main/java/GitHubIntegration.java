import java.io.IOException;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.*;
import org.eclipse.egit.github.core.service.RepositoryService;

public class GitHubIntegration {


    private String _username;
    private String _password;

    public GitHubIntegration() {

    }

    public GitHubIntegration(String username, String password) {
        _username = username;
        _password = password;
    }


    public static void main(String [] args) {


        // GitHubClient client = new GitHubClient();
        // client.setOAuth2Token("d7ee4fc6478de24e8985f747edf4b71f6df01377");

        // System.out.println(client.getUser());
        RepositoryService service = new RepositoryService();
        service.getClient().setOAuth2Token("d7ee4fc6478de24e8985f747edf4b71f6df01377");
        try {
            for (Repository repo : service.getRepositories())
                System.out.println(repo.getName());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }




}