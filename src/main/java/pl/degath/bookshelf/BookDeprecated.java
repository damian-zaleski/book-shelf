package pl.degath.bookshelf;

import java.time.Instant;

class BookDeprecated implements DomainEvent {

    private final Instant when;

    BookDeprecated(Instant when) {
        this.when = when;
    }

    @Override
    public Instant occurredAt() {
        return when;
    }
}
