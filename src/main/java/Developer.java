public interface Developer {
    public void sendNonDev(Results results, NonDeveloper nonDeveloper);
    public void sendAbstraction(Abstraction abstraction, NonDeveloper nonDeveloper);
    public Results fetchResults();
    
}