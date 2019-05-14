public class Review {
    Results results;
    Developer developer;
    NonDeveloper nonDeveloper;

    public Review(Results results) {
        this.results = results;
    }
	public void sendAutomatedResults(Results autoResults) {
        this.developer.sendNonDev(results, nonDeveloper);
    } 
    public void sendHighLevelResults(Results highLevelResults) {
        this.nonDeveloper.sendDev(results, developer);
    } 
    public void setReviewers(Developer developer, NonDeveloper nonDeveloper) {
        this.developer = developer;
        this.nonDeveloper = nonDeveloper;
    }
}

