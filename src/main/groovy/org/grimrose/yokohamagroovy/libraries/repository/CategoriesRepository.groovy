package org.grimrose.yokohamagroovy.libraries.repository

import org.grimrose.yokohamagroovy.libraries.infrastructure.CategoryRecord
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
class CategoriesRepository {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate

    SimpleJdbcInsert insert

    @PostConstruct
    void init() {
        insert = new SimpleJdbcInsert((jdbcTemplate.jdbcOperations) as JdbcTemplate)
                .withTableName("categories")
                .usingGeneratedKeyColumns("category_id")
    }

    private def mapper = { ResultSet rs, index ->
        def id = rs.getLong("category_id")
        def name = rs.getString("category_name")
        new CategoryRecord(id, name)
    }


    Optional<CategoryRecord> findOne(Long id) {
        def param = new MapSqlParameterSource().addValue("id", id)
        try {
            Optional.ofNullable(jdbcTemplate.queryForObject("""
SELECT category_id, category_name
FROM categories
WHERE category_id = :id
""", param, mapper))
        } catch (EmptyResultDataAccessException e) {
            return Optional.<CategoryRecord> empty()
        }
    }

    CategoryRecord save(CategoryRecord record) {
        def param = new BeanPropertySqlParameterSource(record)
        if (record.categoryId == null) {
            def key = insert.executeAndReturnKey(param)
            jdbcTemplate.update("""
INSERT INTO categories(category_name)
VALUES (:categoryName)
""", param)

            record = record.copyWith(categoryId: key.longValue())
        } else {
            jdbcTemplate.update("""
UPDATE categories SET
category_name=:categoryName
WHERE category_id=:categoryId
""", param)
        }
        record
    }

    void delete(Long id) {
        def param = new MapSqlParameterSource().addValue("id", id)
        jdbcTemplate.update("DELETE FROM categories WHERE category_id = :id", param)
    }

}
