package Acceptance;

import common.user.Developer;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;


public class AddReviewerSteps {

    @Given("there is $reviewersCount reviewers assigned to the code review")
    public void givenThereIsOneReviewerAssigned(int reviewersCount){

    }

    @When("a developer adds $addNo reviewers to the review")
    public void whenOneMoreReviewerIsAdded(int addNo){

    }

    @Then("there are $databaseCount reviewers assigned to the review")
    public void thenThereAreTwoReviewersAssignedToTheReview(int $databaseCount){

    }

}