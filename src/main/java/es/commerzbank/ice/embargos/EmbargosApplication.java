package es.commerzbank.ice.embargos;

import java.security.Timestamp;
import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.google.common.base.Predicates;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableAutoConfiguration
@EnableSwagger2
public class EmbargosApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmbargosApplication.class, args);
	}

	@Bean
    public Docket api()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build()
            .directModelSubstitute(Timestamp.class, Long.class)
            .consumes(Collections.singleton("application/json"))
            .produces(Collections.singleton("application/json"))
            .apiInfo(metaData());
    }
	
    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("EMBARGOS REST API")
                .description("API para la gestión de Embargos")
                .version("1.0.0")
                .contact(new Contact("Alten Spain (Barcelona)", "http://www.alten.es", ""))
                .build();
    }
}
