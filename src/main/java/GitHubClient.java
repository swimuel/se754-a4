public class GitHubClient {
    private String username;
    private String password;
    private GitHubConnection gitHubConnection;

    public GitHubClient(GitHubConnection gitHubConnection){
        this.gitHubConnection = gitHubConnection;
        this.username = null;
        this.password = null;
    }


    public void signIn(String username, String password) {
        
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