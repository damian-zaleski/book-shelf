package pl.degath.bookshelf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.degath.bookshelf.port.BookEventSourcedRepository;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookEventSourceRepositoryTest {

    private BookEventSourcedRepository bookRepository = new BookEventSourcedRepository();
    private Clock clock;

    @BeforeEach
    void setUp() {
        clock = mock(Clock.class);
        when(clock.instant()).thenReturn(nowMinusDays(3), nowMinusDays(3), nowMinusDays(1));
    }

    private Instant nowMinusDays(int i) {
        return LocalDateTime.now().minusDays(i).atZone(ZoneId.systemDefault()).toInstant();
    }

    @Test
    @DisplayName("The book can be saved.")
    void save_withValidBook_savesTheBook() {
        UUID uuid = UUID.randomUUID();
        Book theBook = new Book(uuid, Clock.systemDefaultZone());
        theBook.read();

        bookRepository.save(theBook);

        Book found = bookRepository.find(uuid);
        assertThat(found).isNotNull();
        assertThat(found.isRead()).isTrue();
    }

    @Test
    @DisplayName("The state of the book can be found by timestamp.")
    void find_withValidTimeStamp_findsHistoricBook() {
        UUID id = UUID.randomUUID();
        addBookAndChangeRate(id);

        Book found = bookRepository.find(id, nowMinusDays(3));

        assertThat(found.getRate()).isEqualTo(3);
    }

    private Book addBookAndChangeRate(UUID id) {
        Book someBook = new Book(id, clock);
        someBook.read();
        someBook.rate(3);
        bookRepository.save(someBook);
        someBook.rate(4);
        bookRepository.save(someBook);
        return someBook;
    }
}
