import user.Reviewer;
import user.Developer;
import java.util.ArrayList;
import java.util.List;

public class Review {
    private InitialReviewResults results;
    private DeveloperSide developer;
    private NonDeveloperSide nonDeveloper;
    private Developer author;
    private boolean isDevEnvironment;
    private List<Reviewer> reviewers;
    private Abstraction abstraction;
    private boolean approved;

    public Review(InitialReviewResults results, Developer author) {
        this.reviewers = new ArrayList<>();
        this.results = results;
        this.author = author;
        this.isDevEnvironment = true;
        this.approved = false;
    }

    public InitialReviewResults performReview() {
        return this.results;
    }

    public InitialReviewResults getResults() { return this.results; }

	public void sendAutomatedResults(InitialReviewResults autoResults) {
        this.developer.sendNonDev(results, nonDeveloper);
    } 
    public void sendHighLevelResults(InitialReviewResults highLevelResults) {
        this.nonDeveloper.sendDev(results, developer);
    }
    public void sendFeedback(Feedback feedback) {
        this.nonDeveloper.sendFeedback(feedback, developer);
    } 
	public void sendAbstraction(Abstraction abstraction) {
        this.developer.sendAbstraction(abstraction, nonDeveloper);
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
    }
    public void approveReview() {
        this.approved = true;
    }
    public boolean getApprovalStatus() {
        return this.approved;
    }

    public boolean removeReviewer(Reviewer reviewer) throws UnauthorizedActionException {
        if (!this.isDevEnvironment) {
            throw new UnauthorizedActionException();
        }

        boolean success = this.reviewers.remove(reviewer);

        return success;
    }

    public List<Reviewer> getReviewers() {
        return this.reviewers;
    }

    public void setDevEnvironment(boolean isDevEnvironment) {
        this.isDevEnvironment = isDevEnvironment;
    }
}

