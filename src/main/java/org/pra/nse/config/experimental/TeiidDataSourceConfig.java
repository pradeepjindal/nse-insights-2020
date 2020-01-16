package org.pra.nse.config.experimental;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

public class TeiidDataSourceConfig {


//    @Bean
//    @ConfigurationProperties("app.datasource.cardholder")
//    public DataSourceProperties cardHolderDataSourceProperties() {
//        return new DataSourceProperties();
//    }
//    @Bean
//    @ConfigurationProperties("app.datasource.cardholder.configuration")
//    public DataSource cardholderDataSource() {
//        return cardHolderDataSourceProperties().initializeDataSourceBuilder()
//                .type(BasicDataSource.class).build();
//    }
//    /*card data source*/
//    @Bean
//    @ConfigurationProperties("app.datasource.card")
//    public DataSourceProperties cardDataSourceProperties() {
//        return new DataSourceProperties();
//    }
//    @Bean
//    @ConfigurationProperties("app.datasource.card.configuration")
//    public DataSource cardDataSource() {
//        return cardDataSourceProperties().initializeDataSourceBuilder()
//                .type(BasicDataSource.class).build();
//    }


}
