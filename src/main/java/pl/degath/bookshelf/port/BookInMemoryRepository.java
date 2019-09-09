package pl.degath.bookshelf.port;

import pl.degath.bookshelf.Book;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BookInMemoryRepository implements BookRepository {

    private final Map<UUID, Book> books = new ConcurrentHashMap<>();

    @Override
    public void save(Book book) {
        books.put(book.getUuid(), book);
    }

    @Override
    public Book find(UUID id) {
        return books.get(id);
    }
}
