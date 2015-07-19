package org.grimrose.yokohamagroovy.libraries.repository

import org.grimrose.yokohamagroovy.libraries.App
import org.grimrose.yokohamagroovy.libraries.infrastructure.UserRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(loader = SpringApplicationContextLoader, classes = App)
class UsersRepositorySpec extends Specification {

    @Autowired
    UsersRepository usersRepository

    def "insert"() {
        given:
        def record = new UserRecord(
                userName: "その1",
                userAddress: "日本",
                phoneNumber: "xxx-xxxx-xxxx",
                emailAddress: "test@example.com",
                otherUserDetails: "登録対象"
        )
        when:
        def actual = usersRepository.save(record)

        then:
        usersRepository.findOne(actual.userId).present
    }

    def "update"() {
        given:
        def before = new UserRecord(
                userName: "その2",
                userAddress: "日本",
                phoneNumber: "xxx-xxxx-xxxx",
                emailAddress: "test@example.com",
                otherUserDetails: "更新対象"
        )
        def record = usersRepository.save(before)
        def expected = "更新後"
        when:
        usersRepository.save(record.copyWith(otherUserDetails: expected))

        then:
        def result = usersRepository.findOne(record.userId)
        result.present

        result.map { e -> e.otherUserDetails }.orElse("") == expected
    }

    def "delete"() {
        given:
        def before = new UserRecord(
                userName: "その2",
                userAddress: "日本",
                phoneNumber: "xxx-xxxx-xxxx",
                emailAddress: "test@example.com",
                otherUserDetails: "削除対象"
        )
        def record = usersRepository.save(before)
        def id = record.userId

        when:
        usersRepository.delete(id)

        then:
        usersRepository.findOne(id).present == false
    }


}
