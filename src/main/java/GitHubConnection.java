import java.io.IOException;

import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.service.RepositoryService;

public class GitHubConnection {

    public GitHubConnection() {

    }

    public void authenticateUser(String username, String password) throws BadLoginException {
        RepositoryService repoService = new RepositoryService();
        repoService.getClient().setCredentials(username, password);

        // try to get a repository, if it fails then the username/password are incorrect
        try{
            RepositoryId repoId = new RepositoryId("eclipse", "egit-github");
            repoService.getRepository(repoId);
        }
        catch(IOException e) {
            throw new BadLoginException();
        }
    }

}