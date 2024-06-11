package com.practice.task.reader;

import com.practice.task.model.User;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * Configuración para leer un archivo CSV y mapearlo a objetos User.
 */
@Configuration
public class CsvReaderConfig {

    /**
     * Define un lector de archivo plano para leer datos de usuarios desde un archivo CSV.
     *
     * @return Un lector de archivo plano para usuarios.
     */
    @Bean
    public FlatFileItemReader<User> reader() {
        return new FlatFileItemReaderBuilder<User>()
                .name("userReader")
                .resource(new ClassPathResource("data/users.csv")) // Ensure the correct path
                .linesToSkip(1) // Skip the header row
                .delimited()
                .names(new String[]{"id", "nombre", "email"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(User.class);
                }})
                .build();
    }
}
