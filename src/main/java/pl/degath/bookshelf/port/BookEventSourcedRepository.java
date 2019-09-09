package pl.degath.bookshelf.port;

import pl.degath.bookshelf.Book;
import pl.degath.bookshelf.DomainEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BookEventSourcedRepository implements BookRepository {

    private final Map<UUID, List<DomainEvent>> books = new ConcurrentHashMap<>();

    @Override
    public void save(Book book) {
        List<DomainEvent> newChanges = book.getChanges();
        List<DomainEvent> currentChanges = books.getOrDefault(book.getUuid(), new ArrayList<>());
        currentChanges.addAll(newChanges);
        books.put(book.getUuid(), currentChanges);
        book.flushChanges();
    }

    @Override
    public Book find(UUID id) {
        if (!books.containsKey(id)) {
            return null;
        }
        return Book.recreateFrom(id, books.get(id));
    }

    public Book find(UUID id, Instant timestamp) {
        if (!books.containsKey(id)) {
            return null;
        }
        List<DomainEvent> domainEvents = books.get(id)
                .stream()
                .filter(event -> !event.occurredAt().isAfter(timestamp))
                .collect(Collectors.toList());
        return Book.recreateFrom(id, domainEvents);
    }
}
