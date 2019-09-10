package pl.degath.bookshelf.port;

import pl.degath.bookshelf.Book;
import pl.degath.bookshelf.DomainEvent;

import java.util.List;
import java.util.UUID;

public interface BookRepository {

    void save(Book book);

    List<DomainEvent> find(UUID id);
}
