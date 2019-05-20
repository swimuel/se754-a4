import user.Reviewer;
import user.User;
import user.Developer;

import java.util.ArrayList;
import java.util.List;

public class Review {
    private Results results;
    private DeveloperSide developer;
    private NonDeveloperSide nonDeveloper;
    private Developer author;
    private boolean isDevEnvironment;
    private List<Reviewer> reviewers;
    private Database db;


    public Review(Results results, Developer author, Database db) {
        this.reviewers = new ArrayList<>();
        this.results = results;
        this.author = author;
        this.isDevEnvironment = true;
        this.db = db;
    }

	public void sendAutomatedResults(Results autoResults) {
        this.developer.sendNonDev(results, nonDeveloper);
    } 
    public void sendHighLevelResults(Results highLevelResults) {
        this.nonDeveloper.sendDev(results, developer);
    }
    public void setReviewers(DeveloperSide developer, NonDeveloperSide nonDeveloper) {
        this.developer = developer;
        this.nonDeveloper = nonDeveloper;
    }

    public void addReviewer(Reviewer reviewer) throws UnauthorizedActionException {
        if (!this.isDevEnvironment) {
            throw new UnauthorizedActionException();
        }

        this.reviewers.add(reviewer);
        this.db.saveReviewer(this, reviewer);
    }

    public boolean removeReviewer(Reviewer reviewer) throws UnauthorizedActionException {
        if (!this.isDevEnvironment) {
            throw new UnauthorizedActionException();
        }

        boolean success = this.reviewers.remove(reviewer);
        if (success) {
            this.db.removeReviewer(this, reviewer);
        }

        return success;
    }

    public void submitReview(Reviewer reviewer) {
        reviewer.incrementReviewCount();
        this.db.persistReviewer(reviewer);
    }

    public List<Reviewer> getReviewers() {
        return this.reviewers;
    }

    public void setDevEnvironment(boolean isDevEnvironment) {
        this.isDevEnvironment = isDevEnvironment;
    }
}

