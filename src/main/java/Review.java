import java.util.ArrayList;
import java.util.List;

public class Review {
    private Results results;
    private Developer developer;
    private NonDeveloper nonDeveloper;
    private User author;
    private boolean isDevEnvironment;


    public Review(Results results, User author) {
        this.results = results;
        this.author = author;
        this.isDevEnvironment = false;
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

    }

    public List<User> getReviewers() {
        return new ArrayList<>();
    }

    public void setDevEnvironment(boolean isDevEnvironment) {
        this.isDevEnvironment = isDevEnvironment;
    }
}

