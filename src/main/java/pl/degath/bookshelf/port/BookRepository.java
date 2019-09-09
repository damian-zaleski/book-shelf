package pl.degath.bookshelf.port;

import pl.degath.bookshelf.Book;

import java.util.UUID;

public interface BookRepository {

    void save(Book book);

    Book find(UUID id);
}
