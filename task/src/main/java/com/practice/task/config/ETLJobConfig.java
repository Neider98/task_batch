package com.practice.task.config;

import com.practice.task.model.User;
import com.practice.task.processor.UserItemProcessor;
import com.practice.task.reader.DatabaseReaderConfig;
import com.practice.task.writer.DatabaseWriterConfig;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Spring Batch para el proceso ETL.
 */
@Configuration
@EnableBatchProcessing
public class ETLJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DatabaseReaderConfig databaseReaderConfig;

    @Autowired
    private DatabaseWriterConfig databaseWriterConfig;

    @Autowired
    private UserItemProcessor userItemProcessor;

    /**
     * Constructor para la clase BatchConfig.
     * @param jobBuilderFactory Fábrica para construir trabajos.
     * @param stepBuilderFactory Fábrica para construir pasos.
     * @param databaseReaderConfig Configuration para leer de la base de datos
     * @param databaseWriterConfig Configuración para escribir en una base de datos.
     * @param userItemProcessor Procesador de elementos para usuarios.
     */
    public ETLJobConfig(JobBuilderFactory jobBuilderFactory,
                        StepBuilderFactory stepBuilderFactory,
                        DatabaseReaderConfig databaseReaderConfig,
                        DatabaseWriterConfig databaseWriterConfig,
                        UserItemProcessor userItemProcessor) {

        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.databaseReaderConfig = databaseReaderConfig;
        this.databaseWriterConfig = databaseWriterConfig;
        this.userItemProcessor = userItemProcessor;
    }

    /**
     * Definición del Job ETL (Extract, Transform, Load).
     * @return Job para el proceso ETL.
     */
    @Bean
    public Job etlJob() {
        return jobBuilderFactory.get("etlJob")
                .start(etlStep())
                .build();
    }

    /**
     * Definición del paso ETL para leer, procesar y escribir datos.
     * @return Paso para el proceso de ETL.
     */
    @Bean
    public Step etlStep() {
        return stepBuilderFactory.get("etlStep")
                .<User, User>chunk(10)
                .reader(databaseReaderConfig.databaseReader())
                .processor(userItemProcessor)
                .writer(databaseWriterConfig.writer())
                .build();
    }
}
