package net.ionoff.service;

import com.mkyong.api.model.AuthorDto;
import com.mkyong.api.model.BookDto;
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
