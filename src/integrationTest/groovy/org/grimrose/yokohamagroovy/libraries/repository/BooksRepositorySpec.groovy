package org.grimrose.yokohamagroovy.libraries.repository

import org.grimrose.yokohamagroovy.libraries.App
import org.grimrose.yokohamagroovy.libraries.infrastructure.BooksRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.time.LocalDate
import java.time.ZoneId

@ContextConfiguration(loader = SpringApplicationContextLoader, classes = App)
class BooksRepositorySpec extends Specification {

    def publishDay = Date.from(LocalDate.of(2015, 9, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())

    @Autowired
    BooksRepository booksRepository

    def "insert"() {
        given:
        def record = new BooksRecord("isbn1", "title1", publishDay)

        when:
        def actual = booksRepository.save(record)

        then:
        booksRepository.findOne(actual.isbn).present
    }

    def "update"() {
        given:
        def before = new BooksRecord("isbn1", "title1", publishDay)
        def record = booksRepository.save(before)
        def expectTitle = "title2"
        def expectDate = Date.from(LocalDate.of(2015, 9, 10).atStartOfDay(ZoneId.systemDefault()).toInstant())

        when:
        booksRepository.save(record.copyWith(bookTitle: expectTitle, dateOfPublication: expectDate))

        then:
        def result = booksRepository.findOne(record.isbn)
        result.present

        result.map { e -> e.bookTitle }.orElse("") == expectTitle
        result.map { e -> e.dateOfPublication } == Optional.of(expectDate)
    }

    def "delete"() {
        given:
        def before = new BooksRecord("isbn1", "title1", publishDay)
        def record = booksRepository.save(before)
        def isbn = record.isbn

        when:
        booksRepository.delete(isbn)

        then:
        booksRepository.findOne(isbn).present == false
    }

}
