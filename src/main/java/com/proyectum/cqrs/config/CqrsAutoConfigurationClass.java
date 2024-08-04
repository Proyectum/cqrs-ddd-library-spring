package com.proyectum.cqrs.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import(CqrsAutoConfiguration.class)
public class CqrsAutoConfigurationClass {
}
