package org.grimrose.yokohamagroovy.libraries.infrastructure

import groovy.transform.Immutable


@Immutable(copyWith = true)
class CategoryRecord {
    Long categoryId
    String categoryName
}
