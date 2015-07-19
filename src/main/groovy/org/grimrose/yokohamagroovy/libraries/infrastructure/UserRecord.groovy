package org.grimrose.yokohamagroovy.libraries.infrastructure

import groovy.transform.Immutable

@Immutable(copyWith = true)
class UserRecord {

    Long userId
    String userName
    String userAddress
    String phoneNumber
    String emailAddress
    String otherUserDetails

}
