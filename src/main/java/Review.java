import java.util.ArrayList;
import java.util.List;

public class Review {
    private Results results;
    private Developer developer;
    private NonDeveloper nonDeveloper;
    private User author;
    private boolean isDevEnvironment;
    private List<User> reviewers;


    public Review(Results results, User author) {
        this.reviewers = new ArrayList<>();
        this.results = results;
        this.author = author;
        this.isDevEnvironment = true;
    }



	public void sendAutomatedResults(Results autoResults) {
        this.developer.sendNonDev(results, nonDeveloper);
    } 
    public void sendHighLevelResults(Results highLevelResults) {
        this.nonDeveloper.sendDev(results, developer);
    } 
    public void setReviewers(Developer developer, NonDeveloper nonDeveloper) {
        this.developer = developer;
        this.nonDeveloper = nonDeveloper;
    }

    public void addReviewer(User reviewer) throws InvalidReviewerException, UnauthorizedActionException {
        if (!this.isDevEnvironment) {
            throw new UnauthorizedActionException();
        }

        if (reviewer.isDeveloper()) {
            throw new InvalidReviewerException();
        }

        this.reviewers.add(reviewer);
    }

    public List<User> getReviewers() {
        return this.reviewers;
    }

    public void setDevEnvironment(boolean isDevEnvironment) {
        this.isDevEnvironment = isDevEnvironment;
    }
}

