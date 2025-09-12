package br.fai.lds.medlink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "br.fai.lds.medlink")
public class MedlinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedlinkApplication.class,  args);
	}
}