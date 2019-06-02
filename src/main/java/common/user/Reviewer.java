package common.user;

import java.util.UUID;

public class Reviewer extends User {
    private int reviewCount;
    private String name;

    public Reviewer(String name) {
        this();
        this.name = name;
    }

    public Reviewer() {
        this(UUID.randomUUID(), 0);
    }

    public Reviewer(UUID id, int reviewCount) {
        super(id);
        this.reviewCount = reviewCount;
    }

    public int getReviewCount() {
        return this.reviewCount;
    }

    public void incrementReviewCount() {
        this.reviewCount++;
    }
}
