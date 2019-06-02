Narrative: 
In order to be able to manage the reviewers assigned to a code review
As a developer 
I want to be able to add or remove reviewers assigned to a review

Scenario: developer can add a reviewer to a code review
Given user is a developer
When developer adds reviewer to a code review
Then reviewer is persisted to database for that code review 