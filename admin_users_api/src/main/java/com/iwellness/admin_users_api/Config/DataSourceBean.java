package com.iwellness.admin_users_api.Config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DataSourceBean {

    @Autowired Environment env;

@Bean
public DataSource dataSource() {
    final DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("org.sqlite.JDBC");
    dataSource.setUrl("jdbc:sqlite:admin_users_api/iwellness_admin_users_api.db");//Nombre de base de datos
    //dataSource.setUsername(env.getProperty("user"));
    //dataSource.setPassword(env.getProperty("password"));
    return dataSource;
}

}
