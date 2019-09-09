package pl.degath.bookshelf;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.degath.bookshelf.port.BookInMemoryRepository;
import pl.degath.bookshelf.port.BookRepository;

import java.time.Clock;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class BookInMemoryRepositoryTest {

    private BookRepository bookRepository = new BookInMemoryRepository();

    @Test
    @DisplayName("Save & find test")
    void test() {
        UUID uuid = UUID.randomUUID();
        Book someBook = new Book(uuid, Clock.systemDefaultZone());

        someBook.read();
        someBook.rate(4);

        bookRepository.save(someBook);
        var found = bookRepository.find(uuid);

        assertThat(found).isNotNull();
        assertThat(found.getRate()).isEqualTo(4);
    }
}
