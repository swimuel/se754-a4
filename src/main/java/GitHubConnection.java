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

public class GitHubConnection {

    public GitHubConnection() {

    }

    /**
     * This method is called from signIn method in the GitHubClient class.
     * It checks that the username and password are correct
     * 
     * @param username string github username
     * @param password string github password
     * @throws BadLoginException if the username and password are incorrect
     */
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
    public HashMap<String, String> fetchSource(String owner, String repo, String path, String branch, String username, String password){
        ContentsService contentsService = new ContentsService();
        RepositoryId repoId = new RepositoryId(owner, repo);

        contentsService.getClient().setCredentials(username, password);
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
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return source;
    }

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
            String branch, String username, String password) {
        PullRequestService prService = new PullRequestService();
        prService.getClient().setCredentials(username, password);
        RepositoryId repoId = new RepositoryId(owner, repo);
        HashMap<String, String> source = new HashMap<String, String>();

        ContentsService contentsService = new ContentsService();
        contentsService.getClient().setCredentials(username, password);

        try {
            List<CommitFile> files = prService.getFiles(repoId, pullRequestNo);
            for (CommitFile c : files) {
                List<RepositoryContents> fileContents = contentsService.getContents(repoId, c.getFilename(), branch);
                for (RepositoryContents r : fileContents) {
                    source.put(c.getFilename(), r.getContent());
                    
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return source;
    }

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
    public int startListeningForPullRequests(GitHubClient user, String username, String password, String owner, String repo, int mostRecentPullRequestNo) {

        PullRequestListener pullRequestListener = new PullRequestListener(user, username, password, owner, repo,
                mostRecentPullRequestNo);
        pullRequestListener.execute();
        return 0;
    }

    /**
     * This method is called from the mergeChanges method in the GitHubClient class.
     * It is used to automatically merge the pull request at pullRequestNo into master. 
     * If it can not auto merge then it will return -2
     * 
     * @param owner         string owner of the repository 
     * @param repoName      string name of the repository 
     * @param pullRequestNo string number of the pull request
     * @param commitMessage string the message to be added with the merge commit
     * @param username      string github username 
     * @param password      string github password
     * 
     * @return returns 0 if the code is succesfully merged, -2 if the merge is not possible, and -3 if
     *         there is a merge error
     */
    public int mergeChanges(String owner, String repoName, int pullRequestNo, String commitMessage, String username, String password) {

        PullRequestService service = new PullRequestService();

        service.getClient().setCredentials(username, password);
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
     * This method is called from the createPullRequestComment method in the GitHubClient class.
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
    public int createPullRequestComment(String comment, String owner, String repo, int pullRequestNo, String username, String password){

        try {
            IssueService iService = new IssueService();
            iService.getClient().setCredentials(username, password);
            RepositoryId repoId = new RepositoryId(owner, repo);
            iService.createComment(repoId, pullRequestNo, comment);
        } catch (IOException e) {
            e.printStackTrace();
            return -2;
        }

        return 0;
    }

    /**
     * This method is called from the createCodeChangeRequest method in the GitHubClient class.
     * Creates a code change request on the pull request numbered pullRequestNo on the repository 
     * named "owner/repo". A code change request is really just a comment that requires the developers
     * to make a change before merging.
     * 
     * @param owner         string owner of the repository 
     * @param repo          string name of the repository 
     * @param pullRequestNo int number of the pull request
     * @param comment       string comment to be added along with the change request 
     * @param username      string github username
     * @param passwrod      string github password
     * 
     * @return 0 if the code change request is posted succesfully, or -2 if there is some error
     */
    public int createCodeChangeRequest(String owner, String repo, int pullRequestNo, String comment, String username, String password){
        try {
            // set up the user login properites
            Properties props = new Properties();
            props.setProperty("login", username);
            props.setProperty("password", password);

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
}