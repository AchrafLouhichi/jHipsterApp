package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.BookService;
import com.mycompany.myapp.domain.Book;
import com.mycompany.myapp.repository.BookRepository;
import com.mycompany.myapp.repository.search.BookSearchRepository;
import com.mycompany.myapp.service.dto.BookDTO;
import com.mycompany.myapp.service.mapper.BookMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Book.
 */
@Service
@Transactional
public class BookServiceImpl implements BookService{

    private final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final BookSearchRepository bookSearchRepository;

    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper, BookSearchRepository bookSearchRepository) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.bookSearchRepository = bookSearchRepository;
    }

    /**
     * Save a book.
     *
     * @param bookDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public BookDTO save(BookDTO bookDTO) {
        log.debug("Request to save Book : {}", bookDTO);
        Book book = bookMapper.toEntity(bookDTO);
        book = bookRepository.save(book);
        BookDTO result = bookMapper.toDto(book);
        bookSearchRepository.save(book);
        return result;
    }

    /**
     * Get all the books.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Books");
        return bookRepository.findAll(pageable)
            .map(bookMapper::toDto);
    }

    /**
     * Get one book by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public BookDTO findOne(Long id) {
        log.debug("Request to get Book : {}", id);
        Book book = bookRepository.findOne(id);
        return bookMapper.toDto(book);
    }

    /**
     * Delete the book by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Book : {}", id);
        bookRepository.delete(id);
        bookSearchRepository.delete(id);
    }

    /**
     * Search for the book corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Books for query {}", query);
        Page<Book> result = bookSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(bookMapper::toDto);
    }
}
