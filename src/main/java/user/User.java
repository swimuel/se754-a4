package user;

public abstract class User {
    private boolean isDeveloper;

    protected User(boolean isDeveloper) {
        this.isDeveloper = isDeveloper;
    }

    public boolean isDeveloper() {
        return this.isDeveloper;
    }
}
