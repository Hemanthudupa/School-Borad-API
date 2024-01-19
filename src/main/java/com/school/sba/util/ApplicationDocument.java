package com.school.sba.util;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configurable
@OpenAPIDefinition
public class ApplicationDocument {

	Info info() {
		return new Info().title("SCHOOL BOARD API ").version("1.0v")
				.description("School Board API  is a restful API created using springboot mySQL data base ");
	}

	@Bean
	OpenAPI open() {
		return new OpenAPI().info(info());
	}
}
