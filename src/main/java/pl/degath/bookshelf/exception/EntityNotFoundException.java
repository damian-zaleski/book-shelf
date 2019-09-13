package pl.degath.bookshelf.exception;

import java.util.UUID;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(UUID entityId) {
        super(String.format("Entity with id %s not found.", entityId));
    }
}
