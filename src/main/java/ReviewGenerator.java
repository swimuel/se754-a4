import com.google.googlejavaformat.java.FormatterException;
import user.Developer;

public class ReviewGenerator {
    UserAction userAction;
    AutomatedCodeHandler ah;
    Database db;

    public ReviewGenerator(AutomatedCodeHandler ah, Database db, UserAction userAction) {
        this.ah = ah;
        this.db = db;
        this.userAction = userAction;
    }

    public DeveloperReviewHandler generateReviewHandler(SourceCode code, Developer author) throws FormatterException{
        InitialReviewResults results = ah.performAutomatedReview(code);
        Review review = new Review(results, author);
        return new DeveloperReviewHandler(review, this.db);
    }

    public void allocateReviewers(DeveloperReviewHandler rh) {
        userAction.allocateReviewers(rh);
    }
}
