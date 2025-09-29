package br.fai.lds.medlink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Classe principal da aplicação MedLink.
 * 
 * <p>Sistema de gestão médica que permite o gerenciamento de pacientes,
 * médicos, prontuários e comunicação entre profissionais de saúde.</p>
 * 
 * @author MedLink Team
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication(exclude = {
    org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
    org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration.class
})
@ComponentScan(basePackages = "br.fai.lds.medlink")
public class MedlinkApplication {




	/**
	 * Método principal da aplicação MedLink.
	 * 
	 * @param args argumentos da linha de comando
	 */
	public static void main(String[] args) {
		SpringApplication.run(MedlinkApplication.class, args);
	}
}