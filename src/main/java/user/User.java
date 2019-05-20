package user;

import java.util.UUID;

public abstract class User {
    private UUID id;

    protected User(UUID id) {
        this.id = id;
    }
}
