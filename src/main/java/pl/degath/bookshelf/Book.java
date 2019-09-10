package pl.degath.bookshelf;

import io.vavr.API;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Predicates.instanceOf;
import static io.vavr.collection.List.ofAll;

public class Book {

    private final Clock clock;
    private final UUID uuid;
    private int rate = 0;
    private BookState state = BookState.PENDING;
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
                Case($(instanceOf(BookRead.class)), this::bookRead),
                Case($(instanceOf(BookRated.class)), this::bookRated),
                Case($(instanceOf(BookDeprecated.class)), this::bookDeprecated)
        );
    }

    void read() {
        bookRead(this.state.readBook(clock));
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
