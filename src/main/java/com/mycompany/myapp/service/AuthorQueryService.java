package com.mycompany.myapp.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.mycompany.myapp.domain.Author;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.AuthorRepository;
import com.mycompany.myapp.repository.search.AuthorSearchRepository;
import com.mycompany.myapp.service.dto.AuthorCriteria;

import com.mycompany.myapp.service.dto.AuthorDTO;
import com.mycompany.myapp.service.mapper.AuthorMapper;

/**
 * Service for executing complex queries for Author entities in the database.
 * The main input is a {@link AuthorCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AuthorDTO} or a {@link Page} of {@link AuthorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AuthorQueryService extends QueryService<Author> {

    private final Logger log = LoggerFactory.getLogger(AuthorQueryService.class);


    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    private final AuthorSearchRepository authorSearchRepository;

    public AuthorQueryService(AuthorRepository authorRepository, AuthorMapper authorMapper, AuthorSearchRepository authorSearchRepository) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
        this.authorSearchRepository = authorSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AuthorDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AuthorDTO> findByCriteria(AuthorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Author> specification = createSpecification(criteria);
        return authorMapper.toDto(authorRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AuthorDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AuthorDTO> findByCriteria(AuthorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Author> specification = createSpecification(criteria);
        final Page<Author> result = authorRepository.findAll(specification, page);
        return result.map(authorMapper::toDto);
    }

    /**
     * Function to convert AuthorCriteria to a {@link Specifications}
     */
    private Specifications<Author> createSpecification(AuthorCriteria criteria) {
        Specifications<Author> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Author_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Author_.name));
            }
            if (criteria.getBirthDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthDate(), Author_.birthDate));
            }
            if (criteria.getAuthor_bookId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getAuthor_bookId(), Author_.author_books, Book_.id));
            }
        }
        return specification;
    }

}
