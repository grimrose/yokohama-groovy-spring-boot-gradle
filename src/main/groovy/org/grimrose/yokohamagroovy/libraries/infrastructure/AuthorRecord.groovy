package org.grimrose.yokohamagroovy.libraries.infrastructure

import groovy.transform.Immutable

@Immutable(copyWith = true)
class AuthorRecord {
    Long authorId
    String authorFirstname
    String authorSurname
}
