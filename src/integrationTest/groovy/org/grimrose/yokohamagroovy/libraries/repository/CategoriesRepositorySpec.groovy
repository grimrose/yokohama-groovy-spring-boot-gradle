package org.grimrose.yokohamagroovy.libraries.repository

import org.grimrose.yokohamagroovy.libraries.App
import org.grimrose.yokohamagroovy.libraries.infrastructure.CategoryRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(loader = SpringApplicationContextLoader, classes = App)
class CategoriesRepositorySpec extends Specification {

    @Autowired
    CategoriesRepository categoriesRepository

    def "insert"() {
        given:
        def record = new CategoryRecord(categoryName: "cat")

        when:
        def actual = categoriesRepository.save(record)

        then:
        categoriesRepository.findOne(actual.categoryId).present
    }

    def "update"() {
        given:
        def before = new CategoryRecord(categoryName: "cat")
        def record = categoriesRepository.save(before)
        def expected = "imaginary cat"

        when:
        categoriesRepository.save(record.copyWith(categoryName: expected))

        then:
        def result = categoriesRepository.findOne(record.categoryId)
        result.present

        result.map { e -> e.categoryName }.orElse("") == expected
    }

    def "delete"() {
        given:
        def before = new CategoryRecord(categoryName: "cat")
        def record = categoriesRepository.save(before)
        def id = record.categoryId

        when:
        categoriesRepository.delete(id)

        then:
        categoriesRepository.findOne(id).present == false
    }

}
