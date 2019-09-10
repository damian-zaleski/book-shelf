package pl.degath.bookshelf;

import java.time.Clock;
import java.time.Instant;

public enum BookState {

    PENDING {
        @Override
        public BookRead readBook(Clock clock) {
            return new BookRead(Instant.now(clock));
        }
    },
    READ {
        @Override
        public BookRead readBook(Clock clock) {
            throw new IllegalStateException("You have already marked this book as read.");
        }
    },
    DEPRECATED {
        @Override
        public BookRead readBook(Clock clock) {
            throw new IllegalStateException("Don't waste time reading a deprecated book.");
        }
    };

    abstract BookRead readBook(Clock clock);

}
