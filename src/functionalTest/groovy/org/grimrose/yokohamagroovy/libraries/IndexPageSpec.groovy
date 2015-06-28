package org.grimrose.yokohamagroovy.libraries

import geb.spock.GebReportingSpec

class IndexPageSpec extends GebReportingSpec {
    def "Hello, Library!が表示されていること"() {
        when:
        to IndexPage

        then:
        $("p").text() == "Hello, Library!"
    }

}
