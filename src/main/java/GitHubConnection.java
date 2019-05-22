import java.util.HashMap;

public interface GitHubConnection {
    
    /**
     * This method is called from signIn method in the GitHubClient class. It checks
     * that the username and password are correct
     * 
     * @param username string github username
     * @param password string github password
     * @throws BadLoginException if the username and password are incorrect
     */
    public void authenticateUser(String username, String password) throws BadLoginException;
    
    /**
     * This method is called from the fetchSource method in the GitHubClient class.
     * It fetches the source from the repo named "owner/repo" at the path called path
     * and the branch called branch.
     * 
     * @param owner     string owner of the repository 
     * @param repo      string name of the repository 
     * @param path      string path to where to get the source from, if null will grab all source files
     * @param branch    string name of the branch to get the source from 
     * @param username  string username for github 
     * @param password  string password for github
     * 
     * @return returns a hashmap with keys as the fully qualified file name of the files and values conatining the 
     * base64 encoded data of the file, returns null on error
     */
    public HashMap<String, String> fetchSource(String owner, String repo, String path, String branch, String username, String password);

    /**
     * This method is called from the fetchSourceFromPullReques method in the GitHubClient class.
     * It gets the source code from pull request numbered pullRequestNo of the repo named "owner/repo" 
     * 
     * @param owner         string owner of the repository 
     * @param repo          string name of the repository 
     * @param pullRequestNo int number of the pull request
     * @param branch        string name of the branch to get it from
     * @param username      string username for github
     * @param password      string password for github
     * 
     * @return returns a hashmap with keys as the fully qualified file name of the files and values conatining the 
     *         base64 encoded data of the file, returns null on error
     */
    public HashMap<String, String> fetchSourceFromPullRequest(String owner, String repo, int pullRequestNo,
            String branch, String username, String password);

    /**
     * This method is called from the startListeningForPullRequests method in the GitHubClient class.
     * Starts listening for pull requests at the repo called "owner/repo". Creates a swing worker so this
     * method returns immediately. Once a pull request is detected the worker will retrive the source and
     * place it inside the user object. The swing worker will terminate once retieving source from one 
     * pull request.
     * 
     * @param user                     GitHubClient to store the source that is retireved
     * @param username                 string github username
     * @param password                 string github password
     * @param owner                    string owner of the repository 
     * @param repo                     string name of the repository
     * @param mostRecentPullRequestNo  int number that stores the most recent pull request retieved so that 
     *                                 it does not retireve it more than once
     * 
     * @return returns 0 immediately after starting the swing worker 
     */
    public int startListeningForPullRequests(GitHubClient user, String username, String password, String owner, String repo, int mostRecentPullRequestNo);
}