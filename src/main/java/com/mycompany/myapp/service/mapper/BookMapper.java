package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.BookDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Book and its DTO BookDTO.
 */
@Mapper(componentModel = "spring", uses = {AuthorMapper.class})
public interface BookMapper extends EntityMapper<BookDTO, Book> {

    @Mapping(source = "book_author.id", target = "book_authorId")
    @Mapping(source = "book_author.name", target = "book_authorName")
    BookDTO toDto(Book book); 

    @Mapping(source = "book_authorId", target = "book_author")
    Book toEntity(BookDTO bookDTO);

    default Book fromId(Long id) {
        if (id == null) {
            return null;
        }
        Book book = new Book();
        book.setId(id);
        return book;
    }
}
