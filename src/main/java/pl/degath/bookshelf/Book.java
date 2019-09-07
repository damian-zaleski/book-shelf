package pl.degath.bookshelf;

import java.util.UUID;

public class Book {

    private final UUID uuid = UUID.randomUUID();
    private BookState state = BookState.PENDING;
    private int rate = 0;

    void read() {
        if (this.state == BookState.READ) {
            throw new IllegalStateException("You have already marked this book as read.");
        }
        if (this.state == BookState.DEPRECATED) {
            throw new IllegalStateException("Don't waste time reading a deprecated book.");
        }
        if (this.state == BookState.PENDING) {
            state = BookState.READ;
        }
    }


    void rate(int newRate) {
        if (isPending()) {
            throw new IllegalStateException("You can't rate book without reading it.");
        }
        if (isDeprecated()) {
            throw new IllegalStateException("Don't waste time for deprecated books.");
        }
        this.rate = newRate;
    }

    void deprecate() {
        if (isDeprecated()) {
            throw new IllegalStateException("You can't deprecate already deprecated book.");
        }
        this.state = BookState.DEPRECATED;
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
}
