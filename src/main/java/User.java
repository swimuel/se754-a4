public class User {
    private boolean isDeveloper;

    public User(boolean isDeveloper) {
        this.isDeveloper = isDeveloper;
    }

    public boolean isDeveloper() {
        return this.isDeveloper;
    }

    public int getReviewCount() {
        return 1;
    }
}
