package dev.github;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Base64.Decoder;

import com.google.googlejavaformat.java.FormatterException;

import common.SourceCode;
import common.user.Developer;
import dev.ReviewGenerator;

public class GitHubClient {
    private String username;
    private String password;
    private GitHubConnection gitHubConnection;
    private HashMap<String, String> sourceFiles;
    private int mostRecentPullRequestNo;

    public GitHubClient(GitHubConnection gitHubConnection) {
        this.gitHubConnection = gitHubConnection;
        this.username = null;
        this.password = null;
        this.sourceFiles = new HashMap<String, String>();
        this.mostRecentPullRequestNo = 0;
    }

    /**
     * Sets the fields for that common.user so that they can be used to perform other tasks
     *
     * @param username username fo the common.user
     * @param password common.user password
     * @throws BadLoginException
     */
    public void signIn(String username, String password) throws BadLoginException {
        this.gitHubConnection.authenticateUser(username, password);

        // if no exception then the common.user is logged in
        this.username = username;
        this.password = password;
    }

    /**
     * Removes the username and password so that any further common.user actions will fail
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
        if (this.username == null) {
            return null; // if common.user is not logged in can not get contents
        }
        // use githubconnection object to retrieve the source
        HashMap<String, String> source = this.gitHubConnection.fetchSource(owner, repo, path, branch, this.username,
                this.password);

        // if the source is not null add it to the map of all source files
        if (source != null) {
            this.sourceFiles.putAll(source);
        }

        return source;
    }

    /**
     * Grabs the source code that was changed in the pull request with number
     * 'pullRequestNo' from the repository owned by 'owner' called 'repo' and on
     * branch 'branch'. The source is returned in a hashmap with the full file name
     * as the key and the value being the base64 encoded content of the file.
     *
     * @param owner                String owner of repository
     * @param repo                 String name of repository
     * @param pullRequestNo        int number representing the pull request
     * @param branch               String name of the branch to get the source from
     * @param reviewGenerator      used to start the review process once the code is
     *                             fetched
     * @param dev                  developer used to start the review process
     *
     * @return HashMap with the keys as full names of the files and values as the
     *         base64 encoded content of the file.
     * @throws FormatterException
     */
    public HashMap<String, String> fetchSourceFromPullRequest(String owner, String repo, int pullRequestNo,
                                                              String branch, ReviewGenerator reviewGenerator, Developer dev) throws FormatterException {
        if (this.username == null) {
            return null; // if common.user is not logged in can not get contents
        }
        // use githubconneciton object to fetch source
        HashMap<String, String> source = gitHubConnection.fetchSourceFromPullRequest(owner, repo, pullRequestNo, branch,
                this.username, this.password);

        // if some source code was fetched then add it to the map of all source files
        if (source != null) {
            this.sourceFiles.putAll(source);

            // start the review process
            List<SourceCode> code = new ArrayList<SourceCode>();
            Decoder decoder = Base64.getDecoder();

            for(String key : source.keySet()) {
                // convert from base64 to string
                String fileContent = decoder.decode(source.get(key)).toString();
                code.add(new SourceCode(key, fileContent));
            }
            reviewGenerator.generateAndSendReview(code, dev);
        }

        return source;
    }

    /**
     * Creates a swing worker class that will listen for a pull request on a background thread.
     * When there is a pull requeset it will fetch the source code and put it in the this.sourceFiles 
     * field of this class.
     *
     * @param owner            String owner of repo to listen on 
     * @param repo             String name of the repository
     * @param reviewGenerator  used to start the review process once pull request is detected
     *
     * @return 0 on success or -1 if the common.user is not logged in
     */
    public int startListeningForPullRequests(String owner, String repo, ReviewGenerator reviewGenerator) {
        if (this.username == null) {
            return -1;
        }

        return this.gitHubConnection.startListeningForPullRequests(this, this.username, this.password, owner, repo, this.mostRecentPullRequestNo, reviewGenerator);
    }

    /**
     * Used to merge a pull request automatically. If the common.user it not logged in it
     * will fail.
     *
     * @param owner         username of the owner of the repo
     * @param repoName      name of the repo iteslf
     * @param pullRequestNo number of the pull request trying to be merged
     * @param commitMessage message to be included in the merge
     *
     * @return 0 on success, -1 if common.user is not logged in, -2 if source is not automatically
     *         mergeable, throws dev.github.MergeException on failure
     */
    public int mergeChanges(String owner, String repoName, int pullRequestNo, String commitMessage) throws MergeException {
        if (this.username == null) {
            // common.user is not signed in
            return -1;
        }
        return this.gitHubConnection.mergeChanges(owner, repoName, pullRequestNo, commitMessage, this.username, this.password);
    }

    /**
     * Puts a comment on the pull request with id of pullRequestNumber
     *
     * @param comment       the string comment to add to the pull request
     * @param owner         string owner of the repo
     * @param repo          string repo name 
     * @param pullRequestNo int id of the pull request
     *
     * @return returns 0 on success, -1 if the common.user is not logged in, and -2 if there
     *         is an exception 
     */
    public int createPullRequestComment(String comment, String owner, String repo, int pullRequestNo){
        if(this.username == null){
            return -1;
        }

        return this.gitHubConnection.createPullRequestComment(comment, owner, repo, pullRequestNo, this.username, this.password);
    }

    /**
     * Used to create a comment with a code request change to a pull reqest with number pullRequestNo
     *
     * @param owner         String owner of the repository 
     * @param repo          String name of the repository 
     * @param pullRequestNo int number of the pull request
     * @param comment       String comment to add along with the change request.
     *
     * @return returns 0 on success, -1 if common.user is not logged in, and -2 for an exception
     */
    public int createCodeChangeRequest(String owner, String repo, int pullRequestNo, String comment){
        if(this.username ==  null){
            return -1;
        }

        return this.gitHubConnection.createCodeChangeRequest(owner, repo, pullRequestNo, comment, this.username, this.password);
    }

    /**
     * Gets github username that is stored for the common.user
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

    /**
     * Set the most recent pull request number 
     * @param pullRequestNo the most recent pull request number
     */
    public void setMostRecentPullRequestNo(int pullRequestNo) {
        this.mostRecentPullRequestNo = pullRequestNo;
    }

    /**
     * get the most recent pull request number
     */
    public int getMostRecentPullRequestNo() {
        return this.mostRecentPullRequestNo;
    }

    /**
     * Remove all of the currently stored source files
     * from the hashmap
     */
    public void clearSourceFiles() {
        this.sourceFiles.clear();
    }

}
