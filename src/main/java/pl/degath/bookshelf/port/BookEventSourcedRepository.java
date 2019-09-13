package pl.degath.bookshelf.port;

import pl.degath.bookshelf.Book;
import pl.degath.bookshelf.DomainEvent;
import pl.degath.bookshelf.exception.EntityNotFoundException;

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
    public List<DomainEvent> find(UUID id) {
        if (!books.containsKey(id)) {
            throw new EntityNotFoundException(id);
        }
        return books.get(id);
    }

    public List<DomainEvent> find(UUID id, Instant timestamp) {
        if (!books.containsKey(id)) {
            throw new EntityNotFoundException(id);
        }
        return books.get(id)
                .stream()
                .filter(event -> !event.occurredAt().isAfter(timestamp))
                .collect(Collectors.toList());
    }
}
