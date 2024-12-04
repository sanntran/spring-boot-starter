package net.ionoff.service.api;

import net.ionoff.service.persistence.Book;
import net.ionoff.spring.api.model.AuthorDto;
import net.ionoff.spring.api.model.BookDto;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book fromDto(BookDto dto) {
        Book book = new Book();
        book.setAuthor(dto.getAuthor().getName());
        book.setId(dto.getId());
        book.setName(dto.getName());
        book.setPrice(dto.getPrice());
        return book;
    }

    public BookDto toDto(Book entity) {
        return new BookDto().id(entity.getId())
                .author(new AuthorDto().name(entity.getAuthor()))
                .name(entity.getName())
                .price(entity.getPrice());
    }
}
