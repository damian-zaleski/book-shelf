package pl.degath.bookshelf;

import java.time.Instant;

class BookRead implements DomainEvent {

    private final Instant when;

    BookRead(Instant when) {
        this.when = when;
    }

    @Override
    public Instant occurredAt() {
        return when;
    }
}
