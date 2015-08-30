package org.grimrose.yokohamagroovy.libraries.repository

import org.grimrose.yokohamagroovy.libraries.infrastructure.AuthorRecord
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
class AuthorRepository {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate

    SimpleJdbcInsert insert

    @PostConstruct
    void init() {
        insert = new SimpleJdbcInsert((jdbcTemplate.jdbcOperations) as JdbcTemplate)
                .withTableName("author")
                .usingGeneratedKeyColumns("author_id")
    }

    private def mapper = { ResultSet rs, index ->
        def id = rs.getLong("author_id")
        def first = rs.getString("author_firstname")
        def sur = rs.getString("author_surname")
        new AuthorRecord(id, first, sur)
    }


    Optional<AuthorRecord> findOne(Long id) {
        def param = new MapSqlParameterSource().addValue("id", id)
        try {
            Optional.ofNullable(jdbcTemplate.queryForObject("""
SELECT author_id, author_firstname, author_surname
FROM author
WHERE author_id = :id
""", param, mapper))
        } catch (EmptyResultDataAccessException e) {
            return Optional.<AuthorRecord> empty()
        }
    }

    AuthorRecord save(AuthorRecord record) {
        def param = new BeanPropertySqlParameterSource(record)

        if (record.authorId == null) {
            def key = insert.executeAndReturnKey(param)
            jdbcTemplate.update("""
INSERT INTO author(author_firstname, author_surname)
VALUES (:authorFirstname,:authorSurname)
""", param)

            record = record.copyWith(authorId: key.longValue())
        } else {
            jdbcTemplate.update("""
UPDATE author SET
author_firstname=:authorFirstname,
author_surname=:authorSurname
WHERE author_id=:authorId
""", param)
        }
        record
    }

    void delete(Long id) {
        def param = new MapSqlParameterSource().addValue("id", id)
        jdbcTemplate.update("DELETE FROM author WHERE author_id = :id", param)
    }

}
