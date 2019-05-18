import java.util.ArrayList;
import java.util.List;

public class Review {
    private Results results;
    private Developer developer;
    private NonDeveloper nonDeveloper;
    private User author;
    private boolean isDevEnvironment;
    private List<User> reviewers;
    private Database db;
    private Abstraction abstraction;


    public Review(Results results, Abstraction abstraction, User author, Database db) {
        this.reviewers = new ArrayList<>();
        this.results = results;
        this.author = author;
        this.isDevEnvironment = true;
        this.db = db;
        this.abstraction = abstraction;
    }


	public void sendAutomatedResults(Results autoResults) {
        this.developer.sendNonDev(results, nonDeveloper);
    } 
    public void sendHighLevelResults(Results highLevelResults) {
        this.nonDeveloper.sendDev(results, developer);
    } 
	public void sendAbstraction(Abstraction abstraction) {
        this.developer.sendAbstraction(abstraction, nonDeveloper);
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
        this.db.saveReviewer(this, reviewer);
    }

    public List<User> getReviewers() {
        return this.reviewers;
    }

    public void setDevEnvironment(boolean isDevEnvironment) {
        this.isDevEnvironment = isDevEnvironment;
    }
}

