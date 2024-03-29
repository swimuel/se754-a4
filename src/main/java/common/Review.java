package common;

import common.user.Reviewer;
import common.user.Developer;
import java.util.ArrayList;
import java.util.List;

public class Review {

    private Developer author;
    private boolean isDevEnvironment;
    private List<Reviewer> reviewers;
    private boolean approved;
    private InitialReviewResults initialReviewResults;

    public Review(InitialReviewResults initialReviewResults, Developer author) {
        this.reviewers = new ArrayList<>();
        this.author = author;
        this.isDevEnvironment = true;
        this.approved = false;
        this.initialReviewResults = initialReviewResults;
    }

    public InitialReviewResults getResults() {
        return this.initialReviewResults;
    }

    public void addReviewer(Reviewer reviewer) throws UnauthorizedActionException {
        if (!this.isDevEnvironment) {
            throw new UnauthorizedActionException();
        }

        this.reviewers.add(reviewer);
    }
    public void approveReview() throws UnauthorizedActionException {
        if (!this.isDevEnvironment) {
            throw new UnauthorizedActionException();
        }
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

