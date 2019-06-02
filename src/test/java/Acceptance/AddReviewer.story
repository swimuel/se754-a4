Narrative: 
In order to be able to manage the reviewers assigned to a code review
As a developer 
I want to be able to add or remove reviewers assigned to a review

Scenario: the review count is updated when a reviewer is added to the code review
Given there is a reviewer that is assigned to 0 code reviews
When a developer assigns one code review to the reviewer
Then there are 1 reviews assigned the reviewer