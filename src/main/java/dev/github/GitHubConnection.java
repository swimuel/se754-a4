package dev.github;

import dev.ReviewGenerator;

import java.util.HashMap;

public interface GitHubConnection {
    
    /**
     * This method is called from signIn method in the dev.github.GitHubClient class. It checks
     * that the username and password are correct
     * 
     * @param username string github username
     * @param password string github password
     * @throws BadLoginException if the username and password are incorrect
     */
    void authenticateUser(String username, String password) throws BadLoginException;
    
    /**
     * This method is called from the fetchSource method in the dev.github.GitHubClient class.
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
    HashMap<String, String> fetchSource(String owner, String repo, String path, String branch, String username, String password);

    /**
     * This method is called from the fetchSourceFromPullReques method in the dev.github.GitHubClient class.
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
    HashMap<String, String> fetchSourceFromPullRequest(String owner, String repo, int pullRequestNo,
            String branch, String username, String password);

    /**
     * This method is called from the startListeningForPullRequests method in the dev.github.GitHubClient class.
     * Starts listening for pull requests at the repo called "owner/repo". Creates a swing worker so this
     * method returns immediately. Once a pull request is detected the worker will retrive the source and
     * place it inside the common.user object. The swing worker will terminate once retieving source from one
     * pull request.
     * 
     * @param user                     dev.github.GitHubClient to store the source that is retireved
     * @param username                 string github username
     * @param password                 string github password
     * @param owner                    string owner of the repository 
     * @param repo                     string name of the repository
     * @param mostRecentPullRequestNo  int number that stores the most recent pull request retieved so that 
     *                                 it does not retireve it more than once
     * @param reviewGenerator          used to start the review process once a pull requst is retrieved
     * 
     * @return returns 0 immediately after starting the swing worker 
     */
    int startListeningForPullRequests(GitHubClient user, String username, String password, String owner, String repo, int mostRecentPullRequestNo, ReviewGenerator reviewGenerator);

    /**
     * This method is called from the mergeChanges method in the dev.github.GitHubClient class.
     * It is used to automatically merge the pull request at pullRequestNo into master. 
     * If it can not auto merge then it will throw a dev.github.MergeException
     * 
     * @param owner         string owner of the repository 
     * @param repoName      string name of the repository 
     * @param pullRequestNo string number of the pull request
     * @param commitMessage string the message to be added with the merge commit
     * @param username      string github username 
     * @param password      string github password
     * 
     * @return returns 0 if the code is succesfully merged, -2 if the merge is not possible, throws
     *         a dev.github.MergeException if there is an error
     */
    int mergeChanges(String owner, String repoName, int pullRequestNo, String commitMessage, String username, String password) throws MergeException;

    /**
     * This method is called from the createPullRequestComment method in the dev.github.GitHubClient class.
     * It is used to post a comment on a pull request at pullRequestNo at the repository named
     * "owner/repo". 
     * 
     * @param comment       string the comment to leave on the pull request
     * @param owner         string the owner of the repository
     * @param repo          string name of the repository 
     * @param pullRequestNo int number of the pull request 
     * @param username      string github username 
     * @param password      string github password
     * 
     * @return 0 on successfully posting the comment or -2 if there is an error
     */
    int createPullRequestComment(String comment, String owner, String repo, int pullRequestNo, String username, String password);

    /**
     * This method is called from the createCodeChangeRequest method in the dev.github.GitHubClient class.
     * Creates a code change request on the pull request numbered pullRequestNo on the repository 
     * named "owner/repo". A code change request is really just a comment that requires the developers
     * to make a change before merging.
     * 
     * @param owner         string owner of the repository 
     * @param repo          string name of the repository 
     * @param pullRequestNo int number of the pull request
     * @param comment       string comment to be added along with the change request 
     * @param username      string github username
     * @param password      string github password
     * 
     * @return 0 if the code change request is posted succesfully, or -2 if there is some error
     */
    int createCodeChangeRequest(String owner, String repo, int pullRequestNo, String comment, String username, String password);

}
