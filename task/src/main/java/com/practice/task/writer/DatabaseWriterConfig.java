package com.practice.task.writer;

import com.practice.task.model.User;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Objects;

/**
 * Configuración para escribir datos de usuarios en una base de datos.
 */
@Configuration
public class DatabaseWriterConfig {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Constructor que recibe un JdbcTemplate.
     *
     * @param jdbcTemplate El JdbcTemplate para acceder a la base de datos.
     */
    public DatabaseWriterConfig(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Define un escritor batch JDBC para escribir usuarios en la base de datos.
     *
     * @return Un escritor batch JDBC para usuarios.
     */
    @Bean
    public JdbcBatchItemWriter<User> writer() {
        return new JdbcBatchItemWriterBuilder<User>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO users (id, nombre, email) VALUES (:id, " +
                        ":nombre, :email)")
                .dataSource(Objects.requireNonNull(jdbcTemplate.getDataSource()))
                .build();
    }
}
