import com.google.googlejavaformat.java.FormatterException;
import user.Developer;

public class ReviewGenerator {
    AutomatedCodeHandler ah;
    Database db;

    public ReviewGenerator(AutomatedCodeHandler ah, Database db) {
        this.ah = ah;
        this.db = db;
    }

    public DeveloperReviewHandler generateReviewHandler(SourceCode code, Developer author) throws FormatterException{
        InitialReviewResults results = ah.performAutomatedReview(code);
        Review review = new Review(results, author);
        return new DeveloperReviewHandler(review, this.db);
    }
}
