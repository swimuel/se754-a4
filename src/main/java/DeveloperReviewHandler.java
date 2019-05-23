import common.InitialReviewResults;
import user.Reviewer;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Defines the actions that developers can perform on a review.
 * Also manages persistence of the review in the database
 */

public class DeveloperReviewHandler {
    private Review review;
    private Database db;

    public DeveloperReviewHandler(Review review, Database db) {
        this.review = review;
        this.db = db;
        review.setDevEnvironment(true);
    }

    public InitialReviewResults getReviewResults() {
        return review.getResults();
    }

    public Review getReview() {
        return this.review;
    }

    public void addReviewer(Reviewer reviewer) throws UnauthorizedActionException {
        this.review.addReviewer(reviewer);
        reviewer.incrementReviewCount();
        this.db.persistReviewer(reviewer);
        this.db.addReviewer(this.review, reviewer);
    }

    public boolean removeReviewer(Reviewer reviewer) throws UnauthorizedActionException {
        boolean success = this.review.removeReviewer(reviewer);
        if (success) {
            this.db.removeReviewer(this.review, reviewer);
        }
        return success;
    }

    public Reviewer allocateRandomReviewer(Random random) throws UnauthorizedActionException {
        List<Reviewer> reviewers = db.getReviewers();

        // sort them from highest count to lowest count.
        Collections.sort(reviewers, new Comparator<Reviewer>() {
            public int compare(Reviewer r1, Reviewer r2) {
                return r2.getReviewCount() - r1.getReviewCount();
            }
        });

        Map<Reviewer, Integer> reviewerWeighting = new HashMap();
        int sumOfWeights = 0;
        int weight = 1;
        int prevCount = -1;
        for (int i = 0; i < reviewers.size(); i++) {
            Reviewer r = reviewers.get(i);

            // weight does not increase if review count is same as previous element
            // or if this is the first iteration.
            if (prevCount != -1 && r.getReviewCount() != prevCount) {
                weight++;
            }

            reviewerWeighting.put(r, weight);
            sumOfWeights += weight;
            prevCount = r.getReviewCount();
        }

        // calculate range between 0 and 1 for each reviewer.
        // find the reviewer index by finding the first ranges value that the random
        // value is less than.
        double[] ranges = new double[reviewers.size()];
        for (int i = 0; i < ranges.length; i++) {
            int currentWeight = reviewerWeighting.get(reviewers.get(i));
            double rangeVal = (double) currentWeight / (double) sumOfWeights;
            if (i != 0) {
                rangeVal += ranges[i - 1];
            }

            ranges[i] = rangeVal;
        }

        double rand = random.nextDouble();
        Reviewer toAllocate = null;
        for (int i = 0; i < ranges.length; i++) {
            if (rand < ranges[i]) {
                if(toAllocate == null){
                    toAllocate = reviewers.get(i);
                }
            }
        }

        this.addReviewer(toAllocate);
        return toAllocate;
    }

    public Feedback getFeedback(NonDeveloperConnection nonDeveloperConnection) {
        return nonDeveloperConnection.fetchFeedback();
    }
}
