package org.grimrose.yokohamagroovy.libraries

import net.sf.log4jdbc.sql.jdbcapi.DataSourceSpy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

import javax.sql.DataSource

@Configuration
class AppConfig {

    @Autowired
    DataSourceProperties dataSourceProperties

    DataSource dataSource

    @Bean
    DataSource realDataSource() {
        def builder = DataSourceBuilder.create(dataSourceProperties.classLoader)
                .url(dataSourceProperties.url)
                .username(dataSourceProperties.username)
                .password(dataSourceProperties.password)
        dataSource = builder.build()
        dataSource
    }

    @Primary
    @Bean
    DataSource dataSource() {
        new DataSourceSpy(dataSource)
    }

}
