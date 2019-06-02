Narrative: 
In order to be able to manage the reviewers assigned to a code review
As a developer 
I want to be able to add or remove reviewers assigned to a review

Scenario: the reviewer count is updated when a reviewer is added to the code review
Given there is 1 reviewer assigned to a review
When developer adds 1 reviewer the review
Then there are 2 reviewers assigned to the code review