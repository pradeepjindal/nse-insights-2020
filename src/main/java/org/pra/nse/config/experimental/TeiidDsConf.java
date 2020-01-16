package org.pra.nse.config.experimental;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(
//        entityManagerFactoryRef = "teiidEntityManagerFactory",
//        basePackages = { "org.pra.nse.teiid.repo" }
//)
public class TeiidDsConf {

    //@Primary
    @Bean(name = "teiidDataSource")
    @ConfigurationProperties(prefix = "teiid.spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    //@Primary
    @Bean(name = "teiidEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("teiidDataSource") DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages("org.pra.nse.teiid.domain")
                .persistenceUnit("teiid")
                .build();
    }

    //@Primary
    @Bean(name = "teiidTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("teiidEntityManagerFactory")
            EntityManagerFactory entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}