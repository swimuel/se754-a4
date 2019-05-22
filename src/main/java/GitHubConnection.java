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

    
}