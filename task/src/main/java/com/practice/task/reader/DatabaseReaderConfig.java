package com.practice.task.reader;

import com.practice.task.model.User;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

/**
 * Configuración para leer datos de usuarios desde una base de datos.
 */
@Configuration
public class DatabaseReaderConfig {

    @Autowired
    private DataSource dataSource;

    /**
     * Constructor que recibe un DataSource.
     *
     * @param dataSource El DataSource para la conexión a la base de datos.
     */
    public DatabaseReaderConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Define un lector de datos JDBC para leer usuarios desde una base de datos.
     *
     * @return Un lector de datos JDBC para usuarios.
     */
    @Bean
    @StepScope
    public JdbcCursorItemReader<User> databaseReader() {
        return new JdbcCursorItemReaderBuilder<User>()
                .name("userDatabaseReader")
                .dataSource(dataSource)
                .sql("SELECT id, nombre, email FROM users")
                .rowMapper(userRowMapper())
                .build();
    }

    /**
     * Define un mapeador de filas para asignar los resultados de la consulta a objetos User.
     *
     * @return Un mapeador de filas para usuarios.
     */
    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setNombre(rs.getString("nombre"));
            user.setEmail(rs.getString("email"));
            return user;
        };
    }
}
