import java.util.Collection;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.event.Event;
import org.eclipse.egit.github.core.event.PullRequestPayload;
import org.eclipse.egit.github.core.service.EventService;

public class PullRequestListener extends SwingWorker<PullRequestInfo, Integer> {

    private GitHubClient user;
    private String username;
    private String password;
    private String owner;
    private String repo;
    private int mostRecentPull;

    public PullRequestListener(GitHubClient user, String username, String password, String owner, String repo,
            int mostRecentPull) {
        super();
        this.user = user;
        this.username = username;
        this.password = password;
        this.owner = owner;
        this.repo = repo;
        this.mostRecentPull = mostRecentPull;
    }

    @Override
    protected PullRequestInfo doInBackground() throws Exception {
        EventService eventService = new EventService();
        eventService.getClient().setCredentials(this.username, this.password);
        RepositoryId repoId = new RepositoryId(this.owner, this.repo);
        while (true) {
            try {
                Thread.sleep(10000); // give it some time so that you do not hit the rate limit
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            PageIterator<Event> pages = eventService.pageEvents(repoId);
            while (pages.hasNext()) {
                Collection<Event> events = pages.next();
                for (Event e : events) {
                    if (e.getPayload().getClass().equals(PullRequestPayload.class)) {
                        int pullRequestNo = ((PullRequestPayload) e.getPayload()).getNumber();
                        if (pullRequestNo > this.mostRecentPull) {
                            String branch = ((PullRequestPayload) e.getPayload()).getPullRequest().getHead().getRef();
                            this.mostRecentPull = pullRequestNo;
                            return new PullRequestInfo(branch, pullRequestNo);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void done() {
        try {
            PullRequestInfo reqInfo = get();
            // store the source code in the this.user object
            this.user.fetchSourceFromPullRequest(this.owner, this.repo, reqInfo.getPullRequestNo(), reqInfo.getBranch());

            // set the new most recent pull request number
            this.user.setMostRecentPullRequestNo(reqInfo.getPullRequestNo());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
    
}