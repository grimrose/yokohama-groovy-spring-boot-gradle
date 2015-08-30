package org.grimrose.yokohamagroovy.libraries.repository

import org.grimrose.yokohamagroovy.libraries.infrastructure.BooksRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

import javax.annotation.PostConstruct
import java.sql.ResultSet

@Repository
@Transactional
class BooksRepository {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate

    SimpleJdbcInsert insert

    @PostConstruct
    void init() {
        insert = new SimpleJdbcInsert((jdbcTemplate.jdbcOperations) as JdbcTemplate)
                .withTableName("books")
    }

    private def mapper = { ResultSet rs, index ->
        def id = rs.getString("isbn")
        def title = rs.getString("book_title")
        def date = rs.getDate("date_of_publication")
        new BooksRecord(id, title, date)
    }


    Optional<BooksRecord> findOne(String isbn) {
        def param = new MapSqlParameterSource().addValue("isbn", isbn)
        try {
            Optional.ofNullable(jdbcTemplate.queryForObject("""
SELECT isbn, book_title, date_of_publication
FROM books
WHERE isbn = :isbn
""", param, mapper))
        } catch (EmptyResultDataAccessException e) {
            return Optional.<BooksRecord> empty()
        }
    }

    BooksRecord save(BooksRecord record) {
        def param = new BeanPropertySqlParameterSource(record)

        if (findOne(record.isbn).present) {
            jdbcTemplate.update("""
UPDATE books SET
book_title=:bookTitle,
date_of_publication=:dateOfPublication
WHERE isbn=:isbn
""", param)
        } else {
            jdbcTemplate.update("""
INSERT INTO books(isbn, book_title, date_of_publication)
VALUES (:isbn, :bookTitle, :dateOfPublication)
""", param)
        }
        record
    }

    void delete(String isbn) {
        def param = new MapSqlParameterSource().addValue("isbn", isbn)
        jdbcTemplate.update("DELETE FROM books WHERE isbn = :isbn", param)
    }

}
