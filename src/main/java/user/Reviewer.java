package user;

import java.util.UUID;

public class Reviewer extends User {
    private UUID id;
    private int reviewCount;

    public Reviewer() {
        this(UUID.randomUUID(), 0);
    }

    public Reviewer(UUID id, int reviewCount) {
        super(false);
        this.reviewCount = reviewCount;
        this.id = id;
    }

    public int getReviewCount() {
        return this.reviewCount;
    }

    public void incrementReviewCount() {
        this.reviewCount++;
    }
}
