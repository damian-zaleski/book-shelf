package pl.degath.bookshelf;

import java.time.Instant;

public interface DomainEvent {

    Instant occurredAt();
}
