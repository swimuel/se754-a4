public class User {
    private boolean isDeveloper;
    private int reviewCount;

    public User(boolean isDeveloper) {
        this.isDeveloper = isDeveloper;
        this.reviewCount = 0;
    }

    public boolean isDeveloper() {
        return this.isDeveloper;
    }

    public int getReviewCount() {
        return this.reviewCount;
    }
}
