package net.ionoff.service;

import com.mkyong.api.BookApi;
import com.mkyong.api.model.BookDto;
import com.mkyong.api.model.MessageResponseDto;
import net.ionoff.service.error.BookNotFoundException;
import io.swagger.annotations.ApiParam;
import net.ionoff.common.validation.group.Create;
import net.ionoff.common.validation.group.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.groups.Default;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
public class BookController implements BookApi {

    @Autowired
    private BookMapper mapper;

    @Autowired
    private BookRepository repository;

    @Override
    public ResponseEntity<List<BookDto>> getBooks() {
        return ResponseEntity.ok(repository.findAll().stream().map(e -> mapper.toDto(e)).collect(Collectors.toList()));
    }

    // Save
    @Override
    public ResponseEntity<BookDto> createBook(@Validated({Default.class, Create.class}) @RequestBody BookDto body) {
        Book book = repository.save(mapper.fromDto(body));
        return ResponseEntity.ok(mapper.toDto(book));
    }

    // Find
    @Override
    public ResponseEntity<BookDto> getBook(@ApiParam(value = "",required=true) @PathVariable("id") Long id) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));


        return ResponseEntity.ok(mapper.toDto(book));
    }

    @Override
    @Transactional
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @Validated({Default.class, Update.class}) @RequestBody BookDto body) {
        Book book = repository.findById(id)
                .map(x -> {
                    x.setName(body.getName());
                    x.setAuthor(body.getAuthor().getName());
                    x.setPrice(body.getPrice());
                    return x;
                })
                .orElseGet(() -> {
                    body.setId(id);
                    return repository.save(mapper.fromDto(body));
                });
        return ResponseEntity.ok(mapper.toDto(book));
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<MessageResponseDto> deleteBook(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.ok(new MessageResponseDto().message("Deleted"));
    }

}
