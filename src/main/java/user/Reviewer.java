package user;

public class Reviewer extends User {
    private int reviewCount;

    public Reviewer() {
        super(false);
        this.reviewCount = 0;
    }

    public int getReviewCount() {
        return this.reviewCount;
    }

    public void incrementReviewCount() {
        this.reviewCount++;
    }
}
