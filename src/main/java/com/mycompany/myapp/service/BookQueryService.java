package com.mycompany.myapp.service;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.mycompany.myapp.domain.Book;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.BookRepository;
import com.mycompany.myapp.repository.search.BookSearchRepository;
import com.mycompany.myapp.service.dto.BookCriteria;

import com.mycompany.myapp.service.dto.BookDTO;
import com.mycompany.myapp.service.mapper.BookMapper;

/**
 * Service for executing complex queries for Book entities in the database.
 * The main input is a {@link BookCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BookDTO} or a {@link Page} of {@link BookDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BookQueryService extends QueryService<Book> {

    private final Logger log = LoggerFactory.getLogger(BookQueryService.class);


    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final BookSearchRepository bookSearchRepository;

    public BookQueryService(BookRepository bookRepository, BookMapper bookMapper, BookSearchRepository bookSearchRepository) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.bookSearchRepository = bookSearchRepository;
    }

    /**
     * Return a {@link List} of {@link BookDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BookDTO> findByCriteria(BookCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Book> specification = createSpecification(criteria);
        return bookMapper.toDto(bookRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BookDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BookDTO> findByCriteria(BookCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Book> specification = createSpecification(criteria);
        final Page<Book> result = bookRepository.findAll(specification, page);
        return result.map(bookMapper::toDto);
    }

    /**
     * Function to convert BookCriteria to a {@link Specifications}
     */
    private Specifications<Book> createSpecification(BookCriteria criteria) {
        Specifications<Book> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Book_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Book_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Book_.description));
            }
            if (criteria.getPublicationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPublicationDate(), Book_.publicationDate));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Book_.price));
            }
            if (criteria.getBook_authorId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getBook_authorId(), Book_.book_author, Author_.id));
            }
        }
        return specification;
    }

}
