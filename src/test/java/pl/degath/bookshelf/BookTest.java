package pl.degath.bookshelf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookTest {

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book();
    }

    @Test
    @DisplayName("An unread book cannot be rated.")
    void rate_withPendingBook_throwsIllegalStateException() {
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> book.rate(4));

        assertThat(ex.getMessage()).isEqualTo("You can't rate book without reading it.");
    }

    @Test
    @DisplayName("An read book can be rated.")
    void rate_withReadBook_ratesTheBook() {
        book.read();

        book.rate(4);

        assertThat(book.getRate()).isEqualTo(4);
    }

    @Test
    @DisplayName("An deprecated book cannot be rated.")
    void rate_withDeprecatedBook_throwsIllegalStateException() {
        book.deprecate();

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> book.rate(4));

        assertThat(ex.getMessage()).isEqualTo("Don't waste time for deprecated books.");
    }

    @Test
    @DisplayName("An deprecated book cannot be deprecated.")
    void deprecate_withDeprecatedBook_throwsIllegalStateException() {
        book.deprecate();

        IllegalStateException ex = assertThrows(IllegalStateException.class, book::deprecate);

        assertThat(ex.getMessage()).isEqualTo("You can't deprecate already deprecated book.");
    }

    @Test
    @DisplayName("An read book can be deprecated.")
    void deprecate_withReadBook_deprecates() {
        book.read();

        book.deprecate();

        assertThat(book.isDeprecated()).isTrue();
    }

    @Test
    @DisplayName("An pending book can be deprecated.")
    void deprecate_withPendingBook_deprecates() {
        book.deprecate();

        assertThat(book.isDeprecated()).isTrue();
    }

    @Test
    @DisplayName("An deprecated book can't be read.")
    void read_withDeprecatedBook_throwsIllegalStateException() {
        book.deprecate();

        IllegalStateException ex = assertThrows(IllegalStateException.class, book::read);

        assertThat(ex.getMessage()).isEqualTo("Don't waste time reading a deprecated book.");
    }

    @Test
    @DisplayName("An read book can't be read.")
    void read_withReadBook_throwsIllegalStateException() {
        book.read();

        IllegalStateException ex = assertThrows(IllegalStateException.class, book::read);

        assertThat(ex.getMessage()).isEqualTo("You have already marked this book as read.");
    }

    @Test
    @DisplayName("An pending book can be read.")
    void read_withPendingBook_reads() {
        book.read();

        assertThat(book.isRead()).isTrue();
    }
}
