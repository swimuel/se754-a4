Narrative: 
In order to be able to manage the reviewers assigned to a code review
As a developer 
I want to be able to add or remove reviewers assigned to a review

Scenario: the review count is updated when a reviewer is added to the code review
Given there is a new reviewer who has completed 5 reviews
When a developer assigns a code review to the reviewer
Then the new review count stored in the database for that reviewer is 6