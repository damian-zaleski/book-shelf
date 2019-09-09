package pl.degath.bookshelf;

import javaslang.API;
import javaslang.Predicates;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static javaslang.API.Case;
import static javaslang.collection.List.ofAll;

public class Book {

    private final UUID uuid;
    private final Clock clock;
    private BookState state = BookState.PENDING;
    private int rate = 0;
    private List<DomainEvent> changes = new ArrayList<>();

    public Book(UUID uuid, Clock clock) {
        this.uuid = uuid;
        this.clock = clock;
    }

    public static Book recreateFrom(UUID id, List<DomainEvent> domainEvents) {
        return ofAll(domainEvents).foldLeft(new Book(id, Clock.systemDefaultZone()), Book::handleEvent);
    }

    private Book handleEvent(DomainEvent event) {
        return API.Match(event).of(
                Case(Predicates.instanceOf(BookRead.class), this::bookRead),
                Case(Predicates.instanceOf(BookRated.class), this::bookRated),
                Case(Predicates.instanceOf(BookDeprecated.class), this::bookDeprecated)
        );
    }

    void read() {
        if (this.state == BookState.READ) {
            throw new IllegalStateException("You have already marked this book as read.");
        }
        if (this.state == BookState.DEPRECATED) {
            throw new IllegalStateException("Don't waste time reading a deprecated book.");
        }
        if (this.state == BookState.PENDING) {
            bookRead(new BookRead(Instant.now(clock)));
        }
    }

    private Book bookRead(BookRead bookRead) {
        state = BookState.READ;
        changes.add(bookRead);
        return this;
    }

    void rate(int newRate) {
        if (isPending()) {
            throw new IllegalStateException("You can't rate book without reading it.");
        }
        if (isDeprecated()) {
            throw new IllegalStateException("Don't waste time for deprecated books.");
        }
        bookRated(new BookRated(newRate, Instant.now(clock)));
    }

    private Book bookRated(BookRated bookRated) {
        this.rate = bookRated.getRate();
        changes.add(bookRated);
        return this;
    }

    void deprecate() {
        if (isDeprecated()) {
            throw new IllegalStateException("You can't deprecate already deprecated book.");
        }
        bookDeprecated(new BookDeprecated(Instant.now(clock)));
    }

    private Book bookDeprecated(BookDeprecated bookDeprecated) {
        this.state = BookState.DEPRECATED;
        changes.add(bookDeprecated);
        return this;
    }

    private boolean isPending() {
        return this.state == BookState.PENDING;
    }

    boolean isRead() {
        return this.state == BookState.READ;
    }

    int getRate() {
        return this.rate;
    }

    boolean isDeprecated() {
        return this.state == BookState.DEPRECATED;
    }

    public UUID getUuid() {
        return uuid;
    }

    public List<DomainEvent> getChanges() {
        return List.copyOf(changes);
    }

    public void flushChanges() {
        changes.clear();
    }
}
