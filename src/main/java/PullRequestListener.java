import java.util.Collection;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.event.Event;
import org.eclipse.egit.github.core.event.PullRequestPayload;
import org.eclipse.egit.github.core.service.EventService;

public class PullRequestListener extends SwingWorker<PullRequestInfo, Integer> {

    private GitHubClient _user;
    private String _username;
    private String _password;
    private String _owner;
    private String _repo;
    private int _mostRecentPull;

    public PullRequestListener(GitHubClient user, String username, String password, String owner, String repo,
            int mostRecentPull) {
        super();
        _user = user;
        _username = username;
        _password = password;
        _owner = owner;
        _repo = repo;
        _mostRecentPull = mostRecentPull;
    }

    @Override
    protected PullRequestInfo doInBackground() throws Exception {
        EventService eventService = new EventService();
        eventService.getClient().setCredentials(_username, _password);
        RepositoryId repoId = new RepositoryId(_owner, _repo);
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
                        if (pullRequestNo > _mostRecentPull) {
                            String branch = ((PullRequestPayload) e.getPayload()).getPullRequest().getHead().getRef();
                            _mostRecentPull = pullRequestNo;
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
            // store the source code in the _user object
            _user.fetchSourceFromPullRequest(_owner, _repo, reqInfo.getPullRequestNo(), reqInfo.getBranch());

            // set the new most recent pull request number
            _user.setMostRecentPullRequestNo(reqInfo.getPullRequestNo());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
    
}