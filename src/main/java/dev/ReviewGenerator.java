package dev;

import com.google.googlejavaformat.java.FormatterException;
import common.InitialReviewResults;
import common.Review;
import common.SourceCode;
import common.user.Developer;
import common.user.UserAction;
import dev.inspection.AutomatedCodeHandler;

import java.util.List;

public class ReviewGenerator {
    UserAction userAction;
    AutomatedCodeHandler ah;
    Database db;
    NonDeveloperConnection ndc;
    DeveloperReviewHandler rh;

    public ReviewGenerator(AutomatedCodeHandler ah, Database db, UserAction userAction, NonDeveloperConnection ndc) {
        this.ah = ah;
        this.db = db;
        this.userAction = userAction;
        this.ndc = ndc;
        this.rh = null;
    }

    public DeveloperReviewHandler getReviewHandler() {
        return this.rh;
    }

    /**
     * Constructs a review based on the source code and author.
     * Passes it to the common.user to perform allocation then sends it to the
     * reviewer side of the tool.
     */
    public void generateAndSendReview(List<SourceCode> code, Developer author) throws FormatterException {
        generateReviewHandler(code, author);
        allocateReviewers(rh);
        sendReview(rh.getReview());
    }

    public void generateReviewHandler(List<SourceCode> code, Developer author) throws FormatterException{
        InitialReviewResults results = ah.performAutomatedReview(code);
        Review review = new Review(results, author);
        this.rh = new DeveloperReviewHandler(review, this.db);
    }

    public void allocateReviewers(DeveloperReviewHandler rh) {
        userAction.allocateReviewers(rh);
    }

    public void sendReview(Review review) {
        this.ndc.sendReview(review);
    }
}
