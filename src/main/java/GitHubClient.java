public class GitHubClient {
    private String username;
    private String password;
    private GitHubConnection gitHubConnection;

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
     * Gets github username that is stored for the user
     * @return string username or null if no username is currently 
     * stored
     */
    public String getUsername() {
        return this.username;
    }
}