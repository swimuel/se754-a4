import java.io.IOException;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.service.PullRequestService;
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

        GitHubIntegration user = new GitHubIntegration();
        user.signIn("user", "password");

        RepositoryService service = new RepositoryService();
        service.getClient().setCredentials(user._username, user._password);
        try {
            for (Repository repo : service.getRepositories())
                System.out.println(repo.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Used to merge a pull request automatically.
     * If the user it not logged in it will fail.
     * 
     * @param owner username of the owner of the repo
     * @param repoName name of the repo iteslf 
     * @param pullRequestNo number of the pull request trying to be merged
     * @param commitMessage message to be included in the merge 
     * @return 0 on success, -1 if user is not logged in, -2 for merge error, and -3 if the request is not automatically mergeable.
     */
    public int mergeChanges(String owner, String repoName, int pullRequestNo, String commitMessage) {
        PullRequestService service = new PullRequestService();
        if(_username == null) {
            //user is not signed in 
            return -1;
        }
        service.getClient().setCredentials(_username, _password);
        RepositoryId repo = new RepositoryId(owner, repoName);
        try {
            if (service.getPullRequest(repo, pullRequestNo).isMergeable())
                service.merge(repo, pullRequestNo, commitMessage);
            else
                return -2; //repo is not automatically mergeable
        } catch (IOException e) {
            e.printStackTrace();
            return -3;
        }
        return 0;
    }


    /**
     * Gets the username 
     * @return String username
     */
    public String getUsername() {
        return _username;
    }


}