package net.ionoff.service.api;

import lombok.AllArgsConstructor;
import net.ionoff.service.validation.group.Create;
import net.ionoff.service.persistence.Book;
import net.ionoff.service.persistence.BookRepository;
import net.ionoff.spring.api.BookApi;
import net.ionoff.spring.api.model.BookDto;
import net.ionoff.spring.api.model.MessageResponseDto;
import net.ionoff.service.error.BookNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.groups.Default;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@AllArgsConstructor
@RestController
public class BookController implements BookApi {

    private BookMapper mapper;
    private BookRepository bookRepository;

    @Override
    public ResponseEntity<List<BookDto>> getBooks() {
        return ResponseEntity.ok(bookRepository.findAll().stream().map(e -> mapper.toDto(e)).collect(Collectors.toList()));
    }

    // Save
    @Override
    public ResponseEntity<BookDto> createBook(@Validated({Default.class, Create.class}) BookDto body) {
        Book book = bookRepository.save(mapper.fromDto(body));
        return ResponseEntity.ok(mapper.toDto(book));
    }

    // Find
    @Override
    public ResponseEntity<BookDto> getBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));


        return ResponseEntity.ok(mapper.toDto(book));
    }

    @Override
    @Transactional
    public ResponseEntity<BookDto> updateBook(Long id, BookDto body) {
        Book book = bookRepository.findById(id)
                .map(x -> {
                    x.setName(body.getName());
                    x.setAuthor(body.getAuthor().getName());
                    x.setPrice(body.getPrice());
                    return x;
                })
                .orElseGet(() -> {
                    body.setId(id);
                    return bookRepository.save(mapper.fromDto(body));
                });
        return ResponseEntity.ok(mapper.toDto(book));
    }
    
    public ResponseEntity<MessageResponseDto> deleteBook(Long id) {
        bookRepository.deleteById(id);
        return ResponseEntity.ok(new MessageResponseDto().message("Deleted"));
    }

}
