import java.util.HashMap;

public class GitHubClient {
    private String username;
    private String password;
    private GitHubConnection gitHubConnection;
    private HashMap<String, String> sourceFiles;

    public GitHubClient(GitHubConnection gitHubConnection){
        this.gitHubConnection = gitHubConnection;
        this.username = null;
        this.password = null;
    }

    /**
     * Sets the fields for that user so that they can be used to perform other tasks
     * 
     * @param username username fo the user
     * @param password user password
     * @throws BadLoginException
     */
    public void signIn(String username, String password) throws BadLoginException {
        this.gitHubConnection.authenticateUser(username, password);

        // if no exception then the user is logged in
        this.username = username;
        this.password = password;
    }

    /**
     * Removes the username and password so that any further user actions will fail
     */
    public void signOut() {
        this.username = null;
        this.password = null;
    }

    /**
     * Fetches the source files at the repo called "owner/repo" and returns them in
     * a hashmap with the file names as the key and the values as the content which
     * is base64 encoded.
     * 
     * @param owner  string owner of the repository
     * @param repo   string name of the repository
     * @param path   string path to a specific part of the repo, if null it will get
     *               all source
     * @param branch the name of the branch to get the source from, if it is null it
     *               will get source from the master branch
     * 
     * @return hashmap with keys of file names and values of base64 encoded file
     *         contents or null if error
     */
    public HashMap<String, String> fetchSource(String owner, String repo, String path, String branch) {
        return null;

    }

    /**
     * Grabs the source code that was changed in the pull request with number
     * 'pullRequestNo' from the repository owned by 'owner' called 'repo' and on
     * branch 'branch'. The source is returned in a hashmap with the full file name
     * as the key and the value being the base64 encoded content of the file.
     * 
     * @param owner                 String owner of repository
     * @param repo                  String name of repository
     * @param pullRequestNo         int number representing the pull request
     * @param branch                String name of the branch to get the source from
     * @param automatedCodeHandler  used tp start the automatic code inspection process after the code has been fetched
     * 
     * @return HashMap with the keys as full names of the files and values as the
     *         base64 encoded content of the file.
     */
    public HashMap<String, String> fetchSourceFromPullRequest(String owner, String repo, int pullRequestNo, String branch) {

        return null;
    }

    /**
     * Creates a swing worker class that will listen for a pull request on a background thread.
     * When there is a pull requeset it will fetch the source code and put it in the this.sourceFiles 
     * field of this class.
     * 
     * @param owner String owner of repo to listen on 
     * @param repo  String name of the repository
     * 
     * @return 0 on success or -1 if the user is not logged in
     */
    public int startListeningForPullRequests(String owner, String repo) {
        return 0;
    }

    /**
     * Gets github username that is stored for the user
     * @return string username or null if no username is currently 
     * stored
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Get all of the currently stored source files as a 
     * HashMap
     * 
     * @return HashMap<String, String> of all stored source files
     */
    public HashMap<String, String> getSourceFiles() {
        return this.sourceFiles;
    }
}