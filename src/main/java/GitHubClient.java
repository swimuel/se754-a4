import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.RepositoryContents;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.service.ContentsService;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestReview;
import org.kohsuke.github.GHPullRequestReviewBuilder;
import org.kohsuke.github.GHPullRequestReviewEvent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

public class GitHubClient {

    private String _username;
    private String _password;
    private int _mostRecentPullRequestNo;
    private HashMap<String, String> _sourceFiles;

    public GitHubClient() {
        _username = null;
        _password = null;
        _mostRecentPullRequestNo = 0;
    }

    public GitHubClient(String username, String password) {
        this();
        _username = username;
        _password = password;
    }

    /**
     * Sets the fields for that user so that they can be used to perform other tasks
     * 
     * @param username username fo the user
     * @param password user password
     */
    public void signIn(String username, String password) throws BadLoginException {
        _username = username;
        _password = password;

    }

    /**
     * Removes the username and password so that any further user actions will fail
     */
    public void signOut() {
        _username = null;
        _password = null;
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
     * @return hashmap with keys of file names and values of base64 encoded file
     *         contents or null if error
     */
    public HashMap<String, String> fetchSource(String owner, String repo, String path, String branch) {
        if (_username == null) {
            return null; // if user is not logged in can not get contents
        }
        ContentsService contentsService = new ContentsService();
        RepositoryId repoId = new RepositoryId(owner, repo);

        contentsService.getClient().setCredentials(_username, _password);
        HashMap<String, String> source = new HashMap<String, String>();
        try {
            // get contents from path
            List<RepositoryContents> repoContents = contentsService.getContents(repoId, path, branch);
            int repoContentsSize = repoContents.size();
            for (int i = 0; i < repoContentsSize; i++) {
                // if has no content then need to go a level deeper
                if (repoContents.get(i).getContent() == null) {
                    List<RepositoryContents> subContents = contentsService.getContents(repoId,
                            repoContents.get(i).getPath(), branch);
                    // add the sub contents to the end of the repoContents list
                    for (RepositoryContents s : subContents) {
                        repoContents.add(s);
                        // increase repoContentSize so that the loop will iterate through all contents
                        repoContentsSize++;
                    }
                }
                // if content is non null then it is a file so add its content to the output
                else if (repoContents.get(i).getContent() != null) {
                    source.put(repoContents.get(i).getPath(), repoContents.get(i).getContent());
                    _sourceFiles.put(repoContents.get(i).getPath(), repoContents.get(i).getContent());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return source;
    }

    /**
     * Grabs the source code that was changed in the pull request with number
     * 'pullRequestNo' from the repository owned by 'owner' called 'repo' and on
     * branch 'branch'. The source is returned in a hashmap with the full file name
     * as the key and the value being the base64 encoded content of the file.
     * 
     * @param owner         String owner of repository
     * @param repo          String name of repository
     * @param pullRequestNo int number representing the pull request
     * @param branch        String name of the branch to get the source from
     * @return HashMap with the keys as full names of the files and values as the
     *         base64 encoded content of the file.
     */
    public HashMap<String, String> fetchSourceFromPullRequest(String owner, String repo, int pullRequestNo,
            String branch) {
        if (_username == null) {
            return null; // if user is not logged in can not get contents
        }
        PullRequestService prService = new PullRequestService();
        prService.getClient().setCredentials(_username, _password);
        RepositoryId repoId = new RepositoryId(owner, repo);
        HashMap<String, String> source = new HashMap<String, String>();

        ContentsService contentsService = new ContentsService();
        contentsService.getClient().setCredentials(_username, _password);

        try {
            List<CommitFile> files = prService.getFiles(repoId, pullRequestNo);
            for (CommitFile c : files) {
                List<RepositoryContents> fileContents = contentsService.getContents(repoId, c.getFilename(), branch);
                for (RepositoryContents r : fileContents) {
                    source.put(c.getFilename(), r.getContent());
                    _sourceFiles.put(c.getFilename(), r.getContent());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return source;
    }


    /**
     * Creates a swing worker class that will listen for a pull request on a background thread.
     * When there is a pull requeset it will fetch the source code and put it in the _sourceFiles 
     * field of this class.
     * 
     * @param owner String owner of repo to listen on 
     * @param repo  String name of the repository
     * @return 0 on success or -1 if the user is not logged in
     */
    public int startListeningForPullRequests(String owner, String repo) {
        if (_username == null) {
            return -1;
        }
        PullRequestListener pullRequestListener = new PullRequestListener(this, _username, _password, owner, repo,
                _mostRecentPullRequestNo);
        pullRequestListener.execute();
        return 0;
    }


    /**
     * Used to merge a pull request automatically. If the user it not logged in it
     * will fail.
     * 
     * @param owner         username of the owner of the repo
     * @param repoName      name of the repo iteslf
     * @param pullRequestNo number of the pull request trying to be merged
     * @param commitMessage message to be included in the merge
     * @return              0 on success, -1 if user is not logged in, -2 for merge error, and -3
     *                      if the request is not automatically mergeable.
     */
    public int mergeChanges(String owner, String repoName, int pullRequestNo, String commitMessage) {
        if (_username == null) {
            // user is not signed in
            return -1;
        }
        PullRequestService service = new PullRequestService();

        service.getClient().setCredentials(_username, _password);
        RepositoryId repo = new RepositoryId(owner, repoName);
        try {
            if (service.getPullRequest(repo, pullRequestNo).isMergeable())
                service.merge(repo, pullRequestNo, commitMessage);
            else
                return -2; // repo is not automatically mergeable
        } catch (IOException e) {
            e.printStackTrace();
            return -3;
        }
        return 0;
    }


    /**
     * Puts a comment on the pull request with id of pullRequestNumber
     * @param comment       the string comment to add to the pull request
     * @param owner         string owner of the repo
     * @param repo          string repo name 
     * @param pullRequestNo int id of the pull request
     * @return              returns 0 on success, -1 if the user is not logged in, and -2 if there
     *                      is an exception 
     */
    public int createPullRequestComment(String comment, String owner, String repo, int pullRequestNo){
        if(_username == null){
            return -1;
        }
        try {
            IssueService iService = new IssueService();
            iService.getClient().setCredentials(_username, _password);
            RepositoryId repoId = new RepositoryId(owner, repo);
            iService.createComment(repoId, pullRequestNo, comment);
        } catch (IOException e) {
            e.printStackTrace();
            return -2;
        }

        return 0;
    }

    
    /**
     * Used to create a comment with a code request change to a pull reqest with number pullRequestNo
     * @param owner         String owner of the repository 
     * @param repo          String name of the repository 
     * @param pullRequestNo int number of the pull request
     * @param comment       String comment to add along with the change request.
     * @return              returns 0 on success, -1 if user is not logged in, and -2 for an exception
     */
    public int createCodeChangeRequest(String owner, String repo, int pullRequestNo, String comment){
        if(_username ==  null){
            return -1;
        }
        try {
            // set up the user login properites
            Properties props = new Properties();
            props.setProperty("login", _username);
            props.setProperty("password", _password);

            // get the repository
            GitHub gitHub = GitHubBuilder.fromProperties(props).build();
            GHRepository repository = gitHub.getRepository(owner + "/" + repo);

            // get the pull request 
            GHPullRequest pullRequest = repository.getPullRequest(pullRequestNo);

            // build a review object
            GHPullRequestReviewBuilder reviewBuilder = pullRequest.createReview();
            reviewBuilder.body(comment).event(GHPullRequestReviewEvent.REQUEST_CHANGES);

            // send the review
            GHPullRequestReview review = reviewBuilder.create();
        } catch (IOException e) {
            e.printStackTrace();
            return -2;
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

    /**
     * Set the most recent pull request number 
     * @param pullNo the most recent pull request number
     */
    public void setMostRecentPullRequestNo(int pullNo){
        _mostRecentPullRequestNo = pullNo;
    }

    /**
     * Get all of the currently stored source files as a 
     * HashMap
     * @return HashMap<String, String> of all stored source files
     */
    public HashMap<String, String> getSourceFiles() {
        return _sourceFiles;
    }

    /**
     * Remove all of the currently stored source files
     * from the hashmap
     */
    public void clearSourceFiles() {
        _sourceFiles.clear();
    }
}