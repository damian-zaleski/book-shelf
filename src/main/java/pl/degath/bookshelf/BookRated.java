package pl.degath.bookshelf;

import java.time.Instant;

public class BookRated implements DomainEvent {

    private final int rate;
    private final Instant when;

    BookRated(int rate, Instant when) {
        this.rate = rate;
        this.when = when;
    }

    int getRate() {
        return rate;
    }


    @Override
    public Instant occurredAt() {
        return when;
    }
}
