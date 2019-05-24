package common.user;

import common.Feedback;
import dev.DeveloperReviewHandler;

public interface UserAction {
    /** Interacts with the common.user in order to add reviewers */
    void allocateReviewers(DeveloperReviewHandler rh);

    /** Prompts the developer to approve or reject the pull request based on the reviewer feedback */
    boolean getDeveloperApproval(Feedback feedback);
}
