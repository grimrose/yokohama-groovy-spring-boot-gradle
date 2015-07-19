package org.grimrose.yokohamagroovy.libraries.repository

import org.grimrose.yokohamagroovy.libraries.infrastructure.UserRecord
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
class UsersRepository {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate

    SimpleJdbcInsert insert

    @PostConstruct
    void init() {
        insert = new SimpleJdbcInsert((jdbcTemplate.jdbcOperations) as JdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id")
    }

    private def mapper = { ResultSet rs, index ->
        def id = rs.getLong("user_id")
        def userName = rs.getString("user_name")
        def userAddress = rs.getString("user_address")
        def phoneNumber = rs.getString("phone_number")
        def emailAddress = rs.getString("email_address")
        def otherUserDetails = rs.getString("other_user_details")
        new UserRecord(id, userName, userAddress, phoneNumber, emailAddress, otherUserDetails)
    }

    Optional<UserRecord> findOne(Long id) {
        def param = new MapSqlParameterSource().addValue("id", id)
        try {
            Optional.ofNullable(jdbcTemplate.queryForObject("""
SELECT user_id, user_name, user_address, phone_number, email_address, other_user_details
FROM users
WHERE user_id = :id
""", param, mapper))
        } catch (EmptyResultDataAccessException e) {
            return Optional.<UserRecord> empty()
        }
    }

    UserRecord save(UserRecord record) {
        def param = new BeanPropertySqlParameterSource(record)
        if (record.userId == null) {
            def key = insert.executeAndReturnKey(param)
            jdbcTemplate.update("""
INSERT INTO users(user_name, user_address, phone_number, email_address, other_user_details)
VALUES (:userName, :userAddress, :phoneNumber, :emailAddress, :otherUserDetails)
""", param)

            record = record.copyWith(userId: key.longValue())
        } else {
            jdbcTemplate.update("""
UPDATE users SET
user_name=:userName,
user_address=:userAddress,
phone_number=:phoneNumber,
email_address=:emailAddress,
other_user_details=:otherUserDetails
WHERE user_id=:userId
""", param)
        }
        record
    }

    void delete(Long id) {
        def param = new MapSqlParameterSource().addValue("id", id)
        jdbcTemplate.update("DELETE FROM users WHERE user_id = :id", param)
    }

}
