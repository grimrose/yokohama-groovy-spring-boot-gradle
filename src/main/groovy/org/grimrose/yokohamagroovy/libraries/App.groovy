package org.grimrose.yokohamagroovy.libraries

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan

@ComponentScan
@EnableAutoConfiguration
class App {

    static void main(String... args) {
        SpringApplication.run(App, args)
    }

}
