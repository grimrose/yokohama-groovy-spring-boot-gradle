package org.grimrose.yokohamagroovy.libraries.infrastructure

import groovy.transform.Immutable

@Immutable(copyWith = true)
class BooksRecord {
    String isbn
    String bookTitle
    Date dateOfPublication
}
