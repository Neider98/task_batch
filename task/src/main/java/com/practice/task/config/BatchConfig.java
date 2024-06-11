package com.practice.task.config;

import com.practice.task.model.User;
import com.practice.task.processor.UserItemProcessor;
import com.practice.task.reader.CsvReaderConfig;
import com.practice.task.writer.DatabaseWriterConfig;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Spring Batch para el procesamiento de datos.
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private CsvReaderConfig csvReaderConfig;

    @Autowired
    private DatabaseWriterConfig databaseWriterConfig;

    @Autowired
    private UserItemProcessor userItemProcessor;

    @Value("${input.file.path}")
    private String filePath;

    /**
     * Constructor para la clase BatchConfig.
     * @param jobBuilderFactory Fábrica para construir trabajos.
     * @param stepBuilderFactory Fábrica para construir pasos.
     * @param csvReaderConfig Configuración para leer desde un archivo CSV.
     * @param databaseWriterConfig Configuración para escribir en una base de datos.
     * @param userItemProcessor Procesador de elementos para usuarios.
     */
    public BatchConfig(JobBuilderFactory jobBuilderFactory,
                       StepBuilderFactory stepBuilderFactory,
                       CsvReaderConfig csvReaderConfig,
                       DatabaseWriterConfig databaseWriterConfig,
                       UserItemProcessor userItemProcessor) {

        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.csvReaderConfig = csvReaderConfig;
        this.databaseWriterConfig = databaseWriterConfig;
        this.userItemProcessor = userItemProcessor;
    }

    /**
     * Definición del Job para importar usuarios.
     * @return Job para importar usuarios.
     */
    @Bean
    public Job importUserJob() {
        return jobBuilderFactory.get("importUserJob")
                .start(step1())
                .build();
    }

    /**
     * Definición del paso 1 para leer, procesar y escribir usuarios.
     * @return Paso 1 para el proceso de importación de usuarios.
     */
    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<User, User>chunk(10)
                .reader(csvReaderConfig.reader())
                .processor(userItemProcessor)
                .writer(databaseWriterConfig.writer())
                .build();
    }
}