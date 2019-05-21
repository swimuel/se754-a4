import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.RepositoryContents;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.service.ContentsService;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.eclipse.egit.github.core.service.RepositoryService;

public class GitHubConnection {

    public GitHubConnection() {

    }

    /**
     * 
     * @param username
     * @param password
     * @throws BadLoginException
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
     * 
     * @param owner
     * @param repo
     * @param path
     * @param branch
     * @param username
     * @param password
     * @return
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
     * 
     * @param owner
     * @param repo
     * @param pullRequestNo
     * @param branch
     * @param username
     * @param password
     * @return
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

}