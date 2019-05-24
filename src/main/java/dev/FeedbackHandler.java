package dev;

import common.Feedback;
import dev.github.GitHubClient;
import dev.github.MergeException;
import common.user.UserAction;

public class FeedbackHandler {
    private NonDeveloperConnection ndc;
    private Feedback feedback;
    private boolean approvalStatus;
    private GitHubClient ghc;
    private UserAction ua;

    public FeedbackHandler(NonDeveloperConnection ndc, UserAction ua, GitHubClient ghc) {
        this.ndc = ndc;
        this.feedback = null;
        this.approvalStatus = false;
        this.ua = ua;
        this.ghc = ghc;
    }

    public void receiveFeedback() {
        this.feedback = ndc.fetchFeedback();
    }

    public Feedback getFeedback() {
        return this.feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public void getDeveloperApproval() {
        this.approvalStatus = this.ua.getDeveloperApproval(this.feedback);
    }

    public boolean getApprovalStatus() {
        return this.approvalStatus;
    }

    public void setApprovalStatus(boolean approval) {
        this.approvalStatus = approval;
    }

    public void finishReview() throws MergeException {
        // regardless of approval status, we want to post the comments
        this.ghc.createPullRequestComment(this.feedback.getComments(), "tbc", "tbc", 1);

        if (this.approvalStatus) {
            this.ghc.mergeChanges("tc", "tbc", 1, "tbc");
        }
    }
}
