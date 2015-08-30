package org.grimrose.yokohamagroovy.libraries.repository

import org.grimrose.yokohamagroovy.libraries.App
import org.grimrose.yokohamagroovy.libraries.infrastructure.AuthorRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(loader = SpringApplicationContextLoader, classes = App)
class AuthorRepositorySpec extends Specification {

    @Autowired
    AuthorRepository authorRepository

    def "insert"() {
        given:
        def record = new AuthorRecord(authorFirstname: "John", authorSurname: "Doe")

        when:
        def actual = authorRepository.save(record)

        then:
        authorRepository.findOne(actual.authorId).present
    }

    def "update"() {
        given:
        def before = new AuthorRecord(authorFirstname: "John", authorSurname: "Doe")
        def record = authorRepository.save(before)
        def expectFirst = "Alan"
        def expectSur = "Smith"

        when:
        authorRepository.save(record.copyWith(authorFirstname: expectFirst, authorSurname: expectSur))

        then:
        def result = authorRepository.findOne(record.authorId)
        result.present

        result.map { e -> e.authorFirstname }.orElse("") == expectFirst
        result.map { e -> e.authorSurname }.orElse("") == expectSur
    }

    def "delete"() {
        given:
        def before = new AuthorRecord(authorFirstname: "John", authorSurname: "Doe")
        def record = authorRepository.save(before)
        def id = record.authorId

        when:
        authorRepository.delete(id)

        then:
        authorRepository.findOne(id).present == false
    }

}
