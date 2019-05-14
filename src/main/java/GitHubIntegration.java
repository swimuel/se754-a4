import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryContents;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.service.ContentsService;
import org.eclipse.egit.github.core.service.IssueService;
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
     * Removes the username and password so that any further user actions will fail
     */
    public void signOut() {
        _username = null;
        _password = null;
    }

    public static void main(String[] args) {

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
     * Used to merge a pull request automatically. If the user it not logged in it
     * will fail.
     * 
     * @param owner         username of the owner of the repo
     * @param repoName      name of the repo iteslf
     * @param pullRequestNo number of the pull request trying to be merged
     * @param commitMessage message to be included in the merge
     * @return 0 on success, -1 if user is not logged in, -2 for merge error, and -3
     *         if the request is not automatically mergeable.
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
     * Fetches the source files at the repo called "owner/repo" and returns them in
     * a hashmap with the file names as the key and the values as the content which
     * is base64 encoded.
     * 
     * @param owner owner of the repository
     * @param repo  name of the repository
     * @param path  path to a specific part of the repo, if null it will get all
     *              source
     * @return hashmap with keys of file names and values of base64 encoded file
     *         contents
     */
    public HashMap<String, String> fetchSource(String owner, String repo, String path) {
        if (_username == null) {
            return null; // if user is not logged in can not get contents
        }
        ContentsService contentsService = new ContentsService();
        RepositoryId repoId = new RepositoryId(owner, repo);

        contentsService.getClient().setCredentials(_username, _password);
        HashMap<String, String> source = new HashMap<String, String>();
        try {
            // get contents from path
            List<RepositoryContents> repoContents = contentsService.getContents(repoId, path);
            int repoContentsSize = repoContents.size();
            for (int i = 0; i < repoContentsSize; i++) {
                // if has no content then need to go a level deeper
                if (repoContents.get(i).getContent() == null) {
                    List<RepositoryContents> subContents = contentsService.getContents(repoId,
                            repoContents.get(i).getPath());
                    // add the sub contents to the end of the repoContents list
                    for (RepositoryContents s : subContents) {
                        repoContents.add(s);
                        // increase repoContentSize so that the loop will iterate through all contents
                        repoContentsSize++;
                    }
                }
                // if content is non null then it is a file so add its content to the output
                else if (repoContents.get(i).getContent() != null) {
                    source.put(repoContents.get(i).getName(), repoContents.get(i).getContent());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return source;
    }

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

    // public int createPullRequestCommentAtLocation(String comment, String owner, String repo, int pullRequestNo, String filePath, int position, int commitNumber) {
    //     if(_username == null) {
    //         return -1;
    //     }
    //     try {
    //         PullRequestService prService = new PullRequestService();
    //         prService.getClient().setCredentials(_username, _password);
    //         RepositoryId repoId = new RepositoryId(owner, repo);
    //         List<RepositoryCommit> commits = prService.getCommits(repoId, pullRequestNo);
            
    //         CommitComment commitComment = new CommitComment();
    //         commitComment.setBodyText(comment);
    //         commitComment.setBody(comment);
    //         commitComment.setCommitId(commits.get(commitNumber).getSha());
    //         commitComment.setPosition(position);
    //         commitComment.setPath(filePath);
    //         prService.createComment(repoId, pullRequestNo, commitComment);
            
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         return -2;
    //     }
    //     return 0;
    // }

    /**
     * Gets the username 
     * @return String username
     */
    public String getUsername() {
        return _username;
    }
}