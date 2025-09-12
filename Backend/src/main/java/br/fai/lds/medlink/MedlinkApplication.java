package br.fai.lds.medlink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "br.fai.lds.medlink")
public class MedlinkApplication {

	@org.springframework.context.annotation.Bean
	public org.springframework.web.servlet.config.annotation.WebMvcConfigurer corsConfigurer() {
		return new org.springframework.web.servlet.config.annotation.WebMvcConfigurer() {
			@Override
			public void addCorsMappings(org.springframework.web.servlet.config.annotation.CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://localhost:3000", "http://localhost:3001", "http://localhost:4200")
						.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
						.allowedHeaders("*")
						.allowCredentials(true);
			}

			@Override
			public void addResourceHandlers(org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry) {
				// Evita erro de "No static resource" para rotas que não existem
				registry.addResourceHandler("/messages/**")
						.addResourceLocations("classpath:/static/");
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(MedlinkApplication.class,  args);
	}
}